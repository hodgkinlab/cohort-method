package edu.wehi.graphplot.plot.regression;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import edu.wehi.GUI;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.regression.row.RegressionRowView;
import edu.wehi.swing.GBHelper;

public class RegressionView extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	ChartPanel mdnPlot = null;
	JPanel pnlPlot = new JPanel(new BorderLayout());
	JPanel pnlController = new JPanel(new GridBagLayout());
	
	public RegressionView()
	{
		setLayout(new BorderLayout());
		add(pnlPlot,        BorderLayout.CENTER);
		//add(pnlController, 	BorderLayout.SOUTH);
		
		//RegressionRowView.addHeadings(pnlController, gbHelper);
		
//		pnlController.add(new JLabel("Name")			{private static final long serialVersionUID = 1L;	{setToolTipText("Name of Data.");}}, 													gbHelper.nextRow().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("   Min   ")		{private static final long serialVersionUID = 1L;	{setToolTipText("Minimum point to be included in regression.");}}, 						gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("Slider Value")	{private static final long serialVersionUID = 1L;	{setToolTipText("Select your range of data.");}}, 										gbHelper.nextCol().expandW().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("   Max   ")		{private static final long serialVersionUID = 1L;	{setToolTipText("Maximum point to be included in regression.");}}, 						gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("y =      ax    ")		{private static final long serialVersionUID = 1L;	{setToolTipText("<html>Time till first division: y = ax + b <br/> 1st div = 1/a</html>");}},			gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("   +   b ")		{private static final long serialVersionUID = 1L;	{setToolTipText("<html>Time till subsequent division y = ax + b. <br/> 2ndiv = (0.5 - b)/a </html>");}}, gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("   r^2   ")		{private static final long serialVersionUID = 1L;	{setToolTipText("Coefficient of correlation^2.");}}, 									gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("line")			{private static final long serialVersionUID = 1L;	{setToolTipText("Toggle the regression line on and off.");}}, 							gbHelper.nextCol().align(GBHelper.CENTER));
//		pnlController.add(new JLabel("data")			{private static final long serialVersionUID = 1L;	{setToolTipText("Toggle the raw data on and off.");}}, 									gbHelper.nextCol().align(GBHelper.CENTER));
		
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new RegressionView());
	}

	public ChartPanel plot(Collection<GPXYSeries> seriess, String title, String xAxis, String yAxis, GPChartType type)
	{
		ChartPanel plot = GPPlotterFuncs.plot(seriess, title, xAxis, yAxis, type);
		pnlPlot.removeAll();
		pnlPlot.add(plot, BorderLayout.CENTER);
		pnlPlot.revalidate();
		pnlPlot.repaint();
		return plot;
	}

	public void clearPlot()
	{
		pnlPlot.removeAll();
		pnlPlot.revalidate();
		pnlPlot.repaint();
	}

	GBHelper gbHelper = new GBHelper();
	
	public JPanel getControllerPanel()
	{
		return pnlController;
	}
	
	public GBHelper getControllerGBHelper()
	{
		return gbHelper;
	}

	public Component getPlot()
	{
		return pnlPlot;
	}
	
	public void setChartPanel(ChartPanel cp)
	{
		mdnPlot = cp;
	}
	
	public ChartPanel getChartPanel()
	{
		return mdnPlot;
	}
}
