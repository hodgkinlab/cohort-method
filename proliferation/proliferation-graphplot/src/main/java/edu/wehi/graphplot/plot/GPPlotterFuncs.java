package edu.wehi.graphplot.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import edu.wehi.GUI;


public abstract class GPPlotterFuncs
{
	
	static XYSeries createXYSeries(GPXYSeries plot)
	{
		return createXYSeries(plot.coordinates, plot.name);
	}
	
	static XYSeries createXYSeries(List<? extends GPCoordinate> coordinates, String name)
	{
		XYSeries series = new XYSeries(name);
		for (GPCoordinate m : coordinates)
		{
			series.add(m.getX(), m.getY());
		}
		return series;
	}
	
	static XYSeriesCollection createXYSeriesCollection(List<GPXYSeries> plots)
	{
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (GPXYSeries plot : plots)
		{
			dataset.addSeries(createXYSeries(plot));
		}
		return dataset;
	}
	
	public static ChartPanel plot(
			GPXYSeries plot,
			String title,
			String xAxis,
			String yAxis,
			GPChartType type)
	{
		List<GPXYSeries> plots = new ArrayList<>();
		plots.add(plot);
		return plot(plots, title, xAxis, yAxis, type);
	}
	
	public static ChartPanel plot(
			Collection<GPXYSeries> plots,
			String title,
			String xAxis,
			String yAxis,
			GPChartType type)
	{
		YIntervalSeriesCollection collection = new YIntervalSeriesCollection();
		double max = -10000000;
		for (GPXYSeries s : plots)
		{
			if (!s.isVisibleInChart) continue;
			max = Math.max(max, s.maxYVal());
			if (s.coordinates.size() == 0) continue;
			YIntervalSeriesExtension series = GPPlotterFuncs.createIntervalXYDatasetSeries(s);
			collection.addSeries(series);
		}
		collection.setNotify(true);
		return GPPlotterFuncs.plot(plots,collection, title, xAxis, yAxis, type, max);
	}
	
	public static ChartPanel plot(
			Collection<GPXYSeries> gpseries,
			YIntervalSeriesCollection dataset,
			String title, String xAxis, String yAxis,
			GPChartType chartType, double max)
	{
        JFreeChart chart = createXYChart(title, xAxis, yAxis, dataset, chartType);
        ChartUtilities.applyCurrentTheme(chart);
        XYPlot plot = createPlot(chart, chartType, max);
        XYItemRenderer renderer = createRenderer(chartType);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        chart.setBackgroundPaint(Color.WHITE);
        
        hideSeriesHiddenFromKey(dataset, chart);
        adjustSeriesColor(dataset, renderer, chart);
        
        plot.setOutlineVisible(false);
        
        addControllerToPanel(gpseries, panel, dataset, chart);
        
        panel.addChartMouseListener(new ChartMouseListener() {
			
			@Override
			public void chartMouseMoved(ChartMouseEvent event)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent event)
			{
				int x = event.getTrigger().getX();
				int y = event.getTrigger().getY();
				XYItemEntity entity = null;
				try {entity = (XYItemEntity) panel.getEntityForPoint(x,y);} catch(Exception e){}
				if(entity != null)
				{
					int item = entity.getItem();
					int series = entity.getSeriesIndex();
					XYDataset data = entity.getDataset();
					System.out.println("("+data.getXValue(series, item)+", "+data.getYValue(series, item)+")");
				}
			}
		});
        
