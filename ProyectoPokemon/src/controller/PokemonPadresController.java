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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;
 
public class PokemonPadresController implements Initializable {
	
	@FXML
	private Button irACriar;
 
	@FXML
    private Button btnSalir;

    @FXML
    private Button escogerMadre1;

    @FXML
    private Button escogerMadre2;

    @FXML
    private Button escogerMadre3;

    @FXML
    private Button escogerPadre1;

    @FXML
    private Button escogerPadre2;

    @FXML
    private Button escogerPadre3;

    @FXML
    private ImageView imgMadre1;

    @FXML
    private ImageView imgMadre2;

    @FXML
    private ImageView imgMadre3;

    @FXML
    private ImageView imgPadre1;

    @FXML
    private ImageView imgPadre2;

    @FXML
    private ImageView imgPadre3;

    @FXML
    private Label labelMadre1;

    @FXML
    private Label labelMadre2;

    @FXML
    private Label labelMadre3;

    @FXML
    private Label labelPadre1;

    @FXML
    private Label labelPadre2;

    @FXML
    private Label labelPadre3;
    
    @FXML
    void irACriar(ActionEvent event) {
    	
    	String madreSeleccionada = SessionManager.getMadreSeleccionada();
        String padreSeleccionado = SessionManager.getPadreSeleccionado();
    	
    	 if (madreSeleccionada != null && padreSeleccionado != null) {
             
             loadStage("../view/CRIANZA-SCENE.fxml", event);
         } else {
             
             Alert alert = new Alert(AlertType.WARNING);
             alert.setTitle("Selección incompleta");
             alert.setHeaderText(null);
             alert.setContentText("Debes escoger ambos progenitores.");
             alert.showAndWait();
         }
    	
    	
    }

    @FXML
    void escogerMadre1(ActionEvent event) {
    	
    	progenitoraSeleccionada(event);

    }

    @FXML
    void escogerMadre2(ActionEvent event) {
    	
    	progenitoraSeleccionada(event);

    }

    @FXML
    void escogerMadre3(ActionEvent event) {
    	
    	progenitoraSeleccionada(event);

    }

    @FXML
    void escogerPadre1(ActionEvent event) {
    	
    	progenitorSeleccionado(event);

    }

    @FXML
    void escogerPadre2(ActionEvent event) {
    	
    	progenitorSeleccionado(event);

    }

    @FXML
    void escogerPadre3(ActionEvent event) {
    	
    	progenitorSeleccionado(event);

    }
	    
    @FXML
    void salir(ActionEvent event) {
    	
    	loadStage("../view/CRIANZA-SCENE.fxml", event);
 
    }
    
    private List<String> obtenerPokemonHembra(int idUsuario) {
        List<String> pokemonHembra = new ArrayList<>();
        
        String sql = "SELECT * FROM pokemon " +
                     "WHERE id_entrenador = ? " +
                     "AND sexo = 'F' " +
                     "ORDER BY id_pokemon " +
                     "LIMIT 3";

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
    
    private void progenitoraSeleccionada(Event event) {
        Button sourceButton = (Button) event.getSource();
        int pokemonIndex = Integer.parseInt(sourceButton.getId().substring(sourceButton.getId().length() - 1)) - 1;

        List<String> pokemonHembra = obtenerPokemonHembra(SessionManager.getEntrenador().getId_entrenador());

        if (pokemonIndex >= 0 && pokemonIndex < pokemonHembra.size() && pokemonHembra.get(pokemonIndex) != null) {
            String madreSeleccionada = pokemonHembra.get(pokemonIndex);
            SessionManager.setMadreSeleccionada(madreSeleccionada);
            
        }
    }

    private void progenitorSeleccionado(Event event) {
        Button sourceButton = (Button) event.getSource();
        int pokemonIndex = Integer.parseInt(sourceButton.getId().substring(sourceButton.getId().length() - 1)) - 1;

        List<String> pokemonMacho = obtenerPokemonMacho(SessionManager.getEntrenador().getId_entrenador());

        if (pokemonIndex >= 0 && pokemonIndex < pokemonMacho.size() && pokemonMacho.get(pokemonIndex) != null) {
            String padreSeleccionado = pokemonMacho.get(pokemonIndex);
            SessionManager.setPadreSeleccionado(padreSeleccionado);
            
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Parent root = loader.load();

            // Obtén el controlador de la nueva escena y pasa el Pokémon seleccionado
            CrianzaController controller = loader.getController();
            controller.setPadrePokemonImage(SessionManager.getPadreSeleccionado());
            controller.setMadrePokemonImage(SessionManager.getMadreSeleccionada());

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
            Logger.getLogger(PokemonPadresController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private List<String> obtenerPokemonMacho(int idUsuario) {
        List<String> pokemonMacho = new ArrayList<>();
        
        String sql = "SELECT * FROM pokemon " +
                     "WHERE id_entrenador = ? " +
                     "AND sexo = 'M' " +
                     "ORDER BY id_pokemon " +
                     "LIMIT 3";
 
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, idUsuario);
 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pokemonMacho.add(rs.getString("mote"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        while (pokemonMacho.size() < 6) {
        	pokemonMacho.add(null);
        }
        return pokemonMacho;
    }
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		String id_usuario = SessionManager.getEntrenador().getNom_entrenador();
    	List<String> pokemonMacho = obtenerPokemonMacho(SessionManager.getEntrenador().getId_entrenador());
    	List<String> pokemonHembra = obtenerPokemonHembra(SessionManager.getEntrenador().getId_entrenador());
    	
        if (pokemonMacho.get(0) != null) {
            cambiarImagen(imgPadre1, pokemonMacho.get(0));
            labelPadre1.setText(pokemonMacho.get(0));
        } else {
        	labelPadre1.setText("");
        }
        
        if (pokemonHembra.get(0) != null) {
            cambiarImagen(imgMadre1, pokemonHembra.get(0));
            labelMadre1.setText(pokemonHembra.get(0));
        } else {
        	labelMadre1.setText("");
        }
        
        if (pokemonMacho.get(1) != null) {
            cambiarImagen(imgPadre2, pokemonMacho.get(1));
            labelPadre2.setText(pokemonMacho.get(1));
        } else {
        	labelPadre2.setText("");
        }
        
        if (pokemonHembra.get(1) != null) {
            cambiarImagen(imgMadre2, pokemonHembra.get(1));
            labelMadre2.setText(pokemonHembra.get(1));
        } else {
        	labelMadre2.setText("");
        }
        
        if (pokemonMacho.get(2) != null) {
            cambiarImagen(imgPadre3, pokemonMacho.get(2));
            labelPadre3.setText(pokemonMacho.get(2));
        } else {
        	labelPadre3.setText("");
        }
        
        if (pokemonHembra.get(2) != null) {
            cambiarImagen(imgMadre3, pokemonHembra.get(2));
            labelMadre3.setText(pokemonHembra.get(2));
        } else {
        	labelMadre3.setText("");
        }
		
	}
 
}
