package org.processmining.eventstream.dialogs;

import java.util.List;

import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;

public class XSEventStreamConnectionDialogImpl extends ProMPropertiesPanel {

	private static final long serialVersionUID = 1635172555491228421L;
	private final String KEY_STREAM_COMBO = "Streams Connected to Generator:";
	private final ProMComboBox<XSEventStream> combo;

	public XSEventStreamConnectionDialogImpl(final List<XSEventStream> availableStreams) {
		super("Select Stream");
		combo = addComboBox(KEY_STREAM_COMBO, availableStreams);
	}

	public XSEventStream getSelectedStream() {
		return (XSEventStream) combo.getSelectedItem();
	}

}
