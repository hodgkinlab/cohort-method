package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.awt.Component;

public class ParameterString implements Parameterable {
	
	String name;
	String value;
	public ParameterString(String name, String value) {
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public Component getComponent() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		
	}
	
	

}
