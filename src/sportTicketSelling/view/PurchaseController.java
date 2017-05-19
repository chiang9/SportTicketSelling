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


public class PurchaseController {
	
	@FXML 
	private Button comfirmButton;
	
	@FXML
	private TextField creditCardField;
	
	@FXML
	private Button closeButton;
	
	private Main main;
	
	@FXML
	private void initialize() throws IOException {
		
		comfirmButton.setOnAction(e-> {
			main.handlePurchaseComfirm(creditCardField.getText());
		});
		
		closeButton.setOnAction(e->{
			main.closeSecondary();
		});
		
		
	}

}
