package edu.wehi.graphplot.plot.regression;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jfree.chart.ChartPanel;

import edu.wehi.GUI;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPPlotterFuncsActionListiner;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.LineType;
import edu.wehi.graphplot.plot.regression.row.RegressionRowModel;
import edu.wehi.graphplot.plot.regression.row.RegressionRowView;

public class RegressionController
{
	final RegressionModel model;
	RegressionView view = new RegressionView();
	
	public RegressionController(RegressionModel model, ActionListener colorBtnListiner)
	{
		this.model = model;
		List<RegressionRowModel> rows = model.getRegressionRows();
		
		for (int i = 0; i < rows.size(); i++)
		{
			RegressionRowModel r = rows.get(i);
			final int currIdx = i;
			doRegression(r, i);
			r.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					//if (arg0.getPropertyName().equals(RegressionRowModel.SSE_PROPERTY)) return;
					refreshPlot();
				}
			});

			RegressionRowView rowview = new RegressionRowView(r);
			rowview.addBtnRegActionListiner(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					doRegression(r, currIdx);
				}

			});

			rowview.addBtnColorActionListiner(colorBtnListiner);

			rowview.addToComponent(
					view.getControllerPanel(), view.getControllerGBHelper());
		}
	}
	
	final void doRegression(RegressionRowModel r, int i)
	{
		RegressionLine regressionLine = model.regression(r, i);
		r.setDiv1( (0.5-regressionLine.getB())/regressionLine.getA());
		r.setDiv2(1/regressionLine.getA());
		//r.setAB(regressionLine.getA(),regressionLine.getB());
	}
	
	ChartPanel plot(
			Collection<GPXYSeries> seriess,
			String title, String xAxis, String yAxis,
			GPChartType type)
	{
		if (seriess == null)
		{
			view.clearPlot();
			return null;
		}
		
		ChartPanel panel = view.plot(seriess, title, xAxis, yAxis, type);
		
//		new GPPlotterFuncsActionListiner(panel)
//		{
//			@Override
//			public void pointClicked(double x, double y, String series)
//			{
//				model.pointIncludeStatusChanged(x,y,series);
//				refreshPlot();
//			}
//		}; 
		return panel;
	}
	
	public ChartPanel refreshPlot()
	{
		ChartPanel mdnPlot = plot(
			model.getSeries(), model.getTitle(),
			model.getXAxis(), model.getYAxis(), GPChartType.LINE);
		
		return mdnPlot;
	}
	
	public RegressionView getView()
	{
		return view;
	}
	
	public static void main(String[] args) 
	{
		List<GPXYSeries> seriess = new ArrayList<>();
		
		List<GPCoordinateWithName> coords = new ArrayList<GPCoordinateWithName>();
		coords.add(new GPCoordinateWithNameImp(0.0, 	0, 			0, ""));
		coords.add(new GPCoordinateWithNameImp(2.0, 	2, 			0.3, ""));
		coords.add(new GPCoordinateWithNameImp(4.0, 	4, 			0.1, ""));
		coords.add(new GPCoordinateWithNameImp(5.0, 	5.2, 		0.2, ""));
		coords.add(new GPCoordinateWithNameImp(6.0, 	5.99, 		0.1, ""));
		coords.add(new GPCoordinateWithNameImp(8.0, 	7.4, 		0.1, ""));
		coords.add(new GPCoordinateWithNameImp(10.0, 	0.5, 		0.1, ""));


		GPXYSeries hello = new GPXYSeries(coords);
		
		hello.setName("hello");
		hello.setLineType(LineType.POINTS);
		seriess.add(hello);
		
		RegressionController controller = new RegressionController(
				new RegressionModel(seriess, "title", "x", "y"), null);
		controller.refreshPlot();
		
		GUI.gui(controller.getView());
	}
}
