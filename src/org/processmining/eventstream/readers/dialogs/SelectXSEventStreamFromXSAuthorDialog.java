package org.processmining.eventstream.readers.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.eventstream.connections.XSEventXSAuthorXSStreamConnectionImpl;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.widgets.wizard.AbstractRoutableDialog;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class SelectXSEventStreamFromXSAuthorDialog<P extends XSEventReaderParameters>
		extends AbstractRoutableDialog<P> {

	private static final long serialVersionUID = 1406425877389692937L;

	private final List<XSEventXSAuthorXSStreamConnectionImpl> connections;
	private final JComboBox<XSEventStream> streamSelector;

	public SelectXSEventStreamFromXSAuthorDialog(UIPluginContext context, String title, P parameters,
			Dialog<P> previous, Collection<XSEventXSAuthorXSStreamConnectionImpl> connections) {
		this(context, title, parameters, previous, null, connections);
	}

	public SelectXSEventStreamFromXSAuthorDialog(UIPluginContext context, String title, P parameters,
			Dialog<P> previous, Route<P> route, Collection<XSEventXSAuthorXSStreamConnectionImpl> connections) {
		super(context, title, parameters, previous, route);
		this.connections = new ArrayList<>(connections);
		Collections.sort(this.connections, new Comparator<XSEventXSAuthorXSStreamConnectionImpl>() {
			public int compare(XSEventXSAuthorXSStreamConnectionImpl o1, XSEventXSAuthorXSStreamConnectionImpl o2) {
				Date d1 = new Date((long) o1.getObjectWithRole(XSEventXSAuthorXSStreamConnectionImpl.KEY_TIMESTAMP));
				Date d2 = new Date((long) o2.getObjectWithRole(XSEventXSAuthorXSStreamConnectionImpl.KEY_TIMESTAMP));
				return -1 * d1.compareTo(d2);
			}
		});
		this.streamSelector = createConnectionOverview(this.connections);
	}

	private JComboBox<XSEventStream> createConnectionOverview(
			Collection<XSEventXSAuthorXSStreamConnectionImpl> connections) {
		@SuppressWarnings("unchecked")
		JComboBox<XSEventStream> combo = SlickerFactory.instance().createComboBox(new XSEventStream[] {});
		for (XSEventXSAuthorXSStreamConnectionImpl conn : connections) {
			combo.addItem((XSEventStream) conn.getObjectWithRole(XSEventXSAuthorXSStreamConnectionImpl.KEY_STREAM));
		}
		return combo;
	}

	public JComponent visualize() {
		removeAll();
		add(SlickerFactory.instance().createLabel("Please select a stream on which this Author is writing:"));
		add(streamSelector);
		revalidate();
		repaint();
		return this;
	}

	protected boolean canProceedToNext() {
		return true;
	}

	protected String getUserInputProblems() {
		return "";
	}

	public void updateParametersOnGetNext() {
		XSEventStream selectedStream = (XSEventStream) streamSelector.getSelectedItem();
		for (XSEventXSAuthorXSStreamConnectionImpl conn : connections) {
			if (conn.getObjectWithRole(XSEventXSAuthorXSStreamConnectionImpl.KEY_STREAM).equals(selectedStream)) {
				getParameters().setConnection(conn);
				return;
			}
		}
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

}
