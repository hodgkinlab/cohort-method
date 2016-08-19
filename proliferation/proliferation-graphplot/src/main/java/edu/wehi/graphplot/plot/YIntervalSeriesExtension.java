package edu.wehi.graphplot.plot;

import java.awt.Color;

import org.jfree.data.xy.YIntervalSeries;

public class YIntervalSeriesExtension extends YIntervalSeries
{

	private static final long serialVersionUID = 1L;
	
	public boolean		isVisibleInLegend = true;
	public Color		color = null;
	public boolean		isLineDashed = false;

	public boolean isLineDashed() {
		return isLineDashed;
	}

	public void setLineDashed(boolean isLineDashed) {
		this.isLineDashed = isLineDashed;
	}

	public YIntervalSeriesExtension(Comparable<?> key)
	{
		super(key);
	}
	
	public YIntervalSeriesExtension(Comparable<?> key, boolean autoSort, boolean allowDuplicateXValues)
	{
		super(key, autoSort, allowDuplicateXValues);
	}

	public boolean isVisibleInLegend() {
		return isVisibleInLegend;
	}

	public void setVisibleInLegend(boolean isVisibleInLegend) {
		this.isVisibleInLegend = isVisibleInLegend;
	}

	boolean isVisibleInChart = true;
	public void setVisibleInChart(boolean isVisibleInChart) {
		this.isVisibleInChart = isVisibleInChart;
	}
	
	public boolean isVisibleInChart()
	{
		return isVisibleInChart;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	boolean isLineShowing = true;
	public boolean isLineShowing() {
		return isLineShowing;
	}
	
	public void setLineShowing(boolean isLineShowing)
	{
		this.isLineShowing = isLineShowing;
	}

	

}
