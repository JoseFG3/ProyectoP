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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.CombateSessionManager;
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
    	loadStage("../view/COMBATE-MOVIMIENTOS.fxml", event);
    }

    @FXML
    void huirMenu(ActionEvent event) {
    	CombateSessionManager.clear();
    	loadStage("../view/MENU-SCENE.fxml", event);
    }

    @FXML
    void irMochila(ActionEvent event) {
    	loadStage("../view/MOCHILA-SCENE.fxml", event);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	String id_usuario = SessionManager.getEntrenador().getNom_entrenador();
    	nombreUsuario.setText(id_usuario);

    	
    	List<String> pokemon = obtenerEquipoPokemon(SessionManager.getEntrenador().getId_entrenador());
    	
        if (pokemon.get(0) != null) {
            cambiarImagen(imgPokemon, pokemon.get(0));
            nombrePokemon.setText(pokemon.get(0));
        }

        if (CombateSessionManager.getIdRival() == 0) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
                int idRival = seleccionarEntrenadorRival(conn);
                CombateSessionManager.setIdRival(idRival);
                
                String entrenadorRival = obtenerNombreEntrenador(conn, idRival);
                CombateSessionManager.setNombreEntrenadorRival(entrenadorRival);
                
                String pokemonRival = seleccionarPokemonRivalAleatorio(conn, idRival);
                CombateSessionManager.setNombrePokemonRival(pokemonRival);
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                nombreRival.setText("Entrenador Rival");

            }
        }

        nombreRival.setText(CombateSessionManager.getNombreEntrenadorRival());
        nombrePokemonRival.setText(CombateSessionManager.getNombrePokemonRival());
        cambiarImagen(imgPokemonRival, CombateSessionManager.getNombrePokemonRival());
    	
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
    
    //Método para seleccionar aleatoriamente un entrenador rival
    private int seleccionarEntrenadorRival(Connection conn) throws SQLException {
        List<Integer> entrenadoresRivales = new ArrayList<>();
        String sql = "SELECT id_entrenador FROM entrenador ORDER BY id_entrenador";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entrenadoresRivales.add(rs.getInt("id_entrenador"));
                }
            }
        }

        // Si no hay entrenadores rivales en la base de datos, retornar 0 (indicando un error)
        if (entrenadoresRivales.isEmpty()) {
            return 0;
        }

        // Seleccionar aleatoriamente un número entre 0 y 9
        Random random = new Random();
        int indice = random.nextInt(10) + 1; // Genera un número entre 0 y 9
        return entrenadoresRivales.get(indice); // Ajusta el índice para que comience desde 0
    }

    // Método para obtener el nombre del entrenador por su ID
    private String obtenerNombreEntrenador(Connection conn, int idRival) throws SQLException {
        String sql = "SELECT nom_entrenador FROM entrenador WHERE id_entrenador = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRival);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_entrenador");
                }
            }
        }
        return null; // Si no se encuentra el entrenador, retornar null
    }

    // Método para seleccionar un Pokémon al azar del rival por ID de entrenador
    private String seleccionarPokemonRivalAleatorio(Connection conn, int idRival) throws SQLException {
        List<String> pokemonesRival = new ArrayList<>();
        String sql = "SELECT mote FROM pokemon WHERE id_entrenador = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRival);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pokemonesRival.add(rs.getString("mote"));
                }
            }
        }

        // Si no hay Pokémon para el rival en la base de datos, retornar null
        if (pokemonesRival.isEmpty()) {
            return null;
        }

        // Seleccionar aleatoriamente un Pokémon del rival
        Random random = new Random();
        int indice = random.nextInt(pokemonesRival.size()); // Genera un número entre 0 y el tamaño de la lista - 1
        return pokemonesRival.get(indice);
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
        
        while (pokemon.size() < 6) {
        	pokemon.add(null);
        }
        return pokemon;
    }
	
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
}
