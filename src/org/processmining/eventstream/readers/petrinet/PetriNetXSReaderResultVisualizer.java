package org.processmining.eventstream.readers.petrinet;

import javax.swing.JComponent;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.stream.core.interfaces.XSReaderResultVisualizer;

public class PetriNetXSReaderResultVisualizer implements XSReaderResultVisualizer<Petrinet> {

	private static PetriNetXSReaderResultVisualizer singleton = null;

	private static PluginContext context = null;

	public static PetriNetXSReaderResultVisualizer instance(PluginContext context) {
		if (context == null) {
			throw new NullPointerException("The context can not be null");
		} else {
			if (singleton == null) {
				return new PetriNetXSReaderResultVisualizer(context);
			} else if (!context.equals(PetriNetXSReaderResultVisualizer.context)) {
				return new PetriNetXSReaderResultVisualizer(context);
			}
			return singleton;
		}
	}

	private PetriNetXSReaderResultVisualizer(PluginContext context) {
		this.context = context;
	}

	public JComponent visualize(Petrinet net) {
		return ProMJGraphVisualizer.instance().visualizeGraph(context, net);
	}

}
