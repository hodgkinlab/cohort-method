/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wehi.celcalc.cohort.old;

/**
 *
 * @author pavlyshyn.d
 */


import javax.swing.*;

import java.awt.*;


public class CohortGUITemp extends JFrame {
	
	public final JPanel 					mainPanel = new JPanel();
	public final JPanel 					controlPanel = new JPanel(new FlowLayout());
	public final JTabbedPane 				displayTabs = new JTabbedPane();

	public final JButton 					loadButton = new JButton("Load Data");;
	public final JButton 					plotButton = new JButton("Plot Cohort Numbers");;
	public final JButton 					plotParametersButton = new JButton("Plot Parameters");
	public final JButton 					closeButton = new JButton("Close tab");;
	public final JComboBox<ConcComboItem> 	concSelect = new JComboBox<ConcComboItem>();
	
	
	public CohortGUITemp() {
		super("Cohort Fitter");
		setSize(700,900);
		createPanelContents();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

	}

	public static void main(String[] args) {
		CohortGUI cohortGUI = new CohortGUI();
		cohortGUI.setVisible(true);
	}

	private void createPanelContents() {
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		createControlPanel();
		createDisplayTabs();
		add(mainPanel);
	}

	private void createControlPanel() {
		
		controlPanel.setMaximumSize(new Dimension(700, 0));
		controlPanel.add(loadButton);
		controlPanel.add(concSelect);
		controlPanel.add(plotButton);
		controlPanel.add(plotParametersButton);
		controlPanel.add(closeButton);

		mainPanel.add(controlPanel);
	}

	private void createDisplayTabs()
	{
		mainPanel.add(displayTabs);
	}
}
