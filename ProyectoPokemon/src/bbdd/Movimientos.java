package bbdd;

public class Movimientos {
    private int idMovimiento;
    private String nomMovimiento;
    private int potencia;
    private String tipo;
    private String estado;
    private int quita;
    private int turnos;
    private String mejora;
    private int cantMejora;
    private int nivelAprendizaje;

    public Movimientos(int idMovimiento, String nomMovimiento, int potencia, String tipo, String estado, int quita, int turnos, String mejora, int cantMejora, int nivelAprendizaje) {
        this.idMovimiento = idMovimiento;
        this.nomMovimiento = nomMovimiento;
        this.potencia = potencia;
        this.tipo = tipo;
        this.estado = estado;
        this.quita = quita;
        this.turnos = turnos;
        this.mejora = mejora;
        this.cantMejora = cantMejora;
        this.nivelAprendizaje = nivelAprendizaje;
    }

    // Getters y setters
    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getNomMovimiento() {
        return nomMovimiento;
    }

    public void setNomMovimiento(String nomMovimiento) {
        this.nomMovimiento = nomMovimiento;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getQuita() {
        return quita;
    }

    public void setQuita(int quita) {
        this.quita = quita;
    }

    public int getTurnos() {
        return turnos;
    }

    public void setTurnos(int turnos) {
        this.turnos = turnos;
    }

    public String getMejora() {
        return mejora;
    }

    public void setMejora(String mejora) {
        this.mejora = mejora;
    }

    public int getCantMejora() {
        return cantMejora;
    }

    public void setCantMejora(int cantMejora) {
        this.cantMejora = cantMejora;
    }

    public int getNivelAprendizaje() {
        return nivelAprendizaje;
    }

    public void setNivelAprendizaje(int nivelAprendizaje) {
        this.nivelAprendizaje = nivelAprendizaje;
    }
}
