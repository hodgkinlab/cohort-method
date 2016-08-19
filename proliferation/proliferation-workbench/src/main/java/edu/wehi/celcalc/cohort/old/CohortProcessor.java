package edu.wehi.celcalc.cohort.old;

import java.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.fitting.leastsquares.*;
import org.apache.commons.math3.stat.regression.*;
import org.apache.commons.math3.distribution.NormalDistribution;
/**
 * 
 *
 * http://commons.apache.org/proper/commons-math/userguide/optimization.html
 * http://apache-commons.680414.n4.nabble.com/LevenbergMarquardtOptimizer-td4649013.html
 *
 *
 * This class solves the cohort optimisation problem based on cell population
 * data supplied in a CohortExpReader (Share) object called reader. It does so in
 * the following way:
 *
 *  (1) Translates raw cell count data into cohortNumbers. These are cell counts
 *      normalised so that a given cohort of cells has equal presence in each
 *      generation, even though it's raw count would double at each new generation
 *
 *  (2) Creates a LeastSquaresProblem (org.apache.commons.math3.fitting.leastsquares)
 *      based on a cohortEvaluation object supplied with the newly-calculated
 *      cohortNumbers.
 *
 *  (3) Finds an unweighted least-squares fit of this problem using a
 *      Levenberg-Marquardt optimisation
 *
 *  (4) Returns a 2-d list of parameters (mean, standard deviation, normalising
 *      factor) in each timepoint and concentration present in the data.
 *
 * The class should be invoked in this manner:
 *
 * CohortProcessor processor = new CohortProcessor(-);
 * processor.setSettings(-);
 * ArrayList<ArrayList<double[]>> fittedParams = processor.fitter()
 *
 * @author Damian Pavlyshyn
 * pavlyshyn.d@wehi.edu.au
 */
public class CohortProcessor
{

	private int maxDiv = 5;
	private boolean infinity = false;
	private Map<Double, Map<Double, double[]>> localFittedParams = null;

	private  Map<Double, Map<Double, List<Double>>> startParams;
	private final  Map<Double, Map<Double, List<Double>>> liveMeans;
	private final  Map<Double, Map<Double, List<List<Double>>>> liveCellData;

	// Map<Double, Map<Double, List<Double>>> liveCells = reader.getLiveMeans();
	// this.numConcs = reader.getConcentrationList().size();
	//Map<Double, Map<Double, List<Double>>> liveMeans = reader.getLiveMeans();
	// Map<Double, Map<Double, List<List<Double>>>> liveCellData = reader.getLiveData();
	// Map<Double, Map<Double, List<Double>>> liveCellMeans = reader.getLiveMeans();
	
	
	
	
	
	public CohortProcessor(CohortXLSReader reader)
	{
		this(reader.getLiveMeans(), reader.getLiveData());
		setSettings(5,false);
	}
	
	
	
	
	public CohortProcessor(
			Map<Double, Map<Double, List<Double>>> liveMeans,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData)
	{

		this.liveMeans = liveMeans;
		this.liveCellData = liveCellData;
		setStartParams();
	}
	
	





	/*
	 * Fits the cohort numbers at each timepoint and concentration to a
	 * discretised normal distribution normalised to the number of cells.
	 *
	 * Returns a 2D ArrayList where element fittedParams[i][j] contains a triple
	 * [mu, sigma, gamma] of parameters for the discretised normal (gamma is the
	 * normalising contstant) at concentration i and timepoint j
	 */
	public Map<Double, Map<Double, double[]>> fitter()
	{
		Map<Double, Map<Double, double[]>> fittedParams = new HashMap<Double, Map<Double, double[]>>();


		for (Double conc : liveMeans.keySet())
		{
			Map<Double, double[]> concParams = new HashMap<Double, double[]>();
			for (Double time : liveMeans.get(conc).keySet())
			{
				concParams.put(time, singleFitter(conc, time));
			}
			fittedParams.put(conc, concParams);
		}

		this.localFittedParams = fittedParams;
		return fittedParams;
	}

