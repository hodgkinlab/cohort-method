package edu.wehi.celcalc.cohort.gui.scripter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;



public class TestCellCalcScriptEngine {

	
	
	@Test
	public void testEngineCreatesCellCountVariableCorrectly()
	{
		CellCalcScriptEngine engine = new CellCalcScriptEngine();
		List<Measurement> measurements = new ArrayList<>();
		
		double x1 = 0.0;
		double x2 = 2.0;
		
		double y1_1 = 3.0;
		double y1_2 = 4.0;
		double y1_av = (y1_1 + y1_2)/2.0;
		
		double y2_1 = 10.0;
		double y2_2 = 20.0;
		double y2_av = (y2_1 + y2_2)/2.0;
		
		
		measurements.add(new Measurement(x1, y1_1, 1, CellType.LIVE, "1"));
		measurements.add(new Measurement(x1, y1_2, 1, CellType.LIVE, "1"));
		measurements.add(new Measurement(x2, y2_1, 2, CellType.LIVE, "1"));
		measurements.add(new Measurement(x2, y2_2, 2, CellType.LIVE, "1"));
		
		engine.setMeasurements(measurements);
		
		
		assertEquals(y1_av,(double)engine.x.get(1, x1),0.0);
		assertEquals(y2_av,(double)engine.x.get(2, x2),0.0);
		
		
		
	}
	
}
