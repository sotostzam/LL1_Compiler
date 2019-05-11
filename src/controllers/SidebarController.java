package controllers;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import application.Compiler;

import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SidebarController implements Initializable {
	
	@FXML
	private BorderPane rootPane;
    @FXML
    private Button btnHome, btnConvertRules, btnImportRules;
    @FXML
    private Button btnLexicalAnalysis;
    @FXML
    private Button btnSyntaxAnalysis;
    @FXML
    private Button btnIntCode;
    @FXML
    private Button btnOptions;
    @FXML
    private Button btnAbout;
    @FXML
    private Button btnExit;

	@Override
    public void initialize(URL url, ResourceBundle rb) {
		btnHome.setCursor(Cursor.HAND);
		btnLexicalAnalysis.setCursor(Cursor.HAND);
		btnSyntaxAnalysis.setCursor(Cursor.HAND);
		btnIntCode.setCursor(Cursor.HAND);
		btnOptions.setCursor(Cursor.HAND);
		btnAbout.setCursor(Cursor.HAND);
		btnExit.setCursor(Cursor.HAND);
		
		btnConvertRules.setDisable(true);
		btnImportRules.setDisable(true);
		btnLexicalAnalysis.setDisable(true);
		btnIntCode.setDisable(true);
	}
	
	@FXML
	private void showHomeUI() {
		loadUI("Homepage");
	}
	@FXML
	private void showImportRulesUI() {
		loadUI("ImportRules");
	}
	@FXML
	private void showSyntaxAnalysisUI() {
		loadUI("SyntaxAnalysis");
	}
	@FXML
	private void showIntermediateCodeUI() {
		loadUI("IntermediateCode");
	}
	@FXML
	private void showOptionsUI() {
		loadUI("Options");
	}	
	@FXML
	private void showAboutUI() {
		loadUI("About");
	}
	@FXML
	private void exit(){
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	
	private void loadUI (String UI) {
    	InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/bundles/" + Compiler.language), StandardCharsets.UTF_8);
		Parent root = null;
        try {
    		root = FXMLLoader.load(getClass().getResource("/view/" + UI + ".fxml"), new PropertyResourceBundle(isr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        rootPane.setCenter(root);
	}
}
