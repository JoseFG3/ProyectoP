package bbdd;

public class Entrenador {
    private String nom_entrenador; // Nombre del entrenador en la tabla
    private String pass; // Contraseña del entrenador en la tabla
    private int pokedollars; // Cantidad de Pokedólares del entrenador en la tabla
    private int id_entrenador; // Identificador del entrenador en la tabla

    // Constructor
    public Entrenador(String nom_entrenador, String pass, int pokedollars, int id_entrenador) {
        this.nom_entrenador = nom_entrenador;
        this.pass = pass;
        this.pokedollars = pokedollars;
        this.id_entrenador = id_entrenador;
    }

    // Getters y setters
    public String getNom_entrenador() {
        return nom_entrenador;
    }

    public void setNom_entrenador(String nom_entrenador) {
        this.nom_entrenador = nom_entrenador;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPokedollars() {
        return pokedollars;
    }

    public void setPokedollars(int pokedollars) {
        this.pokedollars = pokedollars;
    }

    public int getId_entrenador() {
        return id_entrenador;
    }

    public void setId_entrenador(int id_entrenador) {
        this.id_entrenador = id_entrenador;
    }
}