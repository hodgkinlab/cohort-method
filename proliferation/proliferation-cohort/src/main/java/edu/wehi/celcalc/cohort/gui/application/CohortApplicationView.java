package edu.wehi.celcalc.cohort.gui.application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.ImageUtil;
import javax.swing.JButton;

public class CohortApplicationView extends JFrame {

	static final long serialVersionUID = 1L;
	final JButton btnLoadData = new JButton();

	public CohortApplicationView() {
		setIconImage(ImageUtil.getApplicationIcon());
		setLayout(new BorderLayout());
		
		
		setMinimumSize(new Dimension(636, 345));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		btnLoadData.setSize(400, 400);
		btnLoadData.setVisible(true);
		btnLoadData.setText("Load Data");
		add(btnLoadData, BorderLayout.NORTH);
	}
	
	public JButton getButtonLoadData() {
		return btnLoadData;
	}

	public static void main(String[] args) {
		GUI.guinof(new CohortApplicationView());
	}
}
