package org.processmining.eventstream.readers.uitl;

import java.util.Collection;

import javax.swing.JOptionPane;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.eventstream.connections.XSEventXSAuthorXSStreamConnectionImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.eventstream.readers.dialogs.SelectXSEventStreamFromXSAuthorDialog;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.stream.core.interfaces.XSAuthor;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;
import org.processmining.widgets.wizard.Wizard;
import org.processmining.widgets.wizard.WizardResult;

public class SelectXSEventStreamFromXSAuthorUtil {

	/**
	 * Allows for fetching an event stream form an author first and subsequently
	 * route using the given route.
	 * 
	 * @param context
	 * @param author
	 * @param parameters
	 * @param route
	 * @return
	 */
	public static <P extends XSEventReaderParameters> WizardResult<P> run(final UIPluginContext context,
			final XSAuthor<XSEvent> author, P parameters, Route<P> route) {
		Collection<XSEventXSAuthorXSStreamConnectionImpl> connections;
		try {
			connections = context.getConnectionManager().getConnections(XSEventXSAuthorXSStreamConnectionImpl.class,
					context);
			if (connections.isEmpty()) {
				JOptionPane.showMessageDialog(null, "The selected author is not connected to an event stream",
						"Author not connected to an Event Stream", JOptionPane.WARNING_MESSAGE);
			} else {
				Dialog<P> d = new SelectXSEventStreamFromXSAuthorDialog<P>(context, "test", parameters, null, route,
						connections);
				return Wizard.show(context, d);
			}
		} catch (ConnectionCannotBeObtained e) {
			//JUST CANCEL THE PLUGIN
		}
		context.getFutureResult(0).cancel(true);
		return null;
	}

}
