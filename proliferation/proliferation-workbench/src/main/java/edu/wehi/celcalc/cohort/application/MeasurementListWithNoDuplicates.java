package edu.wehi.celcalc.cohort.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.Measurement;

public class MeasurementListWithNoDuplicates extends ArrayList<Measurement> implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	public MeasurementListWithNoDuplicates()
	{
		super();
	}
	public MeasurementListWithNoDuplicates(Collection<Measurement> items)
	{
		super(items);
	}
	
	@Override
	public boolean addAll(Collection<? extends Measurement> items)
	{
		for (Measurement item : items)
		{
			if (containsName(item.getName()) || contains(item))
			{
				return false;
			}
		}
		addAllNames(items);
		return super.addAll(items);
	}
	

	@Override
	public boolean add(Measurement item)
	{
		if (containsName(item.getName()) || contains(item))
		{
			return false;
		}
		else
		{
			return super.add(item);
		}
	}
	
	@Override
	public boolean remove(Object item)
	{
		if (item instanceof Measurement)
		{
			Measurement measurement = (Measurement) item; 
			names.remove(measurement.getName());
			return super.remove(measurement);
		}
		else
		{
			return false;
		}
	}
	
	Set<String> names = new HashSet<>();
	
	private void addAllNames(Collection<? extends Measurement> items)
	{
		for (Measurement item : items)
		{
			names.add(item.getName());
		}
	}
	
	private boolean containsName(String name)
	{
		return names.contains(name);
	}
	
	public Set<String> getAllNames()
	{
		return names;
	}
	
	public List<String> getAllTreatments()
	{
		Set<String> treatments = new HashSet<>();
		this.stream().forEach(mes -> treatments.add(mes.getTreatment()));
		return new ArrayList<>(treatments);
	}
	

}
