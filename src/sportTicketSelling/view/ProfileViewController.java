package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Window;


public class ProfileViewController {
	@FXML
	private Button closeButton;
	
	@FXML 
	private Button upgradeButton;
	
	@FXML
	private Text usernameText;
	
	@FXML 
	private Text statusText;
	
	@FXML
	private Text pointText;
	
	@FXML
	private Text creditText;
	
	
	private Main main;
	
	
	@FXML
	private void initialize() throws IOException {
	
		closeButton.setOnAction(e-> main.closeSecondary());
		
		upgradeButton.setOnAction(e-> {
			try {
			main.showUpgradeView();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
		
		main.initProfile(usernameText, statusText, pointText,creditText, upgradeButton);
		
		
	}
}
