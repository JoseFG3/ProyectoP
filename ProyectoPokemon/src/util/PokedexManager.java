package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import bbdd.Pokedex;

public class PokedexManager {

    private String url;
    private String usuario;
    private String contrasena;

    // Constructor que acepta las credenciales de conexión a la base de datos
    public PokedexManager(String url, String usuario, String contrasena) {
        this.url = "jdbc:mysql://localhost:3306/getbacktowork";
        this.usuario = "root";
        this.contrasena = "";
    }

    // Método para obtener un Pokémon aleatorio de la Pokédex
    public Pokedex obtenerPokemonAleatorio() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Conectar a la base de datos
            conn = DriverManager.getConnection(url, usuario, contrasena);

            // Contar el número de filas en la tabla pokedex
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM pokedex");
            rs = stmt.executeQuery();
            rs.next();
            int totalFilas = rs.getInt(1);

            // Seleccionar un número de fila aleatorio
            Random random = new Random();
            int filaAleatoria = random.nextInt(totalFilas) + 1;

            // Obtener los datos de la fila aleatoria
            stmt = conn.prepareStatement("SELECT * FROM pokedex LIMIT ?, 1");
            stmt.setInt(1, filaAleatoria - 1); // Las filas en MySQL son indexadas desde 0
            rs = stmt.executeQuery();

            // Leer los datos y crear un objeto PokedexEntry
            if (rs.next()) {
                int numPokedex = rs.getInt("NUM_POKEDEX");
                String nomPokemon = rs.getString("NOM_POKEMON");
                String tipo1 = rs.getString("TIPO1");
                String tipo2 = rs.getString("TIPO2");
                byte[] imagen = rs.getBytes("IMAGEN");
                int nivelEvolucion = rs.getInt("NIVEL_EVOLUCION");
                int numPokedexEvo = rs.getInt("NUM_POKEDEX_EVO");

                return new Pokedex(numPokedex, nomPokemon, tipo1, tipo2, imagen, nivelEvolucion, numPokedexEvo);
            }
        } finally {
            // Cerrar los recursos
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return null; // En caso de error o si no se encuentra ninguna fila
    }
}
