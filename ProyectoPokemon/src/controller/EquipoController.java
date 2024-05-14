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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;
import javafx.application.Platform;

public class EquipoController implements Initializable {

    @FXML
    private Button btnCajaPokemon;

    @FXML
    private Button btnSalir;

    @FXML
    private ImageView imgRectangulo1;

    @FXML
    private ImageView imgRectangulo2;

    @FXML
    private ImageView imgRectangulo3;

    @FXML
    private ImageView imgRectangulo4;

    @FXML
    private ImageView imgRectangulo5;

    @FXML
    private ImageView imgRectangulo6;
    
    @FXML
    private ImageView imgPokemon1;
    
    @FXML
    private ImageView imgPokemon2;
    
    @FXML
    private ImageView imgPokemon3;
    
    @FXML
    private ImageView imgPokemon4;
    
    @FXML
    private ImageView imgPokemon5;
    
    @FXML
    private ImageView imgPokemon6;
    
    @FXML
    private Label labelPokemon1;
    
    @FXML
    private Label labelPokemon2;
    
    @FXML
    private Label labelPokemon3;
    
    @FXML
    private Label labelPokemon4;
    
    @FXML
    private Label labelPokemon5;
    
    @FXML
    private Label labelPokemon6;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    	String id_usuario = SessionManager.getEntrenador().getNom_entrenador();
    	List<String> pokemon = obtenerPokemon(id_usuario);
    	
        if (pokemon.get(0) != null) {
            cambiarImagen(imgPokemon1, pokemon.get(0));
            labelPokemon1.setText(pokemon.get(0));
        } else {
        	labelPokemon1.setText("");
        }
        
        if (pokemon.get(1) != null) {
            cambiarImagen(imgPokemon2, pokemon.get(1));
            labelPokemon2.setText(pokemon.get(1));
        } else {
        	labelPokemon2.setText("");
        }
        
        if (pokemon.get(2) != null) {
            cambiarImagen(imgPokemon3, pokemon.get(2));
            labelPokemon3.setText(pokemon.get(2));
        } else {
        	labelPokemon3.setText("");
        }
        
        if (pokemon.get(3) != null) {
            cambiarImagen(imgPokemon4, pokemon.get(3));
            labelPokemon4.setText(pokemon.get(3));
        } else {
        	labelPokemon4.setText("");
        }
        
        if (pokemon.get(4) != null) {
            cambiarImagen(imgPokemon5, pokemon.get(4));
            labelPokemon5.setText(pokemon.get(4));
        } else {
        	labelPokemon5.setText("");
        }
        
        if (pokemon.get(5) != null) {
            cambiarImagen(imgPokemon6, pokemon.get(5));
            labelPokemon6.setText(pokemon.get(5));
        } else {
        	labelPokemon6.setText("");
        }
    }

    @FXML
    private void irCajaPokemon(ActionEvent event) {
        loadStage("../view/CAJA-POKEMON.fxml", event);
    }

    @FXML
    private void salir(ActionEvent event) {
        loadStage("../view/MENU-SCENE.fxml", event);
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
    
    private List<String> obtenerPokemon(String idUsuario) {
        List<String> pokemon = new ArrayList<>();
        
        String sql = "SELECT * FROM pokemon " +
                     "WHERE id_entrenador = ? " +
                     "ORDER BY id_pokemon " +
                     "LIMIT 6";


        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pokemon.add(rs.getString("mote"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        while (pokemon.size() < 6) {
        	pokemon.add(null);
        }
        return pokemon;
    }
}

