package edu.wehi.celcalc.cohort.gui.analysis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.wehi.celcalc.cohort.runner.Runner;

public class Analysis implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	String name;
	Set<String> filters = new HashSet<String>();
	Set<String> scripts = new HashSet<String>();
	Runner runner = new Runner();
	
	String xAxis;
	String yAxis;
	String title;
	boolean log;
	String group = "";
	
	public  Analysis(String name, Set<String> filters, Set<String> scripts, String xAxis, String yAxis,	String title, boolean log)
	{
		this.name = name;
		this.filters = filters;
		this.scripts = scripts;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.title = title;
		this.log = log;
	}
	
	public Analysis(String name, String filter, String script, String xAxis, String yAxis,	String title, boolean log, String group)
	{
		this(name,
		new HashSet<String>(){
			private static final long serialVersionUID = 1L;
			{
				add(filter);
			}
		},
		new HashSet<String>(){
			private static final long serialVersionUID = 1L;
			{
				add(script);
			}
		},
		xAxis,
		yAxis,
		title,
		log
		);
		this.group = group;
	}
	
	public Analysis()
	{
		this(null,(Set<String>)null,null,null,null,null,false);
	}
	
	public Analysis(String name)
	{
		this(name,(Set<String>)null,null,null,null,null,false);
	}

	public String getxAxis() {
		return xAxis;
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return yAxis;
	}

	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	
	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public String getName()
	{
		return name;
	}
	
	public Set<String> getFilters()
	{
		return filters;
	}

	public void setFilters(Set<String> filters)
	{
		this.filters = filters;
	}

	public Set<String> getScripts()
	{
		return scripts;
	}

	public void setScripts(Set<String> scripts)
	{
		this.scripts = scripts;
	}

	public String getGroup()
	{
		if (group == null) return name;
		if (group.equals("")) return name;
		return group;
	}
	
	public void setGroup(String group)
	{
		this.group = group;
	}	
	
	boolean isShowing = true;
	public boolean isShowing()
	{
		return isShowing;
	}
	
	public void setShowing(boolean isShowing)
	{
		this.isShowing = isShowing;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return name + "\t" + group +"\t"+isShowing;
	}
		
}
