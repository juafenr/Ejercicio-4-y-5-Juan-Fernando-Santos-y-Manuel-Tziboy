public abstract class Vehiculo {
    protected String placa;
    protected String marca;
    protected String modelo;
    protected double tarifaDiaria;
    protected String estado;
    protected int dias;

    public Vehiculo(String placa, String marca, String modelo, double tarifaDiaria, int dias) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = "Disponible";
        this.dias = dias;
    }

    public abstract double cobro();
}