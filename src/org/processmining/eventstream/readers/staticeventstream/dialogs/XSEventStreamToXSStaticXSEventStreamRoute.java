package org.processmining.eventstream.readers.staticeventstream.dialogs;

import org.processmining.eventstream.readers.dialogs.SelectXSEventStreamFromXSAuthorDialog;
import org.processmining.eventstream.readers.staticeventstream.parameters.XSEventStreamToXSStaticEventStreamParameters;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;

public class XSEventStreamToXSStaticXSEventStreamRoute<P extends XSEventStreamToXSStaticEventStreamParameters>
		implements Route<P> {

	public Dialog<P> getNext(Dialog<P> current) {
		if (current instanceof SelectXSEventStreamFromXSAuthorDialog) {
			return new XSEventStreamToXSStaticXSEventStreamDialog<P>(current.getUIPluginContext(),
					current.getParameters(), current);
		} else {
			return null;
		}
	}

}
