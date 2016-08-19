package edu.wehi.celcalc.cohort.scripts;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wehi.celcalc.cohort.PackageScripterUtilities;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.scriptables.MultiInMultiOutPythonScriptEngine;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;

public class TestPythonScript
{
	
	static final String pythontest = "pythontest";
	static final String testScript = pythontest+"/"+"testscript.py";
	
	@Test
	public void testErros()
	{
		List<Measurement> measurements = new ArrayList<>();
		measurements.add(new Measurement(0.0, 10, 0, CellType.LIVE, "test"));
		List<GPDataNode<GPXYSeries>> result = new MultiInMultiOutPythonScriptEngine(PackageScripterUtilities.getAsString(testScript), testScript).script(measurements);
		assertEquals(1,result.size());
		GPXYSeries series = result.get(0).getData();
		double[] xCoords = new double[]{1.0,2.0,3.0};
		double[] yCoords = new double[]{2.0,4.0,6.0};
		double[] lower =   new double[]{1.0,1.0,1.0};
		double[] upper =   new double[]{2.0,1.0,2.0};
		
		for (int i=0; i<series.size(); i++)
		{
			GPCoordinateWithName coord = series.coordinates.get(i);
			assertEquals(xCoords[i], coord.getX(), 0.0);
			assertEquals(yCoords[i], coord.getY(), 0.0);
			assertEquals(yCoords[i]-lower[i], coord.getYLower(), 0.0);
			assertEquals(yCoords[i]+upper[i], coord.getYUpper(), 0.0);
		}
		
	}
	
	

}
