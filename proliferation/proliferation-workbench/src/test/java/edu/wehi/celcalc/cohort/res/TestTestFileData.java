package edu.wehi.celcalc.cohort.res;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestTestFileData {
	
	@Test
	public void testCorrectTestEnv()
	{
		assertTrue(TestFileData.file!=null);
		assertTrue(TestFileData.workbook!=null);
		assertTrue(TestFileData.live_sheet!=null);
		assertTrue(TestFileData.drop_sheet!=null);
		assertTrue(TestFileData.dead_sheet!=null);
	}
	
	
}
