package bbdd;

public class MovimientosPokemon {
    private int idPokemon;
    private int idMovimiento;
    private String activo;

	public MovimientosPokemon(int idPokemon, int idMovimiento) {
        this.idPokemon = idPokemon;
        this.idMovimiento = idMovimiento;
    }

    // Getters y setters
    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }
    
    public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}

