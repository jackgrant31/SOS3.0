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

	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage)
	{
		TextArea taLog = new TextArea();
		
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		primaryStage.setTitle("SOS Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		new Thread( ()-> {
			try {
				ServerSocket serverSocket = new ServerSocket(4969);
				Platform.runLater(()-> taLog.appendText(new Date() + ": Server started at socket 4969\n"));
				Platform.runLater(()-> {
					try {
						taLog.appendText(new Date() + ": Server IP address is "+InetAddress.getLocalHost()+"\n");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				});
				
				while(true)
				{
					Platform.runLater(()->taLog.appendText(new Date() + ": Wait for players to join session "+sessionNo+'\n'));
				
					Socket player1 = serverSocket.accept();
					
					Platform.runLater(()->{
						taLog.appendText(new Date()+": Player 1 joined session "+sessionNo+"\n");
						taLog.appendText("Player 1's IP address " + player1.getInetAddress().getHostAddress()+'\n');
					});
					
					new DataOutputStream(
							player1.getOutputStream()).writeInt(1);
				
					Socket player2 = serverSocket.accept();
					
					Platform.runLater(()->{
						taLog.appendText(new Date()+": Player 2 joined session "+sessionNo+"\n");
						taLog.appendText("Player 2's IP address " + player1.getInetAddress().getHostAddress()+'\n');
					});
					
					new DataOutputStream(
							player2.getOutputStream()).writeInt(2);
					
					Platform.runLater(()->{
						taLog.appendText(new Date()+": Start a thread for new session "+sessionNo+"\n");
					});
					new Thread(new HandleASession(player1, player2)).start();
					sessionNo++;
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}).start();
	}
}
