package edu.wehi.celcalc.cohort.gui.analyizer;

import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.wehi.GUI;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.SingleCDockable;

public class AnalyizerGroupComponent extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	final CControl control;
	
	AnalyizerGroupComponent(Collection<AnalyizerComponent> components, JFrame frame)
	{
		control = new CControl(frame);
        add(control.getContentArea() );
        CGrid grid = new CGrid( control );
		
		int noRows =  (int) Math.ceil(Math.sqrt((double)components.size()));
		if (noRows <= 1)
		{
			noRows=2;
		}
		
		int row = 0;
		int col = 0;
		for (AnalyizerComponent component : components)
		{
			SingleCDockable singleDockable = GUI.createDockable(component, component.guid+"");
			grid.add(col, row, 1, 1, singleDockable);
			row +=1;
			if (row == noRows)
			{
				row = 0;
				col+=1;
			}
		}
		control.getContentArea().deploy( grid );
	}
	
	
	

}
