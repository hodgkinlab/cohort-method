package edu.wehi.graphplot.plot;

import java.util.ArrayList;
import java.util.List;

import edu.wehi.graphplot.plot.series.ScriptMultiInMultiOut;
import edu.wehi.graphplot.plot.series.scriptables.Scriptable;

public class GPDataNode<DATA> {
	
	public final  DATA 	data;
	private Scriptable<?,?> 	parentScript;
	public final List<Scriptable<?,?>> childrenScript = new ArrayList<>();
	private String name;
	
	public GPDataNode(Scriptable<?,?> parentScript, DATA data, String name)
	{
		this.data = data;
		this.parentScript = parentScript;
		this.name = name;
	}
	
	public GPDataNode(String name,DATA data)
	{
		this(null, data, name);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setParentScript(Scriptable<?,?> parent)
	{
		this.parentScript = parent;
	}
	
	public GPDataNode(DATA data)
	{
		this(null, data, "");
	}
	
	public DATA getData()
	{
		return data;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Scriptable<?,?> getParentScript()
	{
		return parentScript;
	}
	
	public void addChildScript(ScriptMultiInMultiOut script)
	{
		childrenScript.add(script);
	}
	
	public void removeChildScript(ScriptMultiInMultiOut script)
	{
		childrenScript.remove(script);
	}
	
	@Override
	public String toString()
	{
		return data.toString();
	}

}