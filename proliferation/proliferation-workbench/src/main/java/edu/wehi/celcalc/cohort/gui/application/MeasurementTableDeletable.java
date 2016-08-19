package edu.wehi.celcalc.cohort.gui.application;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;

public interface MeasurementTableDeletable {

	
	public void notifiedOfMeasurementsToDelete(List<Measurement> measurements);
	
}
