package edu.wehi.celcalc.cohort.data.gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;


public class Filter implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private List<CellType> cellTypes = null;
	
	private List<String> includedTreatments;
	
	private Double minTime;
	
	private Double maxTime;
	
	private Integer minDiv;
	
	private Integer maxDiv;
	
	private Double minCell;
	
	@XmlElement(nillable=true)
	private Double maxCell;
	

	private String name;
	
	public Filter(String name,List<CellType> types,
			List<String> treatments, Double timeFrom, Double timeTo,
			Integer divFrom, Integer divTo, Double countFrom, Double countTo)
	{
		super();
		this.name = name;
		this.cellTypes = types;
		this.includedTreatments = treatments;
		this.minTime = timeFrom;
		this.maxTime = timeTo;
		this.minDiv = divFrom;
		this.maxDiv = divTo;
		this.minCell = countFrom;
		this.maxCell = countTo;
	}
	
	public Filter(String name, CellType type)
	{
		this.name = name;
		this.cellTypes = new ArrayList<CellType>(){
			private static final long serialVersionUID = 1L;
		{
			add(type);
		}};
	}

	public Filter(
			String name,
			CellType type,
			String treatment)
	{
		this.name = name;
		this.cellTypes = new ArrayList<CellType>(){
			private static final long serialVersionUID = 1L;
		{
			add(type);
		}};
		this.includedTreatments = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
		
		{
			add(treatment);
		}};
	}

	public Filter()
	{
	}

	public List<CellType> getTypes() {
		return cellTypes;
	}

	public void setTypes(List<CellType> types) {
		this.cellTypes = types;
	}

	public List<String> getTreatments() {
		return includedTreatments;
	}

	public void setTreatments(List<String> treatments) {
		this.includedTreatments = treatments;
	}

	public Double getTimeFrom() {
		return minTime;
	}

	public void setTimeFrom(Double timeFrom) {
		this.minTime = timeFrom;
	}

	public Double getTimeTo() {
		return maxTime;
	}

	public void setTimeTo(Double timeTo) {
		this.maxTime = timeTo;
	}

	public Integer getDivFrom() {
		return minDiv;
	}

	public void setDivFrom(Integer divFrom) {
		this.minDiv = divFrom;
	}

	public Integer getDivTo() {
		return maxDiv;
	}

	public void setDivTo(Integer divTo) {
		this.maxDiv = divTo;
	}

	public Double getCountFrom() {
		return minCell;
	}

	public void setCountFrom(Double countFrom) {
		this.minCell = countFrom;
	}

	public Double getCountTo() {
		return maxCell;
	}

	public void setCountTo(Double countTo) {
		this.maxCell = countTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		System.out.println("Name:\t" + name);
		
		if (cellTypes != null)
		{
			sb.append("TYPES: \t");
			for (CellType type : cellTypes)
			{
				sb.append(type + ", ");
			}
			sb.append("\n");
		}
		else
		{
			sb.append("TYPES: \t No Constraint \n");
		}
		sb.append("Treatments: \t 	"+includedTreatments 	+"\n");
		sb.append("Time from: \t" 	+ minTime 				+"\n");
		sb.append("Time to: \t" 	+ maxTime 				+"\n");
		sb.append("Div from: \t" 	+ minDiv 				+"\n");
		sb.append("Div to: \t " 	+ maxDiv 				+"\n");
		sb.append("Count from: \t" 	+ minCell 				+"\n");
		sb.append("Count to: \t" 	+ maxCell 				+"\n");
		
		
		return sb.toString();
	}

	public List<Measurement> filter(Collection<Measurement> measurementModelData)
	{
		return MeasurementQuery.query(measurementModelData, this);
	}

	public static Collection<Measurement> filter(Collection<Filter> filters,	Collection<Measurement> measurementModelData)
	{
		if (filters == null) return measurementModelData;
		if (filters.size() == 0) return measurementModelData;
		
		
		Set<Measurement> measurements = new HashSet<>();
		for (Filter f : filters)
		{
			measurements.addAll(f.filter(measurementModelData));
		}
		return measurements;
	}

	public void setNewValues(Filter selectionOptions)
	{
		
		if (name != null)
		{
			if (!selectionOptions.getName().equals(""))
			{
				this.name = selectionOptions.getName();
			}
		}
		
		this.cellTypes = selectionOptions.getTypes();
		this.includedTreatments = selectionOptions.getTreatments();
		this.minTime = selectionOptions.getTimeFrom();
		this.maxTime = selectionOptions.getTimeTo();
		this.minDiv = selectionOptions.getDivFrom();
		this.maxDiv = selectionOptions.getDivTo();
		this.minCell = selectionOptions.getCountFrom();
		this.maxCell = selectionOptions.getCountTo();
	}

	public void setType(CellType live)
	{
		if (cellTypes == null)
		{
			cellTypes = new ArrayList<>();
		}
		this.cellTypes.clear();
		this.cellTypes.add(live);
	}

	public void setTreatment(String treatement)
	{
		includedTreatments = new ArrayList<>();
		includedTreatments.add(treatement);
	}
	
}
