package edu.wehi.demo;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;
import edu.wehi.celcalc.cohort.res.TestFileData;

public class ExampleMeasurementTable {

	public static void main(String[] args) {
		
		JTable table = new JTable();
		MeasurementTableModel model = new MeasurementTableModel(TestFileData.mesurements);
		table.setModel(model);
		GUI.scroll(new JScrollPane(table));
	}
	
}
