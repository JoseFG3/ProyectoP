package controller;
 
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;
 
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
    private Button irPadres;
    
    @FXML
    Button salir;
    
    @FXML
    void salir(ActionEvent event) {
    	
    	loadStage("../view/MENU-SCENE.fxml", event);
 
    }
 
    @FXML
    void abrirHuevo(ActionEvent event) {
        if (imgPadre.getImage() != null && imgMadre.getImage() != null) {
            // Aquí va la lógica para abrir el huevo
            mostrarMensaje("Huevo abierto. ¡Buena suerte con la eclosión!");
        } else {
            mostrarMensajeError("Debes seleccionar tanto al padre como a la madre antes de abrir el huevo.");
        }
    }

    @FXML
    void finCrianza(ActionEvent event) {
        if (imgPadre.getImage() != null && imgMadre.getImage() != null) {
            // Aquí va la lógica para finalizar la crianza
            mostrarMensaje("Emparejamiento finalizado, padre y madre seleccionados.");
        } else {
            mostrarMensajeError("Debes seleccionar tanto al padre como a la madre para finalizar la crianza.");
        }
    }

    // Método auxiliar para mostrar un mensaje de información
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método auxiliar para mostrar un mensaje de error
    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


 
 
    @FXML
    void irEscogerPadres(ActionEvent event) {
    	
    	loadStage("../view/PokemonPadres.fxml", event);
 
    }
    
    
    
    public void setMadrePokemonImage(String pokemonMote) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            String sql = "SELECT imagen FROM pokedex WHERE nom_pokemon = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, pokemonMote);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        byte[] bytesImagen = rs.getBytes("imagen");
                        Image imagen = new Image(new ByteArrayInputStream(bytesImagen));
                        imgMadre.setImage(imagen);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        }
    }
    public void setPadrePokemonImage(String pokemonMote) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            String sql = "SELECT imagen FROM pokedex WHERE nom_pokemon = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, pokemonMote);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        byte[] bytesImagen = rs.getBytes("imagen");
                        Image imagen = new Image(new ByteArrayInputStream(bytesImagen));
                        imgPadre.setImage(imagen);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        }
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