package GUI;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GUIButtons {
	private Button button;
	
	public GUIButtons()
	{
		
	}
	
	public Button createButton(String label)
	{
		button = new Button(label);
		return button;
	}
	
	public void initButton(Stage window, String player1entry, String player2entry, String gridCombo, String mode)
	{
		int modenum;
		if (mode.equals("Normal"))
			modenum=0;
		else if(mode.equals("Extreme"))
			modenum=1;
		else
			modenum=2;
		String lengthString = gridCombo;
		int length = Integer.parseInt(String.valueOf(lengthString.charAt(0)));
		if(length==1)
			length = Integer.parseInt(String.valueOf(lengthString.charAt(0))+String.valueOf(lengthString.charAt(1)));
		String player1 = player1entry;
		String player2 = player2entry;
		NewGUI game = new NewGUI(modenum, length);//length, player1, player2, modenum);
		try {
			game.start(window);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void restartButton(Stage window)
	{
		InitGUI newGame = new InitGUI();
		window.close();
		try {
			newGame.start(window);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
