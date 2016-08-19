package edu.wehi.celcalc.cohort.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author futschik.d
 * 
 * Potential class to use for creating a database
 *
 */
public class Experiment
{
	
	int id;
	Date date;
	String name;
	String description;
	
	List<Measurement> measurements = new ArrayList<Measurement>();

}
