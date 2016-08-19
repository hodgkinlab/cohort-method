package edu.wehi.graphplot.utilities;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class GraphPlotUtil
{

	public static boolean export(File name, JFreeChart chart, int x, int y, PLOTEXPORTFORMAT format)
	{
		switch(format)
		{
		case SVG:
			return exportSVG(name, chart, x, y);
		default:
			return false;
		}
	}
	
	public static boolean exportSVG(File name, JFreeChart chart, int x, int y)
	{
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
		Document document = domImpl.createDocument(null, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		chart.draw(svgGenerator, new Rectangle(x, y));
		boolean useCSS = true;
		
		Writer out = null;
		try
		{
			out = new OutputStreamWriter(new FileOutputStream(name), "UTF-8");
			svgGenerator.stream(out, useCSS);
			return true;
		}
		catch (SVGGraphics2DIOException | UnsupportedEncodingException | FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
	}
}
