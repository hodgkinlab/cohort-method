package edu.wehi.celcalc.cohort.data.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wehi.GUI;
import edu.wehi.swing.GBHelper;

public class MeasurementSelectionsTableView extends JPanel{

	private static final long serialVersionUID = 1L;
	
	HashMap<String, JCheckBox> boxs = new HashMap<String, JCheckBox>();
	
	public MeasurementSelectionsTableView(String[] collections)
	{
		this(new ArrayList<String>(Arrays.asList(collections)));
	}
	
	public MeasurementSelectionsTableView(Collection<String> collections)
	{
		
		setLayout(new BorderLayout());
		
		final JPanel mainPanel = new JPanel();
		JScrollPane scroll = new JScrollPane(mainPanel);
		add(scroll, BorderLayout.CENTER);
		
		mainPanel.setLayout(new GridBagLayout());
		GBHelper gb = new GBHelper();
		
		for (String txt : collections)
		{
			if (boxs.containsKey(txt))
			{
				throw new RuntimeException("Error key wasn't unique");
			}
			JCheckBox box = new JCheckBox(txt);
			mainPanel.add(box, gb.expandW().nextRow());
			boxs.put(txt, box);
		}
	}
	
	public List<String> getSelection()
	{
		List<String> selected = new ArrayList<>();
		for (JCheckBox box : boxs.values())
		{
			if (box.isSelected())
			{
				selected.add(box.getText());
			}
		}
		return selected;
	}

	
	public void setAllSelected(boolean isSelected)
	{
		boxs.values().stream().forEach(b -> b.setSelected(isSelected));
	}

	public static void main(String[] args) {
		GUI.gui(new MeasurementSelectionsTableView(new String[]{"ad","dsfsd"}));
	}

}
