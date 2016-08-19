package edu.wehi.graphplot.plot;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGPCoordinateWithName {

	@Test
	public void testGettersSetters()
	{
		double x = 3.0;
		double y = 2.0;
		double er = 0.0;
		
		GPCoordinate co = new GPCoordinateWithNameImp(x,y,0.0,"");
		assertEquals(x, co.getX(),er);
		assertEquals(y, co.getY(),er);
	}
	
	@Test
	public void testCoordinateAdd()
	{
		double x1 = 1.0;
		double y1 = 12.0;
		double x2 = x1;
		double y2 = 21312.0;
		double er = 0.0001;
		
		assertEquals(x1, x2, er);
		
		GPCoordinateWithNameImp coord1 = new GPCoordinateWithNameImp(x1,y1,0.0,"");
		GPCoordinateWithNameImp coord2 = new GPCoordinateWithNameImp(x2,y2,0.0,"");
		
		GPCoordinateWithName result = GPCoordinateWithName.add(coord1, coord2);
		
		assertEquals(x1, 		result.getX(),		er);
		assertEquals(x2, 		result.getX(),		er);
		assertEquals(y1 + y2,	result.getY(),		er);
	}
	
}
