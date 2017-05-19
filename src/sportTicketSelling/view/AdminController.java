package sportTicketSelling.view;

import sportTicketSelling.Athlete;
import sportTicketSelling.Main;
import sportTicketSelling.Match;
import sportTicketSelling.Sport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


public class AdminController {
	
	
	
	@FXML
	private Button aAddButton;
	
	@FXML
	private Button aRemoveButton;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button mAddButton;
	
	@FXML
	private Button mRemoveButton;
	
	@FXML
	private Button sAddButton;
	
	@FXML
	private Button sRemoveButton;
	
	@FXML
	private ListView<Athlete> mAthleteLView;
	private ObservableList<Athlete> athlistViewData = FXCollections.observableArrayList();
	
	@FXML
	private ChoiceBox<String> mSportCBox;
	
	@FXML
	private ChoiceBox<Integer> mTicketCBox;
	
	@FXML
	private ChoiceBox<String> aSportCBox;
	
	@FXML
	private TextField aAgeField;
	
	@FXML
	private TextField priceField;
	
	@FXML
	private TextField aCountryField;
	
	@FXML
	private TextField aNameField;
	
	@FXML
	private TextField mDateField;
	
	@FXML
	private TextField mLocationField;
	
	@FXML
	private TextField sTitleField;
	
	
	// match table
	@FXML
	private TableView<Match> MatchTable;
	@FXML 
	private TableColumn<Match, String> midCol;
	@FXML
	private TableColumn<Match, String> mlocationCol;
	@FXML
	private TableColumn<Match, String> mdateCol;
	@FXML 
	private TableColumn<Match, String> msportCol;
	@FXML
	private TableColumn<Match, String> mpriceCol;
	@FXML
	private TableColumn<Match, Integer> mticketCol;
		
	final ObservableList<Match> matchData = FXCollections.observableArrayList(
			
		);
	
	
	// athlete table
	@FXML
	private TableView<Athlete> AthleteTable;
	@FXML 
	private TableColumn<Athlete, String> aidCol;
	@FXML
	private TableColumn<Athlete, String> anameCol;
	@FXML
	private TableColumn<Athlete, String> acountryCol;
	@FXML
	private TableColumn<Athlete, String> asportCol;
	@FXML
	private TableColumn<Athlete, String> aageCol;
	
	final ObservableList<Athlete> athleteData = FXCollections.observableArrayList(
			
		);
	
	// sport table
	@FXML
	private TableView<Sport> SportTable;
	@FXML 
	private TableColumn<Sport, String> sidCol;
	@FXML
	private TableColumn<Sport, String> stitleCol;
	
	final ObservableList<Sport> sportData = FXCollections.observableArrayList(
			
		);
	
	private Main main;
	
