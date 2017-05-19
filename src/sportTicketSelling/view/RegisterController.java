package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class RegisterController {

	@FXML
	private Button RegisterBack;
	
	@FXML
	private Button RegisterButton;
	
	@FXML
	private TextField userNameField;
	
	@FXML
	private TextField passwordField;
	
	@FXML 
	private TextField forgotField;
	
	

	private Main main;
	
	
	@FXML
	private void initialize() throws IOException {
	
		RegisterBack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					main.showLoginView();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	});
		
		RegisterButton.setOnAction(e -> { try {
			main.handleRegister(userNameField.getText(), passwordField.getText(), forgotField.getText());
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		}); 
	}
}
