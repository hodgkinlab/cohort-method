package edu.wehi.celcalc.cohort.gui.application;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wehi.swing.GBHelper;

public class SizeConfigurationPanel extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	JTextField txtWidth = new JTextField();
	JTextField txtHeight = new JTextField();
	
	public SizeConfigurationPanel()
	{
		GBHelper gb = new GBHelper();
		setLayout(new GridBagLayout());
		
		txtWidth.setPreferredSize(new Dimension(100,2));
		txtHeight.setPreferredSize(new Dimension(100,2));
		
		add(new JLabel("width"), 	gb);
		add(txtWidth, 				gb.nextCol());
		add(new JLabel("height"),	gb.nextRow());
		add(txtHeight, 				gb.nextCol());
	}
	

	public String getWidthInput()
	{
		return txtWidth.getText();
	}
	
	public String getHeightInput()
	{
		return txtHeight.getText();
	}
	
	public void setWidthInput(String w)
	{
		txtWidth.setText(w);
	}
	
	public void setHeightInput(String h)
	{
		txtHeight.setText(h);
	}
	
	public static void main(String[] args)
	{
		JOptionPane.showConfirmDialog(null, new SizeConfigurationPanel(), "Set Size of Multi Plots", JOptionPane.PLAIN_MESSAGE);
	}

}
