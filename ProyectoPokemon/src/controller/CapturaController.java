package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

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
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.SessionManager;
import java.io.ByteArrayInputStream;
import bbdd.Pokedex;
import util.PokedexManager;
import bbdd.Movimientos;


public class CapturaController implements Initializable {
    private PokedexManager pokedexManager; // Crear una instancia de PokedexManager como variable miembro
 
    private Pokedex pokemonAleatorio;

 
    @FXML
    private Button btnCaptura;
 
    @FXML
    private Button btnReturn;
    @FXML
    private ImageView imageViewCaptura;
    @FXML
    private Button generarPokemon;

 
    private List<String> listaPokemon;
    @FXML
    void Generar(ActionEvent event) {
        try {
            pokemonAleatorio = pokedexManager.obtenerPokemonAleatorio(); // Obtenemos un Pokémon aleatorio y lo asignamos a pokemonAleatorio
            if (pokemonAleatorio != null) {
                System.out.println("Se ha generado un Pokémon aleatorio: " + pokemonAleatorio.getNomPokemon());
                cambiarImagen(pokemonAleatorio.getNomPokemon());
            } else {
                System.out.println("No se pudo obtener ningún Pokémon aleatorio.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    
    @FXML
    void Capturar(ActionEvent event) {
        if (pokemonAleatorio != null) {
            capturarPokemon(pokemonAleatorio);
        } else {
            System.out.println("Primero genera un Pokémon antes de capturar.");
            JOptionPane.showMessageDialog(null, "Primero genera un Pokémon antes de capturar.",
                    "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void capturarPokemon(Pokedex pokemon) {
        // Obtener el ID del entrenador desde la sesión
        int idEntrenador = SessionManager.getEntrenador().getId_entrenador();
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            Statement instruccion = conn.createStatement();
 
            ResultSet resultado = instruccion.executeQuery("SELECT MAX(ID_POKEMON) FROM pokemon");
            int ultimoID = 0;
            if (resultado.next()) {
                ultimoID = resultado.getInt(1);
            }
 
            // Obtener los datos del Pokemon aleatorio
            String motePokemon = pokemon.getNomPokemon();
            int nuevoID = ultimoID + 1;
            int ataque = (int) (Math.random() * 10) + 1;
            int ataqueEspecial = (int) (Math.random() * 10) + 1;
            int defensa = (int) (Math.random() * 10) + 1;
            int defensaEspecial = (int) (Math.random() * 10) + 1;
            int velocidad = (int) (Math.random() * 10) + 1;
            int nivel = 1;
            int fertilidad = 5;
            String sexo = Math.random() < 0.5 ? "M" : "F";
            String estado = null;
            int experiencia = 0;
            int vitalidadMax = (int) (Math.random() * 51) + 50;
            int idObjeto = 0;
            int vitalidad = vitalidadMax/2;
 
            String insertQuery = "INSERT INTO pokemon (ID_POKEMON, MOTE, CAJA, ATAQUE, AT_ESPECIAL, DEFENSA, DEF_ESPECIAL, VELOCIDAD, NIVEL, FERTILIDAD, SEXO, ESTADO, EXPERIENCIA, VITALIDAD_MAX, NUM_POKEDEX, ID_ENTRENADOR, ID_OBJETO, VITALIDAD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
            insertStatement.setInt(1, nuevoID);
            insertStatement.setString(2, motePokemon);
            insertStatement.setInt(3, 1); // Valor fijo de 1 para la columna Caja
            insertStatement.setInt(4, ataque);
            insertStatement.setInt(5, ataqueEspecial);
            insertStatement.setInt(6, defensa);
            insertStatement.setInt(7, defensaEspecial);
            insertStatement.setInt(8, velocidad);
            insertStatement.setInt(9, nivel);
            insertStatement.setInt(10, fertilidad);
            insertStatement.setString(11, sexo);
            insertStatement.setString(12, estado);
            insertStatement.setInt(13, experiencia);
            insertStatement.setInt(14, vitalidadMax);
            insertStatement.setInt(15, pokemon.getNumPokedex()); // Aquí se usa el NUM_POKEDEX correspondiente
            insertStatement.setInt(16, idEntrenador);
            insertStatement.setInt(17, idObjeto);
            insertStatement.setInt(18, vitalidad);
 
            int filasInsertadas = insertStatement.executeUpdate();
            if (filasInsertadas > 0) {
            	String tipoPokemon = pokemon.getTipo1();
            	List<Movimientos> movimientos = obtenerMovimientosAlAzar(conn, tipoPokemon);
                asignarMovimientosAPokemon(nuevoID, movimientos, conn);
            	
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Pokémon Capturado!");
                alert.setHeaderText(null);
                alert.setContentText("Has capturado a: " + motePokemon);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Ha ocurrido un error al insertar el Pokémon.");
                alert.showAndWait();
            }
 
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        }
    }
 
    @FXML
    void irMenu(ActionEvent event) {
        loadStage("../view/MENU-SCENE.fxml", event);
    }
 
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        listaPokemon = new ArrayList<>();
        // Obtener las credenciales del usuario desde SessionManager
        String nombreUsuario = SessionManager.getEntrenador().getNom_entrenador();
        String contrasena = SessionManager.getEntrenador().getPass();
        // Crear una instancia de PokedexManager con las credenciales del usuario
        pokedexManager = new PokedexManager("jdbc:mysql://localhost:3306/getbacktowork", nombreUsuario, contrasena);
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

    private void cambiarImagen(String id_pokemon) {
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
                        imageViewCaptura.setImage(imagen);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        }
    }
    private List<Movimientos> obtenerMovimientosAlAzar(Connection conn, String tipoPokemon) {
        List<Movimientos> movimientos = new ArrayList<>();

        String sql = "SELECT id_movimiento, nom_movimiento, potencia, tipo FROM movimientos WHERE tipo = ? ORDER BY RAND() LIMIT 4";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, tipoPokemon);
        	try(ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                int idMovimiento = rs.getInt("id_movimiento");
	                String nomMovimiento = rs.getString("nom_movimiento");
	                int potencia = rs.getInt("potencia");
	                String tipo = rs.getString("tipo");
	                movimientos.add(new Movimientos(idMovimiento, nomMovimiento, potencia, tipo));
	            }
	        }
        } catch (SQLException ex) {
            Logger.getLogger(CapturaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return movimientos;
    }

    // Método para asignar movimientos a un Pokémon
    private void asignarMovimientosAPokemon(int idPokemon, List<Movimientos> movimientos, Connection conn) {
        String sql = "INSERT INTO movimientos_pokemon (id_pokemon, id_movimiento, activo) VALUES (?, ?, '0')";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Movimientos movimiento : movimientos) {
                stmt.setInt(1, idPokemon);
                stmt.setInt(2, movimiento.getIdMovimiento());
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CapturaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}