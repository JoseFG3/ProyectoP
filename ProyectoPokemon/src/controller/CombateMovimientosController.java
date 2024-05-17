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
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import util.CombateSessionManager;
import util.PokedexManager;
import util.SessionManager;

public class CombateMovimientosController implements Initializable {

    @FXML
    private Button ataque1;

    @FXML
    private Button ataque2;

    @FXML
    private Button ataque3;

    @FXML
    private Button ataque4;

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
    private ProgressBar progressBarPokemon;
    
    @FXML
    private ProgressBar progressBarPokemonRival;
    
    private int idUsuario;
    private int idRival;
    private int idPokemonUsuario;
    private int idPokemonRival;
    
    @FXML
    void usarAtaque1(ActionEvent event) {
        realizarAtaque(1, event);
    }
    
    @FXML
    void usarAtaque2(ActionEvent event) {
        realizarAtaque(2, event);
    }
    
    @FXML
    void usarAtaque3(ActionEvent event) {
        realizarAtaque(3, event);
    }
    
    @FXML
    void usarAtaque4(ActionEvent event) {
        realizarAtaque(4, event);
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String nom_usuario = SessionManager.getEntrenador().getNom_entrenador();
        int id_usuario = SessionManager.getEntrenador().getId_entrenador();
        List<String> pokemon = obtenerEquipoPokemon(id_usuario);

        nombreUsuario.setText(nom_usuario);
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

                seleccionarPokemonRivalAleatorio(conn, idRival); // Modificado para obtener y almacenar el idPokemonRival

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            idRival = CombateSessionManager.getIdRival();
        }

        nombreRival.setText(CombateSessionManager.getNombreEntrenadorRival());
        nombrePokemonRival.setText(CombateSessionManager.getNombrePokemonRival());
        cambiarImagen(imgPokemonRival, CombateSessionManager.getNombrePokemonRival());

        idPokemonRival = CombateSessionManager.getIdPokemonRival(); // Obtener el id del Pokémon rival desde CombateSessionManager

        List<PokemonVitalidad> listaVitalidad = obtenerVitalidadPokemon(id_usuario);
        if (!listaVitalidad.isEmpty()) {
            PokemonVitalidad pokemonVitalidad = listaVitalidad.get(0);
            vitalidadPokemon.setText(pokemonVitalidad.vitalidad + "/" + pokemonVitalidad.vitalidadMax);
            progressBarPokemon.setProgress((double) pokemonVitalidad.vitalidad / pokemonVitalidad.vitalidadMax);
        }

        List<PokemonVitalidad> listaVitalidadRival = obtenerVitalidadPokemon(CombateSessionManager.getIdRival());
        if (!listaVitalidadRival.isEmpty()) {
            PokemonVitalidad pokemonVitalidadRival = listaVitalidadRival.get(0);
            vitalidadPokemonRival.setText(pokemonVitalidadRival.vitalidad + "/" + pokemonVitalidadRival.vitalidadMax);
            progressBarPokemonRival.setProgress((double) pokemonVitalidadRival.vitalidad / pokemonVitalidadRival.vitalidadMax);
        }

