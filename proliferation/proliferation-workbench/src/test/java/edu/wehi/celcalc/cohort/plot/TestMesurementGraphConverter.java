package edu.wehi.celcalc.cohort.plot;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.wehi.celcalc.cohort.data.MesurementGraphConverter;
import edu.wehi.celcalc.cohort.res.TestFileData;
import edu.wehi.graphplot.plot.GPXYSeries;

public class TestMesurementGraphConverter {

	@SuppressWarnings("rawtypes")
	List measurementsList = TestFileData.mesurements;
	
	@SuppressWarnings("unchecked")
	List<GPXYSeries> plots = MesurementGraphConverter.timeSeriessByGroupable(measurementsList,
			"",
			"",
			"",
			"");
	
	
	
	
	@Test
	public void shouldGenerateTheCorrectNumberOfSeries()
	{
		assertNotNull(plots);
		//assertEquals(TestFileData.noTreatments * CellType.values().length, plots.size());
		assertEquals(TestFileData.noOfSeries, plots.size());
	}
	
	@Test
	public void testCorrectNumberOfXYCoordinatesCreated()
	{
		int count = 0;
		for (GPXYSeries p : plots)
		{
			count += p.coordinates.size();
		}
		assertEquals(TestFileData.totalDataPoints, count);
	}
	

}
