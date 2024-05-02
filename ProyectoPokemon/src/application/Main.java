package application;
	
import java.io.IOException;

import bbdd.Conexion;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws LoadException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LOGIN SCENE.fxml"));
			LoginController controller = new LoginController();
			loader.setController(controller);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Conexion.conexionBbdd();
		launch(args);
	}
}
