package GUI;
import java.io.DataOutputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InitGUI extends Application {
	private DataOutputStream toServer;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage window = primaryStage;
		primaryStage.setResizable(false);
		
		GridPane initpane = new GridPane();
		
		Label title = new Label("SOS GAME");
		title.setFont(new Font("Avenir", 30));
		
		Label player1label = new Label("Name for Player 1:");
		TextField player1entry = new TextField ();
		HBox player1box = new HBox();
		player1box.getChildren().addAll(player1label, player1entry);
		player1box.setSpacing(10);
		
		Label player2label = new Label("Name for Player 2:");
		TextField player2entry = new TextField ();
		HBox player2box = new HBox();
		player2box.getChildren().addAll(player2label, player2entry);
		player2box.setSpacing(10);
		
		GUIDropDown guidd = new GUIDropDown();
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "3x3",
			        "4x4",
			        "5x5",
			        "6x6",
			        "7x7",
			        "8x8",
			        "9x9",
			        "10x10"
			    );
		ComboBox<String> gridCombo = guidd.create(options);
		HBox gridBox = guidd.combine("Size of Grid", gridCombo);
		
		ObservableList<String> choices = 
			    FXCollections.observableArrayList(
			        "Normal",
			        "Extreme",
			        "Combat"
			    );
		ComboBox<String> modeCombo = guidd.create(choices);
		HBox modeBox = guidd.combine("Game Mode", modeCombo);
		
		GUIButtons end = new GUIButtons();
		Button endButton = end.createButton("Press when finished");

		player1box.setAlignment(Pos.CENTER);
		player2box.setAlignment(Pos.CENTER);
		gridBox.setAlignment(Pos.CENTER);
		modeBox.setAlignment(Pos.CENTER);
		initpane.add(title,0,0);
		initpane.add(player1box,0,1);
		initpane.add(player2box, 0, 2);
		initpane.add(gridBox, 0, 3);
		initpane.add(modeBox,0,4);
		initpane.add(endButton, 0, 5);
		initpane.setAlignment(Pos.CENTER);
		initpane.setVgap(10);
		Scene initialScene = new Scene(initpane, 500, 600);
		window.setScene(initialScene);	
		
		endButton.setOnAction(e -> {
			try {
				handleButtonClick(window, gridCombo, modeCombo);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		window.show();
	}

	private void handleButtonClick(Stage window, ComboBox<String> gridCombo, ComboBox<String> modeCombo)
			throws IOException {
		int modenum;
		if (modeCombo.getValue().equals("Normal"))
			modenum=0;
		else if(modeCombo.getValue().equals("Extreme"))
			modenum=1;
		else
			modenum=2;
		int length = Integer.parseInt(String.valueOf(gridCombo.getValue().charAt(0)));
		if(length==1)
			length = Integer.parseInt(String.valueOf(gridCombo.getValue().charAt(0))+String.valueOf(gridCombo.getValue().charAt(1)));
		toServer.writeInt(modenum);
		toServer.writeInt(length);
		window.close();
	}

	public void setOutput(DataOutputStream toServer2) {
		toServer = toServer2;
	}
}