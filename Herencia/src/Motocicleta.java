public class Motocicleta extends Vehiculo {
    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int dias, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        this.cilindraje = cilindraje;
    }

    @Override
    public double cobro() {
        double subtotal = 0;
        subtotal += this.tarifaDiaria * this.dias;
        if (this.cilindraje > 250) {
            subtotal += 75;
        }
        return subtotal;
    }
}