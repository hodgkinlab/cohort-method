///////////////////////////////////////////////////////////////////
//  File: CohortExpReader.java
//  Author: Damian Pavlyshyn
//  Date: 24/11/2014
//  Contributors:
//  Tester:
//
//  class CohortExpReader implements methods for parsing experimental
//  data in format used for Cyton fitter into something the processor can use.
//
//  This class is a copied and slightly modified version of CPSMExpReader.java,
//  written by Hui Chen
///////////////////////////////////////////////////////////////////

package edu.wehi.celcalc.cohort.old;

import java.util.*;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
/////////////////////////////////// HSSF-
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *  Class CPSMExpReader implements methods for parse experimental
 *  data file into something the processor can use.
 */


public class CohortExpReader {

	// attribute fields:

	/**
	 * vector that contain a list of concentration.
	 * concentrationList is a vector of double.
	 */
	private Vector concentrationList = new Vector();

	/**
	 * 3D vector that contain a list of survive cell tables.
	 * Each of the table is a 2D vector.
	 * surviveCell is a vector of vector of vector of double.
	 */
	private Vector surviveCell = new Vector();

	/**
	 * 3D vector that contain a list of death cell tables.
	 * Each of the table is a 2D vector.
	 * surviveCell is a vector of vector of vector of double.
	 */
	private Vector deathCell = new Vector();

	/**
	 * vector that contain a list of initial cell number.
	 * initialCellNumber is a vector of double.
	 */
	private Vector initialCellNumber = new Vector();

	/**
	 * vector that contain flags for every death cell table .
	 * deathFlag is a vector of String.
	 * The death String type can be : "Total Death" "Undiv/Div Death"
	 * "Division Death" "Null"
	 */
	private Vector deathFlag = new Vector();

	/**
	 * 2D vector that contain a list of survive time.
	 * Each of the survive time is a vector.
	 * surviveTime is a vector of vector of double.
	 */
	private Vector surviveTime = new Vector();

	/**
	 * 2D vector that contain a list of death time.
	 * Each of the survive time is a vector.
	 * deathTime is a vector of vector of double.
	 */
	private Vector deathTime = new Vector();

	/**
	 * vector that contain a list of maxium division number.
	 * Each of the division number is a integer.
	 * maxDivision is a vector of int.
	 */
	private  Vector maxExpDivision = new Vector();

	/** varible to store the stimulus name. */
	private String stimulus = new String();

	/** varible to store the experiment date. */
	private String date = new String();

	/** varible to store the cell type. */
	private String cellType = new String();

	/** varible to store the additional comment. */
	private String comment = new String();


	/** constructor. */
	public CohortExpReader() {}

	/**
	 * Returns the stimulus name.
	 * @return stimulus name.
	 */
	public String getStimulus() {
		if(!stimulus.equals("-1.0"))
			return stimulus;
	       	else
			return null;
	}

	/**
	 * Returns the date of experiment.
	 * @return data of experiment.
	 */
	public String getDate() {
		if(!date.equals("-1.0"))
			return date;
		else
			return null;
	}

	/**
	 * Returns the cell type.
	 * @return cell type.
	 */
	public String getCellType() {
		if(!cellType.equals("-1.0"))
			return cellType;
		else
			return null;
	}

	/**
	 * Returns the additional comment.
	 * @return additional comment.
	 */
	public String getComment() {
		if(!comment.equals("-1.0"))
			return comment;
		else
			return null;
	}

	/**
	 * Returns the concentration list.
	 * @return concentration list.
	 */
	public Vector getConcentrationList() {
		return concentrationList;
	}

	/**
	 * Returns the initial cell number table.
	 * @return initial cell number list.
	 */
	public Vector getInitialCellNumber() {
		return initialCellNumber;
	}

	/**
	 * Returns the type of death cell table.
	 * @return a vector of String.
	 * The death flag can be: "Total Death" "Undiv/Div Death"
	 * "Division Death" "Null"
	 */
	public Vector getDeathFlag() {
		return deathFlag;
	}

	/**
	 * Returns the survive cell table, which is a 3D vector.
	 * @return survive cell table.
	 */
	public Vector getSurviveCell() {
		return surviveCell;
	}

	/**
	 * Returns the death cell table, which is a 3D vector.
	 * @return death cell table.
	 */
	public Vector getDeathCell() {
		return deathCell;
	}

	/**
	 * Returns the time interval of survive cell, which is a 2D vector.
	 * @return vector of survive time.
	 */
	public Vector getSurviveTime() {
		return surviveTime;
	}

	/**
	 * Returns the time interval of death cell, which is a 2D vector.
	 * @return vector of survive time.
	 */
	public Vector getDeathTime() {
		return deathTime;
	}

	/**
	 * Returns a list of the maxium division number for each survive cell table.
	 * @return vector of maxium division number.
	 */
	public Vector getMaxExpDivision() {
		return maxExpDivision;
	}

