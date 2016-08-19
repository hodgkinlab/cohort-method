package edu.wehi.celcalc.cohort.test;

public enum FILES 
{
	TESTFORMATXLSX("data_format_test.xlsx"),
	TESETPARSING("parsing_test.xlsx"),
	TESTCOHORTINTEGRATION("cohort_integration_test.xlsx");
	
	
	public final String file;
	FILES(String file)
	{
		this.file = file;
	}
}
