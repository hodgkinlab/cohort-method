package edu.wehi.demo;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterController;
import edu.wehi.celcalc.cohort.res.TestFileData;

public class ExampleScriptEngine
{
	
	public static void main(String[] args)
	{
		
		final List<Measurement> measurementsList = TestFileData.mesurements;
		ScripterController controller = new ScripterController(measurementsList);
		
		controller.getView().initGui();
		JMenuBar bar = new JMenuBar();
		bar.add(controller.getScriptMenu());
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(bar);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.add(controller.getView());
	}

}
