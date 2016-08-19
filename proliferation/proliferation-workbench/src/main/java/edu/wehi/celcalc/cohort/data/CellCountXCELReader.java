package edu.wehi.celcalc.cohort.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ejml.simple.SimpleMatrix;

public class CellCountXCELReader {

	public static boolean cellIsNumeric(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return true;
		case Cell.CELL_TYPE_STRING:
			try {
				Double.parseDouble(cell.getStringCellValue());
				return true;
			} catch (Exception e) {
				return false;
			}
		case Cell.CELL_TYPE_FORMULA:
			try {
				Double.parseDouble(cell.getCellFormula());
				return true;
			} catch (Exception e) {
				return false;
			}
		default:
			return false;
		}
	}

	public static double cellValue(Cell cell) {
		try {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_STRING:
				return Double.parseDouble(cell.getStringCellValue());
			case Cell.CELL_TYPE_FORMULA:
				return Double.parseDouble(cell.getCellFormula());

			default:
				return 0.0;
			}
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static String cellToStr(Cell cell) {
		try {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() + "";
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue() + "";
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue() + "";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula() + "";
		default:
			return "";
		}
		} catch (Exception e)
		{
			return "";
		}
	}

	public static int indexOfFirstColumn(XSSFSheet sheet) {
		int minFirstCellIndex = -1;
		for (Row row : sheet) {
			short firstCellInRow = row.getFirstCellNum();
			minFirstCellIndex = (minFirstCellIndex == -1) ? firstCellInRow
					: ((firstCellInRow != -1)?Math.min(firstCellInRow, minFirstCellIndex):minFirstCellIndex);
		}
		return minFirstCellIndex;
	}

	public static int indexOfFirstRow(XSSFSheet sheet) {
		int i=0;
		for (Row row : sheet)
		{
			for (Cell cell : row)
			{
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					continue;
				default:
					return i;
				}
			}
			i++;
		}
		return -1;
	}

	public static int[] indexsOfTreatments(XSSFSheet sheet, int c) {

		List<Integer> indexs = new ArrayList<Integer>();
		int i=0;
		for (Row row : sheet) {
			Cell cell = row.getCell(c);
			if (cell == null)
			{
				i++;
				continue;
			}
			String contents = cellToStr(cell);
			if (contents != "") {
				indexs.add(i);
			}
			i++;
		}
		return indexs.stream().mapToInt(z -> z).toArray();
	}

	public static int[] indexsOfTreatments(XSSFSheet sheet) {
		return indexsOfTreatments(sheet, indexOfFirstColumn(sheet));
	}
	
	public static List<String> treatments(XSSFSheet sheet) {
		List<String> list = new ArrayList<>();
		int[] indexsOfTreatments = indexsOfTreatments(sheet);
		int col = indexOfFirstColumn(sheet);
		for (int i: indexsOfTreatments)
		{
			list.add(cellToStr(sheet.getRow(i).getCell(col)));
		}
		return list;
	}
	

	public static int noOfDivisions(XSSFSheet sheet, int treatmentNo) {
		int c = indexOfFirstColumn(sheet);
		int[] indexesOfTreatments = indexsOfTreatments(sheet, c);

		// handle last case!
		if (treatmentNo == indexesOfTreatments.length - 1) {
			return sheet.getLastRowNum()
					- indexesOfTreatments[indexesOfTreatments.length-1];
		}
		// normal case
		else {
			return indexesOfTreatments[treatmentNo + 1]
					- indexesOfTreatments[treatmentNo]-1;
		}
	}
	
	public static List<Double> getTimes(XSSFSheet sheet, int treatmentNo)
	{
		List<Double> times = new ArrayList<Double>();
		int[] indexsOfTreatments = indexsOfTreatments(sheet);
		int indexOfTreatment = indexsOfTreatments[treatmentNo];
		Row row = sheet.getRow(indexOfTreatment);
		int firstCellNo = indexOfFirstColumn(sheet)+1;
		int lastCellNo = row.getLastCellNum();
		for (int c = firstCellNo; c < lastCellNo; c++)
		{
                    Cell cell = row.getCell(c);
                    if (cell != null)
                    {
                        double time = cellValue(cell);
                        times.add(time);
                    }
		}
		return times;
	}

	public static List<Measurement> processTreatment(XSSFSheet sheet, int treatmentNo, CellType type, String treatment )
	{
		List<Measurement> count = new ArrayList<>();
		
		int[] indexsOfTreatments = indexsOfTreatments(sheet);
		int indexOfTreatment = indexsOfTreatments[treatmentNo];
		int noOfDivisions = noOfDivisions(sheet, treatmentNo);
		
		int firstCol = indexOfFirstColumn(sheet);
		
		// get all the times
		List<Double> times = getTimes(sheet, treatmentNo);
		
		// filling the count
		int i = 0;
		
		// time measurement column
		int kk = 0;
		for (int d = indexOfTreatment+1; d <= indexOfTreatment + noOfDivisions; d++)
		{
			kk = 0;
			for (int c = firstCol+1; c < firstCol+times.size()+1; c++)
			{
				Cell cell = sheet.getRow(d).getCell(c);
				if (cell != null)
				{
					double cells = cellValue(cell);
					double time = times.get(kk);
					Measurement mes = new Measurement(time, cells,i,type, treatment);
					count.add(mes);
					kk++;
                }
			}
			i++;
		}
		return count;
	}

	/**
	 * @return key = treatment, value = list of measurements
	 */
	public static Map<String, List<Measurement>> processSheetToMap(XSSFSheet sheet, String sheetName) {

		Map<String, List<Measurement>> map = new HashMap<>();
		
		int[] treatmentIndexs = indexsOfTreatments(sheet);
		List<String> treatments = treatments(sheet);
		for (int i=0; i<treatmentIndexs.length;i++)
		{
			map.put(treatments.get(i),
					processTreatment(sheet, i, CellType.cellType(sheetName), treatments.get(i)));
		}
		return map;
	}

	

	public static Map<String, List<Measurement>>
		processBookToMap(File file) throws IOException, InvalidFormatException {

		Map<String, List<Measurement>> results = new HashMap<>();

		// Get the workbook instance for XLS file
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(file);

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			
			XSSFSheet sheet = workbook.getSheetAt(i);
			String name = workbook.getSheetName(i).toLowerCase();
			
			if (1 == workbook.getNumberOfSheets())
			{
				name = CellType.LIVE.name;
			}
			
			if (CellType.isIn(name))
			{
				Map<String, List<Measurement>> count = processSheetToMap(sheet,name);
				for (String key : count.keySet())
				{
					List<Measurement> oldVals = results.get(key);
					if (oldVals == null) {
						oldVals = new ArrayList<Measurement>();
					}
					oldVals.addAll(count.get(key));
					results.put(key, oldVals);
				}
			}
		}
		return results;
	}
	
	public static Map<String, List<Measurement>> processBookToMap(String path) throws IOException, InvalidFormatException {
		return processBookToMap(new File(path));
	}
	
	public static Dataset createDatasetFromFile(File file)
			throws IOException, InvalidFormatException {
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		if (workbook.getNumberOfSheets() >= 1) {
			XSSFSheet sheet = workbook.getSheetAt(0);

			List<String> names = new ArrayList();
			List<Integer> positions = new ArrayList();

			String prevName = null;
			for (Row row : sheet) {
				int firstColIdx = row.getFirstCellNum();
				if (firstColIdx == 0) {
					Cell cell = row.getCell(0);
					int type = cell.getCellType();
					
					String currName = null;
					if (type == Cell.CELL_TYPE_NUMERIC) {
						double v = cell.getNumericCellValue();
						currName = String.valueOf(v);
					} else if (type == Cell.CELL_TYPE_STRING) {
						currName = cell.getStringCellValue();
						if (currName.trim().length() == 0) {
							currName = null;
						}
					} else if (type != Cell.CELL_TYPE_BLANK) {
						String errMessage = "error in parsing input file";
						InvalidFormatException e
								= new InvalidFormatException(errMessage);
						throw e;
					}
					
					if (currName == null) {
						continue;
					}
					
					int currPosition = row.getRowNum();

					if (prevName != null) {
						names.add(prevName);
						positions.add(currPosition);
					}
					prevName = currName;
				}
			}
			names.add(prevName);
			int lastPosition = sheet.getLastRowNum() + 1;
			positions.add(lastPosition);

			int start = 0;
			int idxCond = 0;

			List<Condition> conditions = new ArrayList();
			for (Integer p : positions) {
				int stop = (int) p - 1;
				Row row0 = sheet.getRow(start);
				int lastColIdx = row0.getLastCellNum();
				
				int numRows = stop - start + 1;
				int numCols = lastColIdx - 1;
				
				SimpleMatrix data = new SimpleMatrix(numRows, numCols);
				
				for (int i = start; i <= stop; i++) {
					Row row = sheet.getRow(i);
					for (int j = 1; j < lastColIdx; j++) {
						Cell cell = row.getCell(j);
						double n = cell.getNumericCellValue();
						data.set(i - start, j - 1, n);
					}
				}
				
				String condName = names.get(idxCond);
				Condition cond = new Condition(condName, data);
				conditions.add(cond);

				start = (int) p;
				idxCond++;
			}
			String name = file.getName();
			Dataset dataset = new Dataset(name, conditions);
			return dataset;
		}
		Dataset dataset = new Dataset(null, null);
		return dataset;
	}
}
