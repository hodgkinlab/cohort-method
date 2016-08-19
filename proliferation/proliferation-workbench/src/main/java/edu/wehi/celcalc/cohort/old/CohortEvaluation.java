package edu.wehi.celcalc.cohort.old;

import java.util.*;
//import java.lang.Math;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.analysis.*;

/**
 * This class contains information about the cohort opimisation problem. It is
 * designed to be passed to various fields of a LeastSquaresBuilder 
 * (org.apache.commons.math3.fitting.leastsquares).
 *
 * The class contains a list "cohortNumbers" which is not raw cell count data,
 * but the cohort counts as calculated in CohortProcessor.
 *
 * @author Damian Pavlyshyn
 * pavlyshyn.d@wehi.edu.au
 */
public class CohortEvaluation {

	// Contains the cell counts normalised by division growth rate
	private List<Double> cohortNumbers;

	public CohortEvaluation(List<Double> inCohortNumbers) {
		this.cohortNumbers = inCohortNumbers; // These are the measured data
	}

	/* The cohort numbers are to be fitted as a probability distribution
	 * normalised by cell counts. This function generates an evaluation function
	 * that returns that value based on mean, standard deviation and normalisation
	 * factor supplied in the array "params". */

	public MultivariateVectorFunction getModelFunction() {
        return new MultivariateVectorFunction() {
            public double[] value(double[] params) {
                final double mu = params[0];
                final double sigma = Math.abs(params[1])+1e-5;
				//final double sigma = Math.max(params[1], 0.1);
                final double gamma = params[2];

				final double[] model = new double[cohortNumbers.size()];

				//System.out.println(sigma);

				/*if (sigma < 1e-8) {
					for(int i=0; i<cohortNumbers.size(); i++) {
						model[i] = 0;
					}
					return model;
				}*/

				NormalDistribution dist = new NormalDistribution(mu, sigma);

				/*for(int i=0; i<cohortNumbers.size()-1; i++) {
					model[i] = gamma*(dist.cumulativeProbability(i+1) - dist.cumulativeProbability(i));
				}
				model[cohortNumbers.size()-1] = 1-dist.cumulativeProbability(cohortNumbers.size()-1);
				 *
				 */
				for(int i=0; i<cohortNumbers.size()-1; i++) {
					model[i] = gamma*(dist.cumulativeProbability(i+0.5) - dist.cumulativeProbability(i-0.5));
				}
				model[cohortNumbers.size()-1] = 1-dist.cumulativeProbability(cohortNumbers.size()-1.5);
				return model;
            }
        };
    }

	/*
	 * Generates a function that computes the Jacobian for the objective function produced
	 * by getModelFunction. The entries have been computed analytically by
	 * taking partial derivatives of the objective function with respect to mean,
	 * standard deviation and normalisation factor
	 */
	public MultivariateMatrixFunction getModelFunctionJacobian() {
        return new MultivariateMatrixFunction() {
            public double[][] value(double[] params) {
                final double mu = params[0];
                final double sigma = Math.abs(params[1])+1e-5;
				//final double sigma = Math.max(params[1], 0.1);
                final double gamma = params[2];
				
				final double[][] jacobian = new double[cohortNumbers.size()][3];

				NormalDistribution dist = new NormalDistribution(mu, sigma);

				/*for(int i=0; i<cohortNumbers.size()-1; i++) {
					jacobian[i][0] = gamma * (dist.density(i+1) - dist.density(i));
					jacobian[i][1] = gamma/sigma * (
							(i+1-mu)*dist.density(i+1) - (i-mu)*dist.density(i));
					jacobian[i][2] = dist.cumulativeProbability(i+1) - dist.cumulativeProbability(i);
				}

				jacobian[cohortNumbers.size()-1][0] = -gamma * dist.density(cohortNumbers.size()-1);
				jacobian[cohortNumbers.size()-1][1] = -gamma/sigma * (cohortNumbers.size()-1-mu)*dist.density(cohortNumbers.size()-1);
				jacobian[cohortNumbers.size()-1][2] = 1 - dist.cumulativeProbability(cohortNumbers.size()-1);
				*/
				for(int i=0; i<cohortNumbers.size()-1; i++) {
					jacobian[i][0] = gamma * (dist.density(i+0.5) - dist.density(i-0.5));
					jacobian[i][1] = gamma/sigma * (
							(i+0.5-mu)*dist.density(i+0.5) - (i-0.5-mu)*dist.density(i-0.5));
					jacobian[i][2] = dist.cumulativeProbability(i+0.5) - dist.cumulativeProbability(i-0.5);
				}

				jacobian[cohortNumbers.size()-1][0] = -gamma * dist.density(cohortNumbers.size()-1.5);
				jacobian[cohortNumbers.size()-1][1] = -gamma/sigma * (cohortNumbers.size()-1.5-mu)*dist.density(cohortNumbers.size()-1.5);
				jacobian[cohortNumbers.size()-1][2] = 1 - dist.cumulativeProbability(cohortNumbers.size()-1.5);
				return jacobian;
            }
        };
    }

	/*
	 * At the moment, the cohort optimisation is unweighted, but the weights can
	 * be set in this function
	 */
	public double[] weight() {
		double[] weights = new double[cohortNumbers.size()];
		for (int i=0; i<cohortNumbers.size(); i++) {
			//weights[i] = 1;
			//weights[i] = Math.log(cohortNumbers.get(i)+1);
			weights[i] = cohortNumbers.get(i);
		}
		return weights;
	}

	/*
	 * Translates cohortNumbers from their usual structure to a form recognisable
	 * by LeastSquaresBuilder
	 */
	public double[] target() {
		double[] t = new double[cohortNumbers.size()];
		for (int i=0; i<cohortNumbers.size(); i++) {
			t[i] = cohortNumbers.get(i);
		}
		return t;
	}

}
