package Logic;

public class Players {
	private String player1, player2;
	
	public Players(int numPlayers1, String name1, String name2)
	{
		player1 = name1;
		player2 = name2;
	}
	
	public int randomStart()
	{
		int num = (Math.random() <= 0.5) ? 1 : 2;
		return num;
	}
	
	public String getPlayer1()
	{
		return player1;
	}
	
	public String getPlayer2()
	{
		return player2;
	}
	
	public void setPlayer1(String name)
	{
		player1 = name;
	}
	
	public void setPlayer2(String name)
	{
		player2 = name;
	}
}
