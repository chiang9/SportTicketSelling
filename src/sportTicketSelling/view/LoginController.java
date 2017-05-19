package sportTicketSelling.view;

import java.io.IOException;

import sportTicketSelling.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
	@FXML
	private Button RegisterButton;

	@FXML
	private Button forgotPassButton;

	@FXML
	private Button loginButton;
	
	
	@FXML
	private TextField userLoginField;

	@FXML
	private PasswordField passwordLoginField;

	@FXML
	private Text status;

	private Main main;

	@FXML
	private void initialize() throws IOException {
		status.setVisible(false);
		
		forgotPassButton.setOnAction(e-> {
			try {
				main.showForgotPassView();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		RegisterButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					main.showRegisterView();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		});

		loginButton.setOnAction(e -> {
			int loginT = main.handleLogin(userLoginField.getText(), passwordLoginField.getText());
			if (loginT == 1) {
				try {
					main.showMainView();

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (loginT == 2) {
				try {
					main.showAdminView();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (loginT == 0) {
				status.setVisible(true);
			}
		});
	}

}
