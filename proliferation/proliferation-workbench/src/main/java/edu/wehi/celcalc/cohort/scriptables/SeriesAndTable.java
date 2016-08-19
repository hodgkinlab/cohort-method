package edu.wehi.celcalc.cohort.scriptables;

import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.NameableCollection;
import edu.wehi.graphplot.python.NameableMap;

public class SeriesAndTable implements NameableCollection {
	
	final GPXYSeries series;
	
	public SeriesAndTable(String name, GPXYSeries series, NameableMap table) {
		super();
		this.series = series;
		this.table = table;
		this.name = name;
	}
	
	public GPXYSeries getSeries() {
		return series;
	}
	public NameableMap getTable() {
		return table;
	}
	
	final NameableMap table;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	String name;
	
	@Override
	public int size() {
		return -1;
	}

}
