package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.PokedexManager;
import util.SessionManager;

public class CombateController implements Initializable {

    @FXML
    private Button btnCombate;

    @FXML
    private Button btnHuir;

    @FXML
    private Button btnMochila;

    @FXML
    private Button btnPokemon;

    @FXML
    private ImageView imgPokemon;

    @FXML
    private ImageView imgPokemonRival;
    
    @FXML
    private Label nombreUsuario;
    
    @FXML
    private Label nombrePokemon;
    
    @FXML
    private Label nombreRival;
    
    @FXML
    private Label nombrePokemonRival;

    @FXML
    void empezarCombate(ActionEvent event) {

    }

    @FXML
    void huirMenu(ActionEvent event) {
    	loadStage("../view/MENU-SCENE.fxml", event);
    }

    @FXML
    void irMochila(ActionEvent event) {
    	loadStage("../view/MOCHILA-SCENE.fxml", event);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	nombreUsuario.setText(SessionManager.getEntrenador().getNom_entrenador());
    	
    	cambiarImagen(imgPokemon, "Mew");
    	cambiarImagen(imgPokemonRival, "Charizard");
    	
    	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            String entrenadorRival = seleccionarEntrenadorRivalAleatorio(conn);
            if (entrenadorRival != null) {
                nombreRival.setText(entrenadorRival);
            } else {
                nombreRival.setText("Entrenador Rival");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            nombreRival.setText("Entrenador Rival");
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
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Metodo para cambiar imagen
    private void cambiarImagen(ImageView imageView, String id_pokemon) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            // Ejecutar una consulta para obtener la imagen de la base de datos
            String sql = "SELECT imagen FROM pokedex WHERE nom_pokemon = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id_pokemon); // Aquí necesitas proporcionar el id de la imagen que deseas recuperar
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Recuperar la imagen como un conjunto de bytes desde la base de datos
                        byte[] bytesImagen = rs.getBytes("imagen");

                        // Convertir los bytes de la imagen en un objeto Image de JavaFX
                        Image imagen = new Image(new ByteArrayInputStream(bytesImagen));

                        // Establecer la imagen en el ImageView
                        imageView.setImage(imagen);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        }
    }
    
 // Método para seleccionar aleatoriamente un entrenador rival
    private String seleccionarEntrenadorRivalAleatorio(Connection conn) throws SQLException {
        List<String> entrenadoresRivales = new ArrayList<>();
        String sql = "SELECT nom_entrenador FROM entrenador";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entrenadoresRivales.add(rs.getString("nom_entrenador"));
                }
            }
        }

        // Si no hay entrenadores rivales en la base de datos, retornar null
        if (entrenadoresRivales.isEmpty()) {
            return null;
        }

        // Seleccionar aleatoriamente un entrenador rival de la lista
        Random random = new Random();
        int indice = random.nextInt(10) + 1;
        return entrenadoresRivales.get(indice);
    }

    
    
}
