/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wehi.celcalc.cohort.data;

import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author akan
 */
public class TimePoint implements Comparable<TimePoint> {

	private final double time;
	private final SimpleMatrix vals;

	TimePoint(double time, SimpleMatrix vals) {
		this.time = time;
		this.vals = vals;
	}

	public double getTime() {
		return time;
	}

	public SimpleMatrix getValues() {
		return vals;
	}

	@Override
	public int compareTo(TimePoint tp) {
		double otherTime = tp.getTime();
		double dt = this.getTime() - otherTime;
		return (int) Math.signum(dt);
	}
}
