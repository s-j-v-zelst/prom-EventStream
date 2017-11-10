package org.processmining.eventstream.plugins.config.wizz;

import javax.swing.JComponent;

import org.processmining.eventstream.readers.staticeventstream.parameters.XSEventStreamToXSStaticEventStreamParameters;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;

public class XSEventStreamToXSStaticEventStreamWizardStep extends ProMPropertiesPanel
		implements ProMWizardStep<XSEventStreamToXSStaticEventStreamParameters> {

	private static final long serialVersionUID = 2807865486489174303L;
	private static final String NAME = "Event Stream to Static Event Stream Dialog";

	private static final int DEFAULT_NUMBER_OF_EVENTS = 1000;
	private final ProMTextField numEventsField;
	private int numberOfEvents = DEFAULT_NUMBER_OF_EVENTS;

	public XSEventStreamToXSStaticEventStreamWizardStep() {
		super(NAME);
		numEventsField = addTextField("Number of events in static stream:", String.valueOf(DEFAULT_NUMBER_OF_EVENTS));
	}

	public XSEventStreamToXSStaticEventStreamParameters apply(XSEventStreamToXSStaticEventStreamParameters model,
			JComponent component) {
		if (component instanceof XSEventStreamToXSStaticEventStreamWizardStep) {
			XSEventStreamToXSStaticEventStreamWizardStep input = (XSEventStreamToXSStaticEventStreamWizardStep) component;
			model.setTotalNumberOfEvents(Integer.valueOf(input.getNumEventsField().getText()));
		}
		return model;
	}

	public boolean canApply(XSEventStreamToXSStaticEventStreamParameters model, JComponent component) {
		return component instanceof XSEventStreamToXSStaticEventStreamWizardStep;
	}

	public JComponent getComponent(XSEventStreamToXSStaticEventStreamParameters model) {
		return this;
	}

	public String getTitle() {
		return NAME;
	}

	/**
	 * @return the numberOfEvents
	 */
	public int getNumberOfEvents() {
		return numberOfEvents;
	}

	/**
	 * @param numberOfEvents
	 *            the numberOfEvents to set
	 */
	public void setNumberOfEvents(int numberOfEvents) {
		this.numberOfEvents = numberOfEvents;
	}

	/**
	 * @return the numEventsField
	 */
	public ProMTextField getNumEventsField() {
		return numEventsField;
	}

}
