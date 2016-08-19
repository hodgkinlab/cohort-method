package edu.wehi.celcalc.cohort.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.data.gui.Filter;

/**
 * Query class to make queries out of measurements.
 *
 */
public class MeasurementQuery {
	
	public final Collection<Measurement> measurements;
	
	public static List<Measurement> query(Collection<Measurement> data, Filter options)
	{
		return new ArrayList<Measurement>(new MeasurementQuery(data)
		
		.withTypes		(options.getTypes())
		.withTreetments	(options.getTreatments())
		.withMinTime	(options.getTimeFrom())
		.withMaxTime	(options.getTimeTo())
		.withMinDiv		(options.getDivFrom())
		.withMaxDiv		(options.getDivTo())
		.withMinNoCells	(options.getCountFrom())
		.withMaxNoCells	(options.getCountTo())
		
		.measurements);
	}
	
	public MeasurementQuery(Collection<Measurement> measurements)
	{
		this.measurements = measurements;
	}
	
	public MeasurementQuery withMinTime(Double min)
	{
		if (min == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getTime() >= min).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withMaxTime(Double max)
	{
		if (max == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getTime() <= max).collect(Collectors.toSet()));
	}
	
	
	public MeasurementQuery withMaxNoCells(Double max)
	{
		if (max == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getCells() <= max).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withMinNoCells(Double min)
	{
		if (min == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getCells() >= min).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withType(CellType type)
	{
		if (type == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getType().equals(type)).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withTime(Double time)
	{
		if (time == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getTime() == time).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withTypes(Collection<CellType> types)
	{
		if (types == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> types.contains(m.getType())).collect(Collectors.toSet()));
	}
	
	
	
	public MeasurementQuery withTreatment(String treatment)
	{
		if (treatment == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getTreatment().equals(treatment)).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withTreetments(Collection<String> treatments)
	{
		if (treatments == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> treatments.contains(m.getTreatment())).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withMaxDiv(Integer max)
	{
		if (max == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getDiv() <= max).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withMinDiv(Integer min)
	{
		if (min == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getDiv() >= min).collect(Collectors.toSet()));
	}
	
	public MeasurementQuery withDivision(Integer division)
	{
		if (division == null)
		{
			return this;
		}
		return new MeasurementQuery(measurements.stream().filter(m -> m.getDiv() == division).collect(Collectors.toSet()));
	}
		
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (Measurement result : measurements)
		{
			sb.append(result+"\n\n \t-----------\n\n");
		}
		return sb.toString();
	}
	
	public static Set<String> allTreatments(Collection<Measurement> measurements)
	{
		return measurements.stream().map(m -> m.getTreatment()).collect(Collectors.toSet());
	}
	
	public List<Measurement> toSeries()
	{
		return new ArrayList<>(measurements);
	}
	
	public static List<Double> allTimes(Collection<Measurement> measurements)
	{
		Set<Double> result = measurements.stream().map(m -> m.getTime()).collect(Collectors.toSet());
		List<Double> sortedResult = new ArrayList<>(result);
		Collections.sort(sortedResult);
		return sortedResult;
	}

	public static int highestDiv(List<Measurement> selectedMeasurements) 
	{
		return selectedMeasurements.stream().mapToInt(m -> m.getDiv()).max().getAsInt();
	}

	public static Set<CellType> allCellTypes(Collection<Measurement> selectedMeasurements)
	{
		return selectedMeasurements.stream().map(m -> m.getType()).collect(Collectors.toSet());
	}

	public static Set<Integer> allDivisions(Collection<Measurement> selectedMeasurements)
	{
		List<Integer> list = selectedMeasurements.stream().map(m -> m.getDiv()).collect(Collectors.toList());
		Collections.sort(list);
		return new LinkedHashSet<>(list);
	}
	
}
