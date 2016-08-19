package edu.wehi.celcalc.cohort.data;

import java.util.List;

public class Dataset {

	final String name;
	final List<Condition> conditions;

	public Dataset(String name, List<Condition> conditions) {
		this.name = name;
		this.conditions = conditions;
	}
	
	public String getName() {
		return name;
	}
	public List<Condition> getConditions() {
		return conditions;
	}
}
