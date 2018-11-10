package Logic;

public class ExtremeGameLogic extends GameLogic {
	private int numScores=0;

	public ExtremeGameLogic(int length, String player1, String player2) {
		super(length, player1, player2);
	}

	public void setScore(CheckScore check)
	{
		if (check.getScore()>0)
		{
			score[turn%2]+=Math.pow(2, numScores)*check.getScore();
			numScores++;
		}
	}
}
