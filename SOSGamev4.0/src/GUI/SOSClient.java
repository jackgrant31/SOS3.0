package GUI;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import Logic.CombatGameLogic;
import Logic.GameLogic;
import Logic.Timer1;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane; 
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SOSClient extends Application
	{
	private ComboBox<Character> sOro;
	private boolean myTurn = false;

	private Label[] labels;
	private char myToken = ' ';
	private char otherToken = ' ';
	private Cell[][] cell = new Cell[3][3];
	private Label lblTitle = new Label();
	private Label lblStatus = new Label();
	private int rowSelected;
	private int columnSelected;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean continueToPlay = true;
	private boolean waiting = true;
	private String host = "localhost";
	private int player;
	private Label turnLabel;	
	private String letter1 = "S", letter2 = "O", player1, player2;
	private int length;
	private Label playerLabel;
	private GameLogic game;
	private String token = " ";
	private GUITable gtable;
	private Timer1 time;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		// Pane to hold cell
		labels = new Label[9];
		for(int i=0;i<labels.length;i++)
			labels[i]=new Label();
		game = new GameLogic(1, "","");
		GridPane pane = new GridPane(); 
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) 
				pane.add(cell[i][j] = new Cell(i, j), j, i);
		
		ObservableList<Character> letters = 
			    FXCollections.observableArrayList(
			        'S',
			        'O'
			    );
		sOro = new ComboBox<Character>(letters);
		sOro.getSelectionModel().selectFirst();
		
		BorderPane borderPane = new BorderPane(); 
		borderPane.setRight(sOro);
		borderPane.setTop(lblTitle); 
		borderPane.setCenter(pane); 
		borderPane.setBottom(lblStatus);
		// Create a scene and place it in the stage
		Scene scene = new Scene(borderPane, 320, 350); 
		primaryStage.setTitle("TicTacToeClient"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage 
		primaryStage.show(); // Display the stage
		// Connect to the server
		connectToServer();
	}
	
	private void connectToServer() {
		try 
		{
			Socket socket = new Socket(host, 8000);
			
			fromServer = new DataInputStream(socket.getInputStream());
			
			toServer = new DataOutputStream(socket.getOutputStream());
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		new Thread(() -> { 
			try {
		      // Get notification from the server
				player = fromServer.readInt();
		      // Am I player 1 or 2?
				if (player == 1) {  
					Platform.runLater(() -> {
						lblTitle.setText("Player 1 with token 'X'");
						lblStatus.setText("Waiting for player 2 to join");
					});
		        // Receive startup notification from the server
					fromServer.readInt(); // Whatever read is ignored
		        // The other player has joined
					Platform.runLater(() -> lblStatus.setText("Player 2 has joined. I start first"));
		        // It is my turn
		        myTurn = true; 
		}
		else if (player == 2) { 
			Platform.runLater(() -> {
				lblTitle.setText("Player 2 with token 'O'");
				lblStatus.setText("Waiting for player 1 to move"); 
				});
			}	
		while (continueToPlay) { 
			if (player == 1) {
				waitForPlayerAction(); // Wait for player 1 to move 
				sendMove(); // Send the move to the server 
				receiveInfoFromServer(); // Receive info from the server
			}
			else if (player == 2) {
				receiveInfoFromServer(); // Receive info from the server 
				waitForPlayerAction(); // Wait for player 2 to move 
				sendMove(); // Send player 2's move to the server
			} 
		}
		}
		catch (Exception ex) {
		      ex.printStackTrace();
		    }
		  }).start();
		}
		/** Wait for the player to mark a cell */
		private void waitForPlayerAction() throws InterruptedException { 
			while (waiting) {
				Thread.sleep(100); 
			}
		waiting=true;
	}
		
	private void sendMove() throws IOException { 
		toServer.writeInt(rowSelected); // Send the selected row 
		toServer.writeInt(columnSelected); // Send the selected column
		toServer.writeChar( sOro.getValue());
		int[] score = game.getScore();
		toServer.writeInt(0);//score[0]);
		toServer.writeInt(0);//score[1]);
		toServer.writeBoolean(false);
	}
		/** Receive info from the server */
	private void receiveInfoFromServer() throws IOException { // Receive game status
		int status = fromServer.readInt();
		if (status == 1) {
			continueToPlay = false;
			if (player == 1) {
				Platform.runLater(() -> lblStatus.setText("I won! (X)")); }
			else if (player == 2) { 
				Platform.runLater(() ->lblStatus.setText("Player 1 (X) has won!")); 
			receiveMove();
		} }
		else if (status == 2) { // Player 2 won, stop playing 
			continueToPlay = false;
			if (player == 1) {
				Platform.runLater(() -> lblStatus.setText("I won! (O)")); }
			else if (player == 2) { 
				Platform.runLater(() -> lblStatus.setText("Player 2 (O) has won!")); 
				receiveMove();
		} }
			else if (status == 3) { // No winner, game is over 
				continueToPlay = false; 
				Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));
		if (player == 1) { 
			receiveMove();
		} }
		else {
			receiveMove();
			Platform.runLater(() -> lblStatus.setText("My turn")); 
			myTurn = true; // It is my turn
		} }
		
	private void receiveMove() throws IOException { // Get the other player's move
		int row = fromServer.readInt();
		int column = fromServer.readInt();
		char so = fromServer.readChar();
		int p1 = fromServer.readInt();
		int p2 = fromServer.readInt();
		boolean end = fromServer.readBoolean();
		Platform.runLater(() -> cell[row][column].fillCell(Character.toString(so), row * 3 + column));
	}

