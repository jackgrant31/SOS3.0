package GUI;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SOSServer extends Application{
	private int sessionNo = 1; 
	private TextArea taLog;

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		taLog = new TextArea();
		
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		primaryStage.setTitle("SOS Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread( ()-> {
			try {
				ServerSocket serverSocket = serverConnection();
				
				while(true)
				{
					Socket player1 = player1connection(serverSocket);
				
					Socket player2 = player2connection(serverSocket, player1);
					
					startSession(player1, player2);
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}).start();
	}

	private void startSession(Socket player1, Socket player2) {
		Platform.runLater(()->{
			taLog.appendText(new Date()+": Start a thread for new session "+sessionNo+"\n");
		});
		new Thread(new HandleASession(player1, player2)).start();
		sessionNo++;
	}

	private Socket player2connection(ServerSocket serverSocket, Socket player1) throws IOException {
		Socket player2 = serverSocket.accept();
		
		Platform.runLater(()->{
			taLog.appendText(new Date()+": Player 2 joined session "+sessionNo+"\n");
			taLog.appendText("Player 2's IP address " + player1.getInetAddress().getHostAddress()+'\n');
		});
		
		new DataOutputStream(
				player2.getOutputStream()).writeInt(2);
		return player2;
	}

	private Socket player1connection(ServerSocket serverSocket) throws IOException {	
		Platform.runLater(()->taLog.appendText(new Date() + ": Wait for players to join session "+sessionNo+'\n'));
		Socket player1 = serverSocket.accept();
		
		Platform.runLater(()->{
			taLog.appendText(new Date()+": Player 1 joined session "+sessionNo+"\n");
			taLog.appendText("Player 1's IP address " + player1.getInetAddress().getHostAddress()+'\n');
		});
		
		new DataOutputStream(
				player1.getOutputStream()).writeInt(1);
		return player1;
	}

	private ServerSocket serverConnection() throws IOException {
		ServerSocket serverSocket = new ServerSocket(4969);
		Platform.runLater(()-> taLog.appendText(new Date() + ": Server started at socket 4969\n"));
		Platform.runLater(()-> {
			try {
				taLog.appendText(new Date() + ": Server IP address is "+InetAddress.getLocalHost()+"\n");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		});
		return serverSocket;
	}
}