	@FXML
	private void initialize() throws IOException, SQLException {
		mAthleteLView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// insert data into choice box
		ObservableList<String> sportCBoxData = FXCollections.observableArrayList();
		mSportCBox.setItems(sportCBoxData);
		aSportCBox.setItems(sportCBoxData);
				
		
		
		
		logoutButton.setOnAction(e -> {
			try {
				main.showLoginView();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		mAddButton.setOnAction(e -> {
			try {
			String location = mLocationField.getText().trim();
			String date = mDateField.getText().trim();
			String[] dcheck = date.split("-");
			if (dcheck[0].length() != 4 || !isNumeric(dcheck[0])) {
				main.createAlertBox("Alert", "date should be in the format of year-mm-dd");
				return;
			} else if (dcheck[1].length() != 2|| !isNumeric(dcheck[1]) || Integer.parseInt(dcheck[1]) >12 || Integer.parseInt(dcheck[1]) ==0) {
				main.createAlertBox("Alert", "date should be in the format of year-mm-dd");
				return;
			} else if (dcheck[2].length() != 2|| !isNumeric(dcheck[2])|| Integer.parseInt(dcheck[2]) > 31|| Integer.parseInt(dcheck[2]) == 0) {
				main.createAlertBox("Alert", "date should be in the format of year-mm-dd");
				return;
			}
			
			
			
			ObservableList<Athlete> athselected = mAthleteLView.getSelectionModel().getSelectedItems();
			String sport = mSportCBox.getValue().trim();
			int ticketnum = mTicketCBox.getValue();
			String price = priceField.getText();
			if (!isNumeric(price)) {
				main.createAlertBox("Alert", "Please enter the price correctly");
				return;
			}
			
			main.handleAddMatch(matchData, location, date, athselected, ticketnum, sport, priceField.getText(),MatchTable);
			//MatchTable.setItems(matchData);
			} catch (Exception e1) {
				main.createAlertBox("Alert", "please provide all information");
				e1.printStackTrace();
			}
		});
		
		mRemoveButton.setOnAction(e -> {
			try{
			int selectId = MatchTable.getSelectionModel().getSelectedIndex();
			main.handleRemoveMatch(matchData, selectId);
			} catch (Exception e1) {
				main.createAlertBox("Alert", "Please select an item");
			}
		});

		aAddButton.setOnAction(e -> {
			try {
			String name = aNameField.getText();
			String country = aCountryField.getText();
			String age = aAgeField.getText();
			if (!isNumeric(age)) {
				main.createAlertBox("Alert", "age should be a number");
			}
			String sport = aSportCBox.getValue();
			main.handleAddAthlete(athleteData, name, country, sport, age);
			} catch (Exception e2) {
				main.createAlertBox("Alert", "please provide all information");
			}
		});
		

		aRemoveButton.setOnAction(e -> {
			try {
			int selectId = AthleteTable.getSelectionModel().getSelectedIndex();
			main.handleRemoveAthlete(athleteData, selectId);
			} catch (Exception e2) {
				main.createAlertBox("Alert", "Please select an item");
			}
		});
		

		sAddButton.setOnAction(e -> {
			try {
			String title = sTitleField.getText();
			main.handleAddSport(sportData, title, sportCBoxData);
			} catch (Exception e2) {
				main.createAlertBox("Alert", "please provide all information");
			}
		});
		

		sRemoveButton.setOnAction(e -> {
			
			try {
				int selectId = SportTable.getSelectionModel().getSelectedIndex();
				main.handleRemoveSport(sportData, selectId);
				main.adminInit(matchData, athleteData, sportData, athlistViewData, sportCBoxData);
				MatchTable.refresh();
				AthleteTable.refresh();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				main.createAlertBox("Alert", "Please select an item");
			}
			
		});
		
		
		// insert data into match table
		midCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Id"));
		mlocationCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Location"));
		mdateCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Date"));
		msportCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Sport"));
		mpriceCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Price"));
		mticketCol.setCellValueFactory(new PropertyValueFactory<Match, Integer>("Ticket"));
		MatchTable.setItems(matchData);
		
		// insert data into athlete table
		aidCol.setCellValueFactory(new PropertyValueFactory<Athlete, String>("Id"));
		anameCol.setCellValueFactory(new PropertyValueFactory<Athlete, String>("Name"));
		acountryCol.setCellValueFactory(new PropertyValueFactory<Athlete, String>("Country"));
		asportCol.setCellValueFactory(new PropertyValueFactory<Athlete, String>("Sport"));
		aageCol.setCellValueFactory(new PropertyValueFactory<Athlete, String>("Age"));
		AthleteTable.setItems(athleteData);
		
		// insert data into sport table
		sidCol.setCellValueFactory(new PropertyValueFactory<Sport, String>("Id"));
		stitleCol.setCellValueFactory(new PropertyValueFactory<Sport, String>("Title"));
		SportTable.setItems(sportData);
		
		// insert data into ticket picker
		ObservableList<Integer> ticketPicker = FXCollections.observableArrayList();
		ticketPicker.addAll(0,1,2,3,4,5,6,7,8,9,10);
		
		mTicketCBox.setItems(ticketPicker);
		
		// insert data into athlete list view
		mAthleteLView.setCellFactory((list) ->{
			return new ListCell<Athlete>() {
				@Override
				protected void updateItem(Athlete item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.getName() + ", Id: " + item.getId());
					}
				}
			};
		});
		mAthleteLView.setItems(athlistViewData);
		
		
		
		main.adminInit(matchData, athleteData, sportData, athlistViewData, sportCBoxData);
		
	}
	
	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}
}