	/**
	 * extract experimental data from XL spreadsheet file as input flb 15Apr07.
	 * @param file name of experimental data file.
	 */
	public void parseXLSExpFile(String file) {
		String cell;
		double number, concentration, time;
		int i, row, lastRow, table, column, lastCell, firstCell;  // counters
		XSSFWorkbook wb;
		XSSFSheet sheet;
		XSSFRow currLine;
		XSSFCell sCell;
// flb 16Apr07 int nTimes;

		try {
			wb = new XSSFWorkbook(file);
			sheet = wb.getSheetAt(0);
			lastRow = sheet.getLastRowNum();
			row = 0;

// table counter initialize.
			table = 0;

// until it hits the EOF
			while(row <= lastRow) {

// get rid of all the empty lines.
				while(((currLine = sheet.getRow(row++)) == null) || (currLine.getPhysicalNumberOfCells() <= 0)) ;
				lastCell = currLine.getLastCellNum();
				firstCell = currLine.getFirstCellNum();
// handle the concentration cell
				i = firstCell;
				if(((sCell = currLine.getCell((short) i++)) != null) && (sCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)) {
					concentration = sCell.getNumericCellValue();
					concentrationList.addElement(new Double(concentration));
					surviveTime.addElement(new Vector());
				}
				//else
				//	concentration = -1.0;
				//  concentrationList.addElement(new Double(concentration));

// handle the time interval for survive cell
				//surviveTime.addElement(new Vector());
// flb 16Apr07 nTimes = 0;	// temporary flb 16Apr07
				while(i <= lastCell) {
					if(((sCell = currLine.getCell((short) i++)) != null) && (sCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC))
						time = sCell.getNumericCellValue();
					else
						break;
					((Vector) surviveTime.elementAt(table)).addElement(new Double(time));
// flb 16Apr07 nTimes++;	// temporary flb 16Apr07
				}

// flb 16Apr07 System.out.println("CPSMExpReader.parseExpFile() live times = " + nTimes);	// temporary flb 16Apr07
// handle the time interval for death cell
				deathTime.addElement(new Vector());
// flb 16Apr07 nTimes = 0;	// temporary flb 16Apr07
				while(i <= lastCell) {
					if(((sCell = currLine.getCell((short) i++)) != null) && (sCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC))
						time = sCell.getNumericCellValue();
					else
						break;
					((Vector) deathTime.elementAt(table)).addElement(new Double(time));
// flb 16Apr07 nTimes++;	// temporary flb 16Apr07
				}

// flb 16Apr07 System.out.println("CPSMExpReader.parseExpFile() Live + dead times = " + nTimes);
				surviveCell.addElement(new Vector());
				deathCell.addElement(new Vector());

// build all the 3D vector.
				for(i = 0; i < ((Vector) surviveTime.elementAt(table)).size(); i++)
					((Vector) surviveCell.elementAt(table)).addElement(new Vector());
				for(i = 0; i < ((Vector) deathTime.elementAt(table)).size(); i++)
					((Vector) deathCell.elementAt(table)).addElement(new Vector());

// flb 16Apr07 System.out.println("CPSMExpReader.parseExpFile() start row = " + row);
				while(((currLine = sheet.getRow(row++)) != null) && (currLine.getPhysicalNumberOfCells() > 0)) {
					lastCell = currLine.getLastCellNum();

					i = 1;			//get rid of the div number (i = 0)
					column = 0;
					do {
						if(((sCell = currLine.getCell((short) i++)) != null) && (sCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC))
							number = sCell.getNumericCellValue();
						else
							number = 0.0;
						((Vector) ((Vector) surviveCell.elementAt(table)).elementAt(column)).addElement(new Double(number));
						column++;
					} while(column < ((Vector)surviveTime.elementAt(table)).size());

//System.out.println(" size = " + ((Vector)deathTime.elementAt(table)).size());
					if(((Vector) deathTime.elementAt(table)).size() != 0) {
//System.out.println("data = " + data.toString());
						i++;			// get rid of the empty cell.
						column = 0;
//
						do {
							if(((sCell = currLine.getCell((short) i++)) != null) && (sCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC))
								number = sCell.getNumericCellValue();
							else
								number = 0.0;
							((Vector) ((Vector) deathCell.elementAt(table)).elementAt(column)).addElement(new Double(number));
							column++;
						} while(column < ((Vector) deathTime.elementAt(table)).size());
					}
				}
// flb 16Apr07 System.out.println("CPSMExpReader.parseExpFile() end row = " + (row - 1));
				table++;
			}   /* The EOF */
		} catch(Exception e) {	// handle the case of invalid file name or worry input.
e.printStackTrace();
		}

// find the max division number for each survive cell table.
		if(surviveCell.size() == 0 || concentrationList.size() == 0) {
			JOptionPane.showMessageDialog(null, "Cannot open file: Target XLS file invalid.");
			return;
		}

		maxExpDivision = findDivision();

// retrive the initial cell number from survive cell table.
		initialCellNumber = breakTable();

// find the type of each death cell table.
		deathFlag = findFlag();
	}		// end parse XLS exp file

