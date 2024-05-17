package controller;

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
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;

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
    private Button btnReturn;

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
    	List<String> pokemon = obtenerEquipoPokemon(SessionManager.getEntrenador().getId_entrenador());

        if (pokemon.size() == 6) {
            List<PokemonVitalidad> listaVitalidad = obtenerVitalidadPokemon(SessionManager.getEntrenador().getId_entrenador());

            boolean allAtMaxVitality = true;
            for (PokemonVitalidad p : listaVitalidad) {
                if (p.getVitalidad() < p.getVitalidadMax()) {
                    allAtMaxVitality = false;
                    break;
                }
            }

            if (allAtMaxVitality) {
                loadStage("../view/COMBATE-SCENE.fxml", event);
            } else {
                mostrarMensaje("Error", "Uno o más Pokémon no están en su vitalidad máxima. Por favor, recupéralos antes de ir a un combate.");
            }
        } else {
            mostrarMensaje("Error", "No tienes 6 Pokémon en tu equipo. Asegúrate de capturar más Pokémon antes de ir a un combate.");
        }
    }

    @FXML
    void irCrianza(ActionEvent event) {
    	
    	loadStage("../view/CRIANZA-SCENE.fxml", event);

    }

    @FXML
    void irEntrenamiento(ActionEvent event) {
    	
    	mostrarMensaje("Error", "Esta escena no esta creada aun o es inservible");

    }

    @FXML
    void irEquipo(ActionEvent event) {
    	
    	loadStage("../view/EQUIPO-SCENE.fxml", event);

    }
    
    @FXML
    void irTienda(ActionEvent event) {
    	
    	loadStage("../view/TIENDA-SCENE.fxml", event);

    }
    
    @FXML
    void salirMenu(ActionEvent event) {
    	Platform.exit();
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
	
	 private List<String> obtenerEquipoPokemon(int idUsuario) {
	        List<String> pokemon = new ArrayList<>();
	        
	        String sql = "SELECT * FROM pokemon " +
	                     "WHERE id_entrenador = ? " +
	                     "ORDER BY id_pokemon " +
	                     "LIMIT 6";


	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, idUsuario);

	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    pokemon.add(rs.getString("mote"));
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	        return pokemon;
	    }
	 
	 private List<PokemonVitalidad> obtenerVitalidadPokemon(int idEntrenador) {
	        List<PokemonVitalidad> listaVitalidad = new ArrayList<>();

	        String sql = "SELECT mote, vitalidad_max, vitalidad FROM pokemon WHERE id_entrenador = ? LIMIT 6";

	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, idEntrenador);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    String mote = rs.getString("mote");
	                    int vitalidadMax = rs.getInt("vitalidad_max");
	                    int vitalidad = rs.getInt("vitalidad");
	                    listaVitalidad.add(new PokemonVitalidad(mote, vitalidadMax, vitalidad));
	                }
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }

	        return listaVitalidad;
	    }

	    // Clase interna para manejar la vitalidad de los Pokémon
	    private static class PokemonVitalidad {
	        String mote;
	        int vitalidadMax;
	        int vitalidad;

	        PokemonVitalidad(String mote, int vitalidadMax, int vitalidad) {
	            this.mote = mote;
	            this.vitalidadMax = vitalidadMax;
	            this.vitalidad = vitalidad;
	        }
	        
	        public int getVitalidad() {
	        	return vitalidad;
	        }
	        
	        public int getVitalidadMax() {
	        	return vitalidadMax;
	        }
	    }
		
	    private void mostrarMensaje(String titulo, String mensaje) {
	        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
	        alerta.setTitle(titulo);
	        alerta.setHeaderText(null);
	        alerta.setContentText(mensaje);
	        alerta.showAndWait();
	    }

}
