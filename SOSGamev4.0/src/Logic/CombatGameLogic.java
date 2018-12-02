package Logic;

import java.util.ArrayList;
import java.util.Arrays;

import GUI.Sounds;

public class CombatGameLogic extends GameLogic {
	ArrayList<int[]> master;

	public CombatGameLogic(int length, String player1, String player2) {
		super(length, player1, player2);
		master = new ArrayList<>();
	}
	
	@Override
	public void isScore(CheckScore check, int sOro)
	{
		Sounds sound = new Sounds(turn);
		sound.playSound();	
	
		ArrayList<int[]> newAdditions = check.getSpots();
		newAdditions
			.stream()
			.forEach(e->{
				Arrays.sort(e);
				int[] addTurn = new int[4];
				addTurn[0]=turn%2;
				for(int i=1;i<4;i++)
					addTurn[i] = e[i-1];
				master.add(addTurn);
			});
		
		recursion(check,sOro);
	}
	
	public void recursion(CheckScore check, int sOro)
	{
		ArrayList<Integer> array = check.getFar();
		array
			.stream()
			.forEach(e->checkRec(e));
	}
	
	public void checkRec(int ins)
	{
		int length = (int) Math.sqrt(board.length);
		CheckScore check = new CheckScore(ins, board, length);
		check.isASScore(ins+2*length, ins+length, true);
		check.isASScore(ins-2*length,ins-length, true);
		check.isASScore(ins+2,ins+1, !((ins+3)%length==2||(ins+3)%length==1));
		check.isASScore(ins-2,ins-1, !((ins-1)%length==0||(ins-1)%length==length-1));
		check.isASScore(ins-2-(2*length),ins-1-length, !((ins-1-(2*length))%length==0||(ins-1-(2*length))%length==length-1));
		check.isASScore(ins+2+(2*length),ins+1+length, !((ins+3+(2*length))%length==1||(ins+3+(2*length))%length==2));
		check.isASScore(ins+2-2*length,ins+1-length, !((ins+3-(2*length))%length==1||(ins+3-(2*length))%length==2));
		check.isASScore(ins-2+2*length,ins-1+length, !((ins-1+(2*length))%length==0||(ins-1+(2*length))%length==length-1));
		check.isAOScore(ins-length,ins+length, true);
		check.isAOScore(ins+1,ins-1, !((ins+2)%length==1 || ins%length==0));
		check.isAOScore(ins+1-length,ins-1+length, !((ins+2)%length==1 || ins%length==0));
		check.isAOScore(ins+1+length,ins-1-length, !((ins+2)%length==1 || ins%length==0));
		
		ArrayList<int[]> changedSpots = check.getSpots();
		changedSpots
			.stream()
			.forEach(e-> {
				Arrays.sort(e);
				master
					.stream()
					.forEach(a->{
						Arrays.toString(e);
						if(e[0]==a[1]&& e[1]==a[2]&& e[2]==a[3])
							a[0]=turn%2;
					});
				});
		setScore();
	}
	
	public void setScore()
	{
		score[0]=0;
		score[1]=0;
		master
			.stream()
			.forEach(e->score[e[0]]+=1);
	}
	
	public ArrayList<int[]> getAL()
	{
		return master;
	}

}
