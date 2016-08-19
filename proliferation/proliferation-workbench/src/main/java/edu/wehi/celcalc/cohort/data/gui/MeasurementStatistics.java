package edu.wehi.celcalc.cohort.data.gui;

import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wehi.GUI;

public class MeasurementStatistics
implements Serializable 
{
	private static final long serialVersionUID = 1L;

	final Integer noMeasurements;
	final Integer noTreatments;
	final Double lowestCellCount;
	final Double highestCellCount;
	final Double lowestTime;
	final Double highestTime;
	final Integer lowestDivision;
	final Integer highestDivision;
	
	public MeasurementStatistics(
			Integer noMeasurements,
			Integer noTreatments,
			Double lowestCellCount,
			Double highestCellCount,
			Double lowestTime,
			Double highestTime,
			Integer lowestDivision,
			Integer highestDivision)
	{
		super();
		this.noMeasurements = noMeasurements;
		this.noTreatments = noTreatments;
		this.lowestCellCount = lowestCellCount;
		this.highestCellCount = highestCellCount;
		this.lowestTime = lowestTime;
		this.highestTime = highestTime;
		this.lowestDivision = lowestDivision;
		this.highestDivision = highestDivision;
	}
	
	public Integer getLowestDivision() {
		return lowestDivision;
	}

	public Integer getHighestDivision() {
		return highestDivision;
	}

	public Integer getNoMeasurements() {
		return noMeasurements;
	}
	
	public Integer getNoTreatments() {
		return noTreatments;
	}
	
	public Double getLowestCellCount() {
		return lowestCellCount;
	}
	
	public Double getHighestCellCount() {
		return highestCellCount;
	}
	
	public Double getLowestTime() {
		return lowestTime;
	}
	
	public Double getHighestTime() {
		return highestTime;
	}
	
	public JComponent getView()
	{
		JPanel pnl = new JPanel(new FlowLayout());
		
		addDouble(pnl, "No. Measurements: ", noMeasurements);
		addDouble(pnl, "No. Treatments: ", noTreatments);
		addDouble(pnl, "Min no. Cells (for div): ", lowestCellCount);
		addDouble(pnl, "Max no. Cells (for div):", highestCellCount);
		addDouble(pnl, "Lowest time point: ", lowestTime);
		addDouble(pnl, "Highest Time point:", highestTime);
		addDouble(pnl, "Min Division : ", lowestDivision);
		addDouble(pnl, "Max Division : ", highestDivision);

		return pnl;
	}
	
	public void addDouble(JPanel pnl, String lbl, Object value)
	{
		JPanel p = new JPanel();
		p.add(new JLabel(lbl));
		p.add(new JLabel(value+""));
		pnl.add(p);
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new MeasurementStatistics(20, 10, 20.0, 30.9, 0.0, 100.0,0,10).getView());
	}


}
