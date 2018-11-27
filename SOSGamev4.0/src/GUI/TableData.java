package GUI;
public class TableData {
 
   private int start;
   private int end;
   private String score1;
   private String score2;
   private String turn;
 
   public TableData(int start, int end, String score1, String score2, String turn) {
       this.start = start;
       this.score1 = score1;
       this.score2 = score2;
       this.end = end;
       this.turn = turn;
   }
 
   public int getStart() {
       return start;
   }
 
   public void setStart(int start) {
       this.start = start;
   }
   
   public int getEnd() {
       return end;
   }
 
   public void setEnd(int end) {
       this.end = end;
   }
   
   public String getScore1() {
       return score1;
   }
 
   public void setScore1(String score1) {
       this.score1 = score1;
   }
   
   public String getScore2() {
       return score2;
   }
 
   public void setScore2(String score2) {
       this.score2 = score2;
   }
   
   public String getTurn() {
       return turn;
   }
 
   public void setTurn(String turn) {
       this.turn = turn;
   }
 
 
}