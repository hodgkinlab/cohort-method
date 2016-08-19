package edu.wehi.graphplot.plot.regression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.regression.row.RegressionRowModel;

public class RegressionModel
{
	final String title;
	final String xAxis;
	final String yAxis;

	final List<GPXYSeries> rawData = new ArrayList();
	final List<RegressionRowModel> regressionRows = new ArrayList();
	
	final Map<String, PointInfo> infoOfPoints = new HashMap<>();
	final String sillyString = "23#$@23236";

	public RegressionModel(
			Collection<GPXYSeries> rawData,
			String title, String xAxis, String yAxis)
	{
		this.title = title;
		this.xAxis = xAxis;
		this.yAxis = yAxis;

		rawData.stream().forEach((s) -> {
			this.rawData.add(s);
			RegressionRowModel row = new RegressionRowModel(
					s.getName(), s.getColor(), 900, s.minXVal(), s.maxXVal(),
					1, 2, 1, false, true, false);
			regressionRows.add(row);
		});
	}
	
	public List<RegressionRowModel> getRegressionRows()
	{
		return regressionRows;
	}

	public void pointIncludeStatusChanged(double x, double y, String series)
	{
		series = series.replace(sillyString, "");
		final String key = series + "-" + x + "-" + y;
		PointInfo infopoint = infoOfPoints.get(key);
		if (infopoint == null) {
			infoOfPoints.put(key, new PointInfo(series, x, y, false));
		} else {
			infoOfPoints.remove(key);
		}
	}

	public Collection<GPXYSeries> getSeries()
	{
		List<GPXYSeries> seriesToBePlotted = new ArrayList<>();
		
		for (int i = 0; i < rawData.size(); i++)
		{
			GPXYSeries series = rawData.get(i);
			String name = series.getName();
			RegressionRowModel rowModel = regressionRows.get(i);
			
			// add curve with points excluded
			GPXYSeries seriesWithPointsRemoved
					= removeExcludedPointsFromSeries(series, null);
			seriesWithPointsRemoved.setColor(series.getColor());
			if (rowModel.isShowingData())
			{
				seriesToBePlotted.add(seriesWithPointsRemoved);
			}
			
			// plot all points as a scatter so user can click on points and exclude them individually 
			GPXYSeries allPoints = new GPXYSeries(
					sillyString+series.getName(),
					series.coordinates, series.isVisibleInLegend());
			allPoints.setLineShowing(false);
			allPoints.setVisibleInLegend(false);
			allPoints.setColor(series.getColor());
			
			if (rowModel.isShowingData())
			{
				seriesToBePlotted.add(allPoints);	
			}
			
			// plot the regression line
			
			List<GPCoordinateWithName> regressionCoordinates = new ArrayList<>();
			for (GPCoordinateWithName coord : seriesWithPointsRemoved.coordinates)
			{
				if (coord.getX() > rowModel.getMiddleAsFloat()) continue;
				regressionCoordinates.add(new GPCoordinateWithNameImp(coord.getX(), rowModel.getB()+coord.getX()*rowModel.getA(), 0, ""));
			}
			double spline_y = rowModel.getB()+rowModel.getMiddleAsFloat()*rowModel.getA();
			double spline_x0 = rowModel.getMiddleAsFloat();
			regressionCoordinates.add(new GPCoordinateWithNameImp(spline_x0, spline_y, 0, ""));
			
			GPXYSeries regressionSeries = new GPXYSeries(name+"-reg", regressionCoordinates);
			regressionSeries.setLineDashed(true);
			regressionSeries.setVisibleInLegend(false);
			regressionSeries.setColor(series.getColor());
			
			if (rowModel.isShowingRegression())
			{
				seriesToBePlotted.add(regressionSeries);
			}
			
			//plot spline
			List<GPCoordinateWithName> splineSeries = new ArrayList<>();
			
			splineSeries.add(new GPCoordinateWithNameImp(spline_x0, spline_y, 0, ""));
			double maxX = rowModel.getMax();
			splineSeries.add(new GPCoordinateWithNameImp(maxX, spline_y,0,""));
			
			GPXYSeries splineSeriesXY = new GPXYSeries(name + "-spline", splineSeries);
			splineSeriesXY.setColor(series.getColor());
			splineSeriesXY.setLineDashed(true);
			splineSeriesXY.setVisibleInLegend(false);
			if (rowModel.isShowingRegression())
			{
				seriesToBePlotted.add(splineSeriesXY);
			}
			
			// setSSE
			double sse = 0;
			boolean isSkipping = true;
			sse += regressionSeries.sse(seriesWithPointsRemoved);
			for (GPCoordinateWithName c : seriesWithPointsRemoved.coordinates)
			{
				if (c.getX() > rowModel.getMiddleAsFloat())
				{
					if (isSkipping)
					{
						sse += Math.pow(splineSeriesXY.coordinates.get(1).getY() - c.getY(),2);
					}
					isSkipping = false; // skip the first value (already included)
				}
			}
			rowModel.setSse(sse);
		}
		
		return seriesToBePlotted;
	}
	
	public GPXYSeries removeExcludedPointsFromSeries(GPXYSeries series, RegressionRowModel regressionModel)
	{
		List<String> allInfosForSeries = infoOfPoints.keySet().stream().filter(k -> k.startsWith(series.getName())).collect(Collectors.toList());
		List<GPCoordinateWithName> pointsIncludes = new ArrayList<>();
		
		for (GPCoordinateWithName point : series.getCoordinates())
		{
			if (regressionModel != null)
			{
				if (point.getX() > regressionModel.getMiddleAsFloat()) continue;	
			}
			
			if  (!allInfosForSeries.contains(series.getName()+"-"+point.getKeyCode()))
			{
				pointsIncludes.add((GPCoordinateWithName)point);
			}
		}
		return new GPXYSeries(series.getName(), pointsIncludes, series.isVisibleInLegend());
	}
	

	public String getTitle()
	{
		return title;
	}

	public String getXAxis()
	{
		return xAxis;
	}

	public String getYAxis()
	{
		return yAxis;
	}

	public void computRegressionForRow(RegressionRowModel row)
	{
		// TODO Auto-generated method stub
	}

	public RegressionLine regression(RegressionRowModel rowModel, int i)
	{
		GPXYSeries series = rawData.get(i);
		GPXYSeries seriesWithPointsRemoved
				= removeExcludedPointsFromSeries(series, rowModel);
		RegressionLine regressionLine
				= RegressionLine.regression(seriesWithPointsRemoved.coordinates);
		return regressionLine;
	}

}
