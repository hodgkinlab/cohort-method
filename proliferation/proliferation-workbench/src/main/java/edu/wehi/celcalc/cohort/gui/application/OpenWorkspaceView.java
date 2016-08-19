package edu.wehi.celcalc.cohort.gui.application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class OpenWorkspaceView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	JComboBox<String> comboRecentWorkspaces = new JComboBox<>();
	JButton btnBrowse = new JButton("Browse...");
	JCheckBox chkUseDefault = new JCheckBox("Use this as the default workspace & don't ask again.");
	JButton btnOk = new JButton("Ok");
	JButton btnCancel = new JButton("Cancel");
	
	public OpenWorkspaceView()
	{
		//setTitle("Workspace Launcher");
		add(getMainPanel());
		//setIconImage(ImageUtil.getApplicationIcon());
	}
	
	JPanel pnlMain = null;
	private Component getMainPanel()
	{
		if (pnlMain == null)
		{
			pnlMain = new JPanel(new BorderLayout());
			pnlMain.add(getInfoPanel(),				BorderLayout.NORTH);
			pnlMain.add(getSelectWorkspacePanel(), 	BorderLayout.CENTER);
		}
		return pnlMain;
	}

	JPanel pnSelectWorkspace = null;
	private Component getSelectWorkspacePanel()
	{
		if (pnSelectWorkspace == null)
		{
			pnSelectWorkspace = new JPanel(new BorderLayout());
			pnSelectWorkspace.add(getBrowserControlPanel(), BorderLayout.NORTH);
			pnSelectWorkspace.add(getEmptySpacePanel(), BorderLayout.CENTER);
			pnSelectWorkspace.add(getDefaultsPanel(), BorderLayout.SOUTH);
		}
		return pnSelectWorkspace;
	}

	JPanel pnlDefault = null;
	private Component getDefaultsPanel()
	{
		if (pnlDefault == null)
		{
			pnlDefault = new JPanel(new BorderLayout());
			pnlDefault.add(chkUseDefault, BorderLayout.CENTER);
			pnlDefault.add(getOkCancelBtn(), BorderLayout.EAST);
		}
		return pnlDefault;
	}

	JPanel pnlOkCancel = null;
	private Component getOkCancelBtn()
	{
		if (pnlOkCancel == null)
		{
			pnlOkCancel = new JPanel();
			//pnlOkCancel.add(btnOk);
			//pnlOkCancel.add(btnCancel);
		}
		return pnlOkCancel;
	}

	JPanel pnlEmptySpace = null;
	private Component getEmptySpacePanel()
	{
		if (pnlEmptySpace == null)
		{
			pnlEmptySpace = new JPanel();
			pnlEmptySpace.setSize(400, 400);
		}
		return pnlEmptySpace;
	}

	JPanel pnlBroserControl = null;
	private Component getBrowserControlPanel()
	{
		if (pnlBroserControl == null)
		{
			pnlBroserControl = new JPanel(new BorderLayout());
			pnlBroserControl.add(new JLabel("Workspace: "), BorderLayout.WEST);
			pnlBroserControl.add(comboRecentWorkspaces, BorderLayout.CENTER);
			pnlBroserControl.add(btnBrowse, BorderLayout.EAST);
		}
		return pnlBroserControl;
	}

	JEditorPane jEditorPane = null;
	private Component getInfoPanel()
	{
		if (jEditorPane == null)
		{
			// create a JEditorPane
			jEditorPane = new JEditorPane();

			// make it read-only
			jEditorPane.setEditable(false);

			// add a HTMLEditorKit to the editor pane
			HTMLEditorKit kit = new HTMLEditorKit();
			jEditorPane.setEditorKit(kit);
			
			// add some styles to the html
			StyleSheet styleSheet = kit.getStyleSheet();
			styleSheet.addRule("body {color:#000; font-family:times; margin: 4px; }");
			styleSheet.addRule("h1 {color: black;}");
			styleSheet.addRule("h2 {color: black;}");
			styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");
			Document doc = kit.createDefaultDocument();
			jEditorPane.setDocument(doc);
			jEditorPane.setText("<h3> <u> Select a workspace </u></h3>"
					+ "<p>In this mode the application needs a directory to store files for a session.</p>"
					+ "<p>Choose either a new directory to open or an existing workspace.</p>");
		}
		return jEditorPane;
	}
	
	public void initGUI()
	{
		setSize(786, 331);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
	}

	public static void main(String[] args)
	{
		
		OpenWorkspaceView view = new OpenWorkspaceView();
		view.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				
				System.out.println(e.getComponent().getSize());
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		view.setVisible(true);
		view.initGUI();
	}

	public void setComboBoxRecentWorkspacesValues(List<String> all)
	{
		comboRecentWorkspaces.removeAllItems();
		all.forEach( v -> comboRecentWorkspaces.addItem(v));
		if (all.size()!=0)
		{
			comboRecentWorkspaces.setSelectedItem(all.get(0));
		}
	}

	public boolean getIsDefault()
	{
		return chkUseDefault.isSelected();
	}

}
