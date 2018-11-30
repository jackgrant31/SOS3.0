package Logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class AI {
	private char letter1 = 'S', letter2 = 'O';
	private int length;

	private AI(int len)
	{
		length = len;
	}
	
	public static void main(String[] args)
	{
		AI ai = new AI(3);
		ai.readCSV(3);
	}
	
	private char[] pickRandomMove()
	{
		char[] values = new char[3];
		
		if (Math.random()<0.5)
			values[0] = letter1;
		else
			values[0] = letter2;
		
		values[1] = getPos(length);
		
		values[2] = getPos(length);
		
		System.out.println(Arrays.toString(values));
		
		return values;
	}
	
	private char[] pickMove(int row)
	{
		char[] values = new char[3];
		
		int sequence = readCSV(row);
		
		System.out.println(Arrays.toString(values));
		
		return values;
	}
	
	private int readCSV(int row)
	{
		String file = "qL.csv";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int count = 0;
			String line = "";
			while ((line = br.readLine()) != null) {
				if(count == row)
				{
					String[] values = line.split(",");
					System.out.println(Arrays.toString(values));
					int maxval = 0;
					for(int i=0; i<values.length; i++)
					{
						if (Integer.parseInt(values[i])>maxval)
							maxval = Integer.parseInt(values[i]);
					}
					for(int i=0; i<values.length; i++)
					{
						if (Integer.parseInt(values[i])==maxval)
							return i;
					}
				}
				count++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
	        ex.printStackTrace();
	    }
		return 0;
	}
	
	private void initCSV()
	{
		try {
		    PrintWriter pw = new PrintWriter(new File("qL.csv"));
	        StringBuilder sb = new StringBuilder();
			
		    for(int row=0; row<19683;row++)
		    {
		    		for(int col=0; col<19683;col++)
		    		{
		    			sb.append("0");
		    			sb.append(',');
		    		}
		    		sb.append('\n');
		    }
		    pw.write(sb.toString());
	        pw.close();
		}
		catch(Exception ioe) {
		    ioe.printStackTrace();
		}
	}
	
	private char getPos(int len)
	{
		return (char)((int) (Math.random()*len) + 48);
	}
}
