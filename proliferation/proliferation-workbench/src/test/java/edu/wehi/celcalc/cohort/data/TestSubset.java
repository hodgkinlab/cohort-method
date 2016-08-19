package edu.wehi.celcalc.cohort.data;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import edu.wehi.celcalc.cohort.res.TestFileData;

public class TestSubset
{

	@Test
	public void testSubset()
	{
		int sampleSize = 1;
		int seed = 1;

		Collection<Measurement> mess = Measurement.subSet(TestFileData.mesurements, sampleSize, seed);
		assertNotEquals(TestFileData.mesurements.size(), mess.size());
		
		Set<Integer> allDivs = MeasurementQuery.allDivisions(mess);
		String[] treatments = new String[]{"0.0"};
		double[] times = new double[]{25.0};
		
		for (CellType type : CellType.values())
		{
			for (int div : allDivs)
			{
				for (String t : treatments)
				{
					for (Double time : times)
					{
						int size = new MeasurementQuery(mess)
						.withDivision(div)
						.withTreatment(t)
						.withTime(time)
						.withType(type).measurements.size();
						
						assertTrue(size == sampleSize);
						
										
					}
				}
			}
		}

	}

}
