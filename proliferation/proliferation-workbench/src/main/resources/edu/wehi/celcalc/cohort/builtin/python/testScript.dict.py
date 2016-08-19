result_map = {}

gcoords = [(t,g(t)) for t in all_times]

for (x,y) in gcoords:
    result_map[x] = y

result_map["no mes."] = len(measurements)