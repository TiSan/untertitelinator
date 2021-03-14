package de.tisan.church.untertitelinator.data;

import de.tisan.church.untertitelinator.churchtools.instancer.CTEventHub;
import de.tisan.church.untertitelinator.churchtools.instancer.CTEventListener;
import de.tisan.church.untertitelinator.churchtools.instancer.CTInstanceServer;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.CommandPacket;
import de.tisan.church.untertitelinator.churchtools.instancer.packets.Packet;
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
		setIndex(0);
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
		return pause ? new String[] { "", "" } : getCurrentLine().split("\n", 2);
	}

	public String[] getNextLines() {
		return pause ? new String[] { "", "" } : getNextLine().split("\n", 2);
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
		CTInstanceServer.get().sendNewSongLines(getCurrentLines(), getNextLines());
	}
	
	public void enable() {
		this.enabled = true;
	}
	
	public void disable() {
		this.enabled = false;
	}
}

