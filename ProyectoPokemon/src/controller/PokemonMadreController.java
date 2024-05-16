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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;
 
public class PokemonMadreController implements Initializable {
 
    @FXML
    private Button btnSalir;
 
    @FXML
    private Button escogerMadre1;
 
    @FXML
    private Button escogerMadre2;
 
    @FXML
    private Button escogerMadre3;
 
    @FXML
    private Button escogerMadre4;
 
    @FXML
    private Button escogerMadre5;
 
    @FXML
    private Button escogerMadre6;
 
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
 
    @FXML
    void escogerMadre1(ActionEvent event) {
 
    }
 
    @FXML
    void escogerMadre2(ActionEvent event) {
 
    }
 
    @FXML
    void escogerMadre3(ActionEvent event) {
 
    }
 
    @FXML
    void escogerMadre4(ActionEvent event) {
 
    }
 
    @FXML
    void escogerMadre5(ActionEvent event) {
 
    }
 
    @FXML
    void escogerMadre6(ActionEvent event) {
 
    }
 
    @FXML
    void salir(ActionEvent event) {
    	
    	loadStage("../view/CRIANZA-SCENE.fxml", event);
 
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
    
    private List<String> obtenerPokemonHembra(int idUsuario) {
        List<String> pokemonHembra = new ArrayList<>();
        
        String sql = "SELECT * FROM pokemon " +
                     "WHERE id_entrenador = ? " +
                     "AND sexo = 'F' " +
                     "ORDER BY id_pokemon " +
                     "LIMIT 6";
 
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, idUsuario);
 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	pokemonHembra.add(rs.getString("mote"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        while (pokemonHembra.size() < 6) {
        	pokemonHembra.add(null);
        }
        return pokemonHembra;
    }
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		String id_usuario = SessionManager.getEntrenador().getNom_entrenador();
    	List<String> pokemonHembra = obtenerPokemonHembra(SessionManager.getEntrenador().getId_entrenador());
    	
        if (pokemonHembra.get(0) != null) {
            cambiarImagen(imgPokemon1, pokemonHembra.get(0));
            labelPokemon1.setText(pokemonHembra.get(0));
        } else {
        	labelPokemon1.setText("");
        }
        
        if (pokemonHembra.get(1) != null) {
            cambiarImagen(imgPokemon2, pokemonHembra.get(1));
            labelPokemon2.setText(pokemonHembra.get(1));
        } else {
        	labelPokemon2.setText("");
        }
        
        if (pokemonHembra.get(2) != null) {
            cambiarImagen(imgPokemon3, pokemonHembra.get(2));
            labelPokemon3.setText(pokemonHembra.get(2));
        } else {
        	labelPokemon3.setText("");
        }
        
        if (pokemonHembra.get(3) != null) {
            cambiarImagen(imgPokemon4, pokemonHembra.get(3));
            labelPokemon4.setText(pokemonHembra.get(3));
        } else {
        	labelPokemon4.setText("");
        }
        
        if (pokemonHembra.get(4) != null) {
            cambiarImagen(imgPokemon5, pokemonHembra.get(4));
            labelPokemon5.setText(pokemonHembra.get(4));
        } else {
        	labelPokemon5.setText("");
        }
        
        if (pokemonHembra.get(5) != null) {
            cambiarImagen(imgPokemon6, pokemonHembra.get(5));
            labelPokemon6.setText(pokemonHembra.get(5));
        } else {
        	labelPokemon6.setText("");
        }
		
	}
 
}
