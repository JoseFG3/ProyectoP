package util;

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

    public static void setMovimientosPokemonRival(List<Integer> movimientos) {
        CombateSessionManager.movimientosPokemonRival = movimientos;
    }

	public static void clear() {
		idRival = 0;
        nombreEntrenadorRival = null;
        nombrePokemonRival = null;
        movimientosPokemonRival = null;
        idPokemonRival = 0;
		
	}
}
