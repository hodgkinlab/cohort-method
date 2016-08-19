package edu.wehi.celcalc.cohort.test;


import static org.junit.Assert.*;

import java.io.File;

import org.junit.*;



public class TestResourceLoaderForTests {
	
	@Test
	public void testAllFilesAreRetrievable()
	{
		for (FILES file : FILES.values())
		{
			assertTrue("The file wasn't there: "+file,new File(ResourceLoaderForTests.getResource(file)).exists());
		}
	}

}
