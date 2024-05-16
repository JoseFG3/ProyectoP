package controller;
 
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
 
public class CrianzaController {
 
    @FXML
    private Button finCriarza;
 
    @FXML
    private Button huevo;
 
    @FXML
    private ImageView imgMadre;
 
    @FXML
    private ImageView imgPadre;
 
    @FXML
    private Button irHembra;
 
    @FXML
    private Button irMacho;
    
    @FXML
    Button salir;
    
    @FXML
    void salir(ActionEvent event) {
    	
    	loadStage("../view/MENU-SCENE.fxml", event);
 
    }
 
    @FXML
    void abrirHuevo(ActionEvent event) {
 
    }
 
    @FXML
    void finCrianza(ActionEvent event) {
 
    }
 
    @FXML
    void irEscogerHembra(ActionEvent event) {
    	
    	loadStage("../view/PokemonMadre.fxml", event);
 
    }
 
    @FXML
    void irEscogerMacho(ActionEvent event) {
    	
    	loadStage("../view/PokemonMacho.fxml", event);
 
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
            Logger.getLogger(EquipoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
}