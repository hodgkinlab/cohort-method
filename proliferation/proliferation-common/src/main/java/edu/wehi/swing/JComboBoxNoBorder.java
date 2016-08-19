package edu.wehi.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import edu.wehi.GUI;

public class JComboBoxNoBorder<T> extends JComboBox<T>
{
	private static final long serialVersionUID = 1L;
	
	public JComboBoxNoBorder()
	{
		super();
		configure();
	}
	
	public JComboBoxNoBorder(T[] input)
	{
		super(input);
		configure();
	}
	
	public JComboBoxNoBorder(T[] input, IconListRenderer<T> renderer)
	{
		super(input);
		setRenderer(renderer);
		configure();
	}
	
	public JComboBoxNoBorder(ComboBoxModel<T> model)
	{
		super(model);
		configure();
		setBackground(Color.BLACK);
	}
	
	public JComboBoxNoBorder(Vector<T> vector)
	{
		super(vector);
		configure();
	}
	
	private void configure()
	{
		setUI(new MetalComboBoxUI()
		{
			@Override
			public void paintCurrentValueBackground(Graphics g,	Rectangle bounds, boolean hasFocus)
			{
				if (MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme)
				{
				}
				else if (g == null || bounds == null)
				{
					throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
				}
			}
		});
		
		setMaximumSize(new Dimension(300, 200));
	}
	
	public static void main(String[] args)
	{
		GUI.gui(new JComboBoxNoBorder<String>(new String[]{"h3llo", "dsfs", "dsf"}));
	}

}
