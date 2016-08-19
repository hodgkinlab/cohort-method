package edu.wehi.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwingColorUtil
{
	
	static final List<Color> colors = new ArrayList<Color>()
	{
		private static final long serialVersionUID = 1L;
		{
			add(Color.RED);
			add(Color.GREEN);
			add(Color.BLUE);
			add(Color.CYAN);
			add(Color.MAGENTA);
			add(Color.ORANGE);
			add(Color.BLACK);
			//add(Color.DARK_GRAY);
			//add(Color.GRAY);
			//add(Color.LIGHT_GRAY);
			//add(Color.PINK);
			//add(Color.YELLOW);
		}
	};
	
	public static int getNumColors()
	{
		return colors.size();
	}
	
	public static Color getColor(int i)
	{
		return colors.get(i);
	}
	
	public static Color randomColor(long seed)
	{
		return colors.get(new Random(seed*seed + 20).nextInt(colors.size()));
	}
	

}
