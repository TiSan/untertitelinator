package de.tisan.church.untertitelinator.gui.main;

import java.awt.Color;
import java.awt.Dimension;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.GUIKeyerLayerChangePacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
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
		btnBeginLayer.disableEffects();
		btnBeginLayer.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_BEGIN_LAYER));
			}
		});
		add(btnBeginLayer);

		y += preferredSize.height - heightBtn -20;

		btnEndcard = new FlatButton("Endcard", man);
		btnEndcard.setBounds(x, y, widthBtn, heightBtn +20);
		btnEndcard.disableEffects();
		btnEndcard.addMouseListener(Priority.NORMAL, new MouseListenerImpl() {
			@Override
			public void onMouseRelease(MouseReleaseHandler handler) {
				UTEventHub.get().publish(new CommandPacket(Command.TOGGLE_ENDCARD));
			}
		});
		add(btnEndcard);

		x += widthBtn + 5;

		updateThisComponent();
		
		UTEventHub.get().registerListener(new UTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (packet instanceof GUIKeyerLayerChangePacket) {
					GUIKeyerLayerChangePacket bPacket = (GUIKeyerLayerChangePacket) packet;
					switch (bPacket.getLayerName()) {
					case ENDCARD:
						btnEndcard.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					case BEGINLAYER:
						btnBeginLayer.setBackground(bPacket.isVisible() ? btnActiveColor : btnInactiveColor, true);
						break;
					default:
						break;
					} 
				} 
			}
		});
	}

	@Override
	public void updateThisComponent() {
	}
}
