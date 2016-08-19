package edu.wehi.celcalc.cohort.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.wehi.graphplot.plot.GPCoordinateWithName;

public class Measurement implements GPCoordinateWithName, Groupable, Serializable {
	

	private static final long serialVersionUID = 1L;
	
	private int		 id;
	private double 	 cells;
	private double 	 time;
	private int 	 div;
	private CellType type;
	private String 	 treatment;
	private boolean isIncluded = true;
	
	public Measurement(double time, double cells, int div, CellType type, String treatment)
	{
		
		if (treatment == null)
		{
			throw new RuntimeException("Can't pass null for treatment");
		}
		if (treatment == "")
		{
			throw new RuntimeException("Must have a name for treatment");
		}
		
		this.cells = cells;
		this.time = time;
		this.div = div;
		this.type = type;
		this.treatment = treatment.trim();
	}
	
	public Measurement(double time, double cells, int div, CellType type, String treatment, boolean isIncluded)
	{
		this(time, cells, div, type, treatment);
		this.isIncluded = isIncluded;
	}
	
	
	@Override
	public String toString()
	{
		return "time\t\t"+time 				+ "\n" +
				"cells\t\t" 	+ cells 	+ "\n" +
				"div\t\t" 		+ div 		+ "\n" +
				"type\t\t" 		+ type		+ "\n" +
				"included\t\t" 	+ isIncluded+ "\n" +
				"treatment\t" 	+ treatment + "\n\n\n";
	}

	@Override
	public double getX() {
		return time;
	}

	@Override
	public double getY() {
		return cells;
	}

	@Override
	public String getName() {
		return toString();
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getId() {
		return id;
	}


	public boolean isIncluded() {
		return isIncluded;
	}


	public void setIncluded(boolean isIncluded) {
		this.isIncluded = isIncluded;
	}


	public double getCells() {
		return cells;
	}


	public double getTime() {
		return time;
	}


	public int getDiv() {
		return div;
	}


	public CellType getType() {
		return type;
	}


	public String getTreatment() {
		return treatment;
	}


	@Override
	public String getGroupKey() {
		return type+"/"+treatment+"/"+div;
	}
	
	public void setCells(double cells) {
		this.cells = cells;
	}


	public void setTime(double time) {
		this.time = time;
	}


	public void setDiv(int div) {
		this.div = div;
	}


	public void setType(CellType type) {
		this.type = type;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
	
	public GPCoordinateWithName cellByGeneration()
	{
		return new GPCoordinateWithName() {
			
			@Override
			public double getY() {
				return cells;
			}
			
			@Override
			public double getX() {
				return div;
			}
			
			@Override
			public String getName() {
				return toString();
			}
			
			double lower = 0.0;
			@Override
			public void setYLower(double lower) {
				this.lower = lower;
			}

			double upper = 0.0;
			@Override
			public void setYUpper(double upper) {
				this.upper = upper;
			}
			
			@Override
			public double getYUpper()
			{
				return this.getY()  + upper;
			}
			
			@Override
			public double getYLower()
			{
				return this.getY() - lower;
			}
			
		};
	}
	
	public GPCoordinateWithName cellByCohort()
	{
		return new GPCoordinateWithName() {
			
			@Override
			public double getY() {
				return cells/Math.pow(2, div);
			}
			
			@Override
			public double getX() {
				return div;
			}
			
			@Override
			public String getName() {
				return toString();
			}
			
			double lower = 0.0;
			@Override
			public void setYLower(double lower) {
				this.lower = lower;
			}

			double upper = 0.0;
			@Override
			public void setYUpper(double upper) {
				this.upper = upper;
			}
			
			@Override
			public double getYUpper()
			{
				return this.getY()  + upper;
			}
			
			@Override
			public double getYLower()
			{
				return this.getY() - lower;
			}
		};
	}
	
	public GPCoordinateWithName cellByDiv()
	{
		return new GPCoordinateWithName() {
			
			@Override
			public double getY() {
				return cells;
			}
			
			@Override
			public double getX() {
				return div;
			}
			
			@Override
			public String getName() {
				return toString();
			}
			
			double lower = 0.0;
			@Override
			public void setYLower(double lower) {
				this.lower = lower;
			}

			double upper = 0.0;
			@Override
			public void setYUpper(double upper) {
				this.upper = upper;
			}
			
			@Override
			public double getYUpper()
			{
				return this.getY()  + upper;
			}
			
			@Override
			public double getYLower()
			{
				return this.getY() - lower;
			}
		};
	}

	double err = 0.0;
	@Override
	public void setUpperAndLower(double val)
	{
		err = val;
	}

	double lower = 0.0;
	@Override
	public void setYLower(double lower)
	{
		this.lower = lower;
	}


	double upper = 0.0;
	@Override
	public void setYUpper(double upper)
	{
		this.upper = upper;
	}

	public static Collection<Measurement> subSet(
			Collection<Measurement> selectedMeasurements,
			int sampleSize,
			int seed)
	{
		List<Measurement> subset = new ArrayList<>();
		List<Double> allTimes = MeasurementQuery.allTimes(selectedMeasurements);
		Set<Integer> allDivs = MeasurementQuery	.allDivisions(selectedMeasurements);
		Set<String> allTreatments = MeasurementQuery		.allTreatments(selectedMeasurements);
		
		Random rand = new Random(seed);
		
		int i = 1;
		for (CellType type : CellType.values())
		{
			for (String treatment : allTreatments)
			{
				for (double time : allTimes)
				{
					for (int div : allDivs)
					{
						List<Measurement> measurements = new ArrayList<>(new MeasurementQuery(selectedMeasurements)
							.withDivision(div)
							.withTime(time)
							.withTreatment(treatment)
							.withType(type)
						.measurements);
						
						if (measurements.size() == 0)
						{
							continue;
						}
						
						long sseed = (long)(seed*i);
						i++;

						
						
						int noSubElements = Math.min(sampleSize, measurements.size());
						
						if (noSubElements <= 0)
						{
							noSubElements = measurements.size();
						}
						
						// Get the random subet
						Collections.shuffle(measurements, new Random(sseed));
						
						List<Measurement> toAdd = new ArrayList<>();
						
						for (int j = 0; j < noSubElements; j++)
						{
							toAdd.add(measurements.get(rand.nextInt(noSubElements)));
						}
						
						// end of subset code
						
						if (measurements.size() != 0 && toAdd.size() ==0)
						{
							throw new RuntimeException("Error That makes no sense");
						}
						
						subset.addAll(toAdd);
					}
				}
			}
		}

		return subset;
	}
	
	
    public static void main(String a[])
    {
    	
    	Random random = new Random();
        for (int i = 0; i < 20; i++)
        {
        	int randomInt = random.nextInt(2);
        	System.out.println(randomInt);	
        }
    }


	public static Collection<Measurement> subNoPerTimePoint(Collection<Measurement> selectedMeasurements, int seed)
	{
		Random rand = new Random(seed);
		List<Double> allTimes = MeasurementQuery.allTimes(selectedMeasurements);
		Set<Integer> allDivs = MeasurementQuery.allDivisions(selectedMeasurements);
		List<Measurement> measurementSubset = new ArrayList<>();
		
		for (double time : allTimes)
		{
			for (int d : allDivs)
			{
				List<Measurement> all = new ArrayList<>(new MeasurementQuery(selectedMeasurements).withDivision(d).withTime(time).measurements);
				for (int i = 0; i < all.size(); i++)
				{
					int index = rand.nextInt(all.size());
					measurementSubset.add(all.get(index)); 
				}
			}
		}
		return measurementSubset;
	}

}
