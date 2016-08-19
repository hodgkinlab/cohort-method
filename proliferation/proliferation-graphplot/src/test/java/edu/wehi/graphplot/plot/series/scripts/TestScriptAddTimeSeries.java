package edu.wehi.graphplot.plot.series.scripts;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPCoordinateWithNameImp;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;

public class TestScriptAddTimeSeries {

	
	@Test
	public void testAdd()
	{
		ScriptMultiInMultiOut rootParentScript = null;
		
		double acceptableError = 0.000001;
		
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
		
		GPDataNode<GPXYSeries> d1 = new GPDataNode<>(rootParentScript,series1,"");
		
		
		
		
		
		// Second Node
		
		List<GPCoordinateWithName> coordinates2 = new ArrayList<GPCoordinateWithName>();
		
		GPCoordinateWithName b1 = new GPCoordinateWithNameImp(xx1,yy1,0.0,"");
		GPCoordinateWithName b2 = new GPCoordinateWithNameImp(xx2,yy2,0.0,"");;
		coordinates2.add(b1);
		coordinates2.add(b2);
		
		GPXYSeries series2 = new GPXYSeries("", null, coordinates2);
		
		GPDataNode<GPXYSeries> d2 = new GPDataNode<>(rootParentScript,series2,"");
		
		
		// Add together
		List<GPDataNode<GPXYSeries>> input = new ArrayList<>();
		input.add(d1);
		input.add(d2);
		
		ScriptAddTimeSeries script = new ScriptAddTimeSeries(input);
		GPDataNode<GPXYSeries> output = script.getOutput();
		
		// Output should not be null
		assertNotNull(output);
		
		
		// The list of the coordinates in the output should be equal to 2
		assertEquals(2, output.data.coordinates.size() );
		
		GPXYSeries resultSeries = output.getData();
		GPCoordinateWithName coord1 = resultSeries.coordinates.get(0);
		GPCoordinateWithName coord2 = resultSeries.coordinates.get(1);
		
		
		System.out.println("a1+a2");
		System.out.println(a1 +" + " + a2 + " = " + coord1);
		System.out.println(b1 +" + " + b2 + " = " + coord2);
		
		// Check the algebra of the expression
		assertEquals(x1, 		coord1.getX(), acceptableError);
		assertEquals(x2, 		coord2.getX(), acceptableError);
		
		assertEquals(y1+yy1, 	coord1.getY(), acceptableError);
		assertEquals(y2+yy2, 	coord2.getY(), acceptableError);
	}
	
	
}
