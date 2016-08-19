package edu.wehi.celcalc.cohort.gui.browser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.data.gui.MeasurementStatistician;
import edu.wehi.celcalc.cohort.data.gui.MeasurementStatistics;
import edu.wehi.celcalc.cohort.res.TestFileData;
import edu.wehi.graphplot.plot.GPXYSeries;

public class TestDataBrowserDAO
{
	
	@Test
	public void testSaveAndLoadMeasurements() throws IOException
	{
		List<Measurement> measurements = TestFileData.mesurements;

		 try
		 {
			File temp = File.createTempFile("temp-file-name", ".csv");
			assertTrue("Failed to create temporary directory",DataBrowserDAO.saveMeasurements(temp, measurements));
			Collection<Measurement> loadedMeasurements = DataBrowserDAO.loadMeasurements(temp.getAbsolutePath());
			assertNotNull("The loaded measurements werer null",loadedMeasurements);
			assertEquals("The loaded measurements differ from the actual", measurements.size(), loadedMeasurements.size());
			 
			Set<String> loadedTreatments = MeasurementQuery.allTreatments(loadedMeasurements);
			Set<String> treatments = MeasurementQuery.allTreatments(measurements);
			assertEquals("treatments were not equal", loadedTreatments, treatments);
			
			Set<CellType> loadedCellTypes = MeasurementQuery.allCellTypes(loadedMeasurements);
			Set<CellType> cellTypes = MeasurementQuery.allCellTypes(measurements);
			assertEquals("Cell types were not equal", loadedCellTypes, cellTypes);
			
			MeasurementStatistics stats = MeasurementStatistician.stats(measurements);
			MeasurementStatistics loadedStats = MeasurementStatistician.stats(loadedMeasurements);
			
			assertEquals("Stats differ", stats.getHighestCellCount(), 	loadedStats.getHighestCellCount());
			assertEquals("Stats differ", stats.getHighestTime(), 		loadedStats.getHighestTime());
			assertEquals("Stats differ", stats.getHighestDivision(), 	loadedStats.getHighestDivision());
			assertEquals("Stats differ", stats.getLowestCellCount(), 	loadedStats.getLowestCellCount());
			assertEquals("Stats differ", stats.getLowestDivision(), 	loadedStats.getLowestDivision());
			assertEquals("Stats differ", stats.getLowestTime(), 		loadedStats.getLowestTime());
						
			for (CellType type : loadedCellTypes)
			{
				for (String treatment : loadedTreatments)
				{
					GPXYSeries loadedSeries = new GPXYSeries(
							new MeasurementQuery(loadedMeasurements).withType(type).withTreatment(treatment).measurements
							);
					
					GPXYSeries series = new GPXYSeries(
							new MeasurementQuery(loadedMeasurements).withType(type).withTreatment(treatment).measurements
							);
					
					assertArrayEquals("X values differ of "+type+"/"+treatment,series.getAllXCoordinates(), loadedSeries.getAllXCoordinates(), 0.001);
					assertArrayEquals("Y values differ of "+type+"/"+treatment,series.getAllYCoordinates(), loadedSeries.getAllYCoordinates(), 0.001);
				}
			}
		 } 
		 catch(SecurityException se)
		 {
			 se.printStackTrace();
		 }
	}
	
	@Test
	public void testSaveAndLoadFilters() throws IOException
	{
		Filter f1 = new Filter();
		String n1 = "f1";
		f1.setName(n1);
		String n2 = "f2";
		Filter f2 = new Filter();
		f2.setName(n2);
		
		List<Filter> filters = new ArrayList<>();
		
		filters.add(f1);
		filters.add(f2);
		
		File temp = File.createTempFile("somefile", "");
		DataBrowserDAO.saveFilters(temp.getParent(), filters);
		
		String f1_abs = temp.getParent() +"\\"+ f1.getName() +".xml";
		String f2_abs = temp.getParent() +"\\"+ f2.getName() +".xml";
		
		Filter f1_loaded = DataBrowserDAO.loadFilter(f1_abs);
		Filter f2_loaded = DataBrowserDAO.loadFilter(f2_abs);
		
		assertNotNull("Failed to deserialize filter 1", f1_loaded);
		assertNotNull("Failed to deserialize filter 1", f2_loaded);
		
		assertTrue(f1_loaded.getCountFrom() == null);
		assertTrue(f1_loaded.getName().equals(n1));
		
		assertTrue(f2_loaded.getCountFrom() == null);
		assertTrue(f2_loaded.getName().equals(n2));
	}
	
}