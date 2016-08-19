package edu.wehi.celcalc.cohort.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.wehi.celcalc.cohort.data.CellCountXCELReader;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.test.FILES;
import edu.wehi.celcalc.cohort.test.ResourceLoaderForTests;
import edu.wehi.celcalc.cohort.util.Utilities;

public class TestFileData
{

	public static String testFileForReading = ResourceLoaderForTests.getResource(FILES.TESTFORMATXLSX);
	public static FileInputStream file = null;
	public static XSSFWorkbook workbook = null;
	public static XSSFSheet live_sheet = null;
	public static XSSFSheet dead_sheet = null;
	public static XSSFSheet drop_sheet = null;
	public static List<Measurement> mesurements = null;
	public static final int noOfSeries = 63;
	
	
	static
	{
		try
		{
			file = new FileInputStream(new File(testFileForReading.replace("Program%20Files%20(x86)", "Program Files (x86)")));
			workbook = new XSSFWorkbook(file);
			live_sheet = workbook.getSheet(CellType.LIVE.name);
			dead_sheet = workbook.getSheet(CellType.DEAD.name);
			drop_sheet = workbook.getSheet(CellType.DROP.name);
			mesurements = Utilities.toList(CellCountXCELReader.processBookToMap(testFileForReading));
		}
		catch (IOException | InvalidFormatException e)
		{
			e.printStackTrace();
		}
	}
	
	final public static int firstCol = 0;
	final public static int firstRow = 0;
	
	final public static int[] indexsOfTreatments = new int[]{0,8,16};
	public static final String[] actual_treatments = new String[]{"5000.0","1000.0","0.0"};
	public static int noTreatments = actual_treatments.length;
	
	public static final int noMes5000 	= 7 * 15;
	public static final int noMes1000 	= 7 * 15;
	public static final int noMes0 	= 7 * 18;
	public static final int total = noMes5000 + noMes1000 + noMes0;
	public static final int totalDataPoints = (noMes5000 + noMes1000 + noMes0) * CellType.values().length;
	
	public static void main(String[] args)
	{
		System.out.println(mesurements==null);
	}
}
