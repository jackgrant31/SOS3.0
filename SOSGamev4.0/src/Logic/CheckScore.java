package Logic;

import java.util.ArrayList;

public class CheckScore {
	private int ins, length, i=0, score=0;
	private int[] board;
	private int[][] color; 
	private ArrayList<int[]> scoreSpots = new ArrayList<>();
	private ArrayList<Integer> farList = new ArrayList<>();

	public CheckScore(int insert, int[] gameboard, int len)
	{
		ins = insert;
		board = gameboard;
		length = len;
		color = new int[10][2];
	}
	
	public boolean inBounds(int firstCheck, int secondCheck, int length) 
	{
		if ((firstCheck<0 || secondCheck<0)||(firstCheck>length*length-1||secondCheck>length*length-1))
			return false;
		else
			return true;
	}
	
	public boolean checkWin(boolean direction, boolean overflow) 
	{
		if (direction==true && overflow==true)
			return true;
		return false;
	}
	
	public void isASScore(int close, int far, boolean overflow) {
		if(inBounds(far,close,length)&&checkWin(board[far]==2 && board[close]==1,overflow))
		{
			addScore(close, far);
			int[] entry = new int[] {ins, close, far};
			scoreSpots.add(entry);
			farList.add(close);
			farList.add(far);
		}
	}
	
	public void isAOScore(int close, int far, boolean overflow) {
		if(inBounds(far,close,length)&&checkWin(board[far]==1 && board[close]==1,overflow))
		{
			addScore(close, far);
			int[] entry = new int[] {ins, close, far};
			scoreSpots.add(entry);
			farList.add(far);
			farList.add(close);
		}
	}
	
	private void addScore(int close, int far)
	{
		score++;
		color[i][0] = close;
		color[i][1] = far;
		i++;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public ArrayList<int[]> getSpots()
	{
		return scoreSpots;
	}
	
	public int[][] getRoundMoves()
	{
		return color;
	}
	
	public ArrayList<Integer> getFar()
	{
		return farList;
	}

}