	/*
	 * Estimate the time to first division and time to subsequent division based
	 * on the live cell counts provided in reader.
	 *
	 * Reurns a pair of doubles [time to first division, time to subsdquent
	 * division]
	 */
	public double[] getFirstTimes(double conc)
	{
		fitter();

		Map<Double, double[]> concParams = localFittedParams.get(conc);
		SimpleRegression etaRegression = new SimpleRegression();

		for (double[] currParams : concParams.values())
		{
			etaRegression.addData(currParams[0], Math.log(currParams[2]));
		}
		double eta = etaRegression.getSlope();

		Map<Double, Double> newMeans = new HashMap<Double, Double>();
		for (Double time : concParams.keySet())
		{
			NormalDistribution dist = new NormalDistribution(
					concParams.get(time)[0],
					concParams.get(time)[1]+1e-5);
			
			
			
			double sumNum = 0;
			double sumDenom = 0;

			for (double t=-20; t<20; t += 40.0/1000)
			{
				sumNum += Math.exp(-eta*t)*(t+0.5)*dist.probability(t, t+1);
				sumDenom += Math.exp(-eta*t)*dist.probability(t, t+1);
			}
			
			newMeans.put(time, sumNum/sumDenom);
		}

		// Fit a straight line to newmus vs time
		SimpleRegression finalRegression = new SimpleRegression();
		for (Double time : newMeans.keySet())
		{
			finalRegression.addData(time, newMeans.get(time));
		}
		
		
		double b1 = finalRegression.getSlope();
		double b0 = finalRegression.getIntercept();


		/*
		 * Solves for the times:
		 *	mu(t1) = 1 (time to first division)
		 *	t2 - t1 such that mu(t2) = 2, mu(t1) = 1 (time to subsequent division)
		 */
		double[] times = new double[2];
		times[0] = (1-b0)/b1;
		times[1] = 1/b1;
		
		return times;
	}

	public Map<Double, Map<Double, List<List<Double>>>> getCohortNumbers()
	{
		Map<Double, Map<Double, List<List<Double>>>> cohortNumbersMatrix =
				new HashMap<Double, Map<Double, List<List<Double>>>>();


		for(Double conc : liveCellData.keySet())
		{
			Map<Double, List<List<Double>>> concCohortNumbers = new HashMap<Double, List<List<Double>>>();
			for (Double time : liveCellData.get(conc).keySet())
			{
				concCohortNumbers.put(time, new ArrayList<List<Double>>());
				for (int i=0; i<liveCellData.get(conc).get(time).size(); i++) 
				{
					concCohortNumbers.get(time).add(getCohortNumbers(conc, time, i, liveMeans, liveCellData, infinity, maxDiv));
				}
			}
			cohortNumbersMatrix.put(conc, concCohortNumbers);
		}
		return cohortNumbersMatrix;
	}

	
	
	/**
	 * 
	 * 
	 * 		double conc,
			double tPoint,
			int repeat,
			Map<Double, Map<Double, List<Double>>> liveMeans,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData,
			boolean infinity,
			int maxDiv)
	 * 
	 * 
	 */
	
	
	
	
	/*
	 * Optimisation for a given concentration, timepoint happens here.
	 * Mainly a wrapper for LeastSquaresBuilder and LevenbergMarquardtOptimizer
	 */
	private double[] singleFitter(double conc, double tPoint)
	{

		// Generate a description of the optimisation problem
		List<Double> cohortNumbers = getCohortNumbers(conc, tPoint, -1, liveMeans, liveCellData, infinity, maxDiv);
		CohortEvaluation details = new CohortEvaluation(cohortNumbers);

		double[] start = new double[3];

		start[0] = startParams.get(conc).get(tPoint).get(0).doubleValue();
		start[1] = Math.abs(startParams.get(conc).get(tPoint).get(1).doubleValue());
		start[2] = startParams.get(conc).get(tPoint).get(2).doubleValue();

		// Provide all the details of the optimisation problem
		LeastSquaresBuilder builder = new LeastSquaresBuilder()
				.model(details.getModelFunction(), details.getModelFunctionJacobian())
				.checkerPair(new SimpleVectorValueChecker(1e-8, 1e-8))
				.maxIterations(100000)
				.maxEvaluations(100000)
				.start(start)
				.weight(new DiagonalMatrix(details.weight()))
				.target(details.target());

		LeastSquaresProblem problemDescription = builder.build();

		// Perform the optimisation itself
		LevenbergMarquardtOptimizer optim = new LevenbergMarquardtOptimizer();
		LevenbergMarquardtOptimizer.Optimum optimum = optim.optimize(problemDescription);

		// Translate output into a double array
		double[] fittedParams = optimum.getPoint().toArray();
		fittedParams[1] = Math.abs(fittedParams[1]);


		return fittedParams;
	}


	public void setSettings(int inMaxDiv, boolean inInfinity) {
		maxDiv = inMaxDiv;
		infinity = inInfinity;
	}
	
	
	
