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
    private Label vitalidadPokemon;

    @FXML
    private Label vitalidadPokemonRival;

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

    @FXML
    void irPokemon(ActionEvent event) {
        loadStage("../view/POKEMON-COMBATE.fxml", event);
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

                int pokemonRival = seleccionarPokemonRivalAleatorio(conn, idRival);

            } catch (SQLException ex) {
                ex.printStackTrace();
                nombreRival.setText("Entrenador Rival");
            }
        }

        nombreRival.setText(CombateSessionManager.getNombreEntrenadorRival());
        nombrePokemonRival.setText(CombateSessionManager.getNombrePokemonRival());
        cambiarImagen(imgPokemonRival, CombateSessionManager.getNombrePokemonRival());

        List<PokemonVitalidad> listaVitalidad = obtenerVitalidadPokemon(SessionManager.getEntrenador().getId_entrenador());
        if (!listaVitalidad.isEmpty()) {
            PokemonVitalidad pokemonVitalidad = listaVitalidad.get(0); // Obtener el primer Pokémon del equipo
            vitalidadPokemon.setText(pokemonVitalidad.vitalidad + "/" + pokemonVitalidad.vitalidadMax);
        }

        List<PokemonVitalidad> listaVitalidadRival = obtenerVitalidadPokemon(CombateSessionManager.getIdRival());
        if (!listaVitalidadRival.isEmpty()) {
            PokemonVitalidad pokemonVitalidadRival = listaVitalidadRival.get(0); // Obtener el primer Pokémon del rival
            vitalidadPokemonRival.setText(pokemonVitalidadRival.vitalidad + "/" + pokemonVitalidadRival.vitalidadMax);
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

    private void cambiarImagen(ImageView imageView, String id_pokemon) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            String sql = "SELECT imagen FROM pokedex WHERE nom_pokemon = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id_pokemon);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        byte[] bytesImagen = rs.getBytes("imagen");
                        Image imagen = new Image(new ByteArrayInputStream(bytesImagen));
                        imageView.setImage(imagen);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int seleccionarEntrenadorRival(Connection conn) throws SQLException {
        List<Integer> entrenadoresRivales = new ArrayList<>();
        String sql = "SELECT id_entrenador FROM entrenador ORDER BY id_entrenador";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                entrenadoresRivales.add(rs.getInt("id_entrenador"));
            }
        }

        if (entrenadoresRivales.isEmpty()) {
            return 0;
        }

        Random random = new Random();
        int indice = random.nextInt(entrenadoresRivales.size());
        return entrenadoresRivales.get(indice);
    }

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
        return null;
    }

    // Método para seleccionar un Pokémon al azar del rival por ID de entrenador
    private int seleccionarPokemonRivalAleatorio(Connection conn, int idRival) throws SQLException {
        List<Integer> pokemonesRivalIds = new ArrayList<>();
        List<String> pokemonesRivalNombres = new ArrayList<>();
        String sql = "SELECT id_pokemon, mote FROM pokemon WHERE id_entrenador = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idRival);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pokemonesRivalIds.add(rs.getInt("id_pokemon"));
                    pokemonesRivalNombres.add(rs.getString("mote"));
                }
            }
        }

        if (pokemonesRivalIds.isEmpty() || pokemonesRivalNombres.isEmpty()) {
            return 0;
        }

        Random random = new Random();
        int indice = random.nextInt(pokemonesRivalIds.size());
        int idPokemonRival = pokemonesRivalIds.get(indice);
        String nombrePokemonRival = pokemonesRivalNombres.get(indice);

        CombateSessionManager.setIdPokemonRival(idPokemonRival);
        CombateSessionManager.setNombrePokemonRival(nombrePokemonRival);

        // Obtener movimientos del Pokémon rival
        List<Integer> movimientosPokemonRival = new ArrayList<>();
        String movimientosSql = "SELECT id_movimiento FROM movimientos_pokemon WHERE id_pokemon = ?";
        try (PreparedStatement stmt = conn.prepareStatement(movimientosSql)) {
            stmt.setInt(1, idPokemonRival);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientosPokemonRival.add(rs.getInt("id_movimiento"));
                }
            }
        }
        CombateSessionManager.setMovimientosPokemonRival(movimientosPokemonRival);

        return idPokemonRival;
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

    private List<PokemonVitalidad> obtenerVitalidadPokemon(int idEntrenador) {
        List<PokemonVitalidad> listaVitalidad = new ArrayList<>();
        String sql = "SELECT mote, vitalidad_max, vitalidad FROM pokemon WHERE id_entrenador = ?";
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

    private static class PokemonVitalidad {
        String mote;
        int vitalidadMax;
        int vitalidad;

        PokemonVitalidad(String mote, int vitalidadMax, int vitalidad) {
            this.mote = mote;
            this.vitalidadMax = vitalidadMax;
            this.vitalidad = vitalidad;
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