public class Cell extends Pane{
	private int row;
	private int column;
	
public Cell(int row, int column) {
	this.row = row;
	this.column = column;
	this.setPrefSize(2000, 2000); // What happens without this? 228 
	setStyle("-fx-border-color: black"); // Set cell's border
	this.setOnMouseClicked(e -> handleMouseClick());
}
		
		
		
		
		public void setLabel(String letter, int index)
		{
			token= letter;
			if (token.equals(letter1))
			{
				fillCell(letter1,index);
			}
			else if (token.equals(letter2))
			{
				fillCell(letter2,index);
			}
		}
		
		private void handleMouseClick() {
			if (token == " " && myTurn) 
			{
				int yIndex = (int) Math.round(this.getLayoutY()*length/600);
				int xIndex = (int) Math.round(this.getLayoutX()*length/800);
				int index =  xIndex * length + yIndex;
				myTurn = false;
				rowSelected = row;
				columnSelected = column;
				lblStatus.setText("waiting");
				waiting=false;
				setLabel(Character.toString( sOro.getValue()),index);
				addToBoard(xIndex+1,yIndex+1);
//				setLabels(); 
//				gameOver();
//				updateTable();
				update();
			}
		}
		
		private void fillCell(String letter, int index)
		{
			Label addLabel = labels[index];
			addLabel.setText(letter);
			if (game.getTurn()==0)
				addLabel.setTextFill(Color.web("#0076a3"));
			else
				addLabel.setTextFill(Color.web("#FF0000"));
			addLabel.setFont(new Font("Avenir", 30));
			this.getChildren().add(addLabel);
			//game.checkCombatScore(index,String.valueOf(sOro.getValue()));
		}
		
		public void nextCell(int turn, int[][] index1)
		{
			for(int i=0;i<index1.length;i++)
			{
				if(index1[i][0]!=0 || index1[i][1]!=0)
				{
					System.out.println(index1[i][0]);
					System.out.println(index1[i][1]);
					Label firstLabel = labels[index1[i][0]];
					Label secondLabel = labels[index1[i][1]];
					String color;
					if(turn==0)
						color = "#0076a3";
					else
						color = "FF0000";
					firstLabel.setTextFill(Color.web(color));
					secondLabel.setTextFill(Color.web(color));
				}
			}
		}
		
