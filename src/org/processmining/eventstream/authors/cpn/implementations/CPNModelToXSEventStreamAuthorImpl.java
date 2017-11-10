package org.processmining.eventstream.authors.cpn.implementations;

import java.io.IOException;
import java.math.BigInteger;

import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.instance.Binding;
import org.cpntools.accesscpn.engine.highlevel.instance.ValueAssignment;
import org.cpntools.accesscpn.model.PetriNet;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttribute;
import org.processmining.eventstream.authors.cpn.parameters.CPN2XSEventStreamCaseIdentification;
import org.processmining.eventstream.authors.cpn.parameters.CPN2XSEventStreamParameters;
import org.processmining.eventstream.core.factories.XSEventFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.plugins.cpnet.utils.CPNPageStripper;
import org.processmining.plugins.cpnet.utils.TransitionUtils;
import org.processmining.stream.core.abstracts.AbstractXSAuthor;
import org.processmining.stream.visualization.XSDynamicBarChart;

public class CPNModelToXSEventStreamAuthorImpl extends AbstractXSAuthor<XSEvent, String> {

	protected final PetriNet pn;
	protected final org.cpntools.accesscpn.engine.highlevel.instance.State initialState;
	protected final HighLevelSimulator simulator;
	protected final CPN2XSEventStreamParameters parameters;

	protected BigInteger lastStep = BigInteger.valueOf(-1);
	protected final XFactory xfact = XFactoryRegistry.instance().currentDefault();
	protected long currentStep = 0L;
	protected int currentRepetition = 0;
	private final CPNPageStripper pageStripper;

	protected long lastSend = -1;
	protected final long waitTimeNs;

	private static final long MICRO_NANO_RATIO = 1000000;

	public CPNModelToXSEventStreamAuthorImpl(final PetriNet pn,
			final org.cpntools.accesscpn.engine.highlevel.instance.State iState, final HighLevelSimulator sim,
			CPN2XSEventStreamParameters parameters) {
		super("XSEvent Author (CPN Model)", new XSDynamicBarChart("CPN-XSStream Author Visualizer", 1024));
		this.pn = pn;
		initialState = iState;
		simulator = sim;
		this.parameters = parameters;
		this.waitTimeNs = parameters.getTransitionDelayMs() * MICRO_NANO_RATIO;
		if (parameters.isIgnorePage()) {
			pageStripper = new CPNPageStripper(pn);
		} else {
			pageStripper = null;
		}
	}

	protected void workPackage() {
		if (currentRepetition < parameters.getTotalNumberOfRepetitions()) {
			try {
				if (allowedToComputeNewBinding()) {
					lastStep = simulator.getStep();
					final Binding binding = simulator.executeAndGet();
					if (binding != null) {
						if (mayEmitBinding(binding)) {
							processAndEmitBinding(binding);
						}
					}
					currentStep++;
				} else {
					currentRepetition++;
					currentStep = 0;
					initializeSimulator();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (currentRepetition >= parameters.getTotalNumberOfRepetitions()) {
			stopXSRunnable();
		}
	}

	protected boolean allowedToComputeNewBinding() {
		boolean result = true;
		result &= parameters.getMaximumNumberOfStepsPerRepetition() == -1
				|| currentStep < parameters.getMaximumNumberOfStepsPerRepetition();
		try {
			result &= !lastStep.equals(simulator.getStep());
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	protected boolean mayEmitBinding(final Binding binding) {
		boolean result = false;
		switch (parameters.getCaseIdentificationType()) {
			case CPN_VARIABLE :
				result = binding.getValueAssignment(parameters.getCaseIdentifier()) != null;
				break;
			case REPITITION :
				result = true;
				break;
		}
		return result;
	}

	protected void processAndEmitBinding(final Binding binding) {
		XAttribute traceIdentifier = identifyCase(binding);
		XSEvent event = XSEventFactory.createXSEvent(traceIdentifier);
		String eventNameStr = binding.getTransitionInstance().toString();
		if (pageStripper != null) {
			eventNameStr = pageStripper.convertToStringWithoutPages(binding.getTransitionInstance());
		}
		if (parameters.getIgnorePatterns().length == 0
				|| !TransitionUtils.containsIgnorePattern(eventNameStr, parameters.getIgnorePatterns())) {
			XAttribute eventName = xfact.createAttributeLiteral(XConceptExtension.KEY_NAME, eventNameStr,
					XConceptExtension.instance());
			event.put(XConceptExtension.KEY_NAME, eventName);
			if (parameters.isIncludeVariables()) {
				event = addData(event, binding);
			}
			delayEmission();
			if (event == null) {
				System.out.println("Somehow the CPN-generated event was null, we do not send it!");
			} else {
				write(event);
				lastSend = System.nanoTime();
				getVisualization().updateVisualization(eventNameStr);
			}
		}
	}

	protected void delayEmission() {
		if (lastSend != -1 && System.nanoTime() - lastSend < waitTimeNs) {
			long remainingWTNs = waitTimeNs - (System.nanoTime() - lastSend);
			if (remainingWTNs > 0) {
				long ms = remainingWTNs / MICRO_NANO_RATIO;
				// ignore additional nanoseconds....
				try {
					Thread.sleep(ms);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected XSEvent addData(XSEvent event, Binding binding) {
		for (ValueAssignment kv : binding.getAllAssignments()) {
			XAttribute data = xfact.createAttributeLiteral(kv.getName(), kv.getValue(), null);
			event.put("xsevent:data:" + kv.getName(), data);
		}
		return event;
	}

	protected XAttribute identifyCase(final Binding binding) {
		XAttribute traceIdentifier = null;
		switch (parameters.getCaseIdentificationType()) {
			case CPN_VARIABLE :
				traceIdentifier = xfact.createAttributeLiteral(XSEventSignature.TRACE,
						binding.getValueAssignment(parameters.getCaseIdentifier()).getValue(), null);
				break;
			case REPITITION :
				traceIdentifier = xfact.createAttributeLiteral(XSEventSignature.TRACE,
						CPN2XSEventStreamCaseIdentification.REPITITION.getDefaultValue() + "_" + currentRepetition,
						null);
				break;
		}
		return traceIdentifier;
	}

	protected void initializeSimulator() {
		try {
			simulator.initialState();
			simulator.setMarking(initialState);
			simulator.refreshViews();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}
}
