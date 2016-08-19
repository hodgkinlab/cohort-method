package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.awt.Component;

public class ParameterDouble implements Parameterable {
	
	String name;
	double min;
	
	
	public ParameterDouble(String name, double min, double value, double max,
			int n) {
		super();
		this.name = name;
		this.min = min;
		this.value = value;
		this.max = max;
		this.n = n;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}

	public void setValue(double value) {
		this.value = value;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	double value;
	double max;
	int n;


	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		
	}

}
