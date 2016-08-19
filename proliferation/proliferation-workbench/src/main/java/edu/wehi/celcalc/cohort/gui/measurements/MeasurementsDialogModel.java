package edu.wehi.celcalc.cohort.gui.measurements;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;

public class MeasurementsDialogModel {
	
	public final List<Measurement> measurements = new ArrayList<>();

	public List<Measurement> getMeasurements()
	{
		return measurements;
	}

	public Measurement createBlankMeasurement()
	{
		Measurement mes = new Measurement(-1.0, -1.0, -1, CellType.LIVE, "TODO");
		measurements.add(mes);
		return mes;
	}

	public void removeMeasurements(List<Measurement> mess) {
		measurements.removeAll(mess);
	}

}
