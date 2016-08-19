package edu.wehi.celcalc.cohort.gui.analyizer;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartPanel;

import edu.wehi.celcalc.cohort.gui.analysis.Analysis;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPCoordinateWithName;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.python.NameableMap;

public class AnalyizerComponent extends JPanel
{
	
	private static final long serialVersionUID = 1L;
	
	List<GPDataNode<GPXYSeries>> seriesNodes;
	
	String group;
	
	public final long guid = java.util.UUID.randomUUID().getLeastSignificantBits();
	
	ChartPanel chartPanels = null;
	Set<JTable> tables = new HashSet<>();
	
	Analysis analysis;
	
	public void killMe()
	{
		if (seriesNodes != null)
		{
			seriesNodes.clear();
			seriesNodes = null;	
		}
		group = null;
		if (chartPanels != null)
		{
			chartPanels.setVisible(false);
			chartPanels = null;
		}
		if (tables != null)
		{
			tables.clear();
			tables = null;
		}
		if (analysis != null)
		{
			analysis = null;
		}
		
		setVisible(false);
	}
	
	public AnalyizerComponent(Analysis analysis, List<GPDataNode<GPXYSeries>> seriesNodes, Collection<NameableMap> maps)
	{
		super(new BorderLayout());
		this.analysis = analysis;
		this.seriesNodes = seriesNodes;
		group = analysis.getGroup();
		
		if (seriesNodes.size() != 0)
		{
			GPChartType type = (analysis.isLog())? GPChartType.LOG : GPChartType.LINE;
			ChartPanel plot = GPPlotterFuncs.plotNodes(seriesNodes, analysis.getTitle(), analysis.getxAxis(), analysis.getyAxis(), type);
			add(plot, BorderLayout.CENTER);
			chartPanels = plot;
		}
		if (maps.size() != 0)
		{
			maps.remove(null);
			JTable table = new JTable(new KeyValueMapTable(new ArrayList<>(maps)));
			table.setName(analysis.getName());
			tables.add(table);
			table.setMinimumSize(table.getPreferredSize());
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setSize(table.getPreferredSize());
			table.setPreferredScrollableViewportSize(table.getPreferredSize());
			add(scrollPane , BorderLayout.PAGE_END);
		}
	}
	
	public Analysis getAnalysis()
	{
		return analysis;
	}
	
	public ChartPanel getChartPanels()
	{
		return chartPanels;
	}
	
	public String getGroup()
	{
		return group;
	}

	public void exportCVS(File file)
	{	
		exportTablesToCSV(file);
		exportSeriessToSCV(file);
	}

	private boolean exportSeriessToSCV(File dir)
	{
		File file = new File(dir.getAbsolutePath()+"/"+analysis.getName()+"-series"+".csv");
		FileWriter fw = null;
		
		boolean isEmpty = true;
		for (GPDataNode<GPXYSeries> node : seriesNodes)
		{
			GPXYSeries series = node.getData();
			if (series.coordinates.size() != 0)
			{
				isEmpty = false;
			}
		}
		
		if (isEmpty)
		{
			return false;
		}
		
		
		if (!file.exists())
		{
			try 
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		
		try
		{
			fw = new FileWriter(file.getAbsoluteFile());
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		for (GPDataNode<GPXYSeries> node : seriesNodes)
		{
			GPXYSeries series = node.getData();
			
			if (series.coordinates.size() == 0)
			{
				continue;
			}
			
			try
			{

				// name of series
				bw.write(series.getName()+"\n");
				// x cords
				for (GPCoordinateWithName coord : series.coordinates)
				{
					bw.write(coord.getX() + ",");
				}
				bw.write("\n");
				
				// y coord
				for (GPCoordinateWithName coord : series.coordinates)
				{
					bw.write(coord.getY() + ",");
				}
				bw.write("\n\n");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			bw.write("\n");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			bw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}

	private void exportTablesToCSV(File dir)
	{
		for (JTable table : tables)
		{
			if (table == null)
			{
				continue;
			}
			if (table.getName() == null)
			{
				continue;
			}
			
			if (table.getModel().getRowCount() == 0)
			{
				continue;
			}
			
			try
			{
				File file = new File(dir.getAbsolutePath()+"/"+table.getName()+"-table.csv");
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				
				try
				{
					if (!file.exists())
					{
						file.createNewFile();
					}
	
					bw.write(table.getName()+"\n");
					for (int c = 0; c < table.getColumnCount(); c++)
					{
						bw.write(table.getColumnName(c)+", ");
					}
					bw.write("\n");
					
					
					for (int r = 0; r < table.getModel().getRowCount(); r++)
					{
						for (int c = 0; c < table.getModel().getColumnCount(); c++)
						{
							bw.write(table.getModel().getValueAt(r, c)+", ");
						}
						bw.write("\n");
					}
					bw.write("\n\n");
					bw.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}

}
