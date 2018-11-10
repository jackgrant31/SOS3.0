package Logic;

import GUI.Sounds;
import javafx.scene.control.Label;

public class GameLogic {
	protected int[] score = new int[2];
	protected int turn, j;
	public int timeSec=0,lastTimeSec=0;
	protected int[] board;
	private String letter1 = "S";
	protected Label[] labels;
	
	public GameLogic(int length, String player1, String player2)
	{
		 board = new int[length*length];
		 Players play = new Players(2, player1, player2);
		 turn = play.randomStart();
	}
	
	public boolean endOfMatch()
	{
		int count=0;
		for (int i=0;i<board.length;i++)
		{
			if(board[i]==1 || board[i]==2)
			{
				count++;
			}
		}
		if (count==board.length)
			return true;
		return false;
	}
	
	public int[][] insert(int row,int column, String letter)
	{
		int[][] temp;
		int i;
		int insertElement = (row-1) * (int)Math.sqrt(board.length) + column -1;
		if (letter.equals(letter1))
		{
			i = 1;
		} else 
		{
			i = 2;
		}
		board[insertElement]=i;
		j=0;
		temp = checkScore(insertElement,board);
		turn++;
		return temp;
	}
	
	public int[][] addScore(int[][] temp, int i, int close, int far)
	{
		temp[i][0] = close;
		temp[i][1] = far;
		return temp;
	}
	
	public int[][] checkScore(int ins,int[] board) 
	{
		int length = (int) Math.sqrt(board.length);
		CheckScore check = new CheckScore(ins, board, length);
		if (board[ins]==1)
		{
			check.isASScore(ins+2*length, ins+length, true);
			check.isASScore(ins-2*length,ins-length, true);
			check.isASScore(ins+2,ins+1, !((ins+3)%length==2||(ins+3)%length==1));
			check.isASScore(ins-2,ins-1, !((ins-1)%length==0||(ins-1)%length==length-1));
			check.isASScore(ins-2-(2*length),ins-1-length, !((ins-1-(2*length))%length==0||(ins-1-(2*length))%length==length-1));
			check.isASScore(ins+2+(2*length),ins+1+length, !((ins+3+(2*length))%length==1||(ins+3+(2*length))%length==2));
			check.isASScore(ins+2-2*length,ins+1-length, !((ins+3-(2*length))%length==1||(ins+3-(2*length))%length==2));
			check.isASScore(ins-2+2*length,ins-1+length, !((ins-1+(2*length))%length==0||(ins-1+(2*length))%length==length-1));
		}
		else if(board[ins]==2)
		{
			check.isAOScore(ins-length,ins+length, true);
			check.isAOScore(ins+1,ins-1, !((ins+2)%length==1 || ins%length==0));
			check.isAOScore(ins+1-length,ins-1+length, !((ins+2)%length==1 || ins%length==0));
			check.isAOScore(ins+1+length,ins-1-length, !((ins+2)%length==1 || ins%length==0));
		}
		if(check.getScore()>0)
		{
			if(j==0)
				turn-=1;
			j++;
			setScore(check);
			//check.setLast();
			isScore(check, board[ins]);	
		}
		return check.getRoundMoves();
	}
	
	public void isScore(CheckScore check, int sOro)
	{
		Sounds sound = new Sounds();
		sound.playSound();	
	}
	
	public void setScore(CheckScore check)
	{
		score[turn%2]+=check.getScore();
	}
	
	public String whoWon(String name1, String name2)
	{
		if (score[1]>score[0])
			return name2;
		if (score[0]>score[1])
			return name1;
		return "no one ";
	}
	
	public int whoHadMorePoints()
	{
		if (score[1]>score[0])
			return score[1];
		if (score[0]>score[1])
			return score[0];
		return score[0];
	}
	
	public int[] getScore()
	{
		return score;
	}
	
	public int getTurn()
	{
		return turn % 2;
	}	
	
	public void setLabels(Label[] l)
	{
		labels= l;
	}
}
