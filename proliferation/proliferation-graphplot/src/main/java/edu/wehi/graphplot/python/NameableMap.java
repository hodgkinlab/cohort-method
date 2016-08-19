package edu.wehi.graphplot.python;

import java.util.Map;

import edu.wehi.graphplot.plot.NameableCollection;

public interface NameableMap extends NameableCollection, Map<String, Object>, Comparable<NameableMap>{}
