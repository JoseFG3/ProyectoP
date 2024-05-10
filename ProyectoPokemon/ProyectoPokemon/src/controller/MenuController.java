package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class MenuController  implements Initializable{

    @FXML
    private Button btnCaptura;

    @FXML
    private Button btnCenroPokemon;

    @FXML
    private Button btnCombate;

    @FXML
    private Button btnCrianza;

    @FXML
    private Button btnEntrenamiento;

    @FXML
    private Button btnEquipo;
    
    @FXML
    private Button btnTienda;

    @FXML
    void irCaptura(ActionEvent event) {
    	
    	loadStage("../view/CAPTURA-SCENE.fxml", event);

    }

    @FXML
    void irCentroPokemon(ActionEvent event) {
    	
    	loadStage("../view/CENTRO-POKEMON-SCENE.fxml", event);

    }

    @FXML
    void irCombate(ActionEvent event) {
    	
    	loadStage("../view/COMBATE-SCENE.fxml", event);

    }

    @FXML
    void irCrianza(ActionEvent event) {
    	
    	loadStage("../view/CRIANZA-SCENE.fxml", event);

    }

    @FXML
    void irEntrenamiento(ActionEvent event) {
    	
    	loadStage("../view/ENTRENAMIENTO-SCENE.fxml", event);

    }

    @FXML
    void irEquipo(ActionEvent event) {
    	
    	loadStage("../view/EQUIPO-SCENE.fxml", event);

    }
    
    @FXML
    void irTienda(ActionEvent event) {
    	
    	loadStage("../view/TIENDA-SCENE.fxml", event);

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	private void loadStage(String url, Event event) {

		try {

			Object eventSource = event.getSource();
			Node sourceAsNode = (Node) eventSource;
			Scene oldScene = sourceAsNode.getScene();
			Window window = oldScene.getWindow();
			Stage stage = (Stage) window;
			stage.hide();

			Parent root = FXMLLoader.load(getClass().getResource(url));
			Scene scene = new Scene(root);

			Stage newStage = new Stage();
			newStage.setScene(scene);
			newStage.show();

			newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Platform.exit();
				}
			});

		} catch (IOException ex) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

}
