package edu.wehi.celcalc.cohort.old;


import java.util.Map;
import java.util.Map.Entry;
/**
 * For now, this is a testing suite for CohortProcessot
 * 
 * @author Damian Pavlyshyn
 * @author edited Derrick Futschik
 */

public class CohortMain {
	
	static final String data_folder = "data";
	
	

	static final String[] files = new String[]{"sample data input.xls"};
	
	
	public static void main(String[] args) {
		for (String file : files)
		{
			try
			{
				testFile(data_folder+"/"+file);
			}
			catch (Exception e)
			{
				
			}
			System.out.println("Success! "+file);		
		}
	}
	

	public static void testFile(String filename)
	{
		

		CohortXLSReader chortReader = new CohortXLSReader();
		chortReader.parseFile(filename);
		CohortProcessor processor = new CohortProcessor(chortReader);
		processor.setSettings(5,false);

		 Map<Double, Map<Double, double[]>> params = processor.fitter();


		double[] times = processor.getFirstTimes(0);
		System.out.printf("%f %f\n", times[0], times[1]);

	}


}
