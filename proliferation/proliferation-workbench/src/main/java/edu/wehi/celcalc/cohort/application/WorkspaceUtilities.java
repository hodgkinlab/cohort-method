package edu.wehi.celcalc.cohort.application;

import java.io.File;

import javax.swing.JOptionPane;

import edu.wehi.celcalc.cohort.gui.application.ApplicationController;

public class WorkspaceUtilities
{

	public static boolean isValidWorkspace(File workspaceDir)
	{
		boolean isValid = true;
		
		for (Workspacefiles w : Workspacefiles.getAllWorkspaceDirs())
		{
			isValid = isValid && new File(workspaceDir.getAbsolutePath()+"/"+w.getFileName()).isDirectory();
		}
		
		return isValid;
	}

	public static ApplicationController loadWorkspace(String workspaceDir)
	{
		System.out.println("Loading existing workspace.");
		
		// TODO - there may be more things to do
		return new ApplicationController(workspaceDir);
	}
	
	public static void makeDirs(String path)
	{
		File file = new File(path);
		for (Workspacefiles w : Workspacefiles.getAllWorkspaceDirs())
		{
			File dir = new File(file.getAbsolutePath()+"/"+w.getFileName());
			dir.mkdir();
		}
	}

	public static ApplicationController initNewControllerWithWorkspace(String workspaceDir)
	{
		File workspace = new File(workspaceDir);
		
		if (!workspace.isDirectory())
		{
			JOptionPane.showMessageDialog(null,
				    "The directory you have selected is not a directory.",
				    "Not a directory.",
				    JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (workspace.list().length>0)
		{
			JOptionPane.showMessageDialog(null,
				    "This directory is not empty, you must select an empty directory to initialize the workspace in.",
				    "Directory not empty.",
				    JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		makeDirs(workspaceDir);
		
		System.out.println("Initializing new workspace.");
		
		return new ApplicationController(workspaceDir);
	}

}
