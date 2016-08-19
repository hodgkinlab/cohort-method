package edu.wehi.celcalc.cohort.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import edu.wehi.celcalc.cohort.test.FILES;
import edu.wehi.celcalc.cohort.test.ResourceLoaderForTests;

public class TestParsingCellCountXCELReader
{
	
	static String testFileForReading = ResourceLoaderForTests.getResource(FILES.TESETPARSING);
	
	static FileInputStream file = null;
	static XSSFWorkbook workbook = null;
	static XSSFSheet sheet = null;
	
	static
	{
		try
		{
			file = new FileInputStream(new File(testFileForReading));
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(CellType.LIVE.name);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	final static int firstRow = 3;
	final static int firstCol = 1;
	
	
	@Test
	public void testCorrectTestEnv()
	{
		assertTrue(file!=null);
		assertTrue(workbook!=null);
	}
	
	@Test
	public void testFirstCol()
	{
		assertEquals(firstCol,CellCountXCELReader.indexOfFirstColumn(sheet));
	}
	
	@Test
	public void testFirstRow()
	{
		assertEquals(firstRow,CellCountXCELReader.indexOfFirstRow(sheet));
	}
	

}
