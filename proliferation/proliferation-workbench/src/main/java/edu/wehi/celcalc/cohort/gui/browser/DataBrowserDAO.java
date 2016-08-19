package edu.wehi.celcalc.cohort.gui.browser;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;

public class DataBrowserDAO
{
	
	public static Collection<Measurement> loadMeasurements(String absolutePath)
	{
		if (!new File(absolutePath).exists())
		{
			return null;
		}
		
		Collection<Measurement> measurementListWithNoDuplicates = new ArrayList<>();
		BufferedReader br = null;
		
		try
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(absolutePath));
			
			while ((sCurrentLine = br.readLine()) != null)
			{
				String[] lineSplitted = sCurrentLine.split(",");
				
				double time = Double.parseDouble(	lineSplitted[0]);
				double cells = Double.parseDouble(	lineSplitted[1]);
				int div = Integer.parseInt(			lineSplitted[2]);
				CellType type = CellType.cellType(	lineSplitted[3]);
				String treatment = lineSplitted[4];
				boolean isIncluded = lineSplitted[5].equals("1"); 
				
				measurementListWithNoDuplicates.add(new Measurement(time, cells, div, type, treatment, isIncluded));
			}
			br.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				br.close();
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		return measurementListWithNoDuplicates;
	}
	
	public static boolean saveMeasurements(String pathToMeasurements, Collection<Measurement> container)
	{
		return saveMeasurements(new File(pathToMeasurements), container);
	}
	
	public static boolean saveFilters(String absPath, Collection<Filter> filters)
	{
		XStream xstream = new XStream(new DomDriver()); 
        boolean isSaved = true;
        
        if (filters.size() == 0)
        {
        	return false;
        }
        
		for (Filter filter : filters)
		{
			String fileName = filter.getName();
			if (!fileName.endsWith(".xml"))
			{
				fileName += ".xml";
			}
			String abs = absPath +"\\"+fileName;
			FileWriter sw = null;
	        try
	        {
	        	sw = new FileWriter(abs);
				xstream.toXML(filter, sw);
				System.out.println("Filter saved to: "+absPath);
			}
	        catch (Exception e)
	        {
	        	isSaved = false;
				e.printStackTrace();
			}
	        finally
	        {
	        	try
	        	{
					sw.close();
				} catch (Exception e)
	        	{
					isSaved = false;
					e.printStackTrace();
				}
	        }
		}
		return isSaved;
	}

	public static boolean saveMeasurements(File file, Collection<Measurement> container)
	{
		container.forEach(m -> {
			if (m.getTreatment().contains(","))
			{
				throw new RuntimeException("Treatments cannot contain commas.");
			}
		});
		
		Writer writer = null;
		try
		{
			file.setWritable(true);
			
			writer = new PrintWriter(file, "UTF-8");
		    
		    for (Measurement m : container)
		    {
		    	String data = "";
		    	data += m.getTime();
		    	data += "," + m.getCells();
		    	data += "," + m.getDiv();
		    	data += "," + m.getType().getName();
		    	data += "," + m.getTreatment();
		    	data += "," + ((m.isIncluded())?1:0);
		    	data += "\n";
		    	writer.write(data);
		    }
			try
			{
				writer.close();
				System.out.println("Saved measurements to: "+file.getAbsolutePath());
				return true;
			}
		   catch (Exception exx)
		   {
			   System.err.println("Failed to save measurements");
			   exx.printStackTrace();
			   return false;
		   }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		   try
		   {
			   writer.close();
			   return false;
		   }
		   catch (Exception exx)
		   {
			   return false;
		   }
		}
	}

	public static Filter loadFilter(String f1_abs)
	{
		Filter filter = null;
        
        try
        {
        	XStream xstream = new XStream(new DomDriver()); 
        	File file = new File(f1_abs);
        	if (!file.exists())
        	{
        		return null;
        	}
        	filter = (Filter) xstream.fromXML(file);//m.unmarshal(file);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return filter;
	}
	
	
}