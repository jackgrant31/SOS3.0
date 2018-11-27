package GUI;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class GUIDropDown {

	public GUIDropDown() {
		
	}
	
	public ComboBox<String> create( ObservableList<String> options)
	{
		ComboBox<String> gridCombo = new ComboBox<String>(options);
		gridCombo.getSelectionModel().selectFirst();
		return gridCombo;
	}
	
	public HBox combine(String label, ComboBox<String> gridCombo) {
		Label gridSize = new Label(label);
		HBox gridBox = new HBox();
		gridBox.getChildren().addAll(gridSize,gridCombo);
		gridBox.setSpacing(10);
		return gridBox;
	}
}
