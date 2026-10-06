public class Camioneta extends Vehiculo {
    private double capacidad;

    public Camioneta(String placa, String marca, String modelo, double tarifaDiaria, int dias, double capacidad) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        this.capacidad = capacidad;
    }

    @Override
    public double cobro() {
        double subtotal = 0;
        subtotal += this.tarifaDiaria * this.dias;
        subtotal += 100 * this.capacidad * this.dias;
        return subtotal;
    }
}