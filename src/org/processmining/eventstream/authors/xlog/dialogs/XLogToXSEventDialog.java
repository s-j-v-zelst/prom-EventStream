package org.processmining.eventstream.authors.xlog.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.eventstream.authors.xlog.parameters.XLogToXSEventStreamParameters;
import org.processmining.widgets.wizard.AbstractDialog;
import org.processmining.widgets.wizard.Dialog;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class XLogToXSEventDialog extends AbstractDialog<XLogToXSEventStreamParameters> {

	private static final long serialVersionUID = 1345463793436368796L;
	private static final String TITLE = "Configure Event Log Based Stream Generator";
	private boolean containsTimeExtension;
	private JFormattedTextField emissionRateInput = new JFormattedTextField(NumberFormat.getIntegerInstance());
	private XLog log;

	private JCheckBox orderRelativeToTimeStamp = SlickerFactory.instance()
			.createCheckBox("Order relative to time-stamps", false);
	private ActionListener speedEmissionChange = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO: introduce if orderRelativeToTimeStamp was set manually or by this function
			if (useSpecifiedEmissionRate.isSelected()) {
				emissionRateInput.setEnabled(true);
				orderRelativeToTimeStamp.setSelected(false);
				orderRelativeToTimeStamp.setEnabled(true);
			} else {
				// if we use log-based emission rates, we must make sure that we order the events relative to time stamp.
				emissionRateInput.setEnabled(false);
				orderRelativeToTimeStamp.setSelected(true);
				orderRelativeToTimeStamp.setEnabled(false);
			}
		}
	};

	private JCheckBox tagEventOrderNumberInTrace = SlickerFactory.instance()
			.createCheckBox("Tag event's order within trace", false);
	private JCheckBox tagFinalEvent = SlickerFactory.instance().createCheckBox("Tag final events in trace", false);
