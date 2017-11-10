package org.processmining.eventstream.authors.cpn.ui.wizardsteps;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.processmining.eventstream.authors.cpn.parameters.CPN2XSEventStreamCaseIdentification;
import org.processmining.eventstream.authors.cpn.parameters.CPN2XSEventStreamParameters;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.plugins.cpnet.SimulationPropertiesPanel;
import org.processmining.stream.core.enums.CommunicationType;

public class SimulationPropertiesPanelProMWizardStep extends SimulationPropertiesPanel
		implements ProMWizardStep<CPN2XSEventStreamParameters> {

	private static final long serialVersionUID = 4238738412662590919L;

	private JRadioButton caseIdentificationByReplicationButton;
	private JRadioButton customCaseIdentificationRadioButton;
	private ProMTextField customCaseIdentificationTextField;
	private JCheckBox includeAllVariablesOption;
	private JRadioButton setCommSyncButton;
	//	private JCheckBox useSeedField;
	//	private ProMTextField seedField;

	public SimulationPropertiesPanelProMWizardStep(final boolean steps, final boolean repetitions, final boolean delay,
			final boolean updateUI, final boolean timed, final boolean caseIdentification,
			final boolean includeAllVariables, final boolean setCommunicationChannel) {
		super(steps, repetitions, delay, updateUI, timed, false, true, true);
		if (caseIdentification) {
			addCaseIdentificationRadioButtonGroup();
		}
		if (includeAllVariables) {
			includeAllVariablesOption = addCheckBox("Include Variables in Packets", false);
		}
		if (setCommunicationChannel) {
			addSetCommunicationChannelButtonGroup();
		}
		super.getIgnorePage().setSelected(true);
	}

	@Deprecated
	public SimulationPropertiesPanelProMWizardStep(final boolean steps, final boolean repetitions, final boolean delay,
			final boolean updateUI, final boolean timed, final boolean caseIdentification,
			final boolean includeAllVariables, final boolean setCommunicationChannel, boolean seed) {
		super(steps, repetitions, delay, updateUI, timed, false, true, true);
		if (caseIdentification) {
			addCaseIdentificationRadioButtonGroup();
		}
		if (includeAllVariables) {
			includeAllVariablesOption = addCheckBox("Include Variables in Packets", false);
		}
		if (setCommunicationChannel) {
			addSetCommunicationChannelButtonGroup();
		}
		super.getIgnorePage().setSelected(true);

		//		if (seed) {
		//			useSeedField = addCheckBox("Use seed", false);
		//			seedField = addTextField("seed:", "");
		//		}
	}

	private void addCaseIdentificationRadioButtonGroup() {
		final JPanel buttonGroup = new JPanel();
		buttonGroup.setOpaque(false);
		buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.PAGE_AXIS));
		final ButtonGroup caseIdentification = new ButtonGroup();
		caseIdentificationByReplicationButton = createRadioButton(buttonGroup, caseIdentification, "By Repetition");
		final JPanel customCaseIdentificationPanel = new JPanel();
		customCaseIdentificationPanel.setOpaque(false);
		customCaseIdentificationPanel.setLayout(new BoxLayout(customCaseIdentificationPanel, BoxLayout.X_AXIS));
		customCaseIdentificationRadioButton = createRadioButton(customCaseIdentificationPanel, caseIdentification,
				"By a CPN Variable: ", false);
		customCaseIdentificationRadioButton.setSelected(true);
		customCaseIdentificationTextField = new ProMTextField(
				CPN2XSEventStreamCaseIdentification.CPN_VARIABLE.getDefaultValue());
		customCaseIdentificationPanel.add(customCaseIdentificationTextField);
		customCaseIdentificationPanel.add(Box.createHorizontalGlue());
		buttonGroup.add(customCaseIdentificationPanel);
		addProperty("Case Identification", buttonGroup);
	}

	private void addSetCommunicationChannelButtonGroup() {
		final JPanel buttonGroup = new JPanel();
		buttonGroup.setOpaque(false);
		buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.PAGE_AXIS));
		final ButtonGroup setCommButtonGroup = new ButtonGroup();
		createRadioButton(buttonGroup, setCommButtonGroup, "Asynchrounous");
		setCommSyncButton = createRadioButton(buttonGroup, setCommButtonGroup, "Synchrounous");
		setCommSyncButton.setSelected(true);
		addProperty("Stream Communication Type", buttonGroup);
	}

	public String getTitle() {
		return "CPN Tools Based Event Stream Emission Configuration";
	}

	public CPN2XSEventStreamParameters apply(CPN2XSEventStreamParameters model, JComponent component) {
		if (component instanceof SimulationPropertiesPanelProMWizardStep) {
			SimulationPropertiesPanelProMWizardStep userInput = (SimulationPropertiesPanelProMWizardStep) component;
			model.setMaximumNumberOfStepsPerRepetition(userInput.getSteps());
			model.setTotalNumberOfRepetitions(userInput.getRepetitions());
			model.setTransitionDelayMs(userInput.getDelay());
			model.setCaseIdentificationType(userInput.getCaseIdentificationType());
			model.setCaseIdentifier(userInput.getCaseIdentificationValue());
			model.setIncludeVariables(includeAllVariablesOption.isSelected());
			model.setCommunicationType(userInput.getStreamCommunicationType());
			model.setIgnorePage(userInput.isIgnorePage());
			model.setIgnorePatterns(userInput.getIgnorePatterns());
			//			if (userInput.getIsUseSeed()) {
			//				model.setUseSeed(true);
			//				model.setSeed(userInput.getSeed());
			//			} else {
			//				model.setUseSeed(false);
			//			}
		}
		return model;
	}

	public boolean canApply(CPN2XSEventStreamParameters model, JComponent component) {
		return (component instanceof SimulationPropertiesPanelProMWizardStep ? true : false);
	}

	public JComponent getComponent(CPN2XSEventStreamParameters model) {
		return this;
	}

	public CPN2XSEventStreamCaseIdentification getCaseIdentificationType() {
		CPN2XSEventStreamCaseIdentification result;
		if (caseIdentificationByReplicationButton.isSelected()) {
			result = CPN2XSEventStreamCaseIdentification.REPITITION;
		} else {
			result = CPN2XSEventStreamCaseIdentification.CPN_VARIABLE;
		}
		return result;
	}

	public String getCaseIdentificationValue() {
		String result = null;
		if (getCaseIdentificationType() == CPN2XSEventStreamCaseIdentification.CPN_VARIABLE) {
			result = customCaseIdentificationTextField.getText();
		}
		return result;
	}

	public CommunicationType getStreamCommunicationType() {
		CommunicationType type = CommunicationType.ASYNC;
		if (setCommSyncButton.isSelected()) {
			type = CommunicationType.SYNC;
		}
		return type;
	}

//	public boolean getIsUseSeed() {
//		return useSeedField.isSelected();
//	}

	//	public long getSeed() {
	//		return Long.valueOf(seedField.getText()).longValue();
	//	}

}
