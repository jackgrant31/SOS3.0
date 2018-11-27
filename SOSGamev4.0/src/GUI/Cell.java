package GUI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import Logic.CombatGameLogic;
import Logic.GameLogic;
import Logic.Timer1;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Cell extends Pane 
{
	private Label turnLabel;	
	private String letter1 = "S", letter2 = "O", player1, player2;
	private int length;
	private Label playerLabel;
	private ComboBox<String> sOro;
	private GameLogic game;
	private Label[] labels;
	private String token = " ";
	private GUITable gtable;
	private Timer1 time;

	private Label lblTitle = new Label();
	private Label lblStatus = new Label();
	private int rowSelected, player;
	private int columnSelected;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean continueToPlay = true;
	private boolean waiting = true;
	private String host = "localhost";
	private boolean myTurn = false;
	
	public Cell(GameLogic game1, String firstP, String secondP, int len, ComboBox<String> so, Label[] l, Label tlabel, Label plabel, GUITable g, Label timer, Timer1 time1) {
		game = game1;
		game.setLabels(labels);
		player1 = firstP;
		player2 = secondP;
		length = len;
		labels = l;
		sOro = so;
		turnLabel = tlabel;
		playerLabel = plabel;
		gtable=g; 
		time = time1;
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
		if (token == " " ) 
		{
			int yIndex = (int) Math.round(this.getLayoutY()*length/600);
			int xIndex = (int) Math.round(this.getLayoutX()*length/800);
			int index =  xIndex * length + yIndex;
			setLabel((String) sOro.getValue(),index);
			addToBoard(xIndex+1,yIndex+1);
			setLabels(); 
			gameOver();
			updateTable();
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
