package org.processmining.eventstream.readers.staticeventstream.dialogs;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.eventstream.readers.staticeventstream.parameters.XSEventStreamToXSStaticEventStreamParameters;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.widgets.wizard.AbstractDialog;
import org.processmining.widgets.wizard.Dialog;

public class XSEventStreamToXSStaticXSEventStreamDialog<P extends XSEventStreamToXSStaticEventStreamParameters>
		extends AbstractDialog<P> {

	private static final long serialVersionUID = -981032618383097498L;
	private static final String NAME = "Event Stream to Static Event Stream Dialog";

	private static final int DEFAULT_NUMBER_OF_EVENTS = 1000;
	private final ProMTextField numEventsField;

	public XSEventStreamToXSStaticXSEventStreamDialog(UIPluginContext context, P parameters, Dialog<P> previous) {
		super(context, NAME, parameters, previous);
		numEventsField = new ProMTextField(String.valueOf(DEFAULT_NUMBER_OF_EVENTS));
	}

	public boolean hasNextDialog() {
		return false;
	}

	public void updateParametersOnGetNext() {
		getParameters().setTotalNumberOfEvents(Integer.valueOf(numEventsField.getText()));
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

	public JComponent visualize() {
		removeAll();
		add(numEventsField);
		revalidate();
		repaint();
		return this;
	}

	protected boolean canProceedToNext() {
		return true;
	}

	protected Dialog<P> determineNextDialog() {
		return null;
	}

	protected String getUserInputProblems() {
		return "";
	}

}
