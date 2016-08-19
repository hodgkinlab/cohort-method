package edu.wehi.celcalc.cohort.data;

import static org.junit.Assert.*;

import org.junit.*;

public class TestCellType {
	
	@Test
	public void testCellType()
	{
		assertTrue(CellType.cellType("live") == CellType.LIVE);
		assertTrue(CellType.cellType("drop") == CellType.DROP);
		assertTrue(CellType.cellType("drop") == CellType.DROP);
		assertTrue(CellType.cellType("drosdfsp") == null);
	}

}
