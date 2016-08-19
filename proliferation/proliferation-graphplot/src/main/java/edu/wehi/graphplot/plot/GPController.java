package edu.wehi.graphplot.plot;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import edu.wehi.swing.CheckBoxList;

public class GPController {
	
	
	private final GPPlotModel model;
	public final GPView view;
	public final JComponent plot;
	public final JList<String> jlist;
	public final JCheckBox[] options;
	public final Map<GPXYSeries, JCheckBox> seriesAndCheckBox = new HashMap<>();

	public GPController(String title, String xaxis, String yaxis, List<GPXYSeries> seriess, GPChartType type)
	{
		this.model = new GPPlotModel(title, seriess);
		view = new GPView();
		plot = GPPlotterFuncs.plot(model.getSeries(),model.getCollection(), title, xaxis, yaxis, type, 0.0);
		plot.addMouseListener(new PopUpAction());
		view.add(plot, BorderLayout.CENTER);
		
		options = createJCheckBox();
		jlist = createPopUpMenu(options);
	}
	
	
	
	JCheckBox[] createJCheckBox()
	{
		Set<GPXYSeries> set = model.map.keySet();
		final int size = set.size();
		JCheckBox[] options = new JCheckBox[size];
		int i = 0;
		List<GPXYSeries> sorted_list = new ArrayList<>(set);
		sorted_list.sort(new Comparator<GPXYSeries>()
		{

			@Override
			public int compare(GPXYSeries o1, GPXYSeries o2)
			{
				return o1.name.compareTo(o2.name);
			}
		});
		
		for (GPXYSeries s : sorted_list)
		{
			JCheckBox jbox = new JCheckBox(s.name);
			jbox.addItemListener(new BoxAction(jbox, s.id));
			options[i] = jbox;
			seriesAndCheckBox.put(s, jbox);
			i++;
		}
		return options;
	}

	JList<String> createPopUpMenu(JCheckBox[] options) 
	{
		CheckBoxList<String> list = new CheckBoxList<String>(options);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return list;
	}
	
	private void adjustCheckbox()
	{
		for (GPXYSeries s : model.map.keySet())
		{
			JCheckBox box = seriesAndCheckBox.get(s);
			if (!model.isHidden(s) && (box.isSelected() != false))
			{
				box.setSelected(false);
			}
			else if (model.isHidden(s)  && (box.isSelected() != true ))
			{
				box.setSelected(true);
			}
		}
	}
	
	public void hidAllSeries()
	{
		model.hideAllSeries();
	}

	public GPView getView()
	{
		return view;
	}
	
	public class PopUpAction extends MouseAdapter
	{

		public void mouseClicked(MouseEvent event)
		{
			if (event.getClickCount() == 2)
			{
				doPopup(event);
			}
		}

		private void doPopup(MouseEvent e)
		{
			final JPopupMenu popupMenu = new JPopupMenu();
			popupMenu.add(new JScrollPane(jlist));
			adjustCheckbox();
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public class BoxAction implements ItemListener
	{
		final JCheckBox box;
		final long uuid;
		
		public BoxAction(JCheckBox box, long uuid)
		{
			this.box = box;
			this.uuid = uuid;
		}

		@Override
		public void itemStateChanged(ItemEvent arg0)
		{
			if (box.isSelected())
			{
				model.showSeries(uuid);
			}
			else
			{
				model.hideSeries(uuid);
			}
		}
	}
	
}
