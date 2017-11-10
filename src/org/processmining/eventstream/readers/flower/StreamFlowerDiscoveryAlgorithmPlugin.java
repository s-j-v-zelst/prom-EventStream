package org.processmining.eventstream.readers.flower;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.eventstream.readers.acceptingpetrinet.XSEventStreamToAcceptingPetriNetReader;
import org.processmining.eventstream.readers.dialogs.XSEventStreamCaseAndActivityIdentifierDialog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;
import org.processmining.widgets.wizard.Wizard;
import org.processmining.widgets.wizard.WizardResult;

@Plugin(name = "Discover Accepting Petri Net(s) (Flower Miner)", parameterLabels = { "Event Stream",
		"Stream-Based Flower Miner Parameters" }, returnLabels = { "Flower Miner (Event Stream)" }, returnTypes = {
				XSEventStreamToAcceptingPetriNetReader.class }, userAccessible = true)
public class StreamFlowerDiscoveryAlgorithmPlugin {

	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
	@PluginVariant(variantLabel = "XSStream Flower Miner", requiredParameterLabels = { 0 })
	public XSEventStreamToAcceptingPetriNetReader apply(final UIPluginContext context, final XSEventStream stream) {
		StreamFlowerDiscoveryAlgorithmParametersImpl parameters = new StreamFlowerDiscoveryAlgorithmParametersImpl();
		Route<StreamFlowerDiscoveryAlgorithmParametersImpl> route = new StreamFlowerDiscoveryDialogRouteImpl<>();
		Dialog<StreamFlowerDiscoveryAlgorithmParametersImpl> dialog = new XSEventStreamCaseAndActivityIdentifierDialog<StreamFlowerDiscoveryAlgorithmParametersImpl>(
				context, XSEventStreamCaseAndActivityIdentifierDialog.DEFAULT_TITLE, parameters, null, route);
		WizardResult<StreamFlowerDiscoveryAlgorithmParametersImpl> res = Wizard.show(context, dialog);
		if (res.getInteractionResult().equals(InteractionResult.FINISHED)) {
			return apply(context, stream, res.getParameters());
		} else {
			context.getFutureResult(0).cancel(true);
			return null;
		}
	}

	@PluginVariant(variantLabel = "XSStream Inductive Miner", requiredParameterLabels = { 0, 1 })
	public XSEventStreamToAcceptingPetriNetReader apply(final PluginContext context, final XSEventStream stream,
			StreamFlowerDiscoveryAlgorithmParametersImpl parameters) {
		XSEventStreamToAcceptingPetriNetReader reader = new StreamFlowerDiscoveryAlgorithmImpl(context, parameters);
		reader.start();
		stream.connect(reader);
		return reader;
	}

}
