package edu.wehi.celcalc.cohort.gui.application;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import edu.wehi.swing.ImageIconGetter;
import edu.wehi.swing.ImageIconGetter.IconSize;

public enum Perspectives
{
	DATABROWSING	(ImageIconGetter.dataBrowsing, "Raw Data"),
	SCRIPTING		(ImageIconGetter.terminal, "Scripting"),
	//CURVEFITTING	(ImageIconGetter.modelling, "Modelling"),
	ANALYSIS		(ImageIconGetter.drawings, "Analyses");
	
	
	
	;
	public static IconSize defaultSize = IconSize.MED;
	
	public final String icon;
	public final String txt;
	Perspectives(String icon, String txt)
	{
		this.icon = icon;
		this.txt = txt;
	}
	
	public ImageIcon getIcon()
	{
		return ImageIconGetter.getScriptIcon(icon, defaultSize);
	}
	
	public ImageIcon getIcon(IconSize size)
	{
		return ImageIconGetter.getScriptIcon(icon, size);
	}
	
	@Override
	public String toString()
	{
		return txt;
	}
	
	public static Map<Perspectives, Icon> getIconMap()
	{
		Map<Perspectives, Icon> icons = new HashMap<>();
		for (Perspectives p : Perspectives.values())
		{
			icons.put(p, p.getIcon());
		}
		return icons;
	}
	
}
