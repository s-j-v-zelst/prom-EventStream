package org.processmining.eventstream.readers.xlog.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;

import javax.swing.JComponent;

import org.jfree.chart.ChartPanel;
import org.processmining.stream.util.views.DynamicTimeSeriesBarChart;

@Deprecated
public class XSEventStreamToXLogView extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1518178204773772949L;

	private DynamicTimeSeriesBarChart chart;
	private ChartPanel chartPanel;

	public XSEventStreamToXLogView() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		layout.weightx = layout.weighty = 1;
		layout.gridx = layout.gridy = 1;
		layout.anchor = GridBagConstraints.FIRST_LINE_START;

		this.chart = new DynamicTimeSeriesBarChart(60, 1, "Reader Activity", "Time", "# Messages received");
		this.chartPanel = new ChartPanel(this.chart.getChartInstance());

		this.add(this.chartPanel, layout);
	}

	public void newMessageReceived(Date date) {
		this.chart.addCategoryDataPoint(date, "new message received");
	}

}
