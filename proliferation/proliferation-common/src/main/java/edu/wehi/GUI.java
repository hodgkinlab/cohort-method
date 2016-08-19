package edu.wehi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;

public class GUI
{
	
	private static JFrame makeDefaultJFrame()
	{
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		return frame;
	}
	
	public static void guimax(JComponent comp)
	{
		JFrame frame = makeDefaultJFrame();
		frame.add(comp, BorderLayout.CENTER);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		guinof(frame);
	}

	public static void gui(Component component)
	{
		JFrame frame = makeDefaultJFrame();
		frame.add(component, BorderLayout.CENTER);
		guinof(frame);
	}
	
	public static JFrame guinox(Component comp)
	{
		JFrame frame = makeDefaultJFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(comp, BorderLayout.CENTER);
		guinofnoex(frame);
		return frame;
	}
	
	public static void guinof(JFrame frame)
	{
		frame.setSize(500,500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setAlwaysOnTop(true);
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
	
	public static void guinofnoex(JFrame frame)
	{
		frame.setSize(500,500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
	
	public static void scroll(JComponent comp)
	{
		JPanel pnl = new JPanel();
		pnl.setSize(400,400);
		pnl.setLayout(new BorderLayout());
		pnl.add(comp, BorderLayout.CENTER);
		gui(pnl);
	}
	
	public static void main(String[] args)
	{
		gui(new JButton("Test"));
	}

	public static void bar(JMenuBar mainJMenuBar) {
		JFrame frame = makeDefaultJFrame();
		frame.setJMenuBar(mainJMenuBar);
		javax.swing.SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				frame.setVisible(true);
			}
			
		});
	}
	
    public static SingleCDockable createDockable(Component component, String title)
    {
        DefaultSingleCDockable dockable = new DefaultSingleCDockable(title, title, component );
        dockable.setCloseable(true);
        return dockable;
    }
	
    

    
    public static void quickSquareDockingComponent(List<SingleCDockable> comps)
	{
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        CControl control = new CControl(frame);
        
        mainPanel.add( control.getContentArea() );
        frame.add(mainPanel);
        CGrid grid = new CGrid( control );
		
		int noRows =  (int) Math.ceil(Math.sqrt((double)comps.size()));
		if (noRows <= 1)
		{
			noRows=2;
		}
		
		int row = 0;
		int col = 0;
		for (SingleCDockable component : comps)
		{
			grid.add(col, row, 1, 1, component);
			row +=1;
			if (row == noRows)
			{
				row = 0;
				col+=1;
			}
		}
		
		control.getContentArea().deploy( grid );
		
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

    
    public static Component squareDockingComponent(List<SingleCDockable> comps, JFrame frame)
	{
        JPanel mainPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        CControl control = new CControl(frame);
        
        mainPanel.add( control.getContentArea() );
        frame.add(mainPanel);
        CGrid grid = new CGrid( control );
		
		int noRows =  (int) Math.ceil(Math.sqrt((double)comps.size()));
		if (noRows <= 1)
		{
			noRows=2;
		}
		
		int row = 0;
		int col = 0;
		for (SingleCDockable component : comps)
		{
			grid.add(col, row, 1, 1, component);
			row +=1;
			if (row == noRows)
			{
				row = 0;
				col+=1;
			}
		}
		control.getContentArea().deploy( grid );
		return mainPanel;
	}
	

}
