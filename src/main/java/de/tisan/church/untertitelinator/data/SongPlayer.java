package de.tisan.church.untertitelinator.data;

import java.util.Arrays;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.SongLinePacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.UIRefreshPacket;
import de.tisan.church.untertitelinator.settings.UTPersistenceConstants;
import de.tisan.tools.persistencemanager.JSONPersistence;

public class SongPlayer {
	Song song;
	int index;
	boolean pause;
	private boolean enabled;

	public SongPlayer(Song song) {
		this.song = song;
		this.index = 0;
		CTEventHub.get().registerListener(new CTEventListener() {

			@Override
			public void onEventReceived(Packet packet) {
				if (enabled == true && packet instanceof CommandPacket) {
					CommandPacket cPacket = (CommandPacket) packet;
					switch (cPacket.getCommand()) {
					case JUMP_END:
						jumpToEnd();
						CTEventHub.get().publish(new UIRefreshPacket());
						break;
					case JUMP_START:
						jumpToStart();
						CTEventHub.get().publish(new UIRefreshPacket());
						break;
					case NEXT_LINE:
						nextLine();
						CTEventHub.get().publish(new UIRefreshPacket());
						break;
					case PAUSE:
						pause();
						CTEventHub.get().publish(new UIRefreshPacket());
						break;
					case PREVIOUS_LINE:
						previousLine();
						CTEventHub.get().publish(new UIRefreshPacket());
						break;
					default:
						break;
					}
				}
			}

		});
	}

	public void nextLine() {
		if (index + 1 >= song.getSongLines().size()) {
			setIndex(song.getSongLines().size() - 1);
		} else {
			setIndex(index + 1);
		}
	}

	public void previousLine() {
		if (index - 1 < 0) {
			setIndex(0);
		} else {
			setIndex(index - 1);
		}
	}

	public void jumpToStart() {
		setIndex(0);
	}

	public void jumpToEnd() {
		setIndex(song.getSongLines().size() - 1);
	}

	public boolean isOnEnd() {
		return index >= song.getSongLines().size() - 1;
	}

	public String getCurrentLine() {
		return pause ? getBlackoutLine() : song.getSongLines().get(index);
	}

	public String getNextLine() {
		return pause ? getBlackoutLine()
				: (isValidIndex(index + 1) ? song.getSongLines().get(index + 1) : getBlackoutLine());
	}

	public String[] getCurrentLines() {
		if (pause == false) {
			String[] spl = getCurrentLine().split("\n", 2);
			if (spl.length > 1) {
				return spl;
			} else if (spl.length == 1) {
				return new String[] { spl[0], "" };
			}
		}
		return new String[] { "", "" };

	}

	public String[] getNextLines() {
		if (pause == false) {
			String[] spl = getNextLine().split("\n", 2);
			if (spl.length > 1) {
				return spl;
			} else if (spl.length == 1) {
				return new String[] { spl[0], "" };
			}
		}
		return new String[] { "", "" };
	}

	private boolean isValidIndex(int i) {
		return i >= 0 && i <= song.getSongLines().size() - 1;
	}

	public String getBlackoutLine() {
		return (String) JSONPersistence.get().getSetting(UTPersistenceConstants.BLACKOUTLINEFILLER, "    ");
	}

	public String getTitle() {
		return pause ? getBlackoutLine() : song.getTitle();
	}

	public void pause() {
		pause = !pause;
		updateEvent();
	}

	public Song getSong() {
		return song;
	}

	public int getCurrentIndex() {
		return index;
	}

	public boolean isPaused() {
		return pause;
	}

	private void setIndex(int index) {
		this.index = index;
		updateEvent();
	}
	
	public void updateEvent() {
		CTEventHub.get()
		.publish(new SongLinePacket(Arrays.asList(getCurrentLines()), Arrays.asList(getNextLines()), song));
	}

	public void enable() {
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
	}
}
