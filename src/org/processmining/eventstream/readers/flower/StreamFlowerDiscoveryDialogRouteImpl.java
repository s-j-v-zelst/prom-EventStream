package org.processmining.eventstream.readers.flower;

import org.processmining.eventstream.readers.dialogs.XSEventStreamCaseAndActivityIdentifierDialog;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;

public class StreamFlowerDiscoveryDialogRouteImpl<P extends StreamFlowerDiscoveryAlgorithmParametersImpl>
		implements Route<P> {

	private StreamFlowerDataStructureDialogImpl<P> streamToDFADialog = null;

	public Dialog<P> getNext(Dialog<P> current) {
		if (current instanceof XSEventStreamCaseAndActivityIdentifierDialog) {
			if (streamToDFADialog == null) {
				streamToDFADialog = new StreamFlowerDataStructureDialogImpl<P>(current.getUIPluginContext(),
						StreamFlowerDataStructureDialogImpl.DEFAULT_TITLE, current.getParameters(), current, this);
			}
			return streamToDFADialog;
		}
		return null;
	}
}
