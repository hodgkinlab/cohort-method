package edu.wehi.celcalc.cohort.gui.measurements;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;

public interface MeasurementsDialogConfirmActionListeners {

	public void notifiedOfNewMeasurements(List<Measurement> measurements);
	
}