        panel.setPreferredSize(new Dimension(400,400));
		return panel;
	}
	
	private static void hideSeriesHiddenFromKey(YIntervalSeriesCollection dataset, JFreeChart chart)
	{
		int n = dataset.getSeriesCount();
		for (int i = 0; i < n; i++)
		{
			YIntervalSeries intervalSeries = dataset.getSeries(i);
			if (intervalSeries instanceof YIntervalSeriesExtension)
			{
				YIntervalSeriesExtension series = (YIntervalSeriesExtension) dataset.getSeries(i);
				chart.getXYPlot().getRenderer().setSeriesVisibleInLegend(i, series.isVisibleInLegend);
			}
		}
	}
	
	public static void adjustSeriesColor(YIntervalSeriesCollection dataset, XYItemRenderer renderer, JFreeChart chart)
	{
		int n = dataset.getSeriesCount();
		for (int i = 0; i < n; i++)
		{
			YIntervalSeries intervalSeries = dataset.getSeries(i);
			if (intervalSeries instanceof YIntervalSeriesExtension)
			{
				YIntervalSeriesExtension series = (YIntervalSeriesExtension) dataset.getSeries(i);
				if (series.getColor() != null )
				{
					renderer.setSeriesPaint(i, series.getColor());
				}
				if (series.isLineDashed())
				{
					renderer.setSeriesStroke(
							i,
							new BasicStroke(
							        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
							        1.0f, new float[] {6.0f, 6.0f}, 0.0f
							    ));
				}
				if (!series.isLineShowing())
				{
					renderer.setSeriesStroke(
							i,
							new BasicStroke(
							        0.0f, BasicStroke.CAP_BUTT, BasicStroke.CAP_BUTT,
							        1.0f, new float[] {0.0f, 6.0f}, 0.0f
							    ));
				}
			}
		}
	}
	

	private static void addControllerToPanel(Collection<GPXYSeries> gpplots, ChartPanel chartPanel, YIntervalSeriesCollection dataset, JFreeChart chart)
	{
		chartPanel.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent event)
			{
				if (event.getClickCount() == 2)
				{
					doPopup(event);
				}
			}
			
			private void doPopup(MouseEvent e)
			{
				final JPopupMenu popupMenu = new JPopupMenu();
				
				JMenuItem btnExportSVG = new JMenuItem("Export to SVG");
				popupMenu.add(btnExportSVG);
				btnExportSVG.addActionListener(a -> exportSVGAction());
				
				JMenuItem btnExportPDF = new JMenuItem("Export to PDF");
				popupMenu.add(btnExportPDF);
				btnExportPDF.addActionListener(a -> exportPDFAction());
				
				JMenuItem btnExportToCSV = new JMenuItem("Export to CSV");
				popupMenu.add(btnExportToCSV);
				btnExportToCSV.addActionListener(a -> exportCSVAction());

				try {popupMenu.show(e.getComponent(), e.getX(), e.getY());} catch (Exception ex){}
			}

			private void exportSVGAction()
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Export File");
				fileChooser.showSaveDialog(null);
				File file = fileChooser.getSelectedFile();
				if (file == null)
				{
					return;
				}
				if (file.exists() == true)
				{
					return;
				}
				else
				{
					String absPath = file.getAbsolutePath();
					if (!absPath.endsWith(".svg"))
					{
						absPath = absPath + ".svg"; 
					}
					exportSVG(chartPanel, absPath);
				}
			}
			
			private void exportPDFAction()
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Export File");
				fileChooser.showSaveDialog(null);
				File file = fileChooser.getSelectedFile();
				if (file == null)
				{
					return;
				}
				if (file.exists() == true)
				{
					return;
				}
				else
				{
					String absPath = file.getAbsolutePath();
					if (!absPath.endsWith(".pdf"))
					{
						absPath = absPath + ".pdf"; 
					}
					exportPDF(chartPanel, absPath);
				}
			}
			
			private void exportCSVAction()
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Export File");
				fileChooser.showSaveDialog(null);
				File file = fileChooser.getSelectedFile();
				if (file == null)
				{
					return;
				}
				if (file.exists() == true)
				{
					return;
				}
				else
				{
					String absPath = file.getAbsolutePath();
					if (!absPath.endsWith(".csv"))
					{
						absPath = absPath + ".csv"; 
					}
					exportSeriessToSCV(gpplots, absPath);
				}
			}
			
		});
		
	}
	
