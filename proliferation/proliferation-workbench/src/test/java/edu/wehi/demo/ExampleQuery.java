package edu.wehi.demo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.celcalc.cohort.res.TestFileData;

public class ExampleQuery
{
	
	private static final List<Measurement> measurementsList = TestFileData.mesurements;
	private static final Set<Measurement> measurementsSet = new HashSet<>(measurementsList);
	
	public static void main(String[] args){
		
		
		MeasurementQuery query = new MeasurementQuery(measurementsSet);
		
		
//		System.out.println();
//		System.out.println("!!!DIVISIONS QUERRY!!!!");
//		System.out.println();
//		print(query.withDivBetween(0, 2).measurements);
//		System.out.println("--------");
//		
//		System.out.println();
//		System.out.println("!!!TREATMENT QUERRY!!!!");
//		System.out.println();
//		print(query.withTreetment("1000.0").measurements);
//		System.out.println("--------");
		
		System.out.println();
		System.out.println("!!!CHAINED QUERRY!!!!");
		System.out.println();
		
		System.out.println(
				query
						.withMinDiv(0).withMaxDiv(2)
						.withTreatment("1000.0")
						.withType(CellType.LIVE)
			);
		
	}
	
}
