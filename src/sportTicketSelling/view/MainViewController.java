package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import sportTicketSelling.Match;
import sportTicketSelling.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class MainViewController {
	@FXML
	private Button refundButton;
	
	@FXML
	private Button profileButton;
	
	@FXML
	private Button logoutButton;
	
	@FXML
	private Button searchButton;
	
	@FXML
	private Button checkoutButton;
	
	@FXML
	private Button cheapButton;
	
	@FXML
	private Button interButton;
	
	@FXML
	private Button expensiveButton;
	
	@FXML
	private TextField searchField;

	
	// history table define
	@FXML
	private TableView<Transaction> historyTable;
	@FXML 
	private TableColumn<Transaction, String> iTitle;
	@FXML
	private TableColumn<Transaction, String> iPrice;
	
	@FXML
	private TableColumn<Transaction, String> iComfID;
	@FXML
	private TableColumn<Transaction, String> iCount;
	
	final ObservableList<Transaction> transData = FXCollections.observableArrayList();
	
	// match table
	@FXML
	private TableView<Match> resultTable;
	@FXML 
	private TableColumn<Match, String> sportCol;
	@FXML
	private TableColumn<Match, String> athleteCol;
	@FXML
	private TableColumn<Match, String> locationCol;
	@FXML 
	private TableColumn<Match, String> dateCol;
	@FXML
	private TableColumn<Match, String> priceCol;
	
	final ObservableList<Match> matchData = FXCollections.observableArrayList();
	
	
	
	private Main main;
	
	@FXML
	private void initialize() throws IOException {
		logoutButton.setOnAction(e -> {
			try {
				main.showLoginView();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		refundButton.setOnAction(e -> {
			try {
			int selectId = historyTable.getSelectionModel().getSelectedIndex();
			main.handleRefund(transData, selectId);
			} catch (Exception e1){
				main.createAlertBox("Alert", "Please select item");
			}
		});
		
		profileButton.setOnAction(e -> {
			try {
				main.showProfileView();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		searchButton.setOnAction(e -> {
			try {
				main.handleSearch(searchField.getText(), matchData);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		checkoutButton.setOnAction(e -> {
			ObservableList<Match> selectItems = resultTable.getSelectionModel().getSelectedItems();
			
			try {
				main.showCheckOutView(selectItems);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		cheapButton.setOnAction(e-> {
			try {
				main.handlepriceSearch(50, matchData);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		interButton.setOnAction(e->{
			try {
				main.handlepriceSearch(75, matchData);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		expensiveButton.setOnAction(e->{
			try {
				main.handlepriceSearch(200, matchData);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		// insert data into transaction table
		iTitle.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Title"));
		iPrice.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Price"));
		iComfID.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Comf"));
		iCount.setCellValueFactory(new PropertyValueFactory<Transaction, String>("Count"));
		historyTable.setItems(transData);
		
		// insert data into match table
		sportCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Sport"));
		athleteCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Athlete"));
		locationCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Location"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Price"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Match, String>("Date"));
		resultTable.setItems(matchData);
		
		resultTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		
		main.initTable(transData, matchData);

	}
}
