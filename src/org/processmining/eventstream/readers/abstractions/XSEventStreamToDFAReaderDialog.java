package org.processmining.eventstream.readers.abstractions;

import java.util.Map;

import javax.swing.JComponent;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.util.Pair;
import org.processmining.stream.dialog.StreamBasedDataStructureSelector;
import org.processmining.stream.model.datastructure.DSParameter;
import org.processmining.stream.model.datastructure.DSParameterDefinition;
import org.processmining.stream.model.datastructure.DataStructure.Type;
import org.processmining.widgets.wizard.AbstractRoutableDialog;
import org.processmining.widgets.wizard.Dialog;
import org.processmining.widgets.wizard.Route;

@Deprecated // -> moved to StreamAbstractRepresentation package
public class XSEventStreamToDFAReaderDialog<P extends XSEventStreamToDFAReaderParameters>
		extends AbstractRoutableDialog<P> {

	public static String DEFAULT_TITLE = "Configure Event Stream to Directly Follows Abstraction Reader";

	private static final long serialVersionUID = 4755769879740283267L;

	private StreamBasedDataStructureSelector<Pair<XEventClass, XEventClass>> activityActivityDataStructureSelector = new StreamBasedDataStructureSelector<>(
			"Select Activity-Activity (AxA) Data Structure",
			CAxAADataStoreBasedDFAReaderImpl.DEFAULT_ALLOWED_ACTIVITY_ACTIVITY_DATA_STRUCTURES);

	private StreamBasedDataStructureSelector<Pair<String, XEventClass>> caseActivityDataStructureSelector = new StreamBasedDataStructureSelector<>(
			"Select Case-Activity (CxA) Data Structure",
			CAxAADataStoreBasedDFAReaderImpl.DEFAULT_ALLOWED_CASE_ACTIVITY_DATA_STRUCTURES);

	public XSEventStreamToDFAReaderDialog(UIPluginContext context, String title, P parameters, Dialog<P> previous) {
		this(context, title, parameters, previous, null);
	}

	public XSEventStreamToDFAReaderDialog(UIPluginContext context, String title, P parameters, Dialog<P> previous,
			Route<P> route) {
		super(context, title, parameters, previous, route);
	}

	protected boolean canProceedToNext() {
		return true;
	}

	public Map<DSParameterDefinition, DSParameter<?>> getActivityActivityDataStructureParameters() {
		return activityActivityDataStructureSelector.getDataStructureParameterMapping();
	}

	public Type getActivityActivityDataStructureType() {
		return activityActivityDataStructureSelector.getDataStructureType();
	}

	public Map<DSParameterDefinition, DSParameter<?>> getCaseActivityDataStructureParameters() {
		return caseActivityDataStructureSelector.getDataStructureParameterMapping();
	}

	public Type getCaseActivityDataStructureType() {
		return caseActivityDataStructureSelector.getDataStructureType();
	}

	public JComponent visualize() {
		removeAll();
		add(activityActivityDataStructureSelector);
		add(caseActivityDataStructureSelector);
		revalidate();
		repaint();
		return this;
	}

	protected String getUserInputProblems() {
		return "";
	}

	public void updateParametersOnGetNext() {
		getParameters().setActivityActivityDataStructureParameters(getActivityActivityDataStructureParameters());
		getParameters().setActivityActivityDataStructureType(getActivityActivityDataStructureType());
		getParameters().setCaseActivityDataStructureParameters(getCaseActivityDataStructureParameters());
		getParameters().setCaseActivityDataStructureType(getCaseActivityDataStructureType());
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

}
