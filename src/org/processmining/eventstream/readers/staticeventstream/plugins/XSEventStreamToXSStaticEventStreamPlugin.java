package org.processmining.eventstream.readers.staticeventstream.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.core.implementations.XSStaticXSEventStreamArrayListImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.eventstream.readers.staticeventstream.XSEventStreamToXSStaticXSEventStreamReader;
import org.processmining.eventstream.readers.staticeventstream.dialogs.XSEventStreamToXSStaticXSEventStreamDialog;
import org.processmining.eventstream.readers.staticeventstream.parameters.XSEventStreamToXSStaticEventStreamParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.providedobjects.ProvidedObjectID;
import org.processmining.stream.core.interfaces.XSAuthor;
import org.processmining.stream.core.interfaces.XSReader;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Wizard;
import org.processmining.widgets.wizard.WizardResult;

@Plugin(name = "Create Static Event Stream", parameterLabels = { "Author", "Event Stream",
		"Parameters" }, returnLabels = { "Static Event Stream Constructor",
				"Static Event Stream" }, returnTypes = { XSReader.class, XSStaticXSEventStream.class })
public class XSEventStreamToXSStaticEventStreamPlugin {

	// ACCESS METHOD TO RUN USING THE AUTHOR AS AN INPUT, CURRENTLY DISABLED

	//	@UITopiaVariant(affiliation = "Eindhoven University of Technology", author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl")
	//	@PluginVariant(requiredParameterLabels = { 0 })
	//	public static Object[] run(final UIPluginContext context, final XSAuthor<XSEvent> author) {
	//		XSEventStreamToXSStaticEventStreamParameters parameters = new XSEventStreamToXSStaticEventStreamParameters();
	//		Collection<XSEventXSAuthorXSStreamConnectionImpl> connections;
	//		try {
	//			connections = context.getConnectionManager().getConnections(XSEventXSAuthorXSStreamConnectionImpl.class,
	//					context);
	//			if (connections.isEmpty()) {
	//				JOptionPane.showMessageDialog(null, "The selected author is not connected to an event stream",
	//						"Author not connected to an Event Stream", JOptionPane.WARNING_MESSAGE);
	//			} else {
	//				Route<XSEventStreamToXSStaticEventStreamParameters> route = new XSEventStreamToXSStaticXSEventStreamRoute<>();
	//				Dialog<XSEventStreamToXSStaticEventStreamParameters> streamDialog = new SelectXSEventStreamFromXSAuthorDialog<XSEventStreamToXSStaticEventStreamParameters>(
	//						context, "title", parameters, null, route, connections);
	//				WizardResult<XSEventStreamToXSStaticEventStreamParameters> result = Wizard.show(context, streamDialog);
	//				if (result.getInteractionResult().equals(InteractionResult.FINISHED)) {
	//					return run(context, (XSEventStream) result.getParameters().getConnection()
	//							.getObjectWithRole(XSEventXSAuthorXSStreamConnectionImpl.KEY_STREAM), parameters);
	//				}
	//			}
	//		} catch (ConnectionCannotBeObtained e) {
	//			//JUST CANCEL THE PLUGIN
	//		}
	//		context.getFutureResult(0).cancel(true);
	//		return null;
	//	}

	@PluginVariant(requiredParameterLabels = { 1, 2 })
	public static Object[] run(final PluginContext context, final XSEventStream stream,
			XSEventStreamToXSStaticEventStreamParameters params) {
		XSStaticXSEventStream staticStream = new XSStaticXSEventStreamArrayListImpl();
		ProvidedObjectID provObjId = null;
		if (context != null) {
			provObjId = context.getProvidedObjectManager().createProvidedObject("Static Event Stream", staticStream,
					context);
		}
		XSReader<XSEvent, XSStaticXSEventStream> reader = new XSEventStreamToXSStaticXSEventStreamReader(
				"Event Stream to Static Event Stream Reader", params, staticStream, context, provObjId);
		stream.connect(reader);
		reader.start();
		return new Object[] { reader, staticStream };
	}

	@UITopiaVariant(affiliation = "Eindhoven University of Technology", author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl")
	@PluginVariant(requiredParameterLabels = { 1 })
	public static Object[] run(final UIPluginContext context, final XSEventStream stream) {
		XSEventStreamToXSStaticEventStreamParameters parameters = new XSEventStreamToXSStaticEventStreamParameters();
		Dialog<XSEventStreamToXSStaticEventStreamParameters> dialog = new XSEventStreamToXSStaticXSEventStreamDialog<XSEventStreamToXSStaticEventStreamParameters>(
				context, parameters, null);
		WizardResult<XSEventStreamToXSStaticEventStreamParameters> result = Wizard.show(context, dialog);
		if (result.getInteractionResult().equals(InteractionResult.FINISHED)) {
			return run(context, stream, result.getParameters());
		} else {
			context.getFutureResult(0).cancel(true);
		}
		return null;
	}

	public static XSStaticXSEventStream runContextFree(final XSAuthor<XSEvent> author, final XSEventStream stream,
			XSEventStreamToXSStaticEventStreamParameters params) {
		@SuppressWarnings("unchecked")
		XSReader<XSEvent, XSStaticXSEventStream> reader = (XSReader<XSEvent, XSStaticXSEventStream>) run(null, stream,
				params)[0];
		if (author != null) {
			if (!author.isRunning()) {
				author.start();
			}
		} else {
			params.displayWarning("No author provided.");
		}
		while (!reader.isStopped()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return reader.getCurrentResult();
	}

	public static XSStaticXSEventStream runContextFree(final XSEventStream stream,
			XSEventStreamToXSStaticEventStreamParameters parameters) {
		return runContextFree(null, stream, parameters);
	}

}
