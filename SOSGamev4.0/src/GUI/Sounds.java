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
		
		
		//make a new thread and play in that thread
		//fix indents in table and timer
		Media song = new Media(new File(file).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(song);
		mediaPlayer.play();
	}
}
