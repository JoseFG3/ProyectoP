package util;

import bbdd.Entrenador;
import javafx.scene.image.Image;
import util.ProgenitoresModel;

public class SessionManager {
    private static Entrenador entrenador;
    private static ProgenitoresModel progenitoresModel;
    private static String padreSeleccionado;
    private static String madreSeleccionada;
    private static Image madreImage;
    private static Image padreImage;
    
   

    public static String getPadreSeleccionado() {
		return padreSeleccionado;
	}

	public static void setPadreSeleccionado(String padreSeleccionado) {
		SessionManager.padreSeleccionado = padreSeleccionado;
	}

	public static String getMadreSeleccionada() {
		return madreSeleccionada;
	}

	public static void setMadreSeleccionada(String madreSeleccionada) {
		SessionManager.madreSeleccionada = madreSeleccionada;
	}

	public static Entrenador getEntrenador() {
        return entrenador;
    }

    public static void setEntrenador(Entrenador entrenador) {
        SessionManager.entrenador = entrenador;
    }
    
    public static ProgenitoresModel getProgenitoresModel() {
        return progenitoresModel;
    }

    public static void setProgenitoresModel(ProgenitoresModel model) {
        progenitoresModel = model;
    }

    public static Image getMadreImage() {
        return madreImage;
    }

    public static void setMadreImage(Image image) {
        madreImage = image;
    }

    public static Image getPadreImage() {
        return padreImage;
    }

    public static void setPadreImage(Image image) {
        padreImage = image;
    }
}
