package edu.wehi.celcalc.cohort.gui.analysis;

import java.io.Serializable;
import java.util.ArrayList;

import edu.wehi.celcalc.cohort.plotting.properties.Plotories;

public class PlotoriesContainer extends ArrayList<Plotories> implements Serializable
{

	private static final long serialVersionUID = 1L;

	public Plotories getPlotories(String plotoriesName)
	{
		for (Plotories plot : this)
		{
			if (plot.getName().equals(plotoriesName))
			{
				return plot;
			}
		}
		return null;
	}
	

}
