package edu.wehi.celcalc.cohort.gui.analyizer;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import edu.wehi.celcalc.cohort.gui.application.ApplicationData;

public abstract class AnalizerController
{
	final AnalizerView view;
	final AnalizerModel model;
	final AnalizerBar bar;
	
	public AnalizerController(AnalizerView view, AnalizerBar bar, ApplicationData data)
	{
		this.view = view;
		this.model = new AnalizerModel(data);
		this.bar = bar;
		bar.miUpdateAll.addActionListener(e -> resetView());
		bar.miExportPDF.addActionListener(e -> exportPDFAction());
		bar.miExportSVG.addActionListener(e -> exportSVGAction());
		bar.miExportCSV.addActionListener(e -> exportCSVAction());
	}

	private void exportPDFAction()
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose Export Directory for PDFs");
		fc.showOpenDialog(getJFrame());
		File file = fc.getSelectedFile();
		if (file == null)
		{
			return;
		}
		resetView();
		model.exportPDF(file);
	}
	
	private void exportSVGAction()
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose Export Directory for SVGs");
		fc.showOpenDialog(getJFrame());
		File file = fc.getSelectedFile();
		if (file == null)
		{
			return;
		}
		resetView();
		model.exportSVG(file);
	}
	
	private void exportCSVAction()
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose Export Directory for SVGs");
		fc.showOpenDialog(getJFrame());
		File file = fc.getSelectedFile();
		if (file == null)
		{
			return;
		}
		resetView();
		model.exportCSV(file);
	}

	public void resetView()
	{
		view.addComp(model.createAnalizerView(getJFrame()));
	}
	
	public abstract JFrame getJFrame();

	public void setSelectedPane(String result)
	{
		if (result == null) return;
		view.setSelectedPane(result);
	}
	
}
