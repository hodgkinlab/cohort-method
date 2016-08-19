package edu.wehi.graphplot.plot;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPXYSeries;

public class TestGPXSeries {

	@Test
	public void testSeriesAdd()
	{
		double acceptableError = 0.001;
		
		double
		x1 = 1.0, y1=1.0,
		x2 = 2.0, y2=2.0,
		xx1 = x1, yy1 = 2.0,
		xx2 = x2, yy2 = 4.0;
		
		// First Node
		
		List<GPCoordinateWithName> coordinates1 = new ArrayList<GPCoordinateWithName>();
		
		GPCoordinateWithName a1 = new GPCoordinateWithNameImp(x1,y1,0.0,"");
		GPCoordinateWithName a2 = new GPCoordinateWithNameImp(x2,y2,0.0,"");
		coordinates1.add(a1);
		coordinates1.add(a2);
		
		GPXYSeries series1 = new GPXYSeries("", null, coordinates1);
		
		
		// Second Node
		
		List<GPCoordinateWithName> coordinates2 = new ArrayList<GPCoordinateWithName>();
		
		GPCoordinateWithName b1 = new GPCoordinateWithNameImp(xx1,yy1,0.0,"");
		GPCoordinateWithName b2 = new GPCoordinateWithNameImp(xx2,yy2,0.0,"");
		coordinates2.add(b1);
		coordinates2.add(b2);
		
		GPXYSeries series2 = new GPXYSeries("", null, coordinates2);
		
		
		
		
		
		
		GPXYSeries result = series1.add(series2);
		
		assertEquals(2, result.coordinates.size());
		
		assertEquals(x1, 		result.coordinates.get(0).getX(), acceptableError);
		assertEquals(x2, 		result.coordinates.get(1).getX(), acceptableError);
		
		assertEquals(y1+yy1, 		result.coordinates.get(0).getY(), acceptableError);
		assertEquals(y2+yy2, 		result.coordinates.get(1).getY(), acceptableError);
		
		
		
	}
	
}
