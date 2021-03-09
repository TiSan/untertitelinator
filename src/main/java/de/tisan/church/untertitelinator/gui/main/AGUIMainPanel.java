package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;

import javax.swing.JPanel;

import de.tisan.flatui.components.fcommons.FlatLayoutManager;

public abstract class AGUIMainPanel extends JPanel
{
	FlatLayoutManager man;
	GUIMain instance;
	Dimension size;

	public AGUIMainPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize)
	{
		this.man = man;
		this.instance = instance;
		this.size = preferredSize;
		setSize(preferredSize);
		setLayout(null);
	}

	public abstract void updateThisComponent();
}
