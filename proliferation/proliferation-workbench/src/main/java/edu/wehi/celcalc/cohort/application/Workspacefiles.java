package edu.wehi.celcalc.cohort.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum Workspacefiles
{
	DATA("data"),
	FILTER("filter"),
	SCRIPT("script"),
	ANALYSIS("analysis"),
	
	MEASUREMENTS(DATA.getFileName()+"/measurements.csv", true);
	
	final String fileName;
	final boolean isFile;
	
	Workspacefiles(String fileName)
	{
		this.fileName = fileName;
		this.isFile = false;
	}
	
	Workspacefiles(String fileName, boolean isFile)
	{
		this.fileName = fileName;
		this.isFile = isFile;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public static Collection<Workspacefiles> getAllWorkspaceDirs()
	{
		List<Workspacefiles> dirs = new ArrayList<>();
		for (Workspacefiles w : Workspacefiles.values())
		{
			if (!w.isFile)
			{
				dirs.add(w);
			}
		}
		return dirs;
	}
}
