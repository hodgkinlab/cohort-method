/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wehi.celcalc.cohort.old;

//import java.lang.Math;
import java.util.ArrayList;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 *
 * @author pavlyshyn.d
 */
public class Normal {
	double mu, sigma;
	NormalDistribution dist;

	public Normal(double mu, double sigma) {
		this.mu = mu;
		this.sigma = sigma;
		dist = new NormalDistribution(mu, sigma);
	}

	public double getProb(double a, double b) {
		return dist.cumulativeProbability(b) - dist.cumulativeProbability(a);
	}

	static public double normcdf(double mu, double sigma, double t) {
		NormalDistribution dist = new NormalDistribution(mu, sigma);
		return dist.cumulativeProbability(t);
	}

	static public double normpdf(double mu, double sigma, double t) {
		NormalDistribution dist = new NormalDistribution(mu, sigma);
		return dist.density(t);
	}
	
	static public ArrayList<Double> normcdfAtPoints( double mu,  double sigma, ArrayList<Double> timePoints) {
		ArrayList<Double> cdfValues = new ArrayList<Double>();
		NormalDistribution dist = new NormalDistribution(mu, sigma);
		for (int i=0; i<timePoints.size(); i++) {
			cdfValues.add(dist.cumulativeProbability(timePoints.get(i)));
		}
		return cdfValues;
	}

	static public ArrayList<Double> normpdfAtPoints( double mu,  double sigma, ArrayList<Double> timePoints) {
		ArrayList<Double> pdfValues = new ArrayList<Double>();
		NormalDistribution dist = new NormalDistribution(mu, sigma);
		for (int i=0; i<timePoints.size(); i++) {
			pdfValues.add(dist.density(timePoints.get(i)));
		}
		return pdfValues;
	}
}
