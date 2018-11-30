package GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GUITable {
	private TableView<TableData> table;
	private TableColumn<TableData,String> turnCol;
	private TableColumn<TableData,Integer> startCol;
	private TableColumn<TableData,Integer> endCol;
	private TableColumn<TableData,String> score1Col;
	private TableColumn<TableData,String> score2Col;
	private ObservableList<TableData> list;

	public GUITable()
	{
		createTable();
	}
	
	public void createTable()
	{
		table = new TableView<TableData>();
		turnCol = new TableColumn<TableData,String>("Player");
        startCol = new TableColumn<TableData,Integer>("Start");
        endCol = new TableColumn<TableData,Integer>("End");
        score1Col = new TableColumn<TableData,String>("Score1");
        score2Col = new TableColumn<TableData,String>("Score2");
        
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        	score1Col.setCellValueFactory(new PropertyValueFactory<>("score1"));
        	score2Col.setCellValueFactory(new PropertyValueFactory<>("score2"));
        	endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        	turnCol.setCellValueFactory(new PropertyValueFactory<>("turn"));
        	
        	table.getColumns().addAll(turnCol,startCol,endCol,score1Col,score2Col);
	}
	
	public void setList(int start1, int end1, String score1, String score2, String name)
	{
		if(list==null)
		{
			TableData data = new TableData(start1, end1, score1, score2, name);
			list = FXCollections.observableArrayList(data);
			table.setItems(list);
		}
		else
		{
			TableData data = new TableData(start1, end1, score1,score2, name);
			list.add(data);
			table.setItems(list);
		}
	}
	
	public TableView<TableData> getTable()
	{
		return table;
	}
}
