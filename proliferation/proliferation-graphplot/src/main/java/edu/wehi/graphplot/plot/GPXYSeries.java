package edu.wehi.graphplot.plot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.jfree.data.statistics.Statistics;

import edu.wehi.swing.SwingColorUtil;
public class GPXYSeries implements NameableCollection
{
	public final long id = UUID.randomUUID().getMostSignificantBits();
	public final List<? extends GPCoordinateWithName> coordinates;

	public String name;
	public String description;
	public String xAxis;
	public String yAxis;

	public boolean isVisibleInLegend = true;
	public boolean isVisibleInChart = true;
	public boolean isLineDashed = false;

	static Map<String, Color> predefColors = new HashMap<>();
	static int newColorIndex = 0;

	public GPXYSeries(String name, 
			String description,
			Color color,
			List<? extends GPCoordinateWithName> coordinates,
			String	xAxis,
			String yAxis)
	{
		super();

		if (color == null) {
			color = predefColors.get(name);
			if (color == null) {
				color = getNewColor(name);
				predefColors.put(name, color);
			}
		}

		this.name = name;
		this.description = description;
		this.coordinates = coordinates;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
	
	public GPXYSeries(
			String name,
			Color color,
			List<? extends GPCoordinateWithName> coordinates)
	{
		this(name, "", color, coordinates,"","");
	}
	
	public GPXYSeries(
			String name,
			List<? extends GPCoordinateWithName> coordinates)
	{
		this(name, "", null, coordinates,"","");
	}
	
	public GPXYSeries(
			String name,
			List<? extends GPCoordinateWithName> coordinates,
			boolean isVisibleInLegend)
	{
		this(name, coordinates);
		this.isVisibleInLegend = isVisibleInLegend;
	}
	
	public GPXYSeries(Collection<? extends GPCoordinateWithName> coordinates)
	{
		this("", new ArrayList<>(coordinates));
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof GPXYSeries)
		{
			GPXYSeries series = (GPXYSeries) obj;
			
			if (series.size() != size())
			{
				return false;
			}
			
			if (!series.getAllXCoordinates().equals(getAllXCoordinates()))
			{
				return false;
			}
			
			if (!series.getAllYCoordinates().equals(getAllYCoordinates()))
			{
				return false;
			}
			
			return true;
		}
		else {
			return super.equals(obj);
		}
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void setName(String name)
	{
		Color color = predefColors.get(this.name);
		predefColors.put(name, color);
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long stringSum(String str)
	{
		if (str == null) return 0;
		long sum = 0;
		for (int i = 0; i < str.length(); i++) sum += str.charAt(i);
		return sum;
	}
	
	public static void clearPreDefColors()
	{
		predefColors.clear();
		newColorIndex = 0;
	}
	
	public Color getColor()
	{
		return predefColors.get(name);
	}
	
	public void setColor(Color color)
	{
		predefColors.put(name, color);
	}
	
	public static void setPreDefColor(String name, Color color)
	{
		predefColors.put(name, color);
	}
	
	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	public long getId() {
		return id;
	}
	
	public boolean isVisibleInLegend() {
		return isVisibleInLegend;
	}

	public boolean isVisibleInChart() {
		return isVisibleInChart;
	}

	public void setVisibleInChart(boolean isVisibleInChart) {
		this.isVisibleInChart = isVisibleInChart;
	}

	public void setVisibleInLegend(boolean isVisibleInLegend) {
		this.isVisibleInLegend = isVisibleInLegend;
	}
	
	public boolean isLineDashed() {
		return isLineDashed;
	}

	public void setLineDashed(boolean isLineDashed) {
		this.isLineDashed = isLineDashed;
	}

	public GPXYSeries add(GPXYSeries series)
	{
		return add("",series);
	}
	
	public GPXYSeries add(String name, GPXYSeries series)
	{
		
		if (series.coordinates.size() != this.coordinates.size())
		{
			throw new RuntimeException("Lists of different size cannot be added togeter");
		}
		
		Collections.sort(coordinates);
		Collections.sort(series.coordinates);
		
		List<GPCoordinateWithName> newCoordinates = new ArrayList<>();
		for (int i = 0; i < series.coordinates.size(); i++)
		{
			GPCoordinateWithName newCoord = GPCoordinateWithName.add(series.coordinates.get(i), this.coordinates.get(i));
			newCoordinates.add(newCoord);
		}
		
		GPXYSeries result = new GPXYSeries(
				name,
				description,
				predefColors.get(name),
				newCoordinates,
				xAxis,
				yAxis);
		
		return result;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Title:\t\t"+name+"\n");
		sb.append("xAxis:\t\t"+xAxis+"\n");
		sb.append("yAxis:\t\t"+yAxis+"\n");
		sb.append("description:\t"+description+"\n");
		sb.append("color:\t\t"+predefColors.get(name)+"\n");
		sb.append("no coords:\t"+coordinates.size()+"\n");
		
		for (GPCoordinateWithName c : coordinates)
		{
			sb.append("(" + c.getX() + ", " + c.getY() + "), ");
		}

		return sb.toString();
	}
	
	public double[] getAllXCoordinates()
	{
		int sz = this.coordinates.size();
		double[] xs = new double[sz];
		for (int i = 0; i < sz; i++ )
		{
			xs[i] = this.coordinates.get(i).getX();
		}
		return xs;
	}
	
	public List<Double> getAllXCoordinatesAsList()
	{
		List<Double> coordinatesList = new ArrayList<>();
		for (GPCoordinateWithName c : this.coordinates)
		{
			coordinatesList.add(c.getX());
		}
		return coordinatesList;
	}
	
	public double[] getAllYCoordinates()
	{
		int sz = this.coordinates.size();
		double[] ys = new double[sz];
		for (int i = 0; i < sz; i++ )
		{
			ys[i] = this.coordinates.get(i).getY();
		}
		return ys;
	}
	
	public Set<Double> getAllXUnique()
	{
		return new HashSet<>(this.getAllXCoordinatesAsList());
	}
	
	public Map<Double,List<Double>> getCoordinatesByX()
	{
		Map<Double, List<Double>> map = new HashMap<>();
		
		for (GPCoordinateWithName coord : this.coordinates)
		{
			if (!map.containsKey(coord.getX()))
			{
				map.put(coord.getX(), new ArrayList<>());
			}
			map.get(coord.getX()).add(coord.getY());
		}
		return map;
	}
	
	public static double average(List<Double> doubles)
	{
		double sum = 0.0;
		for (double d : doubles)
		{
			sum += d;
		}
		return sum;
	}
	
	public Map<Double, Double> getMeanAtX()
	{
		Map<Double,Double> meanAtX = new HashMap<>();
		Map<Double,List<Double>> coordbyX = getCoordinatesByX();
		for (Double key : coordbyX.keySet())
		{
			meanAtX.put(key, average(coordbyX.get(key)));
		}
		return meanAtX;
	}
	
	public Map<Double,Double> getStdAtX()
	{
		Map<Double, Double> stdByX = new HashMap<>();
		Map<Double, List<Double>> coordByX = getCoordinatesByX();
	
		for (Double x : coordByX.keySet())
		{
			List<Double> ycoords = coordByX.get(x);
			double std = Statistics.getStdDev(ycoords.toArray(new Double[ycoords.size()]));
			stdByX.put(x, std);
		}
		
		return stdByX;
	}
	
	public GPXYSeries getXYSeriesStd()
	{
		List<GPCoordinateWithName> coords = new ArrayList<>();
		Map<Double,Double> stdAtX = getStdAtX();
		for (double x : stdAtX.keySet())
		{
			coords.add(new GPCoordinateWithNameImp(x,stdAtX.get(x),0.0,""));
		}
		return new GPXYSeries(this.name, predefColors.get(name), coords);
	}
	
	public static boolean isSTD = false;
	
	public GPXYSeries computeAverageSeries()
	{
		Map<Double,List<Double>> coordsByX = getCoordinatesByX();
		List<GPCoordinateWithName> averagecoords = new ArrayList<>();
		

		
		for (Double x : coordsByX.keySet())
		{
			double sum = 0.0;
			int n = coordsByX.get(x).size();
			
			if (n == 0)
			{
				continue;
			}
			if (n == 1)
			{
				averagecoords.add(new GPCoordinateWithNameImp(x, coordsByX.get(x).get(0),0.0,""));
				continue;
			}
			for (double y : coordsByX.get(x))
			{
				sum   += y;
			}
			double average = sum / (double) n;
			
			
			// 
			double stdSum = 0.0;
			
			for (double y : coordsByX.get(x))
			{
				stdSum += Math.pow((y - average),2);
			}
			
			// http://www.mathsisfun.com/data/standard-deviation.html
			double std = Math.sqrt(stdSum/(double)(n-1));
			double stdError = std/Math.sqrt(coordsByX.get(x).size());
			
			if (isSTD)
			{
				averagecoords.add(new GPCoordinateWithNameImp(x, average, std,""));
			}
			else
			{
				averagecoords.add(new GPCoordinateWithNameImp(x, average, stdError,""));	
			}
		}
		
		return new GPXYSeries(name, description, predefColors.get(name), averagecoords, xAxis, yAxis);
	}

	public List<? extends GPCoordinateWithName> getCoordinates() {
		return coordinates;
	}
	
	public int size()
	{
		return coordinates.size();
	}

	public double maxYVal()
	{
		return coordinates.stream().mapToDouble(m -> m.getY()).max().getAsDouble();
	}

	public double minXVal()
	{
		return coordinates.stream().mapToDouble(m -> m.getX()).min().getAsDouble();
	}
	
	public double maxXVal()
	{
		return coordinates.stream().mapToDouble(m -> m.getX()).max().getAsDouble();
	}
	
	public double sumY()
	{
		return coordinates.stream().mapToDouble(m -> m.getY()).sum();
	}
	
	public double sumX()
	{
		return coordinates.stream().mapToDouble(m -> m.getX()).sum();
	}

	LineType lineType = LineType.POINTS;
	public void setLineType(LineType lineType)
	{
		this.lineType = lineType;
	}
	
	public LineType getLineType()
	{
		return lineType;
	}

	boolean isLineShowing = true;
	public boolean isLineShowing() {
		return isLineShowing;
	}
	
	public void setLineShowing(boolean isLineShowing)
	{
		this.isLineShowing = isLineShowing;
	}

	public double sse(GPXYSeries otherSeries)
	{
		double sse = 0;
		
		for (int i = 0; i < coordinates.size(); i++)
		{
			try {sse += Math.pow(coordinates.get(i).getY() - otherSeries.coordinates.get(i).getY(),2);}catch(Exception e){System.out.println("TODO fix SSE");} 
		}
		
		return sse;
	}

	public void removeAllCoordsWithX(double d)
	{
		List<GPCoordinateWithName> mess2Rmove = new ArrayList<>();
		for (GPCoordinateWithName c : coordinates)
		{
			if (c.getX() == d) mess2Rmove.add(c);
		}
		for (GPCoordinateWithName c : mess2Rmove)
		{
			coordinates.remove(c);
		}
	}
	
	final Color getNewColor(String name) {
		String[] splittedName = name.split("/");
		int sz = splittedName.length;

		String colorString = "s";
		String intensityString = null;

		if (sz > 2) {
			colorString = splittedName[sz - 2];
			intensityString = splittedName[sz - 1];
		}
		if (sz == 2) {
			colorString = splittedName[1];
		} else if (sz == 1) {
			colorString = splittedName[0];
		}

		if (colorString.equals("LIVE") || colorString.equals("DROP") || colorString.equals("DEAD")) {
			colorString = intensityString;
			intensityString = null;
		}

		Color baseColor;
		if (newColorIndex < SwingColorUtil.getNumColors()) {
			baseColor = SwingColorUtil.getColor(newColorIndex);
			newColorIndex += 1;
		} else {
			Random random = new Random(stringSum(colorString));
			long seed = Math.abs(random.nextInt());
			baseColor = SwingColorUtil.randomColor(seed);
		}

		int r = baseColor.getRed();
		int g = baseColor.getGreen();
		int b = baseColor.getBlue();

		// adjust intensity
		long intensitySum = stringSum(intensityString);
		float i = (intensityString == null) ? 1.0f : new Random(intensitySum * intensityString.charAt(0) * 234).nextFloat();

		r *= i;
		g *= i;
		b *= i;

		Color adjustedColor = new Color(r, g, b);
		return adjustedColor;
	}
}