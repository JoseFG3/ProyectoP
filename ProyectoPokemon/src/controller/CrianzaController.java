package controller;
 
import java.io.ByteArrayInputStream;
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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.swing.JOptionPane;
 
import bbdd.Movimientos;
import bbdd.Pokedex;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.PokedexManager;
import util.SessionManager;
 
public class CrianzaController implements Initializable{
	
	 private PokedexManager pokedexManager; // Crear una instancia de PokedexManager como variable miembro
	    
	    private Pokedex pokemonAleatorio;
	    
	    private boolean crianzaFinalizada = false;
 
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
        if (crianzaFinalizada) { // Verificar si la crianza ha finalizado
            if (imgPadre.getImage() != null && imgMadre.getImage() != null) {
                try {
                    pokemonAleatorio = pokedexManager.obtenerPokemonAleatorio();
                    if (pokemonAleatorio != null) {
                        System.out.println("Ha eclosionado un huevo pokémon: " + pokemonAleatorio.getNomPokemon());
                        eclosionarPokemon(pokemonAleatorio);
                    } else {
                        System.out.println("No se pudo obtener ningún Pokémon aleatorio.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                mostrarMensajeError("Debes seleccionar tanto al padre como a la madre antes de abrir el huevo.");
            }
        } else {
            mostrarMensajeError("Debes finalizar la crianza antes de abrir el huevo.");
        }
    }
 
    private void eclosionarPokemon(Pokedex pokemon) {
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
            int vitalidad = vitalidadMax;
 
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
            insertStatement.setInt(15, pokemon.getNumPokedex());
            insertStatement.setInt(16, idEntrenador);
            insertStatement.setInt(17, idObjeto);
            insertStatement.setInt(18, vitalidad);
 
            int filasInsertadas = insertStatement.executeUpdate();
            if (filasInsertadas > 0) {
                String tipoPokemon = pokemon.getTipo1();
                List<Movimientos> movimientos = obtenerMovimientoPlacaje(conn);
                asignarMovimientosAPokemon(nuevoID, movimientos, conn);
 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Pokémon Capturado!");
                alert.setHeaderText(null);
                alert.setContentText("El huevo ha eclosionado y el pokemon es un: " + motePokemon);
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
 
    private List<Movimientos> obtenerMovimientoPlacaje(Connection conn) {
        List<Movimientos> movimientos = new ArrayList<>();
 
        String sql = "SELECT id_movimiento, nom_movimiento, potencia, tipo FROM movimientos WHERE nom_movimiento = 'Placaje'";
 
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
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
 
 
    @FXML
    void finCrianza(ActionEvent event) {
        if (imgPadre.getImage() != null && imgMadre.getImage() != null) {
            // Aquí va la lógica para finalizar la crianza
            mostrarMensaje("Emparejamiento finalizado, padre y madre seleccionados.");
            crianzaFinalizada = true;
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
    
 
    
    private List<String> listaPokemon;
    @FXML
    void Generar(ActionEvent event) {
        try {
            pokemonAleatorio = pokedexManager.obtenerPokemonAleatorio(); // Obtenemos un Pokémon aleatorio y lo asignamos a pokemonAleatorio
            if (pokemonAleatorio != null) {
                System.out.println("Se ha generado un Pokémon aleatorio: " + pokemonAleatorio.getNomPokemon());
            } else {
                System.out.println("No se pudo obtener ningún Pokémon aleatorio.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void eclosion(ActionEvent event) {
        if (pokemonAleatorio != null) {
            eclosionarPokemon(pokemonAleatorio);
        } else {
            System.out.println("Primero genera un Pokémon antes de capturar.");
            JOptionPane.showMessageDialog(null, "Primero genera un Pokémon antes de capturar.",
                    "ADVERTENCIA", JOptionPane.WARNING_MESSAGE);
        }
    }
  
 
    
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		listaPokemon = new ArrayList<>();
        // Obtener las credenciales del usuario desde SessionManager
        String nombreUsuario = SessionManager.getEntrenador().getNom_entrenador();
        String contrasena = SessionManager.getEntrenador().getPass();
        // Crear una instancia de PokedexManager con las credenciales del usuario
        pokedexManager = new PokedexManager("jdbc:mysql://localhost:3306/getbacktowork", nombreUsuario, contrasena);
		
	}
    
 
}