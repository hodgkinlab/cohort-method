package edu.wehi.graphplot.plot;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class GPCatagorical {

	public final String name;
	public final String description;
	public final Color color;
	public final Map<String, Double> data;

	public GPCatagorical(Map<String, Double> data, String name,
			String description, Color color) {
		super();
		this.name = name;
		this.description = description;
		this.color = color;
		this.data = data;
	}

	public GPCatagorical(String name, String description, Color color) {
		super();
		this.name = name;
		this.description = description;
		this.color = color;
		this.data = new HashMap<>();
	}

}
