package edu.wehi.celcalc.cohort.gui.application;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestFilterView
{

	private static final List<Measurement> measurementsList = new ArrayList<>();
	
	static Measurement mesA = new Measurement(-0.1,	1, 	1, 	CellType.DEAD,	"test1");
	static Measurement mesB = new Measurement(0.0,	2, 	1, 	CellType.DEAD, 	"test1");
	static Measurement mesC = new Measurement(10,	3, 	1, 	CellType.DEAD, 	"test1");
	static Measurement mesD = new Measurement(22,	4, 	1, 	CellType.DEAD,	"test1");
	
	static
	{
		measurementsList.add(mesA);
		measurementsList.add(mesB);
		measurementsList.add(mesC);
		measurementsList.add(mesD);
	}
	
	@Test
	public void testTimeFrom()
	{
		Filter filter = new Filter();
		
		final double time = 10.0; 
		
		FilterView view = new FilterView();
		view.sync(filter);
		view.txtTimeFrom.setText(time+"");

		assertEquals(time, filter.getTimeFrom(), 0.0000001);
		
		assertNull(filter.getCountFrom());
		assertNull(filter.getCountTo());
		assertNull(filter.getDivFrom());
		assertNull(filter.getDivTo());
		assertNull(filter.getName());
		//assertNull(filter.getTimeFrom());
		assertNull(filter.getTimeTo());
		assertNull(filter.getTreatments());
		//assertNull(filter.getTypes());
		
		
		assertEquals(2, filter.filter(measurementsList).size());
		
		
		// test back to null
		view.txtTimeFrom.setText("");
		assertNull(filter.getTimeFrom());
		assertEquals(4, filter.filter(measurementsList).size());
	}
	
	
}
