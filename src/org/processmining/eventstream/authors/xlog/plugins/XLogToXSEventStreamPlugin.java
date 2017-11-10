package org.processmining.eventstream.authors.xlog.plugins;

import org.deckfour.uitopia.api.event.TaskListener;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.authors.xlog.dialogs.XLogToXSEventDialog;
import org.processmining.eventstream.authors.xlog.implementations.XLogToXSEventAuthor;
import org.processmining.eventstream.authors.xlog.parameters.XLogToXSEventStreamParameters;
import org.processmining.eventstream.core.factories.XSEventStreamFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.stream.core.enums.CommunicationType;
import org.processmining.stream.core.interfaces.XSAuthor;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Wizard;
import org.processmining.widgets.wizard.WizardResult;

@Plugin(name = "Generate Event Stream", parameterLabels = { "Event Log (XLog)", "Parameters" }, returnLabels = {
		"Stream Generator", "Event Stream (XSEvent)" }, returnTypes = { XSAuthor.class, XSEventStream.class })
public class XLogToXSEventStreamPlugin {

	@PluginVariant(requiredParameterLabels = { 0 })
	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
	public static Object[] apply(final UIPluginContext context, final XLog log) {
		Dialog<XLogToXSEventStreamParameters> dialog = new XLogToXSEventDialog(context,
				new XLogToXSEventStreamParameters(), log);
		WizardResult<XLogToXSEventStreamParameters> wizResult = Wizard.show(context, dialog);
		if (wizResult.getInteractionResult().equals(TaskListener.InteractionResult.FINISHED)) {
			Object[] authorStream = apply(context, log, wizResult.getParameters());
			XSEventStream stream = (XSEventStream) authorStream[1];
			context.getProvidedObjectManager().createProvidedObject("Event Stream (XSEvent)", stream,
					XSEventStream.class, context);
			context.getGlobalContext().getResourceManager().getResourceForInstance(stream).setFavorite(true);
			return authorStream;
		} else {
			context.getFutureResult(0).cancel(true);
			return null;
		}
	}

	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public static Object[] apply(final PluginContext context, final XLog log,
			final XLogToXSEventStreamParameters parameters) {
		XSAuthor<XSEvent> author = new XLogToXSEventAuthor(context, log, parameters);
		XSEventStream stream = XSEventStreamFactory.createXSEventStream(CommunicationType.SYNC);
		stream.start();
		author.connect(stream);
		//		context.getConnectionManager().addConnection(new XSEventXSAuthorXSStreamConnectionImpl(stream, author));
		return new Object[] { author, stream };
	}

}
