package bbdd;

public class Movimientos {
    private int idMovimiento;
    private String nomMovimiento;
    private int potencia;
    private String tipo;

    public Movimientos(int idMovimiento, String nomMovimiento, int potencia, String tipo) {
        this.idMovimiento = idMovimiento;
        this.nomMovimiento = nomMovimiento;
        this.potencia = potencia;
        this.tipo = tipo;
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
}
