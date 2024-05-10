package util;

import bbdd.Entrenador;

public class SessionManager {
    private static Entrenador entrenador;

    public static Entrenador getEntrenador() {
        return entrenador;
    }

    public static void setEntrenador(Entrenador entrenador) {
        SessionManager.entrenador = entrenador;
    }
}
