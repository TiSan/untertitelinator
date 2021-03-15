package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Command;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.flatui.components.fbutton.FlatButton;
import de.tisan.flatui.components.fcommons.FlatColors;
import de.tisan.flatui.components.fcommons.FlatLayoutManager;
import de.tisan.flatui.components.ficon.FlatIcon;
import de.tisan.flatui.components.flisteners.MouseListenerImpl;
import de.tisan.flatui.components.flisteners.MouseReleaseHandler;
import de.tisan.flatui.components.flisteners.Priority;

public class GUIMainControllerPanel extends AGUIMainPanel
{
	private static final long serialVersionUID = 8492314016575322257L;
	private FlatButton btnStart;
	private FlatButton btnBack;
	private FlatButton btnPause;
	private FlatButton btnForward;
	private FlatButton btnEnd;

	public GUIMainControllerPanel(FlatLayoutManager man, GUIMain instance, Dimension preferredSize)
	{
		super(man, instance, preferredSize);
		int x = 0;
		int y = 0;
		int widthBtn = 121;
		int heightBtn = preferredSize.height;

		btnStart = new FlatButton(null, FlatIcon.FAST_BACKWARD, man);
		btnStart.setBounds(x, y, widthBtn, heightBtn);
		btnStart.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				CTEventHub.get().publish(new CommandPacket(Command.JUMP_START));
			}
		});

		add(btnStart);

		x += widthBtn + 5;

		btnBack = new FlatButton(null, FlatIcon.BACKWARD, man);
		btnBack.setBounds(x, y, widthBtn, heightBtn);

		btnBack.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				CTEventHub.get().publish(new CommandPacket(Command.PREVIOUS_LINE));
			}
		});

		add(btnBack);

		x += widthBtn + 5;

		btnPause = new FlatButton(null, FlatIcon.PAUSE, man);
		btnPause.setBackground(FlatColors.ALIZARINRED);
		btnPause.setBounds(x, y, widthBtn, heightBtn);
		btnPause.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				CTEventHub.get().publish(new CommandPacket(Command.PAUSE));
			}
		});

		add(btnPause);

		x += widthBtn + 5;

		btnForward = new FlatButton(null, FlatIcon.FORWARD, man);
		btnForward.setBounds(x, y, widthBtn, heightBtn);
		btnForward.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{
			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				CTEventHub.get().publish(new CommandPacket(Command.NEXT_LINE));

			}
		});
		add(btnForward);

		x += widthBtn + 5;

		btnEnd = new FlatButton(null, FlatIcon.FAST_FORWARD, man);
		btnEnd.setBounds(x, y, widthBtn, heightBtn);
		btnEnd.addMouseListener(Priority.NORMAL, new MouseListenerImpl()
		{

			@Override
			public void onMouseRelease(MouseReleaseHandler handler)
			{
				CTEventHub.get().publish(new CommandPacket(Command.JUMP_END));
			}
		});

		add(btnEnd);

		CTEventHub.get().registerListener(new CTEventListener()
		{

			@Override
			public void onEventReceived(Packet packet)
			{
				if (packet instanceof SongLinePacket)
				{
					SongLinePacket sPacket = (SongLinePacket) packet;
					if (sPacket.getSongPlayer().isPaused())
					{
						btnPause.setBackground(FlatColors.GREEN);
						btnPause.setIcon(FlatIcon.PLAY);
					}
					else
					{
						btnPause.setBackground(FlatColors.ALIZARINRED);
						btnPause.setIcon(FlatIcon.PAUSE);
					}
				}
			}
		});
	}

	@Override
	public void updateThisComponent()
	{
	}
}