	Map<Double, Map<Double, List<Double>>> computeStartParams(Map<Double, Map<Double, List<List<Double>>>> liveCellData, boolean infinity)
	{
		Map<Double, Map<Double, List<Double>>> liveCells = new HashMap<>();
		Map<Double, Map<Double, List<Double>>> startParams = new HashMap<Double, Map<Double, List<Double>>>();
		
		
		for (Double conc : liveCells.keySet())
		{
			Map<Double, List<Double>> concParams = new HashMap<Double, List<Double>>();
			for (Double time : liveCells.get(conc).keySet())
			{
				double peak = liveCells.get(conc).get(time).get(0);
				double sum=0, meanTotal=0, sdTotal=0;
				for (int j=0; j<getCohortNumbers(conc, time, -1, liveCells, liveCellData, infinity, j).size(); j++)
				{
					Double candidate = getCohortNumbers(conc, time, -1, liveCells, liveCellData, infinity, j).get(j);
					if (candidate > peak)
					{
						peak = candidate;
					}
					meanTotal += j*candidate;
					sum += candidate;
				}

				for (int j=0; j<liveCells.get(conc).get(time).size(); j++)
				{
					Double candidate = liveCells.get(conc).get(time).get(j)/Math.pow(2, j);
					sdTotal += candidate*(j-meanTotal/sum)*(j-meanTotal/sum);
				}

				sum = Math.max(10, sum);
				ArrayList<Double> params = new ArrayList<Double>();
				params.add(new Double(meanTotal/sum));
				params.add(new Double(Math.sqrt(sdTotal/sum)));
				params.add(new Double(sum));
				concParams.put(time, params);
			}
			startParams.put(conc, concParams);
		}
		
		
		return startParams;
	}
	
	
	
	
	/*
	 * Generates a starting guess of parameters by naievely selecting the highest
	 * peak of live cells.
	 */
	public void setStartParams()
	{

		Map<Double, Map<Double, List<Double>>> liveCells = liveMeans;
		startParams = new HashMap<Double, Map<Double, List<Double>>>();
		for (Double conc : liveCells.keySet()) {

			//int numTimes = liveCells.get(conc).size();
			//int numDivs = liveCells.get(conc).get(0).size();

			Map<Double, List<Double>> concParams = new HashMap<Double, List<Double>>();
			for (Double time : liveCells.get(conc).keySet()) {
				double peak = liveCells.get(conc).get(time).get(0);
				double sum=0, meanTotal=0, sdTotal=0;
				//for (int j=0; j<liveCells.get(conc).get(time).size(); j++) {
				for (int j=0; j<getCohortNumbers(conc, time, -1, liveCells, liveCellData, infinity, j).size(); j++) {
					Double candidate = getCohortNumbers(conc, time, -1, liveCells, liveCellData, infinity, j).get(j);
					//Double candidate = liveCells.get(conc).get(time).get(j)/Math.pow(2, j);
					if (candidate > peak) {
						peak = candidate;
					}
					meanTotal += j*candidate;
					sum += candidate;
				}

				for (int j=0; j<liveCells.get(conc).get(time).size(); j++) {
					Double candidate = liveCells.get(conc).get(time).get(j)/Math.pow(2, j);
					sdTotal += candidate*(j-meanTotal/sum)*(j-meanTotal/sum);
				}

				sum = Math.max(10, sum);
				ArrayList<Double> params = new ArrayList<Double>();
				//params.add(new Double(peakLocation));
				params.add(new Double(meanTotal/sum));
				//params.add(new Double(1));
				params.add(new Double(Math.sqrt(sdTotal/sum)));
				params.add(new Double(sum));
				concParams.put(time, params);
			}
			startParams.put(conc, concParams);
		}
	}
	

	/*
	 * Translates raw cell count data into normalised cohort numbers
	 * Performs various filling if a given maxDiv is supplied
	 *
	 * To get the cohort numbers of the means, set repeat to -1
	 */
	public static List<Double> getCohortNumbers(
			double conc,
			double tPoint,
			int repeat,
			Map<Double, Map<Double, List<Double>>> liveMeans,
			Map<Double, Map<Double, List<List<Double>>>> liveCellData,
			boolean infinity,
			int maxDiv)
		{
		
		
		
		List<Double> cohortNumbers = new ArrayList<Double>();
		List<Double> liveCells;
		
		if (repeat < 0) {
			liveCells = liveMeans
					.get(conc)
					.get(tPoint);
		} else {
			liveCells = liveCellData
					.get(conc)
					.get(tPoint)
					.get(repeat);
		}

		/* set maxDiv: infinity means that there is no cutoff, otherwise adjust
		 for zero-offset */
		int nDim = liveCells.size();
		if (!infinity) {
			maxDiv = nDim;
		}

		//divisions.add(new Double(1));
		for (int i=0; i<maxDiv; i++) {
			/* For a given concentration, timepoint and max division, build
			 cohortNumbers from the vector of cell counts per division normalised
			 by 2^div number */
			cohortNumbers.add((liveCells.get(i)/*+0.001*/)/Math.pow(2, i));

		}
		double sum = 0;


		/* Fill up the tail of cohortNumbers */
		if (maxDiv < nDim) {
			sum = 0;
			for (int i=maxDiv; i<nDim; i++) {
				sum += liveCells.get(i)/Math.pow(2, i);
			}
			cohortNumbers.set(cohortNumbers.size()-1, new Double(sum/(nDim-maxDiv)));
		}

		return cohortNumbers;
	}

}
