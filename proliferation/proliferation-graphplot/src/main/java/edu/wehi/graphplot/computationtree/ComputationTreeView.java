package edu.wehi.graphplot.computationtree;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.wehi.GUI;


public class ComputationTreeView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ComputationTreeView()
	{
		this.setLayout(new BorderLayout());
		JButton btn = new JButton("hello");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Hello");
				addComponent(new JButton("hello"));
				repaint();
				revalidate();
			}
		});
		add(btn, BorderLayout.EAST);
	}
	
	private void addComponent(JComponent comp)
	{
		add(comp, BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		GUI.gui(new ComputationTreeView());
	}
	

}
