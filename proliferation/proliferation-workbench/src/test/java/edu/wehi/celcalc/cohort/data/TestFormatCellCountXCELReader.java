package edu.wehi.celcalc.cohort.data;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;

import edu.wehi.celcalc.cohort.res.TestFileData;


public class TestFormatCellCountXCELReader {
	
	@Test
	public void testFirstCol()
	{
		assertEquals(TestFileData.firstCol,CellCountXCELReader.indexOfFirstColumn(TestFileData.live_sheet));
	}
	
	@Test
	public void testFirstRow()
	{
		assertEquals(TestFileData.firstRow,CellCountXCELReader.indexOfFirstRow(TestFileData.live_sheet));
	}
	
	@Test
	public void testIndexOfTreatements()
	{
		int[] indexs_of_treatments = CellCountXCELReader.indexsOfTreatments(TestFileData.live_sheet);
		
		assertEquals("did not find the correct indexes of treatments in the test file",indexs_of_treatments.length, indexs_of_treatments.length);
		for (int i=0; i < indexs_of_treatments.length; i++)
		{
			assertEquals("element "+i +"in indexs_of_treatments differs", TestFileData.indexsOfTreatments[i],indexs_of_treatments[i]);
		}
		assertTrue(Arrays.equals(indexs_of_treatments, TestFileData.indexsOfTreatments));
	}
	
	@Test
	public void testNoOfDivisions()
	{
		// each test has 7 divisions
		String message = "There are 7 divisions counted for each trial";
		assertEquals(message,7,CellCountXCELReader.noOfDivisions(TestFileData.live_sheet, 0));
		assertEquals(message,7,CellCountXCELReader.noOfDivisions(TestFileData.live_sheet, 1));
		assertEquals(message,7,CellCountXCELReader.noOfDivisions(TestFileData.live_sheet, 2));
	}
	
	private void testArraysEqual(double[] a, double[]b)
	{
		assertEquals(a.length,b.length);
		for (int i = 0; i < a.length; i++)
		{
			assertEquals(a[i], b[i],0.01);
		}
	}
	
	@Test
	public void testTreatments()
	{
		List<String> treatments_list = CellCountXCELReader.treatments(TestFileData.live_sheet);
		String[] treatments = treatments_list.toArray(new String[]{});
		
		assertEquals(TestFileData.actual_treatments.length, treatments.length);
		assertArrayEquals(TestFileData.actual_treatments,treatments);
	}
	
	@Test
	public void testGetTimes()
	{
		double[] times0 = CellCountXCELReader.getTimes(TestFileData.live_sheet, 0).stream().mapToDouble(z -> z).toArray();
		double[] times1 = CellCountXCELReader.getTimes(TestFileData.live_sheet, 1).stream().mapToDouble(z -> z).toArray();
		double[] times2 = CellCountXCELReader.getTimes(TestFileData.live_sheet, 2).stream().mapToDouble(z -> z).toArray();
		
		double[] actual_times0 = new double[]{30.5,30.5,30.5,42.5,42.5,42.5,45.0,45.0,45.0,69.0,69.0,69.0,90.5,90.5,90.5};
		double[] actual_times1 = new double[]{30.5,30.5,30.5,42.5,42.5,42.5,45.0,45.0,45.0,69.0,69.0,69.0,90.5,90.5,90.5};
		double[] actual_times2 = new double[]{25.0,25.0,25.0,44.0,44.0,44.0,52.5,52.5,52.5,68.0,68.0,68.0,76.5,76.5,76.5,92.0,92.0,92.0};
		

		testArraysEqual(actual_times0, times0);
		testArraysEqual(actual_times1, times1);
		testArraysEqual(actual_times2, times2);
	}
	
	@Test
	public void testCorrectNumberOfProcessTreatment()
	{
		List<Measurement> count5000 = 		CellCountXCELReader.processTreatment(TestFileData.live_sheet, 0, CellType.LIVE, "sometreatment");
		List<Measurement>  count1000 = 		CellCountXCELReader.processTreatment(TestFileData.live_sheet, 1, CellType.LIVE, "sometreatment");
		List<Measurement>  count0 = 		CellCountXCELReader.processTreatment(TestFileData.live_sheet, 2, CellType.LIVE, "sometreatment");
		
		assertEquals(TestFileData.noMes5000, 	count5000.size());
		assertEquals(TestFileData.noMes1000, 	count1000.size());
		assertEquals(TestFileData.noMes0, 		count0.size());
	}
	
	@Test
	public void testCorrectNumberOfProcessLiveSheet()
	{
		Map<String, List<Measurement>> measurements = CellCountXCELReader.processSheetToMap(TestFileData.live_sheet,CellType.LIVE.name);
		List<List<Measurement>> list_measurements = 		new ArrayList<>(measurements.values());
		
		int count = 0;
		for (List<Measurement> mess : list_measurements)
		{
			count += mess.size();
		}
		assertEquals(TestFileData.total, count);
	}
	
	@Test
	public void processWholeDocument()
	{
		try
		{
			Map<String, List<Measurement>> measurements = 		CellCountXCELReader.processBookToMap(TestFileData.testFileForReading);
			List<List<Measurement>> list_measurements = 		new ArrayList<>(measurements.values());
			
			int count = 0;
			for (List<Measurement> mess : list_measurements)
			{
				count += mess.size();
			}
			assertEquals(TestFileData.total*CellType.values().length, count);
		}
		catch (IOException | InvalidFormatException e)
		{
			fail("Exception Thrown");
		}
	}
	

}