		private void setLabels()
		{
			int turn = game.getTurn();
			if(turn==0)
				turnLabel.setText(" "+player1 + "'s turn");
			else
				turnLabel.setText(" "+player2 + "'s turn");
			
			int[] score = game.getScore();
			playerLabel.setText(" "+player1+" points: "+score[0]+"  "+ player2+" points: "+score[1]);
		}
		
		private void gameOver()
		{
			if (game.endOfMatch()==true) 		    	  	
	    	  	turnLabel.setText(" "+game.whoWon(player1, player2) + " won with "+game.whoHadMorePoints()+" points! The game is over.");
		}
		
		private void addToBoard(int yIndex, int xIndex)
		{
			int[][] test = game.insert(yIndex,xIndex,String.valueOf(sOro.getValue()));
			nextCell(game.getTurn(),test);
		}
		
		public void updateTable()
		{
			String name;
			int turn = game.getTurn();
			if (turn==0)
				name=player1;
			else
				name=player2;
			int[] score = game.getScore();
			gtable.setList(time.lastTime(), time.getTime(), player1+": "+score[0],player2+": "+score[1], name);
		}
		
		public void update()
		{
			ArrayList<int[]> master;
			if(game instanceof CombatGameLogic)
				master =  ((CombatGameLogic)game).getAL();
			else 
				return;
			master
				.stream()
				.forEach(e->{
					String color;
					if(e[0]==1)
					{
						color = "#0076a3";
					}
					else
					{
						color = "FF0000";
					}
					labels[e[1]].setTextFill(Color.web(color));
					labels[e[2]].setTextFill(Color.web(color));
					labels[e[3]].setTextFill(Color.web(color));
				});
		}
		
	}
	}
	


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//public Cell(int row, int column) {
//	this.row = row;
//	this.column = column;
//	this.setPrefSize(2000, 2000); // What happens without this? 228 
//	setStyle("-fx-border-color: black"); // Set cell's border
//	this.setOnMouseClicked(e -> handleMouseClick());
//}
//public char getToken() {
//	return token;
//}
//public void setToken(char c) {
//	token = c;
//	repaint();
//}
//protected void repaint() {
//	if (token == 'S') {
////		Line line1 = new Line(10, 10,
////				this.getWidth() - 10, this.getHeight() - 10);
////		line1.endXProperty().bind(this.widthProperty().subtract(10)); 
////		line1.endYProperty().bind(this.heightProperty().subtract(10));
////		Line line2 = new Line(10, this.getHeight() - 10,
////				this.getWidth() - 10, 10);
////		line2.startYProperty().bind(
////				this.heightProperty().subtract(10));
////		line2.endXProperty().bind(this.widthProperty().subtract(10));
////		this.getChildren().addAll(line1, line2);
//		this.getChildren().add(new Label("S"));
//	}
//	else if (token == 'O') {
////		Ellipse ellipse = new Ellipse(this.getWidth() / 2,
////				this.getHeight() / 2, this.getWidth() / 2 - 10,
////				this.getHeight() / 2 - 10);
////		ellipse.centerXProperty().bind(
////				this.widthProperty().divide(2));
////		ellipse.centerYProperty().bind(
////				this.heightProperty().divide(2));
////		ellipse.radiusXProperty().bind(
////				this.widthProperty().divide(2).subtract(10));
////		ellipse.radiusYProperty().bind(
////				this.heightProperty().divide(2).subtract(10));
////		ellipse.setStroke(Color.BLACK);
////		ellipse.setFill(Color.WHITE);
////		getChildren().add(ellipse); 
//		this.getChildren().add(new Label("O"));
//} }
//  /* Handle a mouse click event */
//	private void handleMouseClick() {
//// If cell is not occupied and the player has the turn 
//	if (token == ' ' && myTurn) {
//		setToken(myToken); // Set the player's token in the cell 
//		myTurn = false;
//		rowSelected = row;
//		columnSelected = column;
//		lblStatus.setText("Waiting for the other player to move");
//		waiting = false;
//}}}}
//
//
//
