package de.tisan.church.untertitelinator.gui.main;

import java.awt.Dimension;
import java.lang.reflect.Field;

import de.tisan.church.untertitelinator.data.Untertitelinator;
import de.tisan.church.untertitelinator.main.Loader;
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
				jumpToStart();
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
				previousLine();
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
				pause();
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
				nextLine();

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
				jumpToEnd();
			}
		});

		add(btnEnd);

	}

	void jumpToStart()
	{
		Untertitelinator.get().getCurrentPlayer().jumpToStart();
		Loader.getMainUi().updateUIComponents();
	}

	void jumpToEnd()
	{
		Untertitelinator.get().getCurrentPlayer().jumpToEnd();
		Loader.getMainUi().updateUIComponents();
	}

	public void nextLine()
	{
		Untertitelinator.get().getCurrentPlayer().nextLine();
		Loader.getMainUi().updateUIComponents();
	}

	public void previousLine()
	{
		Untertitelinator.get().getCurrentPlayer().previousLine();
		Loader.getMainUi().updateUIComponents();
	}

	public void pause()
	{
		Untertitelinator.get().getCurrentPlayer().pause();
		Loader.getMainUi().updateUIComponents();
	}

	@Override
	public void updateThisComponent()
	{
		if (Untertitelinator.get().getCurrentPlayer().isPaused())
		{
			btnPause.setBackground(FlatColors.GREEN);
			try
			{
				Field icon = btnPause.getClass().getDeclaredField("icon");
				icon.setAccessible(true);
				icon.set(btnPause, FlatIcon.PLAY);
			}
			catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			btnPause.setBackground(FlatColors.ALIZARINRED);
			try
			{
				Field icon = btnPause.getClass().getDeclaredField("icon");
				icon.setAccessible(true);
				icon.set(btnPause, FlatIcon.PAUSE);
			}
			catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}
}
