package edu.wehi.celcalc.cohort.gui.scripter;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import jsyntaxpane.DefaultSyntaxKit;

import org.python.util.InteractiveInterpreter;

import console.JConsole;
import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.PackageScripterUtilities;
import edu.wehi.swing.tabbed.ClosableTabbedPane;


public class ScripterView extends JSplitPane 
{
	private static final long serialVersionUID = 1L;
	
	static
	{
		DefaultSyntaxKit.initKit();	
	}
    
	public ScripterView()
	{
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		setTopComponent(getTabbedEditorPnl());
		setBottomComponent(getJConsole());
	}
	
	public void initGui()
	{
		codeTemplateEditor.setText(PackageScripterUtilities.getTemplate());
		setDividerLocation(500);
		revalidate();
		repaint();
	}

	public JEditorPane getCodeEditor()
	{
		return codeTemplateEditor;
	}
	
	ClosableTabbedPane tabbed = null;
	public Component getTabbedEditorPnl()
	{
		if (tabbed == null)
		{
			tabbed = new ClosableTabbedPane()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean tabAboutToClose(int tabIndex)
				{
					String tab = getTabTitleAt(tabIndex);
					
					int choice = JOptionPane.showConfirmDialog(
							null,
							"You are about to close '" + tab + "'\nDo you want to proceed ?",
							"Confirmation Dialog",
							JOptionPane.INFORMATION_MESSAGE);
					
					if (choice == 0)
					{
						mapScriptAndText.remove(tab);
						mapNameAndComponent.remove(tab);
					}
					
					return choice == 0; // if returned false tab closing will be canceled
				}
				
			};
		}
		return tabbed;
	}
	
	JEditorPane codeTemplateEditor = new JEditorPane();
	JScrollPane scrTemplatePane = null;

	JConsole jconsole = null;
	private Component getJConsole()
	{
		if (jconsole == null)
		{
			jconsole = new JConsole();
		}
		return new JScrollPane(jconsole);
	}

	public static void main(String[] args)
	{
		ScripterView view = new ScripterView();
		view.initGui();
		GUI.gui(view);
	}

	public void setEngine(InteractiveInterpreter engine)
	{
		jconsole.setEngine(engine);
	}
	
	Set<KeyCodeListener> keyListeners = new HashSet<>();
	public void addKeyListener(KeyCodeListener l)
	{
		keyListeners.add(l);
	}
	
	public void removeKeyListener(KeyListener l)
	{
		keyListeners.remove(l);
	}

	public String getSyntaxPaneText()
	{
		return codeTemplateEditor.getText();
	}

	public void kill()
	{
		KeyListener[] ls = getKeyListeners();
		for (int i=0; i<ls.length; i++)
		{
			removeKeyListener(ls[i]);
		}
		removeAll();
	}

	
	Map<String, JEditorPane> mapScriptAndText = new HashMap<>();
	Map<String, Component> mapNameAndComponent = new HashMap<>();
	
	public JEditorPane addNewScriptPane(String newScriptName, ActionListener closingAction)
	{
		if (mapScriptAndText.containsKey(newScriptName))
		{
			tabbed.setSelectedComponent(mapNameAndComponent.get(newScriptName));
			return null;
		}
		
		JEditorPane editorPane = new JEditorPane();
		JScrollPane sp = new JScrollPane(editorPane);
		
		editorPane.addKeyListener(new KeyAdapter()
    	{
			@Override
			public void keyReleased(KeyEvent ev)
			{
				keyListeners.forEach(l -> l.recievedKey(ev));
			}		
		});
		
		editorPane.setContentType("text/python");
		mapScriptAndText.put(newScriptName, editorPane);
		mapNameAndComponent.put(newScriptName, sp);
		tabbed.add(newScriptName, sp);
		tabbed.setSelectedComponent(sp);
		return editorPane;
	}

	public String getCurrentCode()
	{
		return getCurrentPane().getText();
	}
	
	public JEditorPane getCurrentPane()
	{
		int tabIndex = tabbed.getSelectedIndex();
		if (tabIndex == -1)
		{
			return null;
		}
		String tabTitle = tabbed.getTabTitleAt(tabIndex);
		return mapScriptAndText.get(tabTitle);
	}
	
	public Collection<String> getAllOpenTabNames()
	{
		return mapScriptAndText.keySet();
	}

	public String getCurrentScriptName()
	{
		int tabIndex = tabbed.getSelectedIndex();
		if (tabIndex == -1)
		{
			return null;
		}
		return tabbed.getTabTitleAt(tabIndex);
	}

	public void setSelectedScript(String name)
	{
		if (name == null)
		{
			return;
		}
		tabbed.setSelectedComponent(mapNameAndComponent.get(name));
	}

}
