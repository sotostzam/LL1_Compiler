package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class HomeController implements Initializable {
	
    @FXML
	private Button btnGettingStarted;
    
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		btnGettingStarted.setCursor(Cursor.HAND);
	}
}
