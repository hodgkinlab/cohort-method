package edu.wehi.celcalc.cohort.gui.analyizer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wehi.celcalc.cohort.gui.analysis.Analysis;

public class AnalizerView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	
	public AnalizerView()
	{
		setLayout(new BorderLayout());
	}
	
	public void updateAnalysis(Analysis analysis)
	{
		
	}
	
	
	public void updateAll()
	{
		
	}

	public void setSelectedPane(String result)
	{
		if (pane != null)
		{
			for (int i = 0; i < pane.getTabCount(); i++)
			{
				if (pane.getTitleAt(i).equals(result))
				{
					pane.setSelectedIndex(i);
					return;
				}
			}
		}
	}

	JTabbedPane pane = null;
	
	public void addComp(JTabbedPane comp)
	{
		removeAll();
		add(comp, BorderLayout.CENTER);
		revalidate();
		repaint();
		pane = comp;
	}

}
