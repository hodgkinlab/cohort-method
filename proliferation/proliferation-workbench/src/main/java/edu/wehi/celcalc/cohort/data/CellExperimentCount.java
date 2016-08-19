package edu.wehi.celcalc.cohort.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CellExperimentCount {
	
	public final String treatment;
	public final List<Measurement> measurements = new ArrayList<Measurement>();
	public final CellType type;
	
	
	/**
	 * 
	 * Assumes the count is of live cells
	 * 
	 * @param treatment - categorical data for treatment
	 */
	public CellExperimentCount(String treatment)
	{
		this.treatment = treatment;
		type = CellType.LIVE;
	}
	
	/**
	 * @param treatment - categorical data for treatment
	 * @param type - type of cells being counted
	 * @param div - what division it is
	 */
	public CellExperimentCount(String treatment, CellType type)
	{
		this.treatment = treatment;
		this.type = type;
	}
	
	/**
	 * @return all times at which a measurement was taken
	 */
	Set<Double> getTimeMeasurementsSet()
	{
		final Set<Double> mesurments = new HashSet<Double>();
		for (Measurement mes : measurements)
		{
			mesurments.add(mes.getTime());
		}
		return mesurments;
	}
	
	/**
	 * @param time - time which measurement was taken
	 * @return all measurements taken at a given time
	 */
	Set<Measurement> getMeasurementsAtTime(double time)
	{
		return measurements.stream().
				filter(p -> p.getTime() == time).
				collect(Collectors.toCollection(() -> new HashSet<Measurement>()));
	}
	

}
