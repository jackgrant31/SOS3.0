package GUI;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HandleASession implements Runnable{
	private Socket player1;
	private Socket player2;
	
	private DataInputStream fromPlayer1;
	private DataOutputStream toPlayer1; 
	private DataInputStream fromPlayer2; 
	private DataOutputStream toPlayer2;
	
	private int turn =0;
	private int row ;
	private int column ;
	private char so ;
	private int p1score;
	private int p2score;
	private boolean end ;
	private boolean endLoop = true;
	
	public HandleASession(Socket player1, Socket player2) { 
		this.player1 = player1;
		this.player2 = player2;
	}
	
	@Override
	public void run() {
		try {
			fromPlayer1 = new DataInputStream(
				player1.getInputStream());
			toPlayer1 = new DataOutputStream(
				player1.getOutputStream());
			fromPlayer2 = new DataInputStream(
				player2.getInputStream());
			toPlayer2 = new DataOutputStream(
				player2.getOutputStream());
			
			toPlayer1.writeInt(1);
			
			chooseGameMode();
			
			while(endLoop)
			{
				if(turn==0)
					manageMove(fromPlayer1, toPlayer2);
				
				if(turn==1)
					manageMove(fromPlayer2, toPlayer1);
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void manageMove(DataInputStream fromStream, DataOutputStream toStream)
	{
		try {
			row = fromStream.readInt();
			column = fromStream.readInt();
			so = fromStream.readChar();
			p1score = fromStream.readInt();
			p2score = fromStream.readInt();
			end = fromStream.readBoolean();
			turn = fromStream.readInt();
			System.out.println(end);
			
			if (end == true) { 
				if (p1score<p2score)
				{
					writeOne();
					sendMove(toStream, row, column, p1score, p2score, so, false, turn);
					endLoop= false;
				}
				else if(p2score<p1score)
				{
					writeTwo();
					sendMove(toStream, row, column, p1score, p2score, so, false, turn);
					endLoop= false;
				}
				else 
				{
					writeThree();
					sendMove(toStream, row, column, p1score, p2score, so, false, turn);
					endLoop= false;
				}
			}
			else {
				toStream.writeInt(4);
				sendMove(toStream, row, column, p1score, p2score, so, false, turn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void chooseGameMode() throws IOException {
		int mode;
		int size; 
		final int CHOOSE_GAME = 5;
		final int WAIT_FOR_CHOICE = 6;
		
		if(Math.random()>0.5)
		{
			toPlayer1.writeInt(CHOOSE_GAME);
			toPlayer2.writeInt(WAIT_FOR_CHOICE);
			mode = fromPlayer1.readInt();
			size = fromPlayer1.readInt();
		}
		else
		{
			toPlayer1.writeInt(WAIT_FOR_CHOICE);
			toPlayer2.writeInt(CHOOSE_GAME);
			mode = fromPlayer2.readInt();
			size = fromPlayer2.readInt();
		}
		
		toPlayer1.writeInt(mode);
		toPlayer1.writeInt(size);
		toPlayer2.writeInt(mode);
		toPlayer2.writeInt(size);
	}
	
	private void sendMove(DataOutputStream out, int row, int column, int p1, int p2, char so, boolean b, int t)
		throws IOException {
		   	out.writeInt(row);
		   	out.writeInt(column);
		   	out.writeChar(so); 
		   	out.writeInt(p1);
		   	out.writeInt(p2);
		   	out.writeBoolean(b);
		   	out.writeInt(t);
		}
		
	private void writeOne() throws IOException {
		toPlayer1.writeInt(1);
		toPlayer2.writeInt(1);
	}
	
	private void writeTwo() throws IOException {
		toPlayer1.writeInt(2);
		toPlayer2.writeInt(2);
	}
	
	private void writeThree() throws IOException {
		toPlayer1.writeInt(3);
		toPlayer2.writeInt(3);
	}
}
