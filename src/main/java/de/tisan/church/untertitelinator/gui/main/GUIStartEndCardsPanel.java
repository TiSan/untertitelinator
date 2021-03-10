package de.tisan.church.untertitelinator.gui.main;

import java.awt.Color;
import java.awt.Dimension;

import de.tisan.church.untertitelinator.gui.keyer.GUIKeyer;
import de.tisan.church.untertitelinator.main.Loader;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;

public class GUIStartEndCardsPanel extends AGUIMainPanel {
	private static final long serialVersionUID = 4824334836357981327L;
	
	Color btnActiveColor = FlatColors.ALIZARINRED;
	Color btnInactiveColor = FlatColors.HIGHLIGHTBACKGROUND;

	private FlatButton btnEndcard;

	private FlatButton btnBeginLayer;

	public GUIStartEndCardsPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize) {
		super(man, instance, preferredSize);
		int x = 0;
		int y = 0;
		int widthBtn = preferredSize.width;
		int heightBtn = 50;

		btnBeginLayer = new FlatButton("Begincard", man);
		btnBeginLayer.setBounds(x, y, widthBtn, heightBtn);
		btnBeginLayer.setBackground(FlatColors.GREEN);
		btnBeginLayer.disableEffects();
		btnBeginLayer.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				GUIKeyer.get().toggleBeginLayer();
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnBeginLayer);

		y += preferredSize.height - heightBtn -20;

		btnEndcard = new FlatButton("Endcard", man);
		btnEndcard.setBounds(x, y, widthBtn, heightBtn +20);
		btnEndcard.setBackground(FlatColors.HIGHLIGHTBACKGROUND);
		btnEndcard.disableEffects();
		btnEndcard.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				GUIKeyer.get().toggleEndcard();
				Loader.getMainUi().updateUIComponents();
			}
		});
		add(btnEndcard);

		x += widthBtn + 5;

	}

	@Override
	public void updateThisComponent() {
		// TODO Auto-generated method stub

		if (GUIKeyer.get().isEndcardVisible()) {
			btnEndcard.setBackground(btnActiveColor, true);
		} else {
			btnEndcard.setBackground(btnInactiveColor, true);
		}

		if (GUIKeyer.get().isBeginLayerVisible()) {
			btnBeginLayer.setBackground(btnActiveColor, true);
		} else {
			btnBeginLayer.setBackground(btnInactiveColor, true);
		}
		
	}
}
