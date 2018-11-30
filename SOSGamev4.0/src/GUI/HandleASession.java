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
			int turn =0;
			int row ;
			int column ;
			char so ;
			int p1score;
			int p2score;
			boolean end ;
			int mode;
			int size; 
			
			if(Math.random()>0.5)
			{
				toPlayer1.writeInt(5);
				toPlayer2.writeInt(6);
				mode = fromPlayer1.readInt();
				size = fromPlayer1.readInt();
			}
			else
			{
				toPlayer1.writeInt(6);
				toPlayer2.writeInt(5);
				mode = fromPlayer2.readInt();
				size = fromPlayer2.readInt();
			}
			
			System.out.println(mode+" "+size);
			
			toPlayer1.writeInt(mode);
			toPlayer1.writeInt(size);
			toPlayer2.writeInt(mode);
			toPlayer2.writeInt(size);
			
			while(true)
			{
				System.out.println(turn);
				
				if(turn ==0)
				{
					
					row = fromPlayer1.readInt();
					column = fromPlayer1.readInt();
					so = fromPlayer1.readChar();
					p1score = fromPlayer1.readInt();
					p2score = fromPlayer1.readInt();
					end = fromPlayer1.readBoolean();
					turn = fromPlayer1.readInt();
					
					System.out.println("player 1:"+row+" "+column);
					
					if (end == true) { 
						if (p1score>p2score)
						{
							toPlayer1.writeInt(1);
							toPlayer2.writeInt(1);
							sendMove(toPlayer2, row, column, p1score, p2score, so, false, turn);
							break;
						}
						else if(p2score>p1score)
						{
							toPlayer1.writeInt(2);
							toPlayer2.writeInt(2);
							sendMove(toPlayer2, row, column, p1score, p2score, so, false, turn);
							break;
						}
						else 
						{
							toPlayer1.writeInt(3);
							toPlayer2.writeInt(3);
							sendMove(toPlayer2, row, column, p1score, p2score, so, false, turn);
							break;
						}
					}
					else {
						toPlayer2.writeInt(4);
						sendMove(toPlayer2, row, column, p1score, p2score, so, false, turn);
					}
				}
				
				System.out.println(turn);
				
				if(turn==1)
				{
					row = fromPlayer2.readInt();
					column = fromPlayer2.readInt();
					so = fromPlayer2.readChar();
					p1score = fromPlayer2.readInt();
					p2score = fromPlayer2.readInt();
					end = fromPlayer2.readBoolean();
					turn = fromPlayer2.readInt();
					
					System.out.println("player 2:"+row+" "+column);
	
					if (end == true) { 
						if (p1score>p2score)
						{
							toPlayer1.writeInt(1);
							toPlayer2.writeInt(1);
							sendMove(toPlayer1, row, column, p1score, p2score, so, false, turn);
							break;
						}
						else if(p2score>p1score)
						{
							toPlayer1.writeInt(2);
							toPlayer2.writeInt(2);
							sendMove(toPlayer1, row, column, p1score, p2score, so, false, turn);
							break;
						}
						else 
						{
							toPlayer1.writeInt(3);
							toPlayer2.writeInt(3);
							sendMove(toPlayer1, row, column, p1score, p2score, so, false, turn);
							break;
						}
					}
					else {
						toPlayer1.writeInt(4);
						sendMove(toPlayer1, row, column, p1score, p2score, so, false, turn);
					}
				}
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
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
		
}
