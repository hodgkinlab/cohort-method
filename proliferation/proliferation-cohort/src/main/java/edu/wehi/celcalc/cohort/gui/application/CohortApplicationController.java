package edu.wehi.celcalc.cohort.gui.application;

import java.io.*;

import java.awt.event.*;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;

import edu.wehi.celcalc.cohort.ImageUtil;
import edu.wehi.celcalc.cohort.cohort.CohortAnalsysisModel;
import edu.wehi.celcalc.cohort.gui.browser.DataBrowserController;

public class CohortApplicationController {

	final CohortApplicationView view;
	final CohortAnalsysisModel model;
	final DataBrowserController dataBrowsingController;
	
	final JPanel pnlTrio = new JPanel();
	final JPanel pnlCellNumVsDiv = new JPanel();
	final JPanel pnlCohortVsDiv = new JPanel();

	final JFrame frameCellNumVsDiv = new JFrame("Population Vs. Division") {

		private static final long serialVersionUID = 1L;

		{
			add(new JScrollPane(pnlCellNumVsDiv));
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setIconImage(ImageUtil.getApplicationIcon());
		}
	};
	final JFrame framepnlCohortVsDiv = new JFrame("Cohort Vs. Division") {

		private static final long serialVersionUID = 1L;

		{
			add(new JScrollPane(pnlCohortVsDiv));
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setIconImage(ImageUtil.getApplicationIcon());
		}
	};

	public CohortApplicationController() {
		view = new CohortApplicationView();
		view.add(new JScrollPane(pnlTrio));
		
		model = new CohortAnalsysisModel();
		
		dataBrowsingController = new DataBrowserController() {
			@Override
			public boolean importFile(File file) {
				boolean isSucessfull = super.importFile(file);
				if (isSucessfull) {
					cohortAnalysis();
				}
				return isSucessfull;
			}
		};
		dataBrowsingController.loadWorkspace();

		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dataBrowsingController.saveWorkspace();
			}
		});

		view.getButtonLoadData().addActionListener(
				e -> dataBrowsingController.importExcelDialog(true));
		
		dataBrowsingController.initGUI();
	}

	void startApplication() {
		view.setVisible(true);
		dataBrowsingController.importExcelDialog(false);
	}

	private void cohortAnalysis() {
		model.showCohortAnalysis(
				dataBrowsingController.getDataset(),
				dataBrowsingController.getSelectedMeasurements(),
				pnlTrio, pnlCellNumVsDiv, pnlCohortVsDiv);
		javax.swing.SwingUtilities.invokeLater(
				() -> {
//					if (!framepnlCohortVsDiv.isVisible()) {
//						GUI.guinofnoex(framepnlCohortVsDiv);
//					}
//					if (!frameCellNumVsDiv.isVisible()) {
//						GUI.guinofnoex(frameCellNumVsDiv);
//					}
				});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.out.println(e.getMessage());
			System.out.println("fatal error, cannot continue");
		}
		UIManager.put(
				"ComboBox.selectionForeground",
				new ColorUIResource(Color.black));

		CohortApplicationController controller = new CohortApplicationController();
		javax.swing.SwingUtilities.invokeLater(() -> controller.startApplication());
	}
}
