package org.processmining.eventstream.readers.dialogs;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.eventstream.readers.abstr.XSEventReaderParameters;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.widgets.wizard.AbstractRoutableDialog;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class XSEventStreamCaseAndActivityIdentifierDialog<P extends XSEventReaderParameters>
		extends AbstractRoutableDialog<P> {

	private static final long serialVersionUID = -7443693702848902867L;
	public static final String DEFAULT_TITLE = "Case and Activity Identification";
	private final ProMTextField caseIdentifier;
	private final ProMTextField activityIdentifier;

	public XSEventStreamCaseAndActivityIdentifierDialog(final UIPluginContext context, final String title,
			final P parameters, final Dialog<P> previous) {
		this(context, title, parameters, previous, null);
	}

	public XSEventStreamCaseAndActivityIdentifierDialog(final UIPluginContext context, final String title,
			final P parameters, final Dialog<P> previous, final Route<P> route) {
		super(context, title, parameters, previous, route);
		caseIdentifier = new ProMTextField(XSEventSignature.TRACE,
				"Select what \"key\" to use in order to identify the case within this data packet.");
		activityIdentifier = new ProMTextField(XConceptExtension.KEY_NAME,
				"Select what \"key\" to use in order to identify the activity within this data packet.");
	}

	private void addCaseIdentificationParameter() {
		JLabel label = SlickerFactory.instance().createLabel("Case Identifier:");
		add(label);
		add(caseIdentifier);
	}

	private void addActivityIdentificationParameter() {
		JLabel label = SlickerFactory.instance().createLabel("Activity Identifier:");
		add(label);
		add(activityIdentifier);
	}

	public JComponent visualize() {
		removeAll();
		addCaseIdentificationParameter();
		addActivityIdentificationParameter();
		revalidate();
		repaint();
		return this;
	}

	protected boolean canProceedToNext() {
		return caseIdentifier.getText().length() != 0 && activityIdentifier.getText().length() != 0;
	}

	protected String getUserInputProblems() {
		String message = "<html><p> Please provide: </p><ul>";
		message += caseIdentifier.getText().length() == 0 ? "<li> A Case Identifier </li>" : "";
		message += activityIdentifier.getText().length() == 0 ? "<li> An Event Identifier </li>" : "";
		message += "</ul></html>";
		return message;
	}

	public void updateParametersOnGetNext() {
		getParameters().setCaseIdentifier(caseIdentifier.getText());
		getParameters().setActivityIdentifier(activityIdentifier.getText());
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

}
