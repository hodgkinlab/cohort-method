package edu.wehi.celcalc.cohort.scriptables;

import java.util.List;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.graphplot.plot.series.scriptables.Scriptable;

public interface MultiMeasurementsInSingleOut  extends Scriptable<List<Measurement>, GPDataNode<GPXYSeries>>{

}
