package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class CheckOutController {

	@FXML
	private Button purchaseButton;
	
	@FXML
	private Button closeButton;
	
	@FXML
	private Button pointButton;
	
	@FXML
	private GridPane itemGridPane;
	
	private Main main;
	
	
	@FXML
	private void initialize() throws IOException {
	
		main.handleCheckOut(itemGridPane);
		
		closeButton.setOnAction(e -> main.closeSecondary());
		
		purchaseButton.setOnAction(e -> {try {
			main.handlePurchase(itemGridPane);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
		
		pointButton.setOnAction(e-> main.handlePointPurchase(itemGridPane));
		
	}
}
