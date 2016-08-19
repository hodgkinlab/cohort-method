package edu.wehi.swing.tabbed;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class TabFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ClosableTabbedPane tabbedPane;

	public TabFrame() {
		addTabbedPane();
		addMenu();

		setSize(500, 400);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void addMenu() {
		// Create menu for adding tabs
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuItem = new JMenuItem("Add Tab");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.addTab("TAB " + (tabbedPane.getTabCount() + 1),
						new JPanel());
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	private void addTabbedPane() {
		// Create ClosableTabbedPane and override the tabAboutToClose
		// to be notified when certain tab is going to close.
		tabbedPane = new ClosableTabbedPane() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean tabAboutToClose(int tabIndex)
			{
				String tab = tabbedPane.getTabTitleAt(tabIndex);
				int choice = JOptionPane.showConfirmDialog(null,
						"You are about to close '" + tab
								+ "'\nDo you want to proceed ?",
						"Confirmation Dialog", JOptionPane.INFORMATION_MESSAGE);
				return choice == 0; // if returned false tab closing will be					// canceled
			}
		};
		getContentPane().add(tabbedPane);
	}

	public static void main(String[] args) {
		new TabFrame();
	}

}
