package de.tisan.church.untertitelinator.gui.main;

import de.tisan.church.untertitelinator.instancer.UTEventHub;
import de.tisan.church.untertitelinator.instancer.UTEventListener;
import de.tisan.church.untertitelinator.instancer.packets.Command;
import de.tisan.church.untertitelinator.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.instancer.packets.Packet;
import de.tisan.church.untertitelinator.instancer.packets.SongLinePacket;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.Anchor;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ficon.FlatIcon;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;
import de.tisan.flatui.helpers.resizehelpers.SwingResizeHelper;

import java.awt.*;

public class GUIMainOverview extends AGUIMainPanel
{
	private static final long serialVersionUID = 8492314016575322257L;

	public GUIMainOverview(FlatLayoutManager man, GUIMain instance, Dimension preferredSize)
	{
		super(man, instance, preferredSize);
		GUIMainControllerPanel pnlController = new GUIMainControllerPanel(man, instance, new Dimension(625, 50));
		pnlController.setLocation(290, 0);
		add(pnlController);

		GUIMainSongTextPanel pnlSongtext = new GUIMainSongTextPanel(man, instance, new Dimension(625, 400));
		pnlSongtext.setLocation(290, 60);
		add(pnlSongtext);

		GUIMainSongListPanel pnlSongList = new GUIMainSongListPanel(man, instance, new Dimension(265, 500));
		pnlSongList.setLocation(10, 60);
		add(pnlSongList);

		GUIMainKeyerPanel pnlKeyer = new GUIMainKeyerPanel(man, instance, new Dimension(625, 110));
		pnlKeyer.setLocation(pnlSongList.getX() + pnlSongList.getWidth() + 10,
				pnlSongtext.getY() + pnlSongtext.getHeight() + 15);
		add(pnlKeyer);

		GUIStartEndCardsPanel pnlStartEnd = new GUIStartEndCardsPanel(man, instance, new Dimension(200, 585));
		pnlStartEnd.setLocation(10, 0);
		add(pnlStartEnd);

		GUIMainConnectionStatePanel pnlConnectionState = new GUIMainConnectionStatePanel(man, instance, new Dimension(625, 50));
		pnlConnectionState.setLocation(10, pnlStartEnd.getY() + pnlStartEnd.getHeight() + 5);
		add(pnlConnectionState);
	}

}
