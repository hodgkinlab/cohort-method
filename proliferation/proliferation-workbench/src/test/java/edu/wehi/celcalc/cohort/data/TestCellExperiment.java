package edu.wehi.celcalc.cohort.data;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TestCellExperiment {
	
	String name = "some name";
	
	
	double t1 = 20.0;
	double t2 = 323.0;
	double t3 = 2323.23;
	double a = 1.0;
	double b = 2.0;
	double c = 3.0;
	
	Measurement mes1 = new Measurement(t1, a, 1, CellType.DEAD,"a");
	Measurement mes2 = new Measurement(t1, b, 1, CellType.DEAD,"a");
	Measurement mes3 = new Measurement(t1, c, 1, CellType.DEAD,"a");
	
	Measurement mesa = new Measurement(t2, 2342.23, 1, CellType.DEAD,"a");
	Measurement mesb = new Measurement(t3, 23423.32423, 1, CellType.DEAD,"a");
	
	
	public CellExperimentCount getTestExp()
	{
		CellExperimentCount exp = new CellExperimentCount(name,CellType.DEAD);
		exp.measurements.add(mes1);
		exp.measurements.add(mes2);
		exp.measurements.add(mes3);
		exp.measurements.add(mesa);
		exp.measurements.add(mesb);
		return exp;
	}
	
	@Test
	public void nameShouldMatch()
	{
		CellExperimentCount exp = getTestExp();
		assertTrue(name == exp.treatment);
	}

	@Test
	public void shouldFilterCorrectly()
	{
		CellExperimentCount exp = getTestExp();
		
		Set<Measurement> mes = exp.getMeasurementsAtTime(t1);
		
		assertTrue(mes.contains(mes1));
		assertTrue(mes.contains(mes2));
		assertTrue(mes.contains(mes3));
		
		assertFalse(mes.contains(mesa));
		assertFalse(mes.contains(mesb));
	}
	
	@Test
	public void testGetTimeMeasurementsSet()
	{
		CellExperimentCount exp = getTestExp();
		
		Set<Double> set = exp.getTimeMeasurementsSet();
		
		assertTrue(set.contains(t1));
		assertTrue(set.contains(t2));
		assertTrue(set.contains(t3));
		
	}

}

