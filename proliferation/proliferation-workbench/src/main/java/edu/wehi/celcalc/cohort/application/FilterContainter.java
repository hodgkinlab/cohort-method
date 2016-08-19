package edu.wehi.celcalc.cohort.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.gui.Filter;

public class FilterContainter implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	
	HashMap<String, Filter> filters = new HashMap<>();
	
	public boolean addFilter(Filter filter)
	{
		if (filter == null)
		{
			return false;
		}
		if (filters.get(filter.getName())!= null)
		{
			return false;
		}
		filters.put(filter.getName(),filter);
		return true;
	}
	
	public List<Filter> getFilters(Collection<String> names)
	{
		return filters.values().stream().filter(m -> names.contains(m.getName())).collect(Collectors.toList());
	}
	
	public Filter getFilter(String filterName)
	{
		return filters.get(filterName);
	}
	
	public Set<String> getFilterNames()
	{
		return filters.keySet();
	}
	
	public Set<Filter> getAllFilters()
	{
		return new HashSet<>(filters.values());
	}

	public Set<Filter> allMatchingFiltersWithNames(List<String> filterNames)
	{
		Set<Filter> matchingFilters = new HashSet<>();
		for (String f : filterNames)
		{
			matchingFilters.add(filters.get(f));
		}
		return matchingFilters;
	}

	public List<Filter> getFilters(Set<String> filterNames)
	{
		if (filterNames == null) return null;
		List<Filter> selectedFilters = new ArrayList<>();
		for (String name : filterNames)
		{
			selectedFilters.add(filters.get(name));
		}
		return selectedFilters;
	}

	public void addAll(Collection<Filter> filters2)
	{
		for (Filter f : filters2)
		{
			filters.put(f.getName(), f);
		}
	}

	public void createAllLiveFiletersForTreatments(Collection<String> treatments)
	{
		for (String treatment : treatments)
		{
			createLiveFilterForTreatment(treatment);
		}
	}
	
	public Filter createLiveFilterForTreatment(String treatment)
	{
		if (filters.containsKey(treatment))
		{
			return filters.get(treatment);
		}
		
		String filterName = CellType.LIVE.name+" "+treatment;
		Filter filter = new Filter(
				filterName,
				CellType.LIVE,
				treatment);
		
		addFilter(filter);
		return filter;
	}
	
	
}
