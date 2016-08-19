package edu.wehi.graphplot.plot.series.scriptables;

import java.io.Serializable;

public interface Scriptable<INPUT,OUTPUT> extends Serializable, Comparable<Scriptable<?,?>> {
	
	public OUTPUT script(INPUT input);
	public String getName();
	public void showDocComp();
	
	default int compareTo(Scriptable<?,?> script)
	{
		return this.getName().compareTo(script.getName());
	}
	
}