//	private JRadioButton useRealLogSpeed = SlickerFactory.instance()
//			.createRadioButton("Use real log-based timing data");
	private JRadioButton useSpecifiedEmissionRate = SlickerFactory.instance().createRadioButton("Use emission speed");
	private JCheckBox xsEventAdditionalDecoration = SlickerFactory.instance()
			.createCheckBox("Include all additional event data elements", false);

	private boolean canIdentifyTrace;
	private JComboBox<String> traceIdentifier;

	private JComboBox<XEventClassifier> eventClassifiers;

	public XLogToXSEventDialog(final UIPluginContext context, final XLogToXSEventStreamParameters parameters,
			XLog log) {
		super(context, TITLE, parameters, null);
		this.log = log;
		containsTimeExtension = checkIfTimeExtensionIsPresentInLog(log);
		canIdentifyTrace = checkIfCanIdentifyTrace(log);
	}

	private boolean checkIfCanIdentifyTrace(XLog log) {
		return !log.getGlobalTraceAttributes().isEmpty();
	}

	protected boolean canProceedToNext() {
		return true;
	}

	private boolean checkIfTimeExtensionIsPresentInLog(XLog log) {
		java.util.List<XAttribute> extensions = log.getGlobalEventAttributes();
		for (XAttribute extension : extensions) {
			if (extension.getKey().equals(XTimeExtension.KEY_TIMESTAMP)) {
				return true;
			}
		}
		return false;
	}

	protected Dialog<XLogToXSEventStreamParameters> determineNextDialog() {
		return null;
	}

	public JPanel emissionSpeedComponenets() {
		JPanel panel = SlickerFactory.instance().createRoundedPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();

		layout.gridx = layout.gridy = 0;
		layout.weightx = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.anchor = GridBagConstraints.FIRST_LINE_START;
		panel.add(SlickerFactory.instance().createLabel("<html><h3>XSEvent emission speed</h3></html>"), layout);
		layout.gridy++;

		ButtonGroup speedEmissionOptions = new ButtonGroup();

//		if (this.containsTimeExtension) {
//			speedEmissionOptions.add(this.useRealLogSpeed);
//			this.useRealLogSpeed.setSelected(false);
//			this.useRealLogSpeed.addActionListener(this.speedEmissionChange);
//		}

		speedEmissionOptions.add(this.useSpecifiedEmissionRate);
		this.useSpecifiedEmissionRate.setSelected(true);
		this.useSpecifiedEmissionRate.addActionListener(this.speedEmissionChange);

		//		if (this.containsTimeExtension) {
		//			// add radio button for real speed
		//			panel.add(this.useRealLogSpeed, layout);
		//			layout.gridy++;
		//		}

		layout.weightx = 0;

		// add radio button for a specified throughput rate
		panel.add(this.useSpecifiedEmissionRate, layout);
		layout.gridx++;

		layout.weightx = 1;

		this.emissionRateInput.setValue(10);
		this.emissionRateInput.setColumns(4);
		panel.add(this.emissionRateInput, layout);
		layout.gridx++;
		panel.add(SlickerFactory.instance().createLabel(" data-packets / second"), layout);

		return panel;
	}

	private void evaluateDataPacketDecoration() {
		if (this.xsEventAdditionalDecoration.isSelected()) {
			getParameters().setAdditionalDecoration(true);
		}
	}

	private void evaluateEmissionOrder() {
		if (orderRelativeToTimeStamp.isSelected()) {
			getParameters().setEmissionOrdering(XLogToXSEventStreamParameters.EmissionOrdering.TIME_STAMP);
		} else {
			getParameters().setEmissionOrdering(XLogToXSEventStreamParameters.EmissionOrdering.LOG);
		}
	}

	private void evaluateEmissionSpeed() {
//		if (useRealLogSpeed.isSelected()) {
//			getParameters().setEmissionSpeedType(XLogToXSEventStreamParameters.EmissionSpeedType.REAL_LOG);
//		} else 
			if (useSpecifiedEmissionRate.isSelected()) {
			getParameters().setEmissionSpeedType(XLogToXSEventStreamParameters.EmissionSpeedType.RATE);
			if (emissionRateInput.getValue() instanceof Integer) {
				getParameters().setEmissionRate((Integer) this.emissionRateInput.getValue());
			} else if (emissionRateInput.getValue() instanceof Long) {
				getParameters().setEmissionRate(((Long) (this.emissionRateInput.getValue())).intValue());
			}
		}
	}

	private void evaluateTagFinalEvents() {
		if (this.tagFinalEvent.isSelected()) {
			getParameters().setTagFinalEvent(true);
		}
	}

	private void evaluateUseEventOrderTag() {
		if (this.tagEventOrderNumberInTrace.isSelected()) {
			getParameters().setTagEventOrderNumber(true);
		}
	}

	private JPanel traceConfigurationComponent() {
		JPanel panel = SlickerFactory.instance().createRoundedPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();

		// position (0,0)
		layout.gridx = 0;
		layout.gridy = 0;
		// span in x-axis
		layout.weightx = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;

		panel.add(SlickerFactory.instance().createLabel("<html><h3>Trace Identification</h3></html>"), layout);
		layout.gridy++;
		panel.add(SlickerFactory.instance().createLabel("<html><p>Select trace identifier: </p></html>"), layout);
		layout.gridy++;
		traceIdentifier = constructTraceIdentifierList();
		panel.add(traceIdentifier, layout);
		return panel;
	}

	public JPanel eventSequenceConfigruationComponents() {
		JPanel panel = SlickerFactory.instance().createRoundedPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();

		// position (0,0)
		layout.gridx = 0;
		layout.gridy = 0;
		// span in x-axis
		layout.weightx = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;
		panel.add(SlickerFactory.instance().createLabel("<html><h3>XSEvent identification</h3></html>"), layout);

		// position (0,y++)
		layout.gridy++;

		eventClassifiers = constructEventClassifiersList();
		panel.add(eventClassifiers, layout);
		layout.gridy++;

		panel.add(this.tagEventOrderNumberInTrace, layout);
		layout.gridy++;

		panel.add(this.tagFinalEvent, layout);

		return panel;
	}

	protected String getUserInputProblems() {
		return "";
	}

	public boolean hasNextDialog() {
		return false;
	}

	public JPanel timingConfigurationComponents() {
		JPanel panel = SlickerFactory.instance().createRoundedPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();

		// position (0,0)
		layout.gridx = 0;
		layout.gridy = 0;
		// span in x-axis
		layout.weightx = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;
		panel.add(SlickerFactory.instance().createLabel("<html><h3>XSEvent emission order</h3></html>"), layout);

		// position (0,y++)
		layout.gridy++;

		panel.add(this.orderRelativeToTimeStamp, layout);
		return panel;
	}

	public void updateParametersOnGetNext() {
		if (canIdentifyTrace) {
			for (XAttribute a : log.getGlobalTraceAttributes()) {
				if (a.getKey().equals(traceIdentifier.getSelectedItem()))
					;
				getParameters().setTraceClassifier(a);
			}
		}
		getParameters().setEventClassifier((XEventClassifier) eventClassifiers.getSelectedItem());
		evaluateUseEventOrderTag();
		evaluateTagFinalEvents();
		evaluateEmissionOrder();
		evaluateEmissionSpeed();
		evaluateDataPacketDecoration();
	}

	public void updateParametersOnGetPrevious() {
		updateParametersOnGetNext();
	}

	public JComponent visualize() {
		removeAll();
		setOpaque(false);
		setLayout(new GridBagLayout());

		GridBagConstraints layout = new GridBagConstraints();
		layout.gridx = 0;
		layout.gridy = 0;
		layout.weightx = 1;
		layout.weighty = 0;
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.anchor = GridBagConstraints.NORTH;

		if (canIdentifyTrace) {
			add(traceConfigurationComponent(), layout);
			layout.gridy++;
		}

		add(eventSequenceConfigruationComponents(), layout);
		layout.gridy++;

		if (containsTimeExtension) {
			add(timingConfigurationComponents(), layout);
			layout.gridy++;
		}

		add(emissionSpeedComponenets(), layout);
		layout.gridy++;

		layout.weighty = 1;
		add(xsEventDecorationComponent(), layout);

		revalidate();
		repaint();
		return this;
	}

	private JComboBox<XEventClassifier> constructEventClassifiersList() {
		eventClassifiers = new JComboBox<>();
		for (XEventClassifier c : log.getClassifiers()) {
			eventClassifiers.addItem(c);
		}
		return eventClassifiers;
	}

	private JComboBox<String> constructTraceIdentifierList() {
		traceIdentifier = new JComboBox<String>();
		for (XAttribute a : log.getGlobalTraceAttributes()) {
			traceIdentifier.addItem(a.getKey());
		}
		return traceIdentifier;

	}

	public JPanel xsEventDecorationComponent() {
		JPanel panel = SlickerFactory.instance().createRoundedPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.gridx = layout.gridy = 0;
		layout.weightx = 1;
		layout.fill = GridBagConstraints.HORIZONTAL;
		panel.add(SlickerFactory.instance().createLabel("<html><h3>XSEvent decoration</h3></html>"), layout);
		layout.gridy++;
		panel.add(xsEventAdditionalDecoration, layout);
		return panel;
	}

}
