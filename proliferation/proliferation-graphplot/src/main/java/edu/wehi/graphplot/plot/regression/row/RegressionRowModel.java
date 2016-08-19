package edu.wehi.graphplot.plot.regression.row;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jgoodies.binding.beans.Model;

import edu.wehi.graphplot.plot.GPXYSeries;

public class RegressionRowModel extends Model implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME_PROPERTY="name";
	String name;
	public static final String COLOR_PROPERTY="color";
	private Color color;
	public static final String MID_PROPERTY="mid";
	int mid;
	final double min;
	final double max;
	public static final String SSE_PROPERTY="sse";
	double sse;
	public static final String DIV1_PROPERTY="div1";
	double div1;
	public static final String DIV2_PROPERTY="div2";
	double div2;
	public static final String ISSHOWINGREGRESSIN_PROPERTY="showingRegression";
	boolean showingRegression;
	public static final String ISSHOWINGDATA_PROPERTY="showingData";
	boolean showingData;
	public static final String SHOWINGSPLINE_PROPERTY="showingSpline";
	boolean showingSpline;
	public static final String SHOWINGCLICKABLE_PROPERTRY = "showingClickable";
	boolean showingClickable = true;
	public static final String SHOWINGRAW_PROPERTY = "showingRaw";
	boolean showingRaw = true;
	
	public static final int N = 1000;
	
	private static Map<String, RegressionRowModel> cached = new HashMap<>();
	
	public RegressionRowModel(
			String name,
			Color color,
			int mid,
			double min,
			double max,
			double sse,
			double div1,
			double div2,
			boolean showingRegression,
			boolean showingData,
			boolean showingSpline) 
	{
		super();
		
		RegressionRowModel cachedInstance = cached.get(name);
		
		if (cachedInstance == null)
		{
			this.name = name;
			this.color = color;
			this.mid = mid;
			this.min = min;
			this.max = max;
			this.sse = sse;
			this.div1 = div1;
			this.div2 = div2;
			this.showingRegression = showingRegression;
			this.showingData = showingData;
			this.showingSpline = showingSpline;
		}
		else
		{
			this.name = cachedInstance.name;
			this.color = cachedInstance.color;
			this.mid = cachedInstance.mid;
			this.min = cachedInstance.min;
			this.max = cachedInstance.max;
			this.sse = cachedInstance.sse;
			this.div1 = cachedInstance.div1;
			this.div2 = cachedInstance.div2;
			this.showingRegression = cachedInstance.showingRegression;
			this.showingData = cachedInstance.showingData;
			this.showingSpline = cachedInstance.showingSpline;
		}
		
		cached.put(name, this);

	}

	public void setName(String name) {
		firePropertyChange(NAME_PROPERTY, this.name, this.name = name);
	}
	
	public void setColor(Color color)
	{
		GPXYSeries.setPreDefColor(name, color);
		firePropertyChange(COLOR_PROPERTY, this.color, this.color = color);
	}

	public void setMid(int mid) {
		firePropertyChange(MID_PROPERTY, this.mid, this.mid = mid);
	}

	public void setSse(double sse) {
		firePropertyChange(SSE_PROPERTY, this.sse, this.sse = sse);
	}

	public void setDiv1(double div1) {
		firePropertyChange(DIV1_PROPERTY, this.div1, this.div1 = div1);
	}

	public void setDiv2(double div2) {
		firePropertyChange(DIV2_PROPERTY, this.div2, this.div2 = div2);
	}

	public void setShowingRegression(boolean showingRegression) {
		firePropertyChange(ISSHOWINGREGRESSIN_PROPERTY, this.showingRegression, this.showingRegression = showingRegression);
	}

	public void setShowingData(boolean showingData) {
		firePropertyChange(ISSHOWINGDATA_PROPERTY, this.showingData, this.showingData = showingData);
	}

	public void setShowingSpline(boolean showingSpline) {
		firePropertyChange(SHOWINGSPLINE_PROPERTY, this.showingSpline, this.showingSpline = showingSpline);
	}
	
	public void setShowingClickable(boolean showingClickable) {
		firePropertyChange(SHOWINGCLICKABLE_PROPERTRY, this.showingClickable, this.showingClickable = showingClickable);
	}

	public void setShowingRaw(boolean showingRaw) {
		firePropertyChange(SHOWINGRAW_PROPERTY, this.showingRaw, this.showingRaw = showingRaw);
	}

	public String getName() {
		return name;
	}
	
	public double getDiv1() {
		return div1; 
	}
	
	public double getDiv2()	{
		return  div2;
	}

	public int getMid() {
		return mid;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double getSse() {
		return sse;
	}

	public double getMiddleAsFloat()
	{
		return min + ((max - min)*mid)/N;
	}

	public boolean isShowingRegression() {
		return showingRegression;
	}

	public boolean isShowingData() {
		return showingData;
	}

	public boolean isShowingSpline() {
		return showingSpline;
	}
	
	public double getA()
	{
		return 1/div2;
	}
	
	public double getB()
	{
		return 0.5 - div1/div2;
	}
	
	public boolean isShowingRaw() {
		return showingRaw;
	}

	@Override
	public String toString()
	{
		String str = "";
		str += "name: " +"\t" + name +"\n";
		str += "mid: " +"\t" + mid +"\n";
		str += "min: " +"\t" + min +"\n";
		str += "max: " +"\t" + max +"\n";
		str += "div1: " +"\t" + div1 +"\n";
		str += "div2: " +"\t" + div2 +"\n";
		str += "r2: " +"\t" + sse +"\n";
		str += "a: " +"\t" + getA() +"\n";
		str += "b: " +"\t" + getB() +"\n";
		str += "isreg: " +"\t" + showingRegression +"\n";
		str += "isdat: " +"\t" + showingData +"\n";
		str += "issp: " +"\t" + showingSpline +"\n";
		str += "israw: " +"\t" + showingRaw +"\n";
				
		return str;
	}

	public void setAB(double a, double b)
	{
		firePropertyChange(DIV2_PROPERTY, this.div2, this.div2 = 1.0/a);
		firePropertyChange(DIV1_PROPERTY, this.div1, this.div1 = (0.5 - b) / a);
	}

	public Color getColor()
	{
		return color;
	}

}
