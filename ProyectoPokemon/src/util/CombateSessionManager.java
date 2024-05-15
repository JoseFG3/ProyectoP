package util;

public class CombateSessionManager {
    private static int idRival;
    private static String nombreEntrenadorRival;
    private static String nombrePokemonRival;

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

    public static String getNombrePokemonRival() {
        return nombrePokemonRival;
    }

    public static void setNombrePokemonRival(String nombrePokemonRival) {
        CombateSessionManager.nombrePokemonRival = nombrePokemonRival;
    }
    
    public static void clear() {
    	idRival = 0;
    	nombreEntrenadorRival = null;
    	nombrePokemonRival = null;
    }
}

