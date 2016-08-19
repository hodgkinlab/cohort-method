package edu.wehi.celcalc.cohort.gui.analysis;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AnalysisDao
{
	
	public static boolean saveAnalysis(Collection<Analysis> analysis, String absPath)
	{
		XStream xstream = new XStream(new DomDriver());
		boolean isSaved = true;
		if (analysis.size() == 0)
		{
			return false;
		}
		for (Analysis a : analysis)
		{
			String fileName = a.getName();
			if (!fileName.endsWith(".xml"))
			{
				fileName += ".xml";
			}
			String abs = absPath +"\\"+fileName;
			System.out.println("Analysis saved to: "+absPath);
			FileWriter sw = null;
	        try
	        {
	        	sw = new FileWriter(abs);
				xstream.toXML(a, sw);
			}
	        catch (Exception e)
	        {
	        	isSaved = false;
				e.printStackTrace();
			}
		}
		return isSaved;
	}
	
	public static Analysis loadAnalysis(String absolutePath)
	{
		Analysis analysis = null;
        
        try
        {
        	XStream xstream = new XStream(new DomDriver()); 
        	File file = new File(absolutePath);
        	if (!file.exists())
        	{
        		return null;
        	}
        	analysis = (Analysis) xstream.fromXML(file);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return analysis;
	}
	
}
