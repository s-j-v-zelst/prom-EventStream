package org.processmining.eventstream.readers.acceptingpetrinet;

import javax.swing.JComponent;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.plugins.VisualizeAcceptingPetriNetPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.stream.core.interfaces.XSReaderResultVisualizer;

public class AcceptingPetriNetXSReaderResultVisualizer implements XSReaderResultVisualizer<AcceptingPetriNet> {

	private static AcceptingPetriNetXSReaderResultVisualizer singleton = null;

	private static PluginContext context = null;

	private AcceptingPetriNetXSReaderResultVisualizer(PluginContext context) {
		this.context = context;
	}

	public static AcceptingPetriNetXSReaderResultVisualizer instance(PluginContext context) {
		if (context == null) {
			throw new NullPointerException("The context can not be null");
		} else {
			if (singleton == null) {
				return new AcceptingPetriNetXSReaderResultVisualizer(context);
			} else if (!context.equals(AcceptingPetriNetXSReaderResultVisualizer.context)) {
				return new AcceptingPetriNetXSReaderResultVisualizer(context);
			}
			return singleton;
		}
	}

	public JComponent visualize(AcceptingPetriNet streamBasedObject) {
		return VisualizeAcceptingPetriNetPlugin.visualize(context, streamBasedObject);
	}

}
