package edu.wehi.celcalc.cohort.runner;

import java.io.Serializable;

public class Runner implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	int seed = 0;
	int runs = 0;
	int sampleSize = 0;
	boolean showAllResults = false;
	boolean isNoSamplesPerTimePoint = false;
	RunnerOptions options;
	

	public Runner(int seed, int runs, int sampleSize, boolean showAllResults, boolean isNoSamplesPerTimePoint, RunnerOptions options)
	{
		super();
		this.seed = seed;
		this.runs = runs;
		this.sampleSize = sampleSize;
		this.showAllResults = showAllResults;
		this.options = options;
	}
	
	public RunnerOptions getOptions() {
		return options;
	}

	public void setOptions(RunnerOptions options) {
		this.options = options;
	}
	
	public boolean isNoSamplesPerTimePoint() {
		return isNoSamplesPerTimePoint;
	}

	public void setNoSamplesPerTimePoint(boolean isNoSamplesPerTimePoint) {
		this.isNoSamplesPerTimePoint = isNoSamplesPerTimePoint;
	}

	
	public Runner()
	{
		this(0,0,0,false, false, RunnerOptions.ALL);
	}
	
	public int getSeed() {
		return seed;
	}
	public void setSeed(int seed) {
		this.seed = seed;
	}
	public int getRuns() {
		return runs;
	}
	public void setRuns(int runs) {
		this.runs = runs;
	}
	public int getSampleSize() {
		return sampleSize;
	}
	public void setSampleSize(int sampleSize) {
		this.sampleSize = sampleSize;
	}
	public boolean isShowAllResults() {
		return showAllResults;
	}
	public void setShowAllResults(boolean showAllResults) {
		this.showAllResults = showAllResults;
	}
	
	@Override
	public String toString()
	{
		return 	"seed: \t" + seed +"\n"+
				"runs: \t" + runs +"\n"+
				"sampleSize: \t" + sampleSize +"\n"+
				"showAllResults: \t" + showAllResults +"\n"+
				"options: \t" + options;
	}
	

}
