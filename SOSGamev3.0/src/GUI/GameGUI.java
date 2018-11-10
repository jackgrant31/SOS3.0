package GUI;
import Logic.CombatGameLogic;
import Logic.ExtremeGameLogic;
import Logic.GameLogic;
import Logic.Timer1;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameGUI extends Application{
	
	public GameGUI(int prevLength, String name1, String name2, int game)
	{
		length = prevLength;
		player1 = name1;
		player2 = name2;
		mode = game;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		primaryStage.setResizable(false);
		
		GridPane gamegridPane = new GridPane();
		gamegridPane.setLayoutX(800);
		gamegridPane.setLayoutY(600);
				
		ObservableList<String> letters = 
			    FXCollections.observableArrayList(
			        letter1,
			        letter2
			    );
		sOro = new ComboBox<String>(letters);
		sOro.getSelectionModel().selectFirst();
		
		GUIButtons resB = new GUIButtons();
		Button restartB = resB.createButton("Restart");
		restartB.setOnAction(e -> {
			resB.restartButton(window);
		});
		
		Button quitB = new Button("Quit");
		quitB.setOnAction(e ->{
			window.close();
		});
		
		Label diffletter = new Label("S or O?:");
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(diffletter,sOro,restartB,quitB);
		vbox.setPadding(new Insets(10,10,10,10));
		vbox.setSpacing(10);
		
		gtable = new GUITable();
		TableView<TableData> table = gtable.getTable();

        timerLabel = new Label("0");
        timerLabel.setTextFill(Color.web("#FF0000"));
        timerLabel.setFont(new Font("Avenir", 30));
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(timerLabel, table);

        if(mode==0)
        		game = new GameLogic(length, player1, player2);
        else if(mode==1)
    			game = new ExtremeGameLogic(length, player1, player2);
        else
    			game = new CombatGameLogic(length, player1, player2);
		Cell[][] cell = new Cell[length][length];
		playerLabel.setText(" "+ player1+" points: 0  "+ player2+" points: 0");
		int turn = game.getTurn();
		if(turn==0)
			turnLabel.setText(" "+player1+"'s turn");
		else
			turnLabel.setText(" "+player2+"'s turn");
		
		BorderPane borderPane = new BorderPane(); 
		borderPane.setCenter(gamegridPane); 
		borderPane.setBottom(turnLabel);
		borderPane.setTop(playerLabel);
		borderPane.setRight(vbox);
		Scene scene  = new Scene(borderPane,1200,600);
		primaryStage.setTitle("SOS Game");
		
		labels = new Label[length*length];
		for(int i=0;i<labels.length;i++)
			labels[i]=new Label();
		
		Timer1 time = new Timer1(timerLabel, game, length);
		for (int row = 0; row < length; row++)
			for (int col = 0; col < length; col++) 
				gamegridPane.add(cell[row][col] = new Cell(game, player1, player2, length, sOro,labels,turnLabel,playerLabel, gtable, timerLabel, time), row, col);
		
		window.setScene(scene);
		window.show();
	}
	
	private Label turnLabel = new Label();	
	private String letter1 = "S", letter2 = "O", player1, player2;
	private int length, mode;
	private Label playerLabel =  new Label();
	private Label timerLabel;
	private Stage window;
	private ComboBox<String> sOro;
	private Label[] labels;
	private GUITable gtable;
	private GameLogic game=null;
}

