package util;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.image.Image;

public class ProgenitoresModel {
    private Image madreImage;
    private Image padreImage;
    
    // Constructor
    public ProgenitoresModel(Image madreImage, Image padreImage) {
        this.madreImage = madreImage;
        this.padreImage = padreImage;
    }

    // Getters y setters
    public Image getMadreImage() {
        return madreImage;
    }

    public void setMadreImage(Image madreImage) {
        this.madreImage = madreImage;
    }

    public Image getPadreImage() {
        return padreImage;
    }

    public void setPadreImage(Image padreImage) {
        this.padreImage = padreImage;
    }
    
    public Image getImageForPokemon(String pokemonNombre) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getbacktowork", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT imagen FROM pokedex WHERE nom_pokemon = ?")) {
            stmt.setString(1, pokemonNombre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Recuperar la imagen como un conjunto de bytes desde la base de datos
                    byte[] bytesImagen = rs.getBytes("imagen");

                    // Convertir los bytes de la imagen en un objeto Image de JavaFX
                    return new Image(new ByteArrayInputStream(bytesImagen));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores
        }
        
        // Si no se pudo obtener la imagen, retornar null
        return null;
    }
}