//	public void saveChartToPDF(
//			JFreeChart chart, String fileName, int width, int height)
//			throws Exception {
//		if (chart != null) {
//			BufferedOutputStream out = null;
//
//			try {
//				out = new BufferedOutputStream(new FileOutputStream(fileName));
//
//				//convert chart to PDF with iText:
//				Rectangle pagesize = new Rectangle(width, height);
//				com.lowagie.text.Document document
//						= new com.lowagie.text.Document(pagesize, 50, 50, 50, 50);
//
//				try {
//					PdfWriter writer = PdfWriter.getInstance(document, out);
//					document.addAuthor("JFreeChart");
//					document.open();
//					PdfContentByte cb = writer.getDirectContent();
//					PdfTemplate tp = cb.createTemplate(width, height);
//					Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
//
//					Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, eight);
//					chart.draw(g2, r2D, null);
//					g2.dispose();
//					cb.addTemplate(tp, 0, 0);
//				} finally {
//					document.close();
//				}
//			} finally {
//				if (out != null) {
//					out.close();
//				}
//			}
//		}//else: input values not availabel
//	}//saveChartToPDF()
	
	public static void exportPDF(ChartPanel chart, String file)
	{
		FileInputStream input = null;
		FileOutputStream output = null;
		try
		{
			File svgFile  = File.createTempFile("svg-graphs", ".svg");
			exportSVG(chart, svgFile.getAbsolutePath());

			input = new FileInputStream(svgFile); 
			output = new FileOutputStream(file);
			
	        Transcoder transcoder = new PDFTranscoder();
	        TranscoderInput transcoderInput = new TranscoderInput(input);
	        TranscoderOutput transcoderOutput = new TranscoderOutput(output);
	        transcoder.transcode(transcoderInput, transcoderOutput);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				input.close();
				output.close();
			}
			catch(Exception ee)
			{
				ee.printStackTrace();
			}
		}
	}
	
	public static Node exportSVG(ChartPanel chart, String file) 
	{
		DOMImplementation mySVGDOM= GenericDOMImplementation.getDOMImplementation();
		Document document = mySVGDOM.createDocument(null, "svg", null);
		SVGGraphics2D my_svg_generator = new SVGGraphics2D(document);
		
		chart.getChart().draw(my_svg_generator, new Rectangle2D.Double(0, 0, 400, 400), null);;
		try
		{
			my_svg_generator.stream(file);
			System.out.println("Saving svg to: "+file);
			return  document;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static JFreeChart createXYChart(
			String title, 
			String xAxis, 
			String yAxis, 
			YIntervalSeriesCollection dataset, 
			GPChartType chartType)
	{
		JFreeChart chart = null;
		switch(chartType)
		{
		case LINE:
			chart =  ChartFactory.createXYLineChart(
	        		title,     
	        		xAxis,                      
	        		yAxis,                     
	                dataset,                  
	                PlotOrientation.VERTICAL,
	                true,                     
	                true,                     
	                false                     
	            );
			break;
		case LOG:
			chart =  ChartFactory.createXYLineChart(
	        		title,      
	        		xAxis,                      
	        		yAxis,                      
	                dataset,                  
	                PlotOrientation.VERTICAL,
	                true,                     
	                true,                     
	                false                     
	            );
			break;
		case SCATTER:
			chart =  ChartFactory.createScatterPlot(
	        		title,     
	        		xAxis,                      
	        		yAxis,                      
	                dataset,                  
	                PlotOrientation.VERTICAL,
	                true,                     
	                true,                     
	                false                     
	            );
			break;
		default:
			throw new RuntimeException("Invalid Chart Type");
		}
		return chart;
	}
	
	public static XYPlot createPlot(JFreeChart chart, GPChartType chartType, double max)
	{
		
		switch (chartType)
		{
		case LINE:
		{
	        XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setDomainPannable(true);
	        plot.setRangePannable(true);
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        rangeAxis.setAutoRangeIncludesZero(false);
	        return plot;
		}
		case LOG:
		{
	        XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setDomainPannable(true);
	        plot.setRangePannable(true);
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        rangeAxis.setAutoRangeIncludesZero(true);
	        rangeAxis.setRange(0.0,max);
	        // log scale
	        plot.setRangeAxis(new LogAxis("No. Cells"));
	        return plot;
		}
		case SCATTER:
		{
	        XYPlot plot = (XYPlot) chart.getPlot();
	        plot.setDomainPannable(true);
	        plot.setRangePannable(true);
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	        rangeAxis.setAutoRangeIncludesZero(false);
			return plot;
		}
		default:
			throw new RuntimeException("Invalid Chart Type");
		}
	}
	
	public static XYItemRenderer createRenderer(GPChartType chartType)
	{
        XYErrorRenderer renderer = new XYErrorRenderer();
        renderer.setBaseLinesVisible(true);
        renderer.setUseFillPaint(true);
        renderer.setBaseFillPaint(Color.white);
        return renderer;
	}
	
	public static void plotSeriesList(GPXYSeries data)
	{
		List<GPXYSeries> serieslist = new ArrayList<GPXYSeries>();
		serieslist.add(data);
		plotSeriesList(serieslist);
	}

	public static void plotSeriesList(List<GPXYSeries> series)
	{
		GPController controller = new GPController("","","", series, GPChartType.LINE);
		JComponent comp = controller.getView();
		GUI.guinox(comp);
	}

	public static YIntervalSeriesExtension createIntervalXYDatasetSeries(GPXYSeries s)
	{
		YIntervalSeriesExtension intervalSeries 
				= new YIntervalSeriesExtension(s.name, false, true);
		if (s.getColor() != null) intervalSeries.setColor(s.getColor());
		intervalSeries.setLineDashed(s.isLineDashed());
		
		s.coordinates.forEach(c ->
		{
			intervalSeries.add(c.getX(), c.getY(), c.getYLower(), c.getYUpper());
		});
		intervalSeries.setVisibleInLegend(s.isVisibleInLegend());
		intervalSeries.setVisibleInChart(s.isVisibleInChart);
		intervalSeries.setLineShowing(s.isLineShowing());
		return intervalSeries;
	}

	public static ChartPanel plotNodes(List<GPDataNode<GPXYSeries>> nodes, String title, String xAxis, String yAxis, GPChartType type)
	{
		return plot(nodes.stream().map(s -> s.data).collect(Collectors.toList()), title, xAxis, yAxis, type);
	}
	
	public static ChartPanel plotNodes(GPDataNode<GPXYSeries> node, String title, String xAxis, String yAxis, GPChartType type)
	{
		return plot(node.data, title, xAxis, yAxis, type);
	}
	
	
	public static boolean exportSeriessToSCV(Collection<GPXYSeries> seriess, String path)
	{
		FileWriter fw = null;
		
		try
		{
			fw = new FileWriter(path);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		for (GPXYSeries series : seriess)
		{
			
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
	

}
