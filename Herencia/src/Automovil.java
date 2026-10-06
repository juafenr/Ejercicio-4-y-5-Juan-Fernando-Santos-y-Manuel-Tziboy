public class Automovil extends Vehiculo {
    private int pasajeros;
    private boolean automatica;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int dias, int pasajeros, boolean automatica) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        this.pasajeros = pasajeros;
        this.automatica = automatica;
    }

    @Override
    public double cobro(){
        double subtotal = 0;
        subtotal += this.tarifaDiaria * this.dias;
        if (this.automatica == true) {
            subtotal += this.dias * 50;
        }
        return subtotal;
    }
}