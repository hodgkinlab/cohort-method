package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.series.ScriptBase;

public abstract class MapAndSeriesScriptBase extends ScriptBase<List<Measurement>, List<GPDataNode<SeriesAndTable>>> implements MapAndSeriesScript {

	private static final long serialVersionUID = 1L;

}
