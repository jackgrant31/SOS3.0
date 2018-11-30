package GUI;
import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sounds {
	int turn;
	
	public Sounds(int t) 
	{
		turn = t;
	}

	public void playSound()
	{
		String file = "SOS4.m4a";
		
		new Thread(() -> { 
			try {
				Media song = new Media(new File(file).toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(song);
				mediaPlayer.play();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}
}
