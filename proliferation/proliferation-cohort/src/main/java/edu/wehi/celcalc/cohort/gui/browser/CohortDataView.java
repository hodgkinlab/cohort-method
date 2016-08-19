package edu.wehi.celcalc.cohort.gui.browser;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public class CohortDataView extends DataBrowserView
{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void addAllcomponents()
	{
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.add("select data", getTopComponent());
		tabbedPane.addTab("raw data", getBottomComponent());
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	public static void main(String[] args)
	{
		DataBrowserModel model = new DataBrowserModel();
		DataBrowserView view = new CohortDataView();	
		DataBrowsingMenu mnu = new DataBrowsingMenu();
		
		DataBrowserController controller
				= new DataBrowserController(model, view, mnu);
		
		
		JFrame frame = new JFrame();
		
		JMenuBar bar = new JMenuBar();
		
		bar.add(mnu.getMeasurements());
		bar.add(mnu.getPlotView());
		bar.add(mnu.getRecentExcelImport());
		JMenu miFile = new JMenu("File");
		bar.add(miFile);
		miFile.add(mnu.getImportExcelJMenuItem());

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.add(controller.getView());
		frame.setJMenuBar(bar);
		controller.initGUI();
		frame.setVisible(true);
	}
	
}
