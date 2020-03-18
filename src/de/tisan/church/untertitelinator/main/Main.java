package de.tisan.church.untertitelinator.main;

import de.tisan.church.untertitelinator.data.Song;
import de.tisan.church.untertitelinator.data.SongPlayer;
import de.tisan.church.untertitelinator.data.Untertitelinator;

public class Main {
	public static void main(String[] args) {
		System.out.println("hi");
		Untertitelinator un = new Untertitelinator();
		Song s = un.getSongs().get(0);
		SongPlayer player = un.createSongPlayerForSong(s);
		System.out.println("Jetzt spielt: " + player.getTitle());
		while(player.isOnEnd() == false) {
			System.out.println(player.getCurrentLine());
			player.nextLine();
		}
		
		GUIMain main = new GUIMain(un);
		main.setVisible(true);
		
	}
}
