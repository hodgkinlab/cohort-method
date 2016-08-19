package edu.wehi.celcalc.cohort.old;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.menu.SingleCDockableListMenuPiece;
import bibliothek.gui.dock.facile.menu.RootMenuPiece;

public class CohortView extends JFrame{
	
	
	
	
	// Docking Components
	CControl control = new CControl(this);
	CGrid grid = new CGrid(control);
	JPanel pnlDocking = new JPanel();
	
	
	
	
	
	// Menu Bar
	JMenu itmFile = new JMenu("File");
	JMenuItem itmOpen = new JMenuItem("open");
	
	// Docking
	RootMenuPiece rootMenuPiece = new RootMenuPiece( "View", false );
	
	JMenuBar bar = null;
	JMenuBar getBar()
	{
		if (bar == null)
		{
			bar = new JMenuBar();
			bar.add(itmFile);
			itmFile.add(itmOpen);
			rootMenuPiece.add(new SingleCDockableListMenuPiece( control ));
			bar.add(rootMenuPiece.getMenu());
		}
		return bar;
	}
	

	
	
	private void init()
	{
		setSize(400, 400);
		setJMenuBar(getBar());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		control.getContentArea().deploy( grid );
		
	}
	
	
	public CohortView()
	{
		init();
	}
	
	public static void main(String[] args)
	{
		new CohortView().setVisible(true);
	}
}
