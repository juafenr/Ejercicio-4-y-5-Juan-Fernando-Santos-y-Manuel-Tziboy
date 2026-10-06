public class Microbus extends Vehiculo {
    private int pasajeros;
    private boolean piloto;

    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int dias, int pasajeros, boolean piloto) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        this.pasajeros = pasajeros;
        this.piloto = piloto;
    }

    @Override
    public double cobro() {
        double subtotal = 0;
        subtotal += this.tarifaDiaria * this.dias;
        if (this.piloto == true) {
            subtotal += this.dias * 250;
        }
        return subtotal;
    }
}