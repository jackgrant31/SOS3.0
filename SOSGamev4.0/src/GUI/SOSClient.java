package GUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SOSClient extends Application{
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private int player;
	private TextArea taLog;
	private Stage clientStage;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		clientStage =primaryStage;
		taLog = new TextArea();
		
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		clientStage.setTitle("SOS Client");
		clientStage.setScene(scene);
		clientStage.show();
		
		connectToServer();
	}
	
	private void connectToServer() {
		try 
		{
			Socket socket = new Socket(InetAddress.getLocalHost(), 4969);
			
			fromServer = new DataInputStream(socket.getInputStream());
			
			toServer = new DataOutputStream(socket.getOutputStream());
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		new Thread(() -> { 
			try {
				assignPlayers();	
				
				assignPlayerChoosingMode();
				
				startGUI();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	private void startGUI() throws IOException {
		int mode = fromServer.readInt();
		int size = fromServer.readInt();
		Platform.runLater(()->{
			NewGUI gui = new NewGUI(mode,size);
			Stage stage = new Stage();
			try {
				gui.setInput(fromServer);
				gui.setOutput(toServer);
				gui.setPlayer(player);
				gui.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			clientStage.close();
		});
	}

	private void assignPlayerChoosingMode() throws IOException {
		int status = fromServer.readInt();
		if (status == 5)
		{
			Platform.runLater(()->{
				taLog.appendText("I choose the game");
				InitGUI init = new InitGUI();
				Stage stage = new Stage();
				init.setOutput(toServer);
				try {
					init.start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		else
			Platform.runLater(()->taLog.appendText("The other player is choosing the settings"));
	}

	private void assignPlayers() throws IOException {
		player = fromServer.readInt();
		player = 2;
		if (player == 1) {  
			Platform.runLater(() -> {
				taLog.appendText("I am player 1.\nWaiting for player 2 to join\n");
			});
			fromServer.readInt();
			Platform.runLater(() -> taLog.appendText("Player 2 has joined\n"));
		}
		else if (player == 2) { 
			Platform.runLater(() -> {
				taLog.appendText("I am player 2.\nWaiting for server\n");
			});
			player=0;
		}
	}
}
