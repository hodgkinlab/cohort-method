package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.awt.Component;

public interface Parameterable {
	
	String getName();
	Component getComponent();
	Object getValue();
	void setValue(Object value);

}
