package edu.wehi.celcalc.cohort.gui.analyizer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class AnalizerBar {

	JMenuItem miUpdateAll = new JMenuItem("Update All (F5)");
	
	JMenu mnuExport = new JMenu("Export");
	JMenuItem miExportPDF = new JMenuItem("As. PDF");
	JMenuItem miExportSVG = new JMenuItem("As. SVG");
	JMenuItem miExportCSV = new JMenuItem("As. CSV");
	
	public AnalizerBar()
	{
		mnuExport.add(miExportPDF);
		mnuExport.add(miExportSVG);
		mnuExport.add(miExportCSV);
	}
	
	public JMenuItem getUpdateMenu()
	{
		return miUpdateAll;
	}
	
	public JMenu getExportMenu()
	{
		return mnuExport;
	}
	
}
