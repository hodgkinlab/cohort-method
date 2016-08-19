/*
 * Author: Damian Pavlyshyn
 * Date: 24/11/2014
 */

package edu.wehi.celcalc.cohort.old;

import java.io.*;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CohortXLSReader {

	private Set concentrationSet = new HashSet<Double>();

	private Map surviveCell = new HashMap<Double, Map<Double, List<List<Double>>>>();
	private Map deathCell = new HashMap<Double, Map<Double, List<List<Double>>>>();
	private Map dropCell = new HashMap<Double, Map<Double, List<List<Double>>>>();

	
	

	
	/*
	 * Reads cell count data from .xls (not .xlsx) formats
	 *
	 * Data should be in the following format:
	 *
	 * Sheets should represent live/drop/dead cell counts and should be labelled
	 * live/drop/dead accordingly
	 *
	 * >>>>>>>
	 *
	 * This is translated into up to three Dictionaries (surviveCell, deathCell,
	 * dropCell), which each have the following format:
	 *
	 * Dictionary (indexed by Double values of concentrations)
	 *  -> Dictionary (indexed by Double values of timepoints)
	 *	  -> List (indexed by repeats starting from 0)
	 *      -> List (indexed by division number starting from 0)
	 *		  -> Double of cell counts
	 */
	public void parseFile(String file) {
		try {
			//POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			FileInputStream fs = new FileInputStream(new File(file));
			XSSFWorkbook wb = new XSSFWorkbook(fs);

			XSSFSheet liveSheet = wb.getSheet("live");
			XSSFSheet deadSheet = wb.getSheet("dead");
			XSSFSheet dropSheet = wb.getSheet("drop");

			for (int cellType=0; cellType < 3; cellType++) {
				XSSFSheet sheet=null;
				// Distinguish which sheets are present and analyse them
				// This is a hack and I'm very sorry if you are reading this
				if (cellType == 0) {
					if (liveSheet != null) {
						sheet = liveSheet;
					} else {
						cellType++;
					}
				}
				if (cellType == 1) {
					if (deadSheet != null) {
						sheet = deadSheet;
					} else {
						cellType++;
					}
				}
				if (cellType == 2) {
					if (dropSheet != null) {
						 sheet = dropSheet;
					} else {
						break;
					}
				}

				int lastRowInd = sheet.getLastRowNum();
				int currRowInd = 0;

				while(currRowInd < lastRowInd) {
					XSSFRow currRow = sheet.getRow(currRowInd);
					// Check if row is a concentration
					int leadIndex = currRow.getFirstCellNum();

					if (leadIndex == 0) {
						// Row contains conentration and times, 
						// so check number of divisions
						int timesRowInd = currRowInd;
						XSSFRow timesRow = sheet.getRow(timesRowInd);

						currRowInd = timesRowInd + 1;
						currRow = sheet.getRow(currRowInd);
						int divisions = 0;

						// Count the number of divisions between concentrations
						while (currRowInd <= lastRowInd && currRow.getCell((short)0) == null) {
							divisions++;
							currRowInd++;
							currRow = sheet.getRow(currRowInd);
						}

						// Record the concentration
						currRow = sheet.getRow(timesRowInd);
						double currConc = timesRow
								.getCell((short)0)
								.getNumericCellValue();
						concentrationSet.add(currConc);

						short firstCellInd = 1;
						short lastCellInd = timesRow.getLastCellNum();

						Map singleConcData = new HashMap<Double, List<List<Double>>>();

						// Loop through all timepoints of a given concentration
						for(short i=firstCellInd; i<lastCellInd; i++) {

							double time = timesRow
								.getCell(i)
								.getNumericCellValue();

							List singleTimeData = new ArrayList<Double>();

							// Loop through each division until you hit the next 
							// timerow or EOF
							for(int j=1; j<=divisions; j++) {
								XSSFCell currCell  = sheet
										.getRow(timesRowInd + j)
										.getCell(i);
								
								if (currCell != null) {
									singleTimeData.add(currCell
											.getNumericCellValue()
											);
								} else {
									break;
								}
							}

							// If there was any data, add it
							if (!singleTimeData.isEmpty()) {
								if (!singleConcData.containsKey(time)) {
									singleConcData.put(time, new ArrayList());
								}

								((List) singleConcData.get(time)).add(singleTimeData);
							}
						}

						if (cellType == 0) {
							surviveCell.put(currConc, singleConcData);
						} else if (cellType == 1) {
							deathCell.put(currConc, singleConcData);
						} else if (cellType == 2) {
							dropCell.put(currConc, singleConcData);
						}

					}
				}
			}

		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}

	/*
	 * Returns the set of concentrations as a list and in increasing order
	 */
	public List<Double> getConcentrationList()
	{
		List<Double> concentrationList = new ArrayList<Double>(concentrationSet);
		Collections.sort(concentrationList);
		return concentrationList;
	}

	/*
	 * Usually the data will take the form of several repeats per (time/concenctration)
	 * key. This function returns an equivalent dataset with the relevant averages
	 * taken over the repeat experiments.
	 */
	public static Map<Double, Map<Double, List<Double>>> getMeans(Map<Double, Map<Double, List<List<Double>>>> cellData)
	{
		Map<Double, Map<Double, List<Double>>> meanData = new HashMap<Double, Map<Double, List<Double>>>();
		for (Double conc : cellData.keySet()) {

			Map<Double, List<Double>> timeMeans = new HashMap<Double, List<Double>>();
			for (Double time : cellData.get(conc).keySet()) {
				List<List<Double>> repeats = cellData.get(conc).get(time);
				List<Double> means = new ArrayList<Double>();
				for (int div = 0; div<repeats.get(0).size(); div++) {

					double runSum = 0;
					int numReps = 0;

					for (int rep=0; rep<repeats.size(); rep++) {
						try
						{
							runSum += repeats.get(rep).get(div);
						}
						catch (Exception e)
						{
							
						}
						numReps ++;
					}

					means.add(runSum/numReps);
				}

				timeMeans.put(time, means);
			}

			meanData.put(conc, timeMeans);
		}
		return meanData;
	}


	/*
	 * Series of methods for displaying interpreted datasets.
	 */
	public Map<Double, Map<Double, List<Double>>> getLiveMeans() {
		return getMeans(surviveCell);
	}

	public Map<Double, Map<Double, List<Double>>> getDeadMeans() {
		return getMeans(deathCell);
	}

	public Map<Double, Map<Double, List<Double>>> getDropMeans() {
		return getMeans(dropCell);
	}

	public Map<Double, Map<Double, List<List<Double>>>> getLiveData() {
		return surviveCell;
	}

	public Map<Double, Map<Double, List<List<Double>>>> getDeadData() {
		return deathCell;
	}

	public Map<Double, Map<Double, List<List<Double>>>> getDropData() {
		return dropCell;
	}
}
