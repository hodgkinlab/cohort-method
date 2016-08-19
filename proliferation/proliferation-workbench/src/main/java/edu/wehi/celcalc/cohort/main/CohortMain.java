package edu.wehi.celcalc.cohort.main;

import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import edu.wehi.celcalc.cohort.ApplicationPreferences;
import edu.wehi.celcalc.cohort.application.WorkspaceUtilities;
import edu.wehi.celcalc.cohort.gui.application.ApplicationController;
import edu.wehi.celcalc.cohort.gui.application.OpenWorkspaceController;

public final class CohortMain
{
	
	public static enum LOADING
	{
		LOADDEFAULT,
		PICKDIR,
	}
	
	public static LOADING loading (OpenWorkspaceController controller)
	{
		if (controller.isOpeningDefault())
		{
			return LOADING.LOADDEFAULT;
		}
		else
		{
			return LOADING.PICKDIR;
		}
	}
	
	public static enum LOADDEFAULT
	{	
		SUCCESS,
		FAIL;
	}
	
	public static LOADDEFAULT loadDefault(OpenWorkspaceController controller)
	{
		try
		{
			String mostRecentWorkspace = controller.getMostRecentWorkspace();
			if (!WorkspaceUtilities.isValidWorkspace(new File(mostRecentWorkspace))) return LOADDEFAULT.FAIL;
			ApplicationController applicationController = WorkspaceUtilities.loadWorkspace(mostRecentWorkspace);
			initAndRunController(applicationController);
			System.out.println("Successfully loaded default workspace.");
			return LOADDEFAULT.SUCCESS;
		}
		catch (Exception e)
		{
			System.err.println("Exception thrown whilst loading existing workspace");
			return LOADDEFAULT.FAIL;
		}
	}
	
	public static enum PICKDIR
	{
		EXISTING,
		NEW,
		FAIL;
		String dir = null;
		public PICKDIR setDir(String dir)
		{
			this.dir = dir;
			return this;
		}
		
		public String getDir()
		{
			return dir;
		}
	}
	
	public static enum EXISTING
	{
		SUCCESS,
		FAIL
	}
	
	public static enum NEW
	{
		SUCCESS,
		FAIL
	}
	
	public static PICKDIR dirType(String path)
	{
		if (path == null)
		{
			System.out.println("The directory picked was null.");
			return PICKDIR.FAIL.setDir(path);
		}
		if (!new File(path).isDirectory())
		{
			System.out.println("The directory picked was not a directory.");
			return PICKDIR.FAIL.setDir(path);
		}
		if (WorkspaceUtilities.isValidWorkspace(new File(path)))
		{
			System.out.println("The directory picked is a valid workspace directory.");
			return PICKDIR.EXISTING.setDir(path);
		}
		System.out.println("No workspace detected in the selected directory.");
		return PICKDIR.NEW.setDir(path);
	}
	
	public static EXISTING loadExistingWorkspace(String path)
	{
		ApplicationController application = WorkspaceUtilities.loadWorkspace(path);
		if (application == null)
		{
			System.err.println("Failed to load existing workspace.");
			return EXISTING.FAIL;
		}
		else
		{
			System.out.println("Running existing workspace");
			initAndRunController(application);
			return EXISTING.SUCCESS;
		}
	}
	
	public static void killControllerAndReset(OpenWorkspaceController openWorkspaceController, String[] args)
	{
		System.out.println("Resetarting application with no default workspace.");
		openWorkspaceController.removeWorkspace(openWorkspaceController.getMostRecentWorkspace());
		openWorkspaceController.isOpeningDefault(false);
		openWorkspaceController.dispose();
		main(args);
		return;
	}
	
	public static NEW createAndInitNewDirectory(String path)
	{
		System.out.println("Attempting to initialize a new workspace.");
		ApplicationController applicationController = WorkspaceUtilities.initNewControllerWithWorkspace(path);
		if (applicationController != null)
		{
			initAndRunController(applicationController);
			System.out.println("New workspace initialize successfully.");
			return NEW.SUCCESS;
		}
		else
		{
			System.out.println("Workspace failed to initialize.");
			return NEW.FAIL;
		}
	}
	
	public static void main(String[] args) 
	{
		setLookAndFeel();
		
		boolean isLimitedMode = ApplicationPreferences.prefs.getBoolean(ApplicationPreferences.isLimitedMode, true);
		
		if (isLimitedMode)
		{
			ApplicationController con = new ApplicationController(null);
			initAndRunController(con);
			JOptionPane.showMessageDialog(con.getView(), "Limited mode - Open data by either importing an excel file via: File->Import Excel or drag and drop."+
					"\n Or open a measurements.csv file in the same format as that of the workspace. ");
			return;
		}
		
		OpenWorkspaceController openWorkspaceController = new OpenWorkspaceController();
		openWorkspaceController.initController();
		
		LOADING loading = openWorkspaceController.isOpeningDefault() ? LOADING.LOADDEFAULT : LOADING.PICKDIR; 

		switch (loading)
		{
		case LOADDEFAULT:
			System.out.println("Attempting to load the default.");
			switch(loadDefault(openWorkspaceController))
			{
			case FAIL:
				JOptionPane.showMessageDialog(null,
					    "The default workspace does not contain an existing workspace.",
					    "Default Workspace corruption.",
					    JOptionPane.ERROR_MESSAGE);
				killControllerAndReset(openWorkspaceController, args);
				return;
			case SUCCESS:
				return;
			}
			break;
		case PICKDIR:
			int response = JOptionPane.showConfirmDialog(null,openWorkspaceController.getView(),"Directory Dialog",JOptionPane.PLAIN_MESSAGE);
			if (response != JOptionPane.OK_OPTION)
			{
				System.exit(0);
			}
			String workspace = openWorkspaceController.getSelectedWorkspace();
			switch(dirType(workspace))
			{
			case EXISTING:
				switch(loadExistingWorkspace(workspace))
				{
				case FAIL:
					JOptionPane.showMessageDialog(null,
						    "The existing workspace could not be loaded.",
						    "Existing Workspace corruption.",
						    JOptionPane.ERROR_MESSAGE);
					killControllerAndReset(openWorkspaceController, args);
					break;
				case SUCCESS:
					return;
				}
				break;
			case FAIL:
				JOptionPane.showMessageDialog(null,
					    "The default workspace does not contain an existing workspace.",
					    "Default Workspace corruption.",
					    JOptionPane.ERROR_MESSAGE);
				killControllerAndReset(openWorkspaceController, args);
				return;
			case NEW:
				switch (createAndInitNewDirectory(workspace))
				{
				case FAIL:
					JOptionPane.showMessageDialog(null,
						    "The workspace has failed to initialize, you will need to try again.",
						    "Failed to create new Workspace.",
						    JOptionPane.ERROR_MESSAGE);
					killControllerAndReset(openWorkspaceController, args);
					return;
				case SUCCESS:
					return;
				}
			}
		default:
			JOptionPane.showMessageDialog(null,
				    "Failed to initialize any workspace.",
				    "Catastrophic Error!.",
				    JOptionPane.ERROR_MESSAGE);
			return;
		}
		
	}
	
	public static void initAndRunController(final ApplicationController controller)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				controller.initGUI();
				controller.getView().setExtendedState(JFrame.MAXIMIZED_BOTH);
				controller.getView().setVisible(true);
			}
		});
		
	}

	public static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		UIManager.put("ComboBox.selectionForeground", new ColorUIResource(Color.black));
	}
}
