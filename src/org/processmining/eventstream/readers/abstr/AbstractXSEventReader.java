package org.processmining.eventstream.readers.abstr;

import java.util.Arrays;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XAttributeLiteral;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.framework.util.Pair;
import org.processmining.stream.core.abstracts.AbstractXSReader;
import org.processmining.stream.core.abstracts.AbstractXSVisualization;
import org.processmining.stream.core.interfaces.XSVisualization;

public abstract class AbstractXSEventReader<R, V, P extends XSEventReaderParameters> extends AbstractXSReader<XSEvent, R, V> {

	private XEventClass[] eventClasses = new XEventClass[0];
	private final P parameters;
	private XSVisualization<V> visualization;

	public AbstractXSEventReader(final String name, final XSVisualization<V> visualization, final P parameters) {
		super(name, visualization);
		this.parameters = parameters;
		this.visualization = visualization != null ? visualization : new AbstractXSVisualization<V>("dummy") {
			public JComponent asComponent() {
				return new JPanel();
			}

			protected void workPackage() {
			}

			public void update(Pair<Date, String> message) {
			}

			public void update(String object) {
			}

			public void updateVisualization(Pair<Date, V> newArtifact) {
			}

			public void updateVisualization(V newArtifact) {
			}
		};
	}

	protected boolean canReadPacket(XSEvent dataPacket) {
		boolean result = true;
		result &= dataPacket.containsKey(getParameters().getCaseIdentifier());
		result &= dataPacket.containsKey(getParameters().getActivityIdentifier());
		return result;
	}

	protected int findEventIdentifierIndex(String eventClassStr) {
		for (int i = 0; i < getEventClasses().length; i++) {
			if (eventClasses[i].toString().equals(eventClassStr)) {
				return i;
			}
		}
		return -1;
	}

	protected XEventClass getActivity(XSEvent event) {
		int indexOfEventClass = findEventIdentifierIndex(event.get(getParameters().getActivityIdentifier()).toString());
		if (indexOfEventClass != -1) {
			return eventClasses[indexOfEventClass];
		} else {
			XEventClass ec = new XEventClass(
					((XAttributeLiteral) event.get(getParameters().getActivityIdentifier())).getValue(),
					eventClasses.length);
			setEventClasses(Arrays.copyOf(getEventClasses(), getEventClasses().length + 1));
			getEventClasses()[eventClasses.length - 1] = ec;
			return ec;
		}
	}

	public XEventClass[] getEventClasses() {
		return eventClasses;
	}

	public P getParameters() {
		return parameters;
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}

	public XSVisualization<V> getVisualization() {
		return visualization;
	}

	public void handleNextPacket(XSEvent event) {
		if (canReadPacket(event)) {
			processNewXSEvent(((XAttributeLiteral) event.get(getParameters().getCaseIdentifier())).getValue(),
					getActivity(event));
		}
	}

	public abstract void processNewXSEvent(String caseId, XEventClass activity);

	public void setEventClasses(XEventClass[] eventClasses) {
		this.eventClasses = eventClasses;
	}

	public void setVisualization(XSVisualization<V> visualization) {
		this.visualization = visualization;
	}
	
	@Override
	public long measureUsedMemory() {
		return super.measureUsedMemory();
	}

}
