package edu.wehi.celcalc.cohort.gui.parameterer;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.gui.parameterer.type.Parameter;
import edu.wehi.swing.GBHelper;
import edu.wehi.swing.JComboBoxNoBorder;

public class ParametererView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	final GBHelper gb = new GBHelper();
	
	JTextField txtInput = new JTextField("EnterInUniqueName");
	JComboBox<Parameter> comboParameterType = new JComboBoxNoBorder<Parameter>(Parameter.values());
	JButton btnPlus = new JButton("+");
	JPanel pnlNewInput = new JPanel()
	{
		private static final long serialVersionUID = 1L;
		{
			GBHelper gb = new GBHelper();
			setLayout(new GridBagLayout());
			add(txtInput, gb.expandW());
			add(comboParameterType, gb.nextCol());
			add(btnPlus, gb.nextCol());
		}
	};
	
	JPanel pnlParameters = new JPanel(new GridBagLayout());
	
	JPanel pnlNewParamsPanel = new JPanel()
	{
		private static final long serialVersionUID = 1L;
		{
			setLayout(new BorderLayout());
			add(pnlNewInput, BorderLayout.NORTH);
			add(pnlParameters, BorderLayout.CENTER);
		}
	};
	

	
	JPanel pnlEditParametersSet = new JPanel()
	{
		private static final long serialVersionUID = 1L;
		{
			setLayout(new BorderLayout());
			add(pnlNewParamsPanel, BorderLayout.NORTH);
			//add(pnlEditParameters, BorderLayout.NORTH);
		}
	};
	
	
	
	JTable tableParamaterSets = new JTable();
	
	
	public ParametererView()
	{
		GBHelper gh = new GBHelper();
		
		setLayout(new GridBagLayout());

		add(pnlNewParamsPanel, gh.nextRow().expandW());
	}
	

	
	public static void main(String[] args)
	{
		GUI.gui(new ParametererView().pnlNewInput);
	}

}
