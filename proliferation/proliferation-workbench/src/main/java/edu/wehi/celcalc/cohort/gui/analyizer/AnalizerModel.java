package edu.wehi.celcalc.cohort.gui.analyizer;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import bibliothek.gui.dock.common.SingleCDockable;
import edu.wehi.celcalc.cohort.gui.analysis.Analysis;
import edu.wehi.celcalc.cohort.gui.application.ApplicationData;
import edu.wehi.graphplot.plot.GPPlotterFuncs;


public class AnalizerModel
{

	final ApplicationData data;
	
	Set<AnalyizerComponent> lastAnalizerComponents = new HashSet<>();
	List<SingleCDockable> lastDockables = new ArrayList<>();
	
	public AnalizerModel(ApplicationData data)
	{
		this.data = data;
	}

	public JTabbedPane createAnalizerView(JFrame frame)
	{
		lastDockables.forEach(d -> d.setVisible(false));
		lastDockables.clear();
		
		Map<Analysis, AnalyizerComponent> analizerMap = Analyizer.createAnalysisMap(
				data.getAnalysisContainer().getAllAnalysiss(),
				data.getMeasurementListWithNoDuplicates(),
				data.getFilterContainter(),
				data.getScriptsContainter(),
				data.getPlotoriesContainer());

		for (AnalyizerComponent c : lastAnalizerComponents)
		{
			c.killMe();
		}
		
		lastAnalizerComponents.clear();
		lastAnalizerComponents.addAll(analizerMap.values());
		
		JTabbedPane pane = new JTabbedPane();
		
		Map<String, List<AnalyizerComponent>> componentsGrouped = analizerMap.values().stream().collect(Collectors.groupingBy( m-> 
		{
			Analysis analysis = m.getAnalysis();
			String group = analysis.getGroup();
			if (group == null) return analysis.getName();
			if (group.equals("")) return analysis.getName();
			return group;
		}));
		
		for (String group : componentsGrouped.keySet())
		{
			List<AnalyizerComponent> comps = componentsGrouped.get(group);
			
			int sz = comps.size();
			
			if (sz == 0)
			{
				continue;
			}
			if (sz == 1)
			{
				pane.add(group, comps.get(0));
				continue;
			}
			
			JPanel pnl = new JPanel();
			
			int w = (int)Math.ceil(Math.sqrt(sz));
			pnl.setLayout(new GridLayout(w,w));
			
			for (AnalyizerComponent comp : comps)
			{
				pnl.add(comp);
			}
			
			pane.addTab(group, new JScrollPane(pnl));
			
		}
		
		return pane;
	}

	public void exportPDF(File file)
	{
		lastAnalizerComponents.forEach(a ->
		{
			ChartPanel chartPanels = a.getChartPanels();
			if (chartPanels == null)
			{
				return;
			}
			GPPlotterFuncs.exportPDF(chartPanels, file.getAbsolutePath()+"/"+a.getAnalysis().getName()+".pdf");
			
		});
	}

	public void exportSVG(File file)
	{
		lastAnalizerComponents.forEach(a ->
		{
			ChartPanel chartPanels = a.getChartPanels();
			if (chartPanels == null)
			{
				return;
			}
			GPPlotterFuncs.exportSVG(chartPanels, file.getAbsolutePath()+"/"+a.getAnalysis().getName()+".svg");
			
		});
	}

	public void exportCSV(File file)
	{
		lastAnalizerComponents.forEach(a ->
		{
			a.exportCVS(file);
		});
	}

}
