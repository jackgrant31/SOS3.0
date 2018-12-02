package GUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import Logic.CombatGameLogic;
import Logic.ExtremeGameLogic;
import Logic.GameLogic;
import Logic.NormalGameLogic;
import Logic.Timer1;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NewGUI extends Application{
	
	public NewGUI(int mode, int size)
	{
		mode1 =mode;
		length = size;
	}	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		primaryStage.setResizable(false);
		
		GridPane gamegridPane = new GridPane();
		gamegridPane.setLayoutX(800);
		gamegridPane.setLayoutY(600);
				
		ObservableList<String> letters = 
			    FXCollections.observableArrayList(
			        letter1,
			        letter2
			    );
		sOro = new ComboBox<String>(letters);
		sOro.getSelectionModel().selectFirst();
		
		GUIButtons resB = new GUIButtons();
		Button restartB = resB.createButton("Restart");
		restartB.setOnAction(e -> {
			resB.restartButton(window);
		});
		
		Button quitB = new Button("Quit");
		quitB.setOnAction(e ->{
			window.close();
		});
		
		Label diffletter = new Label("S or O?:");
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(diffletter,sOro,restartB,quitB);
		vbox.setPadding(new Insets(10,10,10,10));
		vbox.setSpacing(10);
		
		gtable = new GUITable();
		TableView<TableData> table = gtable.getTable();

        timerLabel = new Label("0");
        timerLabel.setTextFill(Color.web("#FF0000"));
        timerLabel.setFont(new Font("Avenir", 30));
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(timerLabel, table);

        if(mode1==0)
        		game = new NormalGameLogic(length, player1, player2);
        else if(mode1==1)
    			game = new ExtremeGameLogic(length, player1, player2);
        else
    			game = new CombatGameLogic(length, player1, player2);
        
		playerLabel.setText(" "+ player1+" points: 0  "+ player2+" points: 0");
		int turn = game.getTurn();
		if(turn==0)
			turnLabel.setText(" "+player1+"'s turn");
		else
			turnLabel.setText(" "+player2+"'s turn");
		
		BorderPane borderPane = new BorderPane(); 
		borderPane.setCenter(gamegridPane); 
		borderPane.setBottom(turnLabel);
		borderPane.setTop(playerLabel);
		borderPane.setRight(vbox);
		Scene scene  = new Scene(borderPane,1200,600);
		primaryStage.setTitle("SOS Game");
		
		labels = new Label[length*length];
		for(int i=0;i<labels.length;i++)
			labels[i]=new Label();
		
		cell = new Cell[length][length];
		for (int row = 0; row < length; row++)
			for (int col = 0; col < length; col++) 
				gamegridPane.add(cell[row][col] = new Cell(row,col), row, col);
		
		window.setScene(scene);
		window.show();
		
		connectToServer();
	}
	
	private Label turnLabel = new Label();	
	private String letter1 = "S", letter2 = "O", player1="Player1", player2="Player2";
	private int length, mode1;
	private Label playerLabel =  new Label();
	private Label timerLabel;
	private Stage window;
	private ComboBox<String> sOro;
	private Label[] labels;
	private GUITable gtable;
	private GameLogic game=null;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean continueToPlay = true;
	private boolean waiting = true;
	private int player;
	private String token = " ";
	private Timer1 time;
	private boolean myTurn = false;
	private Label lblStatus = new Label();
	private int rowSelected;
	private int columnSelected;
	private Cell[][] cell;
	
	
	private void connectToServer() {
		
		new Thread(() -> { 
			try {
				if (player == 1) {  
					Platform.runLater(() -> {
						playerLabel.setTextFill(Color.web("#0076a3"));
						turnLabel.setText("Waiting for player 2 to join");
					});
					Platform.runLater(() -> turnLabel.setText("Player 2 has joined. I start first"));
					myTurn = true; 
				}
				else if (player == 2) { 
					Platform.runLater(() -> {
						playerLabel.setTextFill(Color.web("#FF0000"));
						turnLabel.setText("Waiting for player 1 to move"); 
					});
					player=0;
				}	
				while (continueToPlay) { 
					if (game.getTurn()!=player) {
						waitForPlayerAction();
						sendMove();
					}
					else if (game.getTurn()==player) {
						receiveInfoFromServer(); 
					} 
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}
		
	private void waitForPlayerAction() throws InterruptedException { 
		while (waiting) {
			Thread.sleep(300); 
		}
		waiting=true;
	}
		
	private void sendMove() throws IOException { 
		toServer.writeInt(rowSelected); 
		toServer.writeInt(columnSelected); 
		toServer.writeChar( sOro.getValue().charAt(0));
		int[] score = game.getScore();
		toServer.writeInt(score[0]);
		toServer.writeInt(score[1]);
		toServer.writeBoolean(!continueToPlay);
		toServer.writeInt(game.getTurn());
		if (game.getTurn()==player)
			Platform.runLater(() -> turnLabel.setText("Not my turn"));
	}
		
	private void receiveInfoFromServer() throws IOException { 
		int status = fromServer.readInt();
		if (status == 1) {
			continueToPlay = false;
			if (player == 1) {
				Platform.runLater(() -> lblStatus.setText("I won!")); }
			else if (player == 0) { 
				Platform.runLater(() ->lblStatus.setText("Player 1 has won!")); 
			receiveMove();
			} 
		}
		else if (status == 2) { 
			continueToPlay = false;
			if (player == 1) {
				Platform.runLater(() -> lblStatus.setText("I won! ")); }
			else if (player == 0) { 
				Platform.runLater(() -> lblStatus.setText("Player 2 has won!")); 
				receiveMove();
			} 
		}
		else if (status == 3) { 
			continueToPlay = false; 
			Platform.runLater(() -> lblStatus.setText("Game is over, no winner!"));
			if (player == 1) { 
				receiveMove();
			} 
		}
		else {
			receiveMove();
			if (game.getTurn()!=player)
			{
				Platform.runLater(() -> turnLabel.setText("My turn")); 
				myTurn = true;
			}
		}
	}
		
	private void receiveMove() throws IOException { 
		int row = fromServer.readInt();
		int column = fromServer.readInt();
		String so = Character.toString(fromServer.readChar());
		fromServer.readInt();
		fromServer.readInt();
		fromServer.readBoolean();
		fromServer.readInt();
		Cell c = cell[row][column];
		int index =  row * length + column;
		c.setLabel(so,index);
		c.addToBoard(row+1,column+1,so);
		c.setLabels(); 
		c.gameOver();
		c.updateTable();
		c.update();
	}
	
	public void setInput(DataInputStream inp)
	{
		fromServer = inp;
	}
	
	public void setOutput(DataOutputStream outp)
	{
		toServer = outp;
	}
	
	public void setPlayer(int p)
	{
		player = p;
	}

	
	public class Cell extends Pane 
	{
		private int row, column;
		
		public Cell(int row, int col) {
			time = new Timer1(timerLabel,game,1);
			this.row = row;
			this.column = col;
			time.timerStart();
			setStyle("-fx-border-color: black"); 
			this.setPrefSize(2000, 2000); 
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
			if (myTurn) 
			{
				int yIndex = (int) Math.round(this.getLayoutY()*length/600);
				int xIndex = (int) Math.round(this.getLayoutX()*length/800);
				int index =  xIndex * length + yIndex;
				rowSelected = row;
				columnSelected = column;
				waiting=false;
				setLabel((String) sOro.getValue(),index);
				addToBoard(xIndex+1,yIndex+1,(String) sOro.getValue());
				setLabels(); 
				gameOver();
				updateTable();
				update();
				if(game.getTurn()==player)
					myTurn = false;
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
			Platform.runLater(()->this.getChildren().add(addLabel));
		}
		
		public void nextCell(int turn, int[][] index1)
		{
			for(int i=0;i<index1.length;i++)
			{
				if(index1[i][0]!=0 || index1[i][1]!=0)
				{
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
			int[] score = game.getScore();
			Platform.runLater(()->playerLabel.setText(" Player1 points: "+score[0]+"  Player2 points: "+score[1]));
		}
		
		private void gameOver()
		{
			if (game.endOfMatch()==true) 	
			{
	    	  		turnLabel.setText(" "+game.whoWon(player1, player2) + " won with "+game.whoHadMorePoints()+" points! The game is over.");
	    	  		continueToPlay = false;
			}
		}
		
		private void addToBoard(int yIndex, int xIndex, String so)
		{
			int[][] test = game.insert(yIndex,xIndex,so);
			nextCell(game.getTurn(),test);
		}
		
		public void updateTable()
		{
			String name;
			int turn = game.getTurn();
			if (turn==0)
				name="Player1";
			else
				name="Player2";
			int[] score = game.getScore();
			gtable.setList(time.lastTime(), time.getTime(), "Player1: "+score[0],"Player2: "+score[1], name);
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