        List<String> movimientosPokemonUsuario = obtenerMovimientosPokemon(obtenerPrimerPokemonUsuario(id_usuario));
        asignarMovimientosABotones(movimientosPokemonUsuario);
    }

    
    private void realizarAtaque(int idMovimiento, Event event) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "")) {
            conn.setAutoCommit(false);

            // Registrar acción del usuario
            registrarAccionUsuario(conn, idMovimiento);

            // Elegir y registrar acción del rival
            int movimientoRival;
            try {
                movimientoRival = elegirMovimientoRival(conn, CombateSessionManager.getIdPokemonRival());
            } catch (IllegalArgumentException e) {
                // Manejar el caso donde el Pokémon rival no tiene movimientos disponibles
                mostrarError("El Pokémon rival no tiene movimientos disponibles.");
                return;
            }

            registrarAccionRival(conn, movimientoRival);
            
            actualizarVitalidad(conn, obtenerPrimerPokemonUsuario(SessionManager.getEntrenador().getId_entrenador()), idMovimiento, CombateSessionManager.getIdPokemonRival(), movimientoRival);
            // Actualizar la interfaz de usuario después de realizar los ataques
            conn.commit();
            loadStage("../view/COMBATE-SCENE.fxml", event);

        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarError("Error al realizar el ataque: " + ex.getMessage());
        }
    }

    
    private void registrarAccionUsuario(Connection conn, int idMovimiento) throws SQLException {
        // Obtener el máximo id_turno
        int maxIdTurno = 0;
        String getMaxIdTurnoSql = "SELECT COALESCE(MAX(id_turno), 0) + 1 AS max_id_turno FROM turno";
        try (PreparedStatement stmt = conn.prepareStatement(getMaxIdTurnoSql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maxIdTurno = rs.getInt("max_id_turno");
            }
        }

        // Insertar la acción del usuario
        String insertTurnoSql = "INSERT INTO turno (id_turno, id_pokemon_usuario, id_movimiento_usuario) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertTurnoSql)) {
            stmt.setInt(1, maxIdTurno);
            stmt.setInt(2, obtenerPrimerPokemonUsuario(SessionManager.getEntrenador().getId_entrenador()));
            stmt.setInt(3, idMovimiento);
            stmt.executeUpdate();
        }
    }


    private void registrarAccionRival(Connection conn, int idMovimiento) throws SQLException {
        String sql = "UPDATE turno SET id_pokemon_rival = ?, id_movimiento_rival = ? WHERE id_turno = (SELECT MAX(id_turno) FROM turno)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPokemonRival);
            stmt.setInt(2, idMovimiento);
            stmt.executeUpdate();
        }
    }

    private int elegirMovimientoRival(Connection conn, int idPokemon) throws SQLException {
        List<Integer> movimientos = new ArrayList<>();
        String sql = "SELECT id_movimiento FROM movimientos_pokemon WHERE id_pokemon = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPokemon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(rs.getInt("id_movimiento"));
                }
            }
        }

        if (movimientos.isEmpty()) {
            throw new IllegalArgumentException("El Pokémon rival no tiene movimientos disponibles.");
        }

        Random random = new Random();
        return movimientos.get(random.nextInt(movimientos.size()));
    }


    private void actualizarVitalidad(Connection conn, int idPokemonUsuario, int idMovimientoUsuario, int idPokemonRival, int idMovimientoRival) throws SQLException {
        int damageUsuario = calcularDamage(conn, idMovimientoUsuario);
        int damageRival = calcularDamage(conn, idMovimientoRival);

        // Actualizar vitalidad del Pokémon rival
        String sql = "UPDATE pokemon SET vitalidad = GREATEST(0, vitalidad - ?) WHERE id_pokemon = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, damageUsuario);
            stmt.setInt(2, idPokemonRival);
            stmt.executeUpdate();
        }

        // Actualizar vitalidad del Pokémon del usuario
        sql = "UPDATE pokemon SET vitalidad = GREATEST(0, vitalidad - ?) WHERE id_pokemon = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, damageRival);
            stmt.setInt(2, idPokemonUsuario);
            stmt.executeUpdate();
        }
    }

    private int calcularDamage(Connection conn, int idMovimiento) throws SQLException {
        String sql = "SELECT potencia FROM movimientos WHERE id_movimiento = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMovimiento);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("potencia");
                }
            }
        }
        return 0;
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
        String sql = "SELECT id_entrenador FROM entrenador WHERE id_entrenador BETWEEN 1 AND 10 ORDER BY id_entrenador";
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
        
        public void setVitalidad(int vitalidad_nueva) {
            this.vitalidad = vitalidad_nueva;
        }
    }
    
    private int obtenerPrimerPokemonUsuario(int idUsuario) {
        int primerPokemon = 0;
        String sql = "SELECT id_pokemon FROM pokemon WHERE id_entrenador = ? ORDER BY id_pokemon LIMIT 1";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    primerPokemon = rs.getInt("id_pokemon");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return primerPokemon;
    }
    
    // Método para obtener los movimientos de un Pokémon
    private List<String> obtenerMovimientosPokemon(int pokemon) {
        List<String> movimientos = new ArrayList<>();
        String sql = "SELECT nom_movimiento FROM movimientos_pokemon mp JOIN movimientos m ON mp.id_movimiento = m.id_movimiento WHERE mp.id_pokemon = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pokemon);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    movimientos.add(rs.getString("nom_movimiento"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return movimientos;
    }

    // Método para asignar los movimientos a los botones
    private void asignarMovimientosABotones(List<String> movimientos) {
        if (movimientos.size() >= 4) {
            ataque1.setText(movimientos.get(0));
            ataque2.setText(movimientos.get(1));
            ataque3.setText(movimientos.get(2));
            ataque4.setText(movimientos.get(3));
        }
    }
    
    private void mostrarError(String mensaje) {
        // Implementar una manera de mostrar errores en la interfaz de usuario
        System.err.println(mensaje);
    }

    
}
