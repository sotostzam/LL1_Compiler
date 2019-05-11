package application;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.PropertyResourceBundle;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class GraphicsFXML extends Application {
	
	private double x,y;
	
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	InputStream is = getClass().getResourceAsStream("/bundles/" + Compiler.language);
    	InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
    	Parent root = FXMLLoader.load(getClass().getResource("/view/Sidebar.fxml"), new PropertyResourceBundle(isr));
    	
    	root.setOnMousePressed(new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			x = event.getSceneX();
    			y = event.getSceneY();
    		}
    	});
    	
    	root.setOnMouseDragged(new EventHandler<MouseEvent>() {
    		@Override
    		public void handle(MouseEvent event) {
    			primaryStage.setX(event.getScreenX() - x);
    			primaryStage.setY(event.getScreenY() - y);
    		}
    	});
    	
        primaryStage.setTitle("LL(1) Parser");
    	primaryStage.getIcons().add(new Image("images/LL1_Logo.png"));
    	primaryStage.setMinWidth(1280);
    	primaryStage.setMinHeight(720);
    	primaryStage.setScene(new Scene(root, 1280, 720));
    	primaryStage.initStyle(StageStyle.UNDECORATED);
    	primaryStage.show();
    	
    }
	
    public static void startGraphics(String[] args) {
    	//System.out.println(javafx.scene.text.Font.getFamilies());
      	launch(args);
    }
}