	/**
	 * Returns the initial cell number from survive cell table.
	 * @return vector of first cell of each table.
	 */
	private Vector findDivision() {
		Vector division = new Vector();

		for(int i=0; i<surviveCell.size(); i++) {
			int number = ((Vector)((Vector)surviveCell.elementAt(i)).firstElement()).size() - 1;
			division.addElement(new Integer(number));
		}

		return division;
	}

	/**
	 * Returns the initial cell number from survive cell table.
	 * @return vector of first cell of each table.
	 */
	private Vector breakTable() {
		Vector iniCellNo = new Vector();

		for(int i=0; i<surviveCell.size(); i++)
			iniCellNo.addElement(
				((Vector)((Vector)surviveCell.elementAt(i)).firstElement()).firstElement()
			);
		return iniCellNo;
	}

	/**
	 * Returns a list of flag that identify the death table .
	 * @return vector of flag.
	 */
	private Vector findFlag() {
		Vector flagList = new Vector();

		for(int i=0; i<deathCell.size(); i++)  {
			if(((Vector)deathCell.elementAt(i)).isEmpty()) {
				flagList.addElement("Null");
			}
			else {
				Vector row = (Vector)deathCell.elementAt(i);
				int flag = find2DMax(row);
				removeTail(flag, i);
				if(flag == 0)
					flagList.addElement("Null");
				else if(flag == 1)
					flagList.addElement("Total Death");
  				else if(flag == 2)
					flagList.addElement("Undiv/Div Death");
				else
	 				flagList.addElement("Division Death");
			}
		}

		return flagList;
	}

	/**
	 * Returns a number that identify the actual size table, ie number of column .
	 * @return number of column.
	 */
	private int find2DMax(Vector row) {
		int max = 0, column = 0;

		for(int i=0; i<row.size(); i++) {
			column = findMax((Vector)row.elementAt(i));
			if(column > max)
				max = column;
			else;
		}

		return max;
	}

	/**
	 * Returns a number that identify the maxium number of row in each table.
	 * @return number of row.
	 */
	private int findMax(Vector row) {
		int counter=0, max=0;
		double cell;

		for(int i=0; i<row.size(); i++) {
			cell = ((Double)row.elementAt(i)).doubleValue();
			if(cell != 0.0){
				max += counter;
				max++;
				counter =0;
			}
			else
				counter++;
		}

		return max;
	}

	/**
	 * Remove the all zero row in each of the death cell table.
	 */
	private void removeTail(int start, int index) {
		int i;

		for (i=0; i<((Vector)deathCell.elementAt(index)).size(); i++)
			while(((Vector)((Vector)deathCell.elementAt(index)).elementAt(i)).size() != start )
				((Vector)((Vector)deathCell.elementAt(index)).elementAt(i)).removeElementAt(start);

		return;
	}

	/**
	 * Takes a line as input and test whether it is empty or not.
	 * (a line that contain only empty cells is consider as empty
	 * Returns boolean that tell if the line is empty or not.
	 * @param target String that contain one line read from experimental file.
	 * @return whether the line is empty.
	 */
	private boolean lineEmpty(String target){
		String temp = target.replaceAll(",", "");
		if (temp.length() != 0)
			return false;
		else
			return true;
	}

	/**
	 * Insert "-1.0" between two comma so that the tokenizer can reckonize it.
	 * @param target String that contain one line read from experimental file.
	 * @return String that with "-1.0" insert between two comma.
	 */
	private String handleComma(String target) {
		target = target.replaceAll(",,,,,,", ",-1.0,-1.0,-1.0,-1.0,-1.0,");
		target = target.replaceAll(",,,,,", ",-1.0,-1.0,-1.0,-1.0,");
		target = target.replaceAll(",,,,", ",-1.0,-1.0,-1.0,");
		target = target.replaceAll(",,,", ",-1.0,-1.0,");
		target = target.replaceAll(",,", ",-1.0,");
		return target;
	}

	/**
	 * Insert "0" between two comma so that the tokenizer can reckonize it.
	 * @param target String that contain one line read from experimental file.
	 * @return String that with "0" insert between two comma.
	 */
	private String handleCommaZero(String target) {
		target = target.replaceAll(",,,,,,,,,,", ",0,0,0,0,0,0,0,0,0,");
		target = target.replaceAll(",,,,,,,,,", ",0,0,0,0,0,0,0,0,");
		target = target.replaceAll(",,,,,,,,", ",0,0,0,0,0,0,0,");
		target = target.replaceAll(",,,,,,,", ",0,0,0,0,0,0,");
		target = target.replaceAll(",,,,,,", ",0,0,0,0,0,");
		target = target.replaceAll(",,,,,", ",0,0,0,0,");
		target = target.replaceAll(",,,,", ",0,0,0,");
		target = target.replaceAll(",,,", ",0,0,");
		target = target.replaceAll(",,", ",0,");
		return target;
	}
}
