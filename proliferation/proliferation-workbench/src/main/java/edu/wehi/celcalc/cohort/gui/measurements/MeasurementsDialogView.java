package edu.wehi.celcalc.cohort.gui.measurements;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;
import edu.wehi.celcalc.cohort.gui.application.MeasurementTable;

public class MeasurementsDialogView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/**
		Tree.collapsedIcon
		FileChooser.directoryIcon
		FileChooser.detailsViewIcon
		OptionPane.questionIcon
		FileChooser.newFolderIcon
		FileView.floppyDriveIcon
		Tree.openIcon
		Tree.expandedIcon
		OptionPane.informationIcon
		Tree.closedIcon
		Tree.leafIcon
		FileChooser.upFolderIcon
		OptionPane.errorIcon
		ToolBar.handleIcon
		FileChooser.floppyDriveIcon
		FileChooser.fileIcon
		RadioButton.icon
		FileView.fileIcon
	 */
	
	JButton btnNew = 			new JButton("New",					UIManager.getIcon("Tree.leafIcon"));
	JButton btnDelete =			new JButton("Delete",				UIManager.getIcon("Tree.expandedIcon"));
	JButton btnConfirm = 		new JButton("Create Measurements",	UIManager.getIcon("OptionPane.informationIcon"));
	MeasurementTable table = 	new MeasurementTable();
	
	public MeasurementsDialogView()
	{
		setLayout(new BorderLayout());
		
		JPanel pnlNorth = new JPanel();
		pnlNorth.add(btnNew);
		pnlNorth.add(btnDelete);
		add(pnlNorth, BorderLayout.NORTH);
		
		JScrollPane pane = new JScrollPane(table);
		add(pane,BorderLayout.CENTER);
		
		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnConfirm);
		add(pnlSouth, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args)
	{
		GUI.guinof(new MeasurementsDialogView());
	}

	public void setMeasurementTableModel(MeasurementTableModel tableModel) {
		table.setMeasurementTableModel(tableModel);
		table.revalidate();
		table.repaint();
	}

	public void setSelectedMeasurement(Measurement mes) {
		table.setSelectedMeasurement(mes);
	}


	public List<Measurement> getSelectedMeasurements() {
		return table.getSelectedMeasurements();
	}

}
