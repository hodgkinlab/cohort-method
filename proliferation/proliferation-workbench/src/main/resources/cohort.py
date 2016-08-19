""" Script to setup scripting environment """

#java imports
from java.lang import System as javasystem

# graphplot imports
from edu.wehi.graphplot.plot import *
from edu.wehi.graphplot.plot import GPChartType
from edu.wehi.graphplot.python.PyPlottingFunctions import plotPyList as plotPyList

# celcalc imports
from edu.wehi.celcalc.cohort.plotting.CohortPlottingFunctions import plotShow as plotShow;
from edu.wehi.celcalc.cohort.data import *
from edu.wehi.celcalc.cohort.plotting import PlotOptions
from edu.wehi.celcalc.cohort.plotting import PlotScale
from edu.wehi.celcalc.cohort.plotting.CohortPlottingFunctions import *