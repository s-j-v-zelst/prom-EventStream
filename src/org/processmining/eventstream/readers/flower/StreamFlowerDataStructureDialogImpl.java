package org.processmining.eventstream.readers.flower;

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

public class StreamFlowerDataStructureDialogImpl<P extends StreamFlowerDiscoveryAlgorithmParametersImpl>
		extends AbstractRoutableDialog<P> {

	private static final long serialVersionUID = 261093762868290106L;
	public static String DEFAULT_TITLE = "Configure Stream Based Flower Discovery Data Structure";

	private StreamBasedDataStructureSelector<Pair<XEventClass, XEventClass>> allowedDataStructureSelector = new StreamBasedDataStructureSelector<>(
			"Select Data Structure", StreamFlowerDiscoveryAlgorithmImpl.ALLOWED_DATA_STRUCTURES);

	public StreamFlowerDataStructureDialogImpl(UIPluginContext context, String title, P parameters,
			Dialog<P> previous) {
		this(context, title, parameters, previous, null);
	}

	public StreamFlowerDataStructureDialogImpl(UIPluginContext context, String title, P parameters, Dialog<P> previous,
			Route<P> route) {
		super(context, title, parameters, previous, route);
	}

	public void updateParametersOnGetNext() {
		getParameters().setDataStructureType(getDataStructureType());
		getParameters().setDataStructureParameters(getDataStructureParameters());
	}

	public Map<DSParameterDefinition, DSParameter<?>> getDataStructureParameters() {
		return allowedDataStructureSelector.getDataStructureParameterMapping();
	}

	public Type getDataStructureType() {
		return allowedDataStructureSelector.getDataStructureType();
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

	public JComponent visualize() {
		removeAll();
		add(allowedDataStructureSelector);
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

}
