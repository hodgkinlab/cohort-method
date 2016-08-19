package edu.wehi.swing;

import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Map<Object, Icon> icons = new HashMap<Object, Icon>();
 * comboBox.setRenderer(new IconListRenderer(icons));
 *
 */
public class IconListRenderer<T> extends DefaultListCellRenderer
{
	private static final long serialVersionUID = 1L;
	private Map<T, Icon> icons = null;

	public IconListRenderer(Map<T, Icon> icons)
	{
		this.icons = icons;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		// Get icon to use for the list item value
		Icon icon = icons.get(value);

		// Set icon to display for value
		label.setIcon(icon);
		label.setOpaque(false);
		return label;
	}
}