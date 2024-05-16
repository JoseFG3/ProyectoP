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

public class CentroPokemonController implements Initializable {
	 
    @FXML
    private Button btnRecuperar;
 
    @FXML
    private Button btnReturn;
 
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
    private Label labelVitalidad1;
 
    @FXML
    private Label labelVitalidad2;
 
    @FXML
    private Label labelVitalidad3;
 
    @FXML
    private Label labelVitalidad4;
 
    @FXML
    private Label labelVitalidad5;
 
    @FXML
    private Label labelVitalidad6;
 
    @FXML
    void irMenu(ActionEvent event) {
        loadStage("../view/MENU-SCENE.fxml", event);
    }
 
    @FXML
    void recuperarVitalidad(ActionEvent event) {
        int idUsuario = SessionManager.getEntrenador().getId_entrenador();
 
        // Actualizar la vitalidad en la base de datos
        actualizarVitalidadAlMaximo(idUsuario);
 
        // Refrescar las etiquetas de vitalidad
        List<Integer> vitalidades = obtenerVitalidadPokemon(idUsuario);
 
        if (vitalidades.get(0) != null) {
            labelVitalidad1.setText("Vitalidad: " + vitalidades.get(0));
        } else {
            labelVitalidad1.setText("No disponible");
        }
 
        if (vitalidades.get(1) != null) {
            labelVitalidad2.setText("Vitalidad: " + vitalidades.get(1));
        } else {
            labelVitalidad2.setText("No disponible");
        }
 
        if (vitalidades.get(2) != null) {
            labelVitalidad3.setText("Vitalidad: " + vitalidades.get(2));
        } else {
            labelVitalidad3.setText("No disponible");
        }
 
        if (vitalidades.get(3) != null) {
            labelVitalidad4.setText("Vitalidad: " + vitalidades.get(3));
        } else {
            labelVitalidad4.setText("No disponible");
        }
 
        if (vitalidades.get(4) != null) {
            labelVitalidad5.setText("Vitalidad: " + vitalidades.get(4));
        } else {
            labelVitalidad5.setText("No disponible");
        }
 
        if (vitalidades.get(5) != null) {
            labelVitalidad6.setText("Vitalidad: " + vitalidades.get(5));
        } else {
            labelVitalidad6.setText("No disponible");
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
 
    @Override
    public void initialize(URL arg0, ResourceBundle rb) {
        int idUsuario = SessionManager.getEntrenador().getId_entrenador();
        List<String> pokemon = obtenerEquipoPokemon(idUsuario);
        List<Integer> vitalidades = obtenerVitalidadPokemon(idUsuario);
 
        if (pokemon.get(0) != null) {
            cambiarImagen(imgPokemon1, pokemon.get(0));
            labelPokemon1.setText(pokemon.get(0));
            if (vitalidades.get(0) != null) {
                labelVitalidad1.setText("Vitalidad: " + vitalidades.get(0));
            } else {
                labelVitalidad1.setText("No disponible");
            }
        } else {
            labelPokemon1.setText("");
            labelVitalidad1.setText("No disponible");
        }
 
        if (pokemon.get(1) != null) {
            cambiarImagen(imgPokemon2, pokemon.get(1));
            labelPokemon2.setText(pokemon.get(1));
            if (vitalidades.get(1) != null) {
                labelVitalidad2.setText("Vitalidad: " + vitalidades.get(1));
            } else {
                labelVitalidad2.setText("No disponible");
            }
        } else {
            labelPokemon2.setText("");
            labelVitalidad2.setText("No disponible");
        }
 
        if (pokemon.get(2) != null) {
            cambiarImagen(imgPokemon3, pokemon.get(2));
            labelPokemon3.setText(pokemon.get(2));
            if (vitalidades.get(2) != null) {
                labelVitalidad3.setText("Vitalidad: " + vitalidades.get(2));
            } else {
                labelVitalidad3.setText("No disponible");
            }
        } else {
            labelPokemon3.setText("");
            labelVitalidad3.setText("No disponible");
        }
 
        if (pokemon.get(3) != null) {
            cambiarImagen(imgPokemon4, pokemon.get(3));
            labelPokemon4.setText(pokemon.get(3));
            if (vitalidades.get(3) != null) {
                labelVitalidad4.setText("Vitalidad: " + vitalidades.get(3));
            } else {
                labelVitalidad4.setText("No disponible");
            }
        } else {
            labelPokemon4.setText("");
            labelVitalidad4.setText("No disponible");
        }
 
        if (pokemon.get(4) != null) {
            cambiarImagen(imgPokemon5, pokemon.get(4));
            labelPokemon5.setText(pokemon.get(4));
            if (vitalidades.get(4) != null) {
                labelVitalidad5.setText("Vitalidad: " + vitalidades.get(4));
            } else {
                labelVitalidad5.setText("No disponible");
            }
        } else {
            labelPokemon5.setText("");
            labelVitalidad5.setText("No disponible");
        }
 
        if (pokemon.get(5) != null) {
            cambiarImagen(imgPokemon6, pokemon.get(5));
            labelPokemon6.setText(pokemon.get(5));
            if (vitalidades.get(5) != null) {
                labelVitalidad6.setText("Vitalidad: " + vitalidades.get(5));
            } else {
                labelVitalidad6.setText("No disponible");
            }
        } else {
            labelPokemon6.setText("");
            labelVitalidad6.setText("No disponible");
        }
    }
 
    private List<String> obtenerEquipoPokemon(int idUsuario) {
        List<String> pokemon = new ArrayList<>();
        String sql = "SELECT mote FROM pokemon WHERE id_entrenador = ? ORDER BY id_pokemon LIMIT 6";
 
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
 
    private List<Integer> obtenerVitalidadPokemon(int idUsuario) {
        List<Integer> vitalidades = new ArrayList<>();
        String sql = "SELECT vitalidad FROM pokemon WHERE id_entrenador = ? ORDER BY id_pokemon LIMIT 6";
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, idUsuario);
 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vitalidades.add(rs.getInt("vitalidad"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
 
        while (vitalidades.size() < 6) {
            vitalidades.add(null);
        }
 
        return vitalidades;
    }
 
    private void actualizarVitalidadAlMaximo(int idUsuario) {
        String sql = "UPDATE pokemon SET vitalidad = vitalidad_max WHERE id_entrenador = ?";
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
 
    private void cambiarImagen(ImageView imageView, String nomPokemon) {
        String sql = "SELECT imagen FROM pokedex WHERE nom_pokemon = ?";
 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, nomPokemon);
 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    byte[] bytesImagen = rs.getBytes("imagen");
                    Image imagen = new Image(new ByteArrayInputStream(bytesImagen));
                    imageView.setImage(imagen);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}