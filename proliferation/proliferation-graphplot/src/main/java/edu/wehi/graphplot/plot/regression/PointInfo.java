package edu.wehi.graphplot.plot.regression;

public class PointInfo
{
	String series;
	double x;
	double y;
	boolean isIncluded;
	
	public PointInfo(String series, double x, double y, boolean isIncluded) {
		super();
		this.series = series;
		this.x = x;
		this.y = y;
		this.isIncluded = isIncluded;
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public boolean isIncluded() {
		return isIncluded;
	}
	public void setIncluded(boolean isIncluded) {
		this.isIncluded = isIncluded;
	}
	
	
	
}
