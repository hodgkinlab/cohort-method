"""

    Welcome to Scripter. Scripter provides a scripting interface by embedding Jython (http://www.jython.org/) (the java implementation of the Python programming language)
    into the application.
    
    This template provides examples of how to use some the predefined functions and scripts, lets get started.
     
"""


"""
# IMPORTANT VARIABLES!
"""

print 'measurements - holds the raw data from java'
print measurements
print '\n\n\n'

print "If your not sure what methods there are on an object in Python you can always do this."
print dir(measurements[0])
print '\n\n\n'

print 'all_times - holds a list of all time points which measurements were taken at'
print all_times,
print '\n\n\n'

print 'all_divisions - holds al list of all the divisions'
print all_divisions
print '\n\n\n'

"""
# IMPORTANT FUNCTIONS

where:
    k = division
    t = time
    
    x(k,t) - number of cells
    
    c(k,t) = x(k,t)/2^k - cohort number
    
    S(t) = SUM(c(k,t)) k = 0...inf - cohort sum
    
    g(t) = SUM(kc(k,t)/S(t)) - mean division
    
    d = lim t->inf g(t) - division destiny (not yet implemented)
    
"""

print "Lets use all the functions: "
print "\n\n\n"

for t in all_times:
    print "at time t=",t
    for k in all_divisions:
        print 'x(',k,',',t,')=',x(k,t),
        print 'c(',k,',',t,')=',c(k,t),
    print "\n\n"
    print "S(",t,")=",S(t)
    print "g(",t,")=",g(t)
    print "\n\n" 


"""
# Plotting Measurements

There are plotting functions which work on the raw data, you can see examples of this bellow, but these are offered
via the gui so there is no need to use them usually, but they are there.

"""

print 'Here are the options for graphs:'
print PlotOptions.values()
print GPChartType.values()
print '\n\n\n'

#Here are some examples uncomment anyone to plot
#plotShow(measurements, GPChartType.LINE,        PlotOptions.MEANTREATMENT)
#plotShow(measurements, GPChartType.SCATTER,     PlotOptions.RAWDATA)
#plotShow(measurements, GPChartType.LINE,        PlotOptions.MEANTREATMENTDIVISION)
#plotShow(measurements, GPChartType.LINE,    PlotOptions.POPULATIONVSDIVISION)


"""
# Creating New Plots

It's not that much use to simply measurements with the predefined scripts.
However there is plotPyList which can operate on a simple list of the form [(x1,y1),(x2,y2)].
That is a list of tuples and plot them. 
"""

# lets create a plot of c(k,t) for t_0
t = all_times[0]
coords = [(k,c(k,t)) for k in all_divisions]
plotPyList(coords,"c(k,t) = x(k,t)/2^k - cohort number @ t="+str(t))

# how about S(t) vs t
coords = [(t,S(t)) for t in all_times]
plotPyList(coords,"S(t) = SUM(c(k,t)) k = 0...inf - cohort sum")

# how about g(t) vs t
coords = [(t,g(t)) for t in all_times]
plotPyList(coords,"g(t) = SUM(kc(k,t)/S(t)) - mean division")

# % cohort sum vs mean division
x_coords = [g(t) for t in all_times]
y_raw = [S(t) for t in all_times]
Y = max(y_raw)
y_normalised = [y/Y*100 for y in y_raw]
coords = zip(x_coords, y_normalised)
plotPyList(coords,"% cohort sum vs mean division")


