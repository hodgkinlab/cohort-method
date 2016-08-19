/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wehi.celcalc.cohort.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author akan
 */
public class Condition {

	final String name;
	final List<TimePoint> timePoints;

	public Condition(String name, SimpleMatrix data) {
		this.name = name;

		int dataRows = data.numRows();
		int dataCols = data.numCols();

		HashMap<Double, List<Integer>> tpMap = new HashMap();
		for (int j = 0; j < dataCols; j++) {
			Double t = data.get(0, j);
			if (!tpMap.containsKey(t)) {
				List<Integer> indexList = new ArrayList();
				indexList.add(j);
				tpMap.put(t, indexList);
			} else {
				List<Integer> indexList = tpMap.get(t);
				indexList.add(j);
			}
		}

		timePoints = new ArrayList();
		int numDivs = dataRows - 1;

		for (Double t : tpMap.keySet()) {
			List<Integer> indexList = tpMap.get(t);
			int numReps = indexList.size();

			SimpleMatrix vals = new SimpleMatrix(numDivs, numReps);
			for (int i = 0; i < numDivs; i++) {
				for (int j = 0; j < numReps; j++) {
					int iSrc = i + 1;
					int jSrc = indexList.get(j);
					double v = data.get(iSrc, jSrc);
					vals.set(i, j, v);
				}
			}

			TimePoint tp = new TimePoint(t, vals);
			timePoints.add(tp);
		}
		Collections.sort(timePoints);
	}
	
	public String getName() {
		return name;
	}
	
	public List<TimePoint> getTimePoints() {
		return timePoints;
	}
}
