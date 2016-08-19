package edu.wehi.celcalc.cohort.bell;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.scripts.ScriptCohortByDivision;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;

public class BellController
{
	private final BellView view;
	final String treatment;
	
	public BellController(String treatment, int div, double time)
	{
		this.treatment = treatment;
		view = new BellView(treatment, div, time);
		
		getView().addBtnFitActionListener(e -> {
			autoOpt();
			plot();
			});
		
		getView().addBellable(new Bellable()
		{

			@Override
			public void bellChange(String treatment, int div, double time, String mu, String sigma, String scaleFactor)
			{
				plot();
			}
			
		});
	}
	
	public void addBellable(Bellable bellable)
	{
		getView().addBellable(bellable);
	}
	
	public void plot()
	{
		double mean = 0.0;
		try{mean = (view.getMu().equals(""))? 0.0 : Double.parseDouble(view.getMu());}catch(Exception e){}
		double std = 1.0;
		try{std = (view.getSigma().equals(""))? 0.0 : Double.parseDouble(view.getSigma());}catch(Exception e){}
		plot(measurements, mean, std);
	}
	
	DecimalFormat df = new DecimalFormat(".###");
	
	private void autoOpt()
	{
		GPXYSeries result = new ScriptCohortByDivision().scriptxy(measurements).get(0);
		result.removeAllCoordsWithX(0.0);
		double[] mu_sigma_scale = BellUtilities.bell(result);
		double mu = mu_sigma_scale[0];
		double sigma = mu_sigma_scale[1];
		double scale = mu_sigma_scale[2];
		
		getView().setMu(df.format(mu));
		getView().setSigma(df.format(sigma));
		getView().setScale(df.format(scale));
		
		System.out.println("mu="+mu+" "+ "sigma="+sigma);
	}

	List<Measurement> measurements = new ArrayList<>();
	
	public void inputData(List<Measurement> measurements)
	{
		this.measurements = measurements;
		autoOpt();
		plot();
	}
	
	private Component plot(List<Measurement> measurements, double mean, Double std) 
	{
		GPXYSeries fittedSeries = getFittedSiers();
		List<GPXYSeries> result = new ScriptCohortByDivision().scriptxy(measurements);
		result.add(fittedSeries);
		
		ChartPanel pnl = GPPlotterFuncs.plot(result, "", "", "", GPChartType.LINE);
		getView().plot(pnl);
		return pnl;
	}
	
	private GPXYSeries getFittedSiers()
	{
		double mean = 0.0; 
		try {mean = (view.getMu().equals(""))? 0.0 : Double.parseDouble(view.getMu());}catch(Exception e){}
		double std = 1.0;
		try{std = (view.getSigma().equals(""))? 0.0 : Double.parseDouble(view.getSigma());} catch(Exception e){}
		
		
		return BellUtilities.normFit(treatment, mean, std, 0, 9, Double.parseDouble(view.getScale()), 50);
	}
	
	public BellView getView()
	{
		return view;
	}
	
	public static void main(String[] args)
	{
		BellController controller = new BellController("hello", 2, 0.2);
		
		
		GUI.gui(controller.getView());
	}

}
