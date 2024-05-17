package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CombateSessionManager {
    private static int idRival;
    private static String nombreEntrenadorRival;
    private static int idPokemonRival;
    private static String nombrePokemonRival;
    private static List<Integer> movimientosPokemonRival;



	public static int getIdRival() {
		return idRival;
	}

	public static void setIdRival(int idRival) {
		CombateSessionManager.idRival = idRival;
	}

	public static String getNombreEntrenadorRival() {
		return nombreEntrenadorRival;
	}

	public static void setNombreEntrenadorRival(String nombreEntrenadorRival) {
		CombateSessionManager.nombreEntrenadorRival = nombreEntrenadorRival;
	}

	public static int getIdPokemonRival() {
		return idPokemonRival;
	}

	public static void setIdPokemonRival(int idPokemonRival) {
		CombateSessionManager.idPokemonRival = idPokemonRival;
	}

	public static String getNombrePokemonRival() {
		return nombrePokemonRival;
	}

	public static void setNombrePokemonRival(String nombrePokemonRival) {
		CombateSessionManager.nombrePokemonRival = nombrePokemonRival;
	}

	public static List<Integer> getMovimientosPokemonRival() {
		return movimientosPokemonRival;
	}

	public static void setMovimientosPokemonRival(List<Integer> movimientosPokemonRival) {
		CombateSessionManager.movimientosPokemonRival = movimientosPokemonRival;
	}

	public static void clear() {
		idRival = 0;
        nombreEntrenadorRival = null;
        nombrePokemonRival = null;
        movimientosPokemonRival = null;
        idPokemonRival = 0;
		
        curarPokemon();
	}
	
    public static void curarPokemon() {
        String url = "jdbc:mysql://localhost:3306/getbacktowork";
        String user = "root";
        String password = "";

        String sql = "UPDATE pokemon SET vitalidad = vitalidad_max WHERE id_pokemon BETWEEN 13 AND 68";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Pokémon curados: " + rowsUpdated);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
