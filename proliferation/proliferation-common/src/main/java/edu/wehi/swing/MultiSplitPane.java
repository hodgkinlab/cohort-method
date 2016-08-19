package edu.wehi.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MultiSplitPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private int orientation;
	private Boolean needSplit;
	private Boolean splited;
	private Component firstChild;
	private MultiSplitPane private_second_child;
	private ArrayList<Component> components;

	public MultiSplitPane(int newOrientation) {
		this.orientation = newOrientation;
		this.needSplit = false;
		this.splited = false;
		this.components = new ArrayList<Component>();
	}

	public Component addComp(Component comp) {
		this.components.add(comp);

		if (this.splited) {
			private_second_child.addComp(comp);
		} else if (this.needSplit) {
			// Remove first child.
			this.remove(this.firstChild);
			// Setup SplitPane
			this.setLayout(new BorderLayout());
			JSplitPane splitPane = new JSplitPane(this.orientation);
			this.add(splitPane, BorderLayout.CENTER);
			splitPane.setLeftComponent(this.firstChild);
			this.private_second_child = new MultiSplitPane(this.orientation);
			this.private_second_child.addComp(comp);
			splitPane.setRightComponent(this.private_second_child);
			this.splited = true;
		} else {
			// First child
			this.firstChild = comp;
			this.setLayout(new BorderLayout());
			this.add(comp, BorderLayout.CENTER);
			// Set flag.
			this.needSplit = true;
		}
		// Validate to redisplay components.
		this.validate();
		return comp;
	}

	public ArrayList<Component> getComps() {
		return this.components;
	}
}
