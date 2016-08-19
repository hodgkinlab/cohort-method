package edu.wehi.celcalc.cohort.gui.application;

import java.awt.Component;

import javax.swing.JFileChooser;

import edu.wehi.celcalc.cohort.ApplicationPreferences;
import edu.wehi.celcalc.cohort.main.CohortMain;

public class OpenWorkspaceController
{
	final OpenWorkspaceView view = new OpenWorkspaceView();
	RecentStrings recentWorkspaces = new RecentStrings();
	
	public OpenWorkspaceController()
	{
		view.btnCancel							.addActionListener(e -> System.exit(0));
		view.btnBrowse							.addActionListener(e -> showBrowseDialog());
		view.btnOk								.addActionListener(e -> startApplication());
		view.comboRecentWorkspaces				.addActionListener(e -> comboSelected());
		view.chkUseDefault						.addActionListener(e -> syncDefaults());
	}
	
	private void comboSelected()
	{
		recentWorkspaces.setMostRecentString((String)view.comboRecentWorkspaces.getSelectedItem());
	}
	
	public void syncDefaults()
	{
		ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.openDefaultPref, view.chkUseDefault.isSelected());
	}

	public boolean isOpeningDefault()
	{
		return ApplicationPreferences.prefs.getBoolean(ApplicationPreferences.openDefaultPref, false);
	}
	

	private void startApplication()
	{
		setVisble(false);
	}

	private void showBrowseDialog()
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(view);
		System.out.println(fc.getSelectedFile());
		recentWorkspaces.setMostRecentString(fc.getSelectedFile().toString());
		syncComboBoxValues();
	}
	
	public void initController()
	{
		view.initGUI();
		syncComboBoxValues();
		view.chkUseDefault.setSelected(isOpeningDefault());
	}
	
	public void syncComboBoxValues()
	{
		view.setComboBoxRecentWorkspacesValues(recentWorkspaces.getAllRecentStrings());
	}
	
	public static void main(String[] args)
	{
		CohortMain.setLookAndFeel();
		OpenWorkspaceController controller = new OpenWorkspaceController();		
		controller.view.setVisible(true);
		controller.initController();
	}

	public String getSelectedWorkspace()
	{
		syncDefaults();
		return recentWorkspaces.getMostRecentString();
	}

	public void dispose()
	{
		//view.dispose();
	}

	public void removeWorkspace(String selectedWorkspace)
	{
		recentWorkspaces.removeRecentString(selectedWorkspace);
	}

	public void setVisble(boolean b)
	{
		view.setVisible(b);
	}

	public void isOpeningDefault(boolean b)
	{
		ApplicationPreferences.prefs.putBoolean(ApplicationPreferences.openDefaultPref, false);
	}

	public String getMostRecentWorkspace()
	{
		return recentWorkspaces.getMostRecentString();
	}

	public Component getView()
	{
		return view;
	}
	
}
