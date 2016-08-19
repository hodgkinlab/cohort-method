package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.awt.Component;

public class ParameterInteger implements Parameterable {

	String name;
	int min;
	int value;
	int max;
	int n;
	
	
	
	
	public ParameterInteger(String name, int min, int value, int max, int n) {
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
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}

	public void setValue(int value) {
		this.value = value;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}


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
