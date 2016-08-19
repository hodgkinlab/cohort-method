package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.awt.Component;

public class ParameterBoolean implements Parameterable {
	
	String name;
	boolean value;
	
	public ParameterBoolean(String name, boolean value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
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
