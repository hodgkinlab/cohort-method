package demo;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration application showing a time series chart where you can
 * dynamically add (random) data by clicking on a button.
 */
public class ExampleDynamicDataDemo1 extends ApplicationFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public ExampleDynamicDataDemo1(String title) {
        super(title);
        MyDemoPanel demoPanel = new MyDemoPanel();
        setContentPane(demoPanel);
    }

    static class MyDemoPanel extends JPanel implements ActionListener {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** The time series data. */
        private TimeSeries series;

        /** The most recent value added. */
        private double lastValue = 100.0;

        /**
         * Creates a new instance.
         */
        public MyDemoPanel() {
            super(new BorderLayout());
            this.series = new TimeSeries("Random Data");
            TimeSeriesCollection dataset = new TimeSeriesCollection(
                    this.series);
            ChartPanel chartPanel = new ChartPanel(createChart(dataset));
            chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            JButton button = new JButton("Add New Data Item");
            button.setActionCommand("ADD_DATA");
            button.addActionListener(this);
            buttonPanel.add(button);

            add(chartPanel);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        /**
         * Creates a sample chart.
         *
         * @param dataset  the dataset.
         *
         * @return A sample chart.
         */
        private JFreeChart createChart(XYDataset dataset) {
            JFreeChart result = ChartFactory.createTimeSeriesChart( 
                    "Dynamic Data Demo", "Time", "Value", dataset);
            XYPlot plot = (XYPlot) result.getPlot();
            ValueAxis axis = plot.getDomainAxis();
            axis.setAutoRange(true);
            axis.setFixedAutoRange(60000.0);  // 60 seconds
            axis = plot.getRangeAxis();
            axis.setRange(0.0, 200.0);
            return result;
        }

        /**
         * Handles a click on the button by adding new (random) data.
         *
         * @param e  the action event.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("ADD_DATA")) {
                double factor = 0.90 + 0.2 * Math.random();
                this.lastValue = this.lastValue * factor;
                Millisecond now = new Millisecond();
                System.out.println("Now = " + now.toString());
                this.series.add(new Millisecond(), this.lastValue);
            }
        }

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        return new ExampleDynamicDataDemo1.MyDemoPanel();
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        ExampleDynamicDataDemo1 demo = new ExampleDynamicDataDemo1(
                "JFreeChart: DynamicDataDemo1.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}

