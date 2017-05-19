package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class ForgotPassController {
	@FXML
	private TextField usernameField;
	
	@FXML
	private TextField ansField;
	
	@FXML
	private PasswordField passField;
	
	@FXML
	private Button backButton;
	
	@FXML
	private Button submitButton;
	
	@FXML
	private Label status;
	
	private Main main;

	@FXML
	private void initialize() throws IOException {
		status.setVisible(false);

        backButton.setOnAction(e-> main.closeSecondary());

        submitButton.setOnAction(e -> {
            String username=usernameField.getText();
            String ans=ansField.getText();

            String pass = passField.getText();

            Boolean b=main.handleForgotPassSubmit(username,ans,pass);
            
            if (b)
            {
            	main.createAlertBox("Success", "your password had been changed");
            	status.setVisible(true);
                status.setText("Password had been changed");
            } else {
            	status.setVisible(true);
                status.setText("Incorrect username/answers, Please try again");
            }
        });

    }
}
