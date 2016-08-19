"""
\text{Cohort Numer at First Time Point} \\
c(k,t_0)=\frac{x(k,t_0)}{2^k}
"""
t = all_times[0] # cohort at first timepoint time_0
result_coords = [(k,c(k,t)) for k in all_divisions]