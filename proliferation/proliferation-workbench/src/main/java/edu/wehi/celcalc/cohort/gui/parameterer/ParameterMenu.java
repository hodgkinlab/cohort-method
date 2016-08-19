package edu.wehi.celcalc.cohort.gui.parameterer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ParameterMenu extends JMenu
{
	
	private static final long serialVersionUID = 1L;
	
	JMenuItem itmNewParameterSet = new JMenuItem("New Parameter Set"); 

	public ParameterMenu()
	{
		super("Parameters");
		add(itmNewParameterSet);
	}
	
}
