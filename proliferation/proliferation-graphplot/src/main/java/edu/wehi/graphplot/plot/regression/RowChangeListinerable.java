package edu.wehi.graphplot.plot.regression;

public interface RowChangeListinerable
{
	
	public void rowChanged(double regressionMin, double regressionMax, double a, double b, double r2, boolean isShowingLine, boolean isShowingData);

}
