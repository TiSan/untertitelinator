package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ffont.FlatFont;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;

public class GUIMainKeyerPanel extends AGUIMainPanel
{
	private static final long serialVersionUID = 5033708428105225125L;

	public GUIMainKeyerPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize)
	{
		super(man, instance, preferredSize);
		int x = 0;
		int y = 0;
		int widthBtn = 100;
		int heightBtn = 70;

		JLabel lblViewHideElements = new JLabel("Ebenen im Keyer ein-/ausblenden [Unterste --> Oberste]");
		lblViewHideElements.setFont(FlatFont.getInstance(18, Font.PLAIN));
		lblViewHideElements.setForeground(FlatColors.WHITE);
		lblViewHideElements.setBounds(x, y, 550, 30);
		add(lblViewHideElements);

		y = lblViewHideElements.getY() + lblViewHideElements.getHeight() + 10;

		FlatButton btnUntertitel = new FlatButton("Untertitel", man);
		btnUntertitel.setBounds(x, y, widthBtn, heightBtn);
		btnUntertitel.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
		btnUntertitel.disableEffects();
		btnUntertitel.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleUntertitel();
				btnUntertitel.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnUntertitel);

		x += widthBtn + 5;

		FlatButton btnKollekte = new FlatButton("Kollekte", man);
		btnKollekte.setBounds(x, y, widthBtn, heightBtn);
		btnKollekte.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
		btnKollekte.disableEffects();
		btnKollekte.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleKollekte();
				btnKollekte.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnKollekte);

		x += widthBtn + 5;

		FlatButton btnEndcard = new FlatButton("Endcard", man);
		btnEndcard.setBounds(x, y, widthBtn, heightBtn);
		btnEndcard.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
		btnEndcard.disableEffects();
		btnEndcard.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleEndcard();
				btnEndcard.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnEndcard);

		x += widthBtn + 5;

		FlatButton btnTitelfolie = new FlatButton("Begincard", man);
		btnTitelfolie.setBounds(x, y, widthBtn, heightBtn);
		btnTitelfolie.setBackground(FlatColors.GREEN);
		btnTitelfolie.disableEffects();
		btnTitelfolie.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleBeginLayer();
				btnTitelfolie.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnTitelfolie);

		x += widthBtn + 5;

		FlatButton btnLogo = new FlatButton("Logo", man);
		btnLogo.setBounds(x, y, widthBtn, heightBtn);
		btnLogo.setBackground(FlatColors.GREEN);
		btnLogo.disableEffects();
		btnLogo.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleLogo();
				btnLogo.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnLogo);

		x += widthBtn + 5;

		FlatButton btnWindowBar = new FlatButton("Max-Button", man);
		btnWindowBar.setBounds(x, y, widthBtn, heightBtn);
		btnWindowBar.setBackground(FlatColors.GREEN);
		btnWindowBar.disableEffects();
		btnWindowBar.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				boolean state = GUIKeyer.get().toggleWindowBar();
				btnWindowBar.setBackground(state ? FlatColors.GREEN : FlatColors.HIGHLIGHTBACKGROUND, true);
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnWindowBar);
	}

	@Override
	public void updateThisComponent()
	{
		
	}
}
