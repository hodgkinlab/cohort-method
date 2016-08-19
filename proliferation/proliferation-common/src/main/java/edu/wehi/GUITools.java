package edu.wehi;

import java.awt.Component;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GUITools
{
	public static void setMenuItemsWithNoActionListenersInactive(JMenuBar bar)
	{
		for (Component com : bar.getComponents())
		{
			setMenuItemsWithNoActionListenersInactive(com);
		}
	}
	
	public static void setMenuItemsWithNoActionListenersInactive(Component com)
	{
			if (com instanceof JMenu)
			{
				JMenu menu = (JMenu) com;
				for (int i = 0; i < menu.getItemCount(); i++)
				{
					if (menu.getItem(i) instanceof JMenu)
					{
						JMenu secondMenu = (JMenu) menu.getItem(i);
						for (int j = 0; j < secondMenu.getItemCount(); j++)
						{
							inactiveIfNoActionListener(secondMenu.getItem(j));
						}
						continue;
					}
					JMenuItem c = menu.getItem(i);
					JMenuItem itm = (JMenuItem) c;
					inactiveIfNoActionListener(itm);
				}
			}
	}
	
	private static void inactiveIfNoActionListener(JMenuItem itm)
	{
		if (!(itm instanceof JMenuItem))
		{
			return;
		}
		
		if (itm.getActionListeners().length == 0 &&
			itm.getComponentCount() == 0)
		{
			itm.setEnabled(false);
		}
	}
	
}
