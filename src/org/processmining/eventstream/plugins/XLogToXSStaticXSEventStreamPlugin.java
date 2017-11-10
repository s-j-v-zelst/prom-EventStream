package org.processmining.eventstream.plugins;

import org.deckfour.uitopia.api.event.TaskListener;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.algorithms.XLogToXSStaticXSEventStreamAlgorithm;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.eventstream.dialogs.XLogToXSStaticXSEventStreamDialog;
import org.processmining.eventstream.parameters.XLogToXSStaticXSEventStreamParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Wizard;
import org.processmining.widgets.wizard.WizardResult;

@Plugin(name = "Convert to Static Event Stream", parameterLabels = { "Event Log (XLog)", "Parameters" }, returnLabels = {
		"Static Event Stream (XSEvent)" }, returnTypes = { XSStaticXSEventStream.class })
public class XLogToXSStaticXSEventStreamPlugin {

	@PluginVariant(requiredParameterLabels = { 0 })
	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
	public static XSStaticXSEventStream apply(final UIPluginContext context, final XLog log) {
		Dialog<XLogToXSStaticXSEventStreamParameters> dialog = new XLogToXSStaticXSEventStreamDialog(context,
				new XLogToXSStaticXSEventStreamParameters(), log);
		WizardResult<XLogToXSStaticXSEventStreamParameters> wizResult = Wizard.show(context, dialog);
		if (wizResult.getInteractionResult().equals(TaskListener.InteractionResult.FINISHED)) {
			XSStaticXSEventStream stream = apply(context, log, wizResult.getParameters());
			return stream;
		} else {
			context.getFutureResult(0).cancel(true);
			return null;
		}
	}

	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public static XSStaticXSEventStream apply(final PluginContext context, final XLog log,
			final XLogToXSStaticXSEventStreamParameters parameters) {
		XLogToXSStaticXSEventStreamAlgorithm algorithm = new XLogToXSStaticXSEventStreamAlgorithm(context, log, parameters);
		return algorithm.get();
	}

}
