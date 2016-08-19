package edu.wehi.celcalc.cohort.util;

public enum PROPERTIES
{
	RECENTIMPORTFILENAME	("cell/recent/import.recent"),
	RECENTWORKSPACE			("cell/recent/workspace.recent"),
	RECENTWORKSPACEDATANAME	("workspace.workspace")
	
	
	;
	
	public final String fileName;
	PROPERTIES(String fileName)
	{
		this.fileName = fileName;
	}
}
