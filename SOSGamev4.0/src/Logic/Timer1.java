package Logic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer1 {
	private Integer timeSec=0;
	private int lastTimeSec=0, length;
	private Label timerLabel;
	private GameLogic game;

	public Timer1(Label time, GameLogic g, int len)
	{
		timerLabel = time;
		game=g;
		length=len;
	}
	
	public void timerStart()
	{
		Timeline timeline = new Timeline();
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.getKeyFrames().add(
	    		new KeyFrame(Duration.seconds(1),
	    			event -> {
	    				timeSec++;
	    				Integer error = (int) timeSec/(length*length);
					timerLabel.setText(error.toString());
						if(game.endOfMatch())
						{
							timeline.stop();
						}
	    			}));
	    timeline.playFromStart();
	}
	
	public int getTime()
	{
		lastTimeSec = timeSec/ (length*length);
		return timeSec/ (length*length);
	}
	
	public int lastTime()
	{
		return lastTimeSec;
	}
}
