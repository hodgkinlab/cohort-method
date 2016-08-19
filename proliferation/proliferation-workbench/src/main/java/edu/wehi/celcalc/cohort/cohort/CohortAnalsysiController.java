package edu.wehi.celcalc.cohort.cohort;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Dataset;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.gui.application.FilterView;

public abstract class CohortAnalsysiController
{

	CohortAnalsysisModel model = new CohortAnalsysisModel();
	
	public CohortAnalsysiController(CohortMenu mnu)
	{
		mnu.miCohortAnalysis.addActionListener(e -> showCohortAnalysis(getPnlTrio(), getPnlCellNumVsDiv(), getPnlCohortVsDiv()));
	}
	
	public abstract JPanel getPnlTrio();
	public abstract JPanel getPnlCellNumVsDiv();
	public abstract JPanel getPnlCohortVsDiv();

	public void showCohortAnalysis(JPanel pnlTrio, JPanel pnlCellNumVsDiv, JPanel pnlCohortVsDiv)
	{
		FilterView filterView = new FilterView();
		Filter filter = new Filter();
		filter.setType(CellType.LIVE);
		filterView.syncTreatments(getTreatments());
		filterView.sync(filter);
		
		int value = JOptionPane.showConfirmDialog(null,filterView,"Experiments",JOptionPane.PLAIN_MESSAGE);
		
		if (value == JOptionPane.OK_OPTION)
		{
			model.showCohortAnalysis(
					getDataset(), filter.filter(getMeasurements()), pnlTrio, pnlCellNumVsDiv, pnlCohortVsDiv);
		}
	}
	
	public abstract Dataset getDataset();
	public abstract List<Measurement> getMeasurements();
	public abstract List<String> getTreatments();
	
}
