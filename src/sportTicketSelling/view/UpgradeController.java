package sportTicketSelling.view;

import java.io.IOException;
import javafx.scene.control.Label;
import sportTicketSelling.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class UpgradeController {
	@FXML
	private Button submitButton;
	
	@FXML
	private Button closeButton;
	
	@FXML
	private Text dateField;
    @FXML
    private ChoiceBox<String> feeCBox;

    @FXML
    private TextField cardField;
    @FXML
    private Label status;

    private Main main;

    @FXML
    private void initialize() throws IOException {
    	String extime = main.getExpiryTime();
    	dateField.setText(extime);
//    	int year = 1 + Integer.parseInt(getCurrentTime().split("-")[0]);
//		String md = getCurrentTime().split("-")[1] + "-" +getCurrentTime().split("-")[2];
//		String expiryDate = "" + year + "-" + md;
    	
        status.setText("");

        ObservableList<String> feePicker = FXCollections.observableArrayList();
        feePicker.addAll(
                "$10, for 10 points",
                "$20, for 100 points",
                "$30, for 300 points");

        feeCBox.setItems(feePicker);

        closeButton.setOnAction(e-> main.closeSecondary());

        submitButton.setOnAction(e -> {
            try {
            	if (!feeCBox.getValue().trim().equals("$10, for 10 points") && !feeCBox.getValue().trim().equals("$20, for 100 points") &&
            			!feeCBox.getValue().trim().equals("$30, for 300 points")) {
            		main.createAlertBox("Alert", "Please fill in all information");
            		return;
            	}

                Boolean b = main.handleUpgradeSubmit(cardField.getText().trim(), feeCBox.getValue().trim(), dateField.getText().trim());
                // System.out.print(b);
                if(b){
                    status.setText("Card is Valid");
                }else{
                	status.setText("Card is Invalid");
                	}
            } catch (Exception e2) {
                main.createAlertBox("Alert", "Please fill in all information");
            }
        });



    }

}
