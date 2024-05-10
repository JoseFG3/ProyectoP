package bbdd;

public class Pokedex {
    private int numPokedex;
    private String nomPokemon;
    private String tipo1;
    private String tipo2;
    private byte[] imagen;
    private int nivelEvolucion;
    private int numPokedexEvo;

    // Constructor
    public Pokedex(int numPokedex, String nomPokemon, String tipo1, String tipo2, byte[] imagen, int nivelEvolucion, int numPokedexEvo) {
        this.numPokedex = numPokedex;
        this.nomPokemon = nomPokemon;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.imagen = imagen;
        this.nivelEvolucion = nivelEvolucion;
        this.numPokedexEvo = numPokedexEvo;
    }

    // Métodos getter y setter para cada campo
    public int getNumPokedex() {
        return numPokedex;
    }

    public void setNumPokedex(int numPokedex) {
        this.numPokedex = numPokedex;
    }

    public String getNomPokemon() {
        return nomPokemon;
    }

    public void setNomPokemon(String nomPokemon) {
        this.nomPokemon = nomPokemon;
    }

    public String getTipo1() {
        return tipo1;
    }

    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public int getNivelEvolucion() {
        return nivelEvolucion;
    }

    public void setNivelEvolucion(int nivelEvolucion) {
        this.nivelEvolucion = nivelEvolucion;
    }

    public int getNumPokedexEvo() {
        return numPokedexEvo;
    }

    public void setNumPokedexEvo(int numPokedexEvo) {
        this.numPokedexEvo = numPokedexEvo;
    }
}