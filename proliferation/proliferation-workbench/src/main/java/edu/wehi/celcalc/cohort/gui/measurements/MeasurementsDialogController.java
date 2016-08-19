package edu.wehi.celcalc.cohort.gui.measurements;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;



public class MeasurementsDialogController {

	MeasurementsDialogModel model = new MeasurementsDialogModel();
	MeasurementsDialogView view = new MeasurementsDialogView();
	final List<MeasurementsDialogConfirmActionListeners> confirmListeners = new ArrayList<>();
	
	public MeasurementsDialogController()
	{
		view.btnNew.addActionListener(e ->{
			Measurement mes = model.createBlankMeasurement();
			refreshTable();
			view.setSelectedMeasurement(mes);
		});
		
		view.btnDelete.addActionListener(e ->{
			List<Measurement> mess = view.getSelectedMeasurements();
			model.removeMeasurements(mess);
			refreshTable();
		});
		
		view.btnConfirm.addActionListener(e ->{
			confirmListeners.forEach(l -> l.notifiedOfNewMeasurements(model.getMeasurements()));
		});
	}
	
	public JFrame getView()
	{
		return view;
	}

	private void refreshTable()
	{
		MeasurementTableModel tableModel = new MeasurementTableModel(model.getMeasurements());
		view.setMeasurementTableModel(tableModel);
	}

	public void addMeasurementsDialogConfirmActionListener(MeasurementsDialogConfirmActionListeners listener)
	{
		confirmListeners.add(listener);
	}
	
	public void removeMeasurementsDialogConfirmActionListeners(MeasurementsDialogConfirmActionListeners listener)
	{
		confirmListeners.remove(listener);
	}
	
	
	public static void main(String[] args) {
		GUI.guinof(new MeasurementsDialogController().getView());
	}
		
}
