from cohort import *
"""
    Note this script must be executed in the global namespace as it requires the variables x_java & all_divisions be defined.
    These should be passed into Jython by java.
"""

def x(k,t):
    """ x(k,t) - number of cells """
    global x_java
    return x_java.get(k,t)

def X(t):
    """ S(t) = SUM(c(k,t)) k = 0...inf - cohort sum """
    global all_divisions
    return sum([x(k,t) for k in all_divisions])

def c(k,t):
    """ c(k,t) = x(k,t)/2^k - cohort number """
    return x(k,t)/(2**k)

def S(t):
    """ S(t) = SUM(c(k,t)) k = 0...inf - cohort sum """
    global all_divisions
    return sum([c(k,t) for k in all_divisions])

def g(t):
    """ g(t) = SUM(kc(k,t)/S(t)) - mean division """
    global all_divisions
    return sum([k*c(k,t)/S(t) for k in all_divisions])

def G(k):
    """ G(k) = SUM(g(i))  i = 1..k"""
    return sum([g(i) for i in range(k)])

def phi(k):
    """ (1 - G(k)) / (1 - G(k - 1) ) - progression factor """
    if (k == 0):
        return g(0)
    else:
        return (1-G(k))/(1 - G(k-1))