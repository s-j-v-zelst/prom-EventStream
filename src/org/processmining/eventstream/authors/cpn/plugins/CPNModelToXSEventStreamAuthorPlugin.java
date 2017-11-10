package org.processmining.eventstream.authors.cpn.plugins;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.cpntools.accesscpn.engine.highlevel.HighLevelSimulator;
import org.cpntools.accesscpn.engine.highlevel.instance.State;
import org.cpntools.accesscpn.model.PetriNet;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.PluginInfo;
import org.processmining.eventstream.authors.cpn.implementations.CPNModelToXSEventStreamAuthorImpl;
import org.processmining.eventstream.authors.cpn.parameters.CPN2XSEventStreamParameters;
import org.processmining.eventstream.authors.cpn.ui.wizardsteps.SimulationPropertiesPanelProMWizardStep;
import org.processmining.eventstream.authors.cpn.utils.CPNetUtils;
import org.processmining.eventstream.core.factories.XSEventStreamFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginCategory;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.Pair;
import org.processmining.framework.util.ui.wizard.ListWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.cpnet.ColouredPetriNet;
import org.processmining.plugins.cpnet.ErrorThrowingColouredPetriNet;
import org.processmining.stream.core.interfaces.XSAuthor;

@Plugin(name = "Generate Event Stream (CPN)", parameterLabels = { "CPN Tools Model", "Parameters" }, returnLabels = {
		"Stream Generator", "Event Stream" }, returnTypes = { XSAuthor.class,
				XSEventStream.class }, help = "This plugin generates an event stream, conforming to the XSEventStream interface from a CPN Tools model."
						+ "The user can specify how the stream should identify cases, either by CPN variable or per run. If the model contains a token generator "
						+ "it is adviced to use a CPN variable for case identification. ", categories = {
								PluginCategory.Analytics, PluginCategory.Enhancement }, keywords = { "stream", "cpn" })
public class CPNModelToXSEventStreamAuthorPlugin {

	@UITopiaVariant(uiLabel = "Generate Event Stream (CPN)", affiliation = UITopiaVariant.EHV, author = PluginInfo.AUTHOR, email = PluginInfo.AUTHOR_CONTACT)
	@PluginVariant(requiredParameterLabels = { 0 })
	public static Object[] apply(final UIPluginContext context, ColouredPetriNet cpnModel) {
		Object[] result = null;
		ProMWizardStep<CPN2XSEventStreamParameters> pan1 = new SimulationPropertiesPanelProMWizardStep(true, true, true,
				false, false, true, true, true);
		List<ProMWizardStep<CPN2XSEventStreamParameters>> list = new ArrayList<>();
		list.add(pan1);
		ListWizard<CPN2XSEventStreamParameters> wizz = new ListWizard<CPN2XSEventStreamParameters>(list);
		CPN2XSEventStreamParameters parameters = ProMWizardDisplay.show(context, wizz,
				new CPN2XSEventStreamParameters());
		if (parameters != null) {
			result = apply(context, cpnModel, parameters);
			XSEventStream stream = (XSEventStream) result[1];
			context.getProvidedObjectManager().createProvidedObject("Event Stream (XSEvent)", stream,
					XSEventStream.class, context);
			context.getGlobalContext().getResourceManager().getResourceForInstance(stream).setFavorite(true);
		} else {
			context.getFutureResult(0).cancel(true);
		}
		return result;
	}

	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public static Object[] apply(final PluginContext context, final ColouredPetriNet cpnModel,
			CPN2XSEventStreamParameters parameters) {
		Object[] result = null;
		XSAuthor<XSEvent> author;
		try {
			ErrorThrowingColouredPetriNet eCPN = new ErrorThrowingColouredPetriNet(cpnModel.getPetriNet());
			context.getProgress().setMinimum(0);
			context.getProgress().setMaximum(4);
			context.getProgress().setValue(0);
			context.log("Fetching CPN model...");
			PetriNet net = eCPN.getPetriNet();
			context.getProgress().inc();
			context.log("Fetching simulator...");
			HighLevelSimulator simulator = eCPN.getSimulatorWithError();
			context.log("Fetching initial marking...");
			State initialMarking = simulator.getMarking();
			context.getProgress().inc();
			author = new CPNModelToXSEventStreamAuthorImpl(net, initialMarking, simulator, parameters);
			context.getProgress().inc();
			context.log("Building stream...");
			XSEventStream stream = XSEventStreamFactory.createXSEventStream(parameters.getCommunicationType());
			author.connect(stream);
			stream.start();
			context.getProgress().inc();
			//			context.getConnectionManager().addConnection(new XSEventXSAuthorXSStreamConnectionImpl(stream, author));
			result = new Object[] { author, stream };
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, CPNetUtils.getCPNParseError(e));
			context.getFutureResult(0).cancel(true);
		}
		return result;
	}

	public static Pair<XSAuthor<XSEvent>, XSEventStream> apply(final ColouredPetriNet cpnModel,
			CPN2XSEventStreamParameters parameters) throws Exception {
		XSAuthor<XSEvent> author;
		ErrorThrowingColouredPetriNet eCPN = new ErrorThrowingColouredPetriNet(cpnModel.getPetriNet());
		PetriNet net = eCPN.getPetriNet();
		HighLevelSimulator simulator = eCPN.getSimulatorWithError();
		State initialMarking = simulator.getMarking();
		author = new CPNModelToXSEventStreamAuthorImpl(net, initialMarking, simulator, parameters);
		XSEventStream stream = XSEventStreamFactory.createXSEventStream(parameters.getCommunicationType());
		author.connect(stream);
		stream.start();
		return new Pair<>(author, stream);
	}
}
