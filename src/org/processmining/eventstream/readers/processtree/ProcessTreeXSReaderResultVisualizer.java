package org.processmining.eventstream.readers.processtree;

import javax.swing.JComponent;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.visualization.tree.TreeVisualization;
import org.processmining.stream.core.interfaces.XSReaderResultVisualizer;

public class ProcessTreeXSReaderResultVisualizer implements XSReaderResultVisualizer<ProcessTree> {

	private static ProcessTreeXSReaderResultVisualizer singleton = null;

	private static PluginContext context = null;

	public static ProcessTreeXSReaderResultVisualizer instance(PluginContext context) {
		if (context == null) {
			throw new NullPointerException("The context can not be null");
		} else {
			if (singleton == null) {
				return new ProcessTreeXSReaderResultVisualizer(context);
			} else if (!context.equals(ProcessTreeXSReaderResultVisualizer.context)) {
				return new ProcessTreeXSReaderResultVisualizer(context);
			}
			return singleton;
		}
	}

	private ProcessTreeXSReaderResultVisualizer(PluginContext context) {
		this.context = context;
	}

	public JComponent visualize(ProcessTree tree) {
		TreeVisualization visualizer = new TreeVisualization();
		return visualizer.visualize(context, tree);
	}

}
