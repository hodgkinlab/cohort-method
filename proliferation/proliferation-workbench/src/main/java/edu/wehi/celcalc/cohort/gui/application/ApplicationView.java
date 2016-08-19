package edu.wehi.celcalc.cohort.gui.application;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.ImageUtil;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;
import edu.wehi.celcalc.cohort.gui.analyizer.AnalizerView;
import edu.wehi.celcalc.cohort.gui.analysis.AnalysisView;
import edu.wehi.celcalc.cohort.gui.browser.DataBrowserView;
import edu.wehi.celcalc.cohort.gui.parameterer.ParametererView;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterView;

public class ApplicationView extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	final ApplicationMenuBar bar = new ApplicationMenuBar();
	

	/** Container for the graph **/
	AnalizerView analizerPanel = new AnalizerView();
	
	ScripterView scripterView = new ScripterView();
	
	ParametererView parameterView = new ParametererView();
	
	public ApplicationView()
	{
		setSize(700,700);
		setJMenuBar(bar);
		setLayout(new BorderLayout());
		add(getMainPnl(), BorderLayout.CENTER);
		setIconImage(ImageUtil.getApplicationIcon());
	}
	
	JSplitPane pnMain = null;
	public JSplitPane getMainPnl()
	{
		if (pnMain == null)
		{
			pnMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			pnMain.setLeftComponent(getLeftComponent());
			pnMain.setRightComponent(analizerPanel);
			pnMain.setDividerLocation(0.5);
		}
		return pnMain;
	}

	CardLayout card = null;
	JPanel cardPnl = new JPanel();
	JComponent getLeftComponent()
	{
		if (card == null)
		{
			card = new CardLayout();
			cardPnl.setLayout(card);
			cardPnl.add(Perspectives.DATABROWSING.txt, 	getDataBrowsingPanel());
			cardPnl.add(Perspectives.SCRIPTING.txt, 	getScriptingPnl());
			//cardPnl.add(Perspectives.CURVEFITTING.txt, 	getParameterView());
			cardPnl.add(Perspectives.ANALYSIS.txt,		getAnalysisPnl());
		}
		return cardPnl;
	}
	
	
	AnalysisView analysisPnl = null;
	AnalysisView getAnalysisPnl()
	{
		if (analysisPnl == null)
		{
			analysisPnl = new AnalysisView();
		}
		return analysisPnl;
	}

	
	DataBrowserView dataBrowsingView = null;
	DataBrowserView getDataBrowsingPanel()
	{
		if (dataBrowsingView == null)
		{
			dataBrowsingView = new DataBrowserView();
		}
		return dataBrowsingView;
	}
	
	
	public static void main(String[] args) 
	{
		GUI.guinof(new ApplicationView());
	}

	public void reset()
	{
		dataBrowsingView.setMeasurementTableModel(new MeasurementTableModel(new ArrayList<>()));
		removePlot();
		dataBrowsingView.clearAllFilters();
		dataBrowsingView.clearStatistics();
	}


	private void removePlot()
	{
		analizerPanel.removeAll();
		revalidate();
		repaint();
	}


	public void setPerspective(Perspectives perspective)
	{
		if (isLimitedMode)
		{
			return;
		}
		card.show(cardPnl, perspective.txt);
		bar.perspectiveBox.setSelectedItem(perspective);
	}
	
	ParametererView getParameterView()
	{
		return parameterView;
	}
	
	private Component getScriptingPnl()
	{
		return scripterView;
	}
	
	@Override
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		scripterView.initGui();
	}
	
	public List<String> getSelectedSelections()
	{
		return dataBrowsingView.getSelectedFilters();
	}
	
	public ApplicationMenuBar getApplicationMenuBar()
	{
		return bar;
	}
	
	boolean isLimitedMode = false;
	
	public void limitedMode()
	{
		setPerspective(Perspectives.ANALYSIS);
		isLimitedMode = true;
		bar.limitedMode();
	}
	
	@Override
	public void processWindowEvent(WindowEvent ev)
	{
		if (ev.getID() == WindowEvent.WINDOW_CLOSING)
		{
			// NASTY HACK FIX - I do not know why the application always wants to close on a cancel of the confirm close dialog after simulating
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			super.processWindowEvent(ev);
			return;
		}
		super.processWindowEvent(ev);
		return;
	}

}
