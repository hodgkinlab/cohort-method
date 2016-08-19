package edu.wehi.celcalc.cohort.gui.scripter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartPanel;

import edu.wehi.celcalc.cohort.PackageScripterUtilities;
import edu.wehi.celcalc.cohort.application.Workspacefiles;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.MeasurementQuery;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.graphplot.plot.GPPlotterFuncs;
import edu.wehi.graphplot.plot.GPXYSeries;
import edu.wehi.swing.CursorController;
import edu.wehi.swing.DirectoryRestrictedFileSystemView;

public class ScripterController implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	ScripterView view = new ScripterView();
	ScripterMenu mnu = new ScripterMenu();
	ScripterModel model;
	private final String workspaceDir;
	
	public ScripterController(List<Measurement> selections)
	{
		this(new ScripterView(), new ScripterMenu(),new ScripterModel(selections), "C:/Users/futschik.d/Downloads/script");
	}
	
	public ScripterController()
	{
		this(new ScripterView(), new ScripterMenu(), new ScripterModel(),"");
	}
	
	public ScripterController(ScripterView view, ScripterMenu mnu)
	{
		this(view,mnu, new ScripterModel(), "");
	}
	
	public ScripterController(ScripterView view, ScripterMenu mnu, String workspaceDir, ScripterContainer scripterContainer)
	{
		this(view,mnu, new ScripterModel(scripterContainer), workspaceDir);
	}
	
	public void setMeasurements(List<Measurement> measurements)
	{
		model.engine.initEngineVariablesAndFunctions(measurements);
	}
	
	public ScripterController(ScripterView view, ScripterMenu mnu, ScripterModel model, String workspaceDir)
	{
		this.model = model;
		this.view = view;
		this.mnu = mnu;
		this.workspaceDir = workspaceDir;
		
		view.addKeyListener(new KeyActionHandler());
		
		ActionListener l = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				runScript();
			}
		};
		ActionListener runScriptWithWaitCursor = CursorController.createListener(view, l);
		
		KeyCodeListener editorScriptListener = new KeyCodeListener()
		{
			public void recievedKey(KeyEvent key)
			{
				if (key.getID() == KeyEvent.KEY_RELEASED && key.getKeyCode() == KeyEvent.VK_F4)
				{
					runScript();
				}
				else if(key.getKeyCode()==KeyEvent.VK_S &&
						((key.getModifiers() & KeyEvent.CTRL_MASK)==KeyEvent.CTRL_MASK))
				{
					saveCurrentScript();
				}
			}
		};
		
		view.addKeyListener(editorScriptListener);
		
		mnu.mniRunScript					.addActionListener(runScriptWithWaitCursor);
		mnu.mniNewScript					.addActionListener(e -> newScriptAction());
		mnu.mniOpenScript					.addActionListener(e -> openScriptDialog());
		mnu.mniSaveScript					.addActionListener(e -> saveCurrentScript());
		
		//mnu.mniExampleCohort				.addActionListener(e -> {});
		mnu.mniExampleCohortSum				.addActionListener(e -> openExample(PackageScripterUtilities.cohortSumExamplePY));
		mnu.mniExampleCohortSumVsMeanDiv	.addActionListener(e -> openExample(PackageScripterUtilities.cohortSumVsMeanDivisionExamplePY));
		mnu.mniMeanDiv						.addActionListener(e -> openExample(PackageScripterUtilities.meanDivisionExamplePY));
		mnu.mniExampleTemplate				.addActionListener(e -> openExample(PackageScripterUtilities.templatePy));
		
		initGUI();
	}
	
	public void saveCurrentScript()
	{
		if (view.getCurrentPane() == null)
		{
			return;
		}
		
		if (!view.getCurrentPane().isEditable())
		{
			return;
		}
		
		if (view.getCurrentScriptName() == null)
		{
			return;
		}
		
		saveScript(workspaceDir+"/"+ Workspacefiles.SCRIPT.getFileName()+"/" + view.getCurrentScriptName(), view.getCurrentCode());
	}
	
	public boolean saveScript(String scriptAbsPath, String scriptCode)
	{
		File file = new File(scriptAbsPath); 
	    try
	    {
	        BufferedWriter out = new BufferedWriter(new FileWriter(file));
	        out.write(scriptCode);
	        out.close();
	        System.out.println("Saved script to: "+scriptAbsPath);
	    }
	    catch (IOException e)
	    {
	    	e.printStackTrace();
	    	return false;
	    }
	    updateFromDir();
	    return true;
	}

	private void openScriptDialog()
	{
		System.out.println(workspaceDir);
		File cf = new File(workspaceDir+"/"+Workspacefiles.SCRIPT.getFileName());
		FileSystemView fsv = new DirectoryRestrictedFileSystemView(cf);
		JFileChooser fileChooser = new JFileChooser(fsv);
		fileChooser.setCurrentDirectory(cf);
		fileChooser.showOpenDialog(view);
		File file = fileChooser.getSelectedFile();
		if (file == null)
		{
			return;
		}
		openScript(file);
	}

	private void openScript(File file)
	{
		String name = file.getName();
		JEditorPane pane = addNewScriptPane(name, null);
		try
		{
			pane.setText(FileUtils.readFileToString(file));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(view, "Could not open file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void openExample(String scriptName)
	{
		JEditorPane pane = addNewScriptPane(scriptName);
		pane.setText(PackageScripterUtilities.getAsString(scriptName));
		pane.setEditable(false);
	}
	
	public JEditorPane addNewScriptPane(String scriptName)
	{
		return addNewScriptPane(scriptName, null);
	}
	
	public JEditorPane addNewScriptPane(String scriptName, ActionListener al)
	{
		return view.addNewScriptPane(scriptName, al);
	}

	private void runIterScript()
	{
		List<GPXYSeries> seriess = model.executeIterCode(view.getCurrentCode());
		ChartPanel panel = GPPlotterFuncs.plot(seriess, "Result of Script", "x", "y", GPChartType.LINE);
		JFrame f = new JFrame();
		f.add(panel);
		f.setSize(500, 500);
		f.setVisible(true);
	}

	public boolean newScriptAction()
	{
		String newScriptName = showNewScriptDialog();
		if (newScriptName == null)
		{
			return false;
		}
		if (!newScriptName.endsWith(".py"))
		{
			newScriptName += ".py";
		}
		
		boolean success = createNewScript(newScriptName);
		if (success == false)
		{
			return false;
		}
		ActionListener closingAction = null;
		addNewScriptPane(newScriptName, closingAction);
		return scripts.add(newScriptName);
	}

	Set<String> scripts = new HashSet<>();
	
	private boolean createNewScript(String newScriptName)
	{
		File newFile = new File(workspaceDir + "/" + Workspacefiles.SCRIPT + "/" + newScriptName);
		if(!newFile.exists())
		{
			try
			{
				return newFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				showCreateError("File Error", "Could not create new file.");
			}
		}
		else
		{
			showCreateError("File Error", "The file name specified already exists");
		}
		return false;
	}

	public String getStringInput(String title, String txt)
	{
		return (String) JOptionPane.showInputDialog(view, txt, title, JOptionPane.PLAIN_MESSAGE, null, null, "new.py");
	}
	public void showCreateError(String title, String txt) {	JOptionPane.showMessageDialog(view, txt,title, JOptionPane.ERROR_MESSAGE);}
	
	private String showNewScriptDialog()
	{
		return getStringInput("New Script", "New Script Name");
	}

	public JMenu getScriptMenu()
	{
		return mnu;
	}
	
	private void initGUI()
	{
		updateFromDir();
		mnu.setMenuItemsWithNoActionListenersInactive();
		view.setEngine(model.getEngine());
	}
	
	public void updateFromDir()
	{
		model.updateFromDir(workspaceDir);
	}
	
	public ScripterView getView()
	{
		return view;
	}
	
	class KeyActionHandler implements KeyCodeListener
	{
		@Override
		public void recievedKey(KeyEvent key)
		{
			if (key.getKeyCode() == KeyEvent.VK_F6)
			{
				runScript();
			}
		}
	}
	
	public void runScript()
	{
		System.out.println("Running Script");
		String scriptName = view.getCurrentScriptName();
		if (scriptName == null)
		{
			return;
		}
		
		if (scriptName.endsWith(".iter.py"))
		{
			runIterScript();
		}
		else if (scriptName.endsWith(".console.py"))
		{
			if (MeasurementQuery.allTreatments(model.getEngine().getMeasurements()).size()>1)
			{
				int result = JOptionPane.showConfirmDialog(
						view,
						"More than one treatment was detected, note that you cannot cell count functions.",
						"WARNING",
						JOptionPane.WARNING_MESSAGE);
				if (result == 0)
				{
					model.executeCode(view.getCurrentCode());
				}
			}
			else
			{
				model.executeCode(view.getCurrentCode());
			}
		}
		else
		{
			runIterScript();
		}
	}
	
	public boolean saveAllScripts()
	{
		Collection<String> names = view.getAllOpenTabNames();
		String currScript = view.getCurrentScriptName();
		for (String name : names)
		{
			if (name.endsWith(PackageScripterUtilities.scriptExt))
			{
				continue;
			}
			else
			{
				view.setSelectedScript(name);
				saveCurrentScript();
			}
		}
		view.setSelectedScript(currScript);
		return true;
	}
	
	public void kill()
	{
		model.kill();
		view.kill();
		this.view = null;
		this.model = null;
	}
	
	public static void main(String[] args)
	{
		ScripterController controller = new ScripterController();
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
