package edu.wehi.celcalc.cohort.old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;



public class CohortController {
	
	private final CohortGUITemp view = new CohortGUITemp();
	private final CohortModel model = new CohortModel();
	
	class OpenFileAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JFileChooser loadDialogue = new JFileChooser("C:\\Users\\futschik.d\\Downloads");
			int returnVal = loadDialogue.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = loadDialogue.getSelectedFile();
				List<Double> concsList = model.parseFile(file.getPath());
				for (Double conc : concsList) {
					view.concSelect.addItem(new ConcComboItem(conc));
				}
				view.displayTabs.removeAll();
			}
		}
	}

	
	class PlotCohortNumbersAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	class PlotCohortParametersAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	class CloseTabAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	class TabSelectedAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	public CohortController()
	{
		view.loadButton.			addActionListener(new OpenFileAction());
		view.plotButton.			addActionListener(new PlotCohortNumbersAction());
		view.plotParametersButton.	addActionListener(new PlotCohortParametersAction());
		view.closeButton.			addActionListener(new CloseTabAction());
	}
	
	public void setVisible()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				view.setVisible(true);
			}
		});
	}
	
	public static void main(String[] args) {
		new CohortController().setVisible();
	}
	
	

}
