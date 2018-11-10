package GUI;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sounds {
	
	public Sounds() 
	{
		
	}

	public void playSound()
	{
		String file;
		file = "SOS4.m4a";
		
		Media song = new Media(new File(file).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(song);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mediaPlayer.play();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
