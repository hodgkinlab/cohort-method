package edu.wehi.celcalc.cohort.runner;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wehi.GUI;
import edu.wehi.swing.GBHelper;

public class RunnerView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	JLabel lblSeed = new JLabel("Seed");
	JLabel lblRuns = new JLabel("Runs");
	JLabel lblSamples = new JLabel("Samples");
	JLabel lblShowAll = new JLabel("Show All Results");
	
	JSpinner spSeed = new JSpinner();
	JSpinner spRuns = new JSpinner();
	JSpinner spSamples = new JSpinner();
	JCheckBox bxShowAll = new JCheckBox();
	
	JComboBox<RunnerOptions> boxRunner = new JComboBox<>(RunnerOptions.values());
	
	
	public RunnerView()
	{
		setLayout(new BorderLayout());

		JPanel pnlMain = new JPanel(new GridBagLayout());
		GBHelper gb = new GBHelper();
		
		JLabel lblSettings = new JLabel("Run Settings");
		lblSettings.setFont(new Font("Serif",Font.BOLD,16));
		
		pnlMain.add(lblSettings, gb.width(2));
		pnlMain.add(boxRunner, gb.nextCol().align(GridBagConstraints.EAST).width(2));
		
		pnlMain.add(lblSeed, gb.nextRow());
		pnlMain.add(spSeed, gb.nextCol().expandW().width(2));
		
		pnlMain.add(lblRuns, gb.nextRow());
		pnlMain.add(spRuns, gb.nextCol().expandW().width(2));
		
		pnlMain.add(lblSamples, gb.nextRow());
		pnlMain.add(spSamples, gb.nextCol().expandW());
		
		pnlMain.add(lblShowAll, gb.nextRow());
		pnlMain.add(bxShowAll, gb.nextCol().expandW());
		
		add(pnlMain, BorderLayout.CENTER);
		
		final ActionListener al = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				inputValuesChange();
			}
		};
		
		final ChangeListener cl = new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				inputValuesChange();
			}
		};
		
		bxShowAll.addActionListener(al);
		
		spSeed.addChangeListener(cl);
		spRuns.addChangeListener(cl);
		spSamples.addChangeListener(cl);
		boxRunner.addActionListener(e -> {
			inputValuesChange();
			syncApplicableEnabilibty();
			});
	}
	
	public void inputValuesChange()
	{
		if (isSyncing) return;
		syncNewValues();
		System.out.println(runner);
		System.out.println("\n\n-------\n\n");
	}
	
	public void syncNewValues()
	{
		if (isEnabledActions == false && isSyncing != false)
		{
			return;
		}
		if (runner != null)
		{
			runner.setSeed(			(Integer)			spSeed.getValue());
			runner.setRuns(			(Integer)			spRuns.getValue());
			runner.setSampleSize(	(Integer)			spSamples.getValue());
			runner.setShowAllResults(bxShowAll.isSelected());
			runner.setOptions(		(RunnerOptions) 	boxRunner.getSelectedItem());
		}
	}
	
	boolean isEnabledActions = false;
	public void setControlsEnabled(boolean isEnabled)
	{
		this.isEnabledActions = isEnabled;
		spSeed.setEnabled(isEnabled);
		spRuns.setEnabled(isEnabled);
		spSamples.setEnabled(isEnabled);
		bxShowAll.setEnabled(isEnabled);
		boxRunner.setEnabled(isEnabled);
		syncNewValues();
	}
	
	boolean isSyncing = false;
	Runner runner = null;
	public void sync(Runner runner)
	{
		this.runner = runner;
		isSyncing = true;
		if (runner == null)
		{
			setControlsEnabled(false);
			clearValues();
		}
		else
		{
			spRuns.setValue(runner.getRuns());
			spSamples.setValue(runner.getSampleSize());
			spSeed.setValue(runner.getSeed());
			bxShowAll.setSelected(runner.isShowAllResults());
			setBoxValue(runner.getOptions());
			setControlsEnabled(true);
			syncApplicableEnabilibty();
		}
		isSyncing = false;
	}
	
	
	private void clearValues()
	{
		spSeed.setValue(0);
		spRuns.setValue(0);
		spSamples.setValue(0);
		bxShowAll.setSelected(false);
		setBoxValue(RunnerOptions.ALL);
	}
	
	public void setBoxValue(RunnerOptions option)
	{
		boxRunner.setSelectedItem(option);
		syncApplicableEnabilibty();
	}
	
	public void syncApplicableEnabilibty()
	{
		RunnerOptions option = (RunnerOptions) boxRunner.getSelectedItem();
		if (option == RunnerOptions.SETTINGS)
		{
			setControlsEnabled(true);
		}
		else
		{
			setControlsEnabled(false);
			if (option == RunnerOptions.PERMES)
			{
				spSeed.setEnabled(true);
				spRuns.setEnabled(true);
			}
		}
		boxRunner.setEnabled(true);
	}

	public static void main(String[] args)
	{
		RunnerView view = new RunnerView();
		view.sync(new Runner(10, 2, 3, false, false, RunnerOptions.ALL));
		GUI.gui(view);
	}
	
	public void initGUI()
	{
		syncApplicableEnabilibty();
	}

}