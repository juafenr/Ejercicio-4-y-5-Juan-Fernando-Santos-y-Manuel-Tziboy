public class Motocicleta extends Vehiculo {
    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int dias, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        positivo(cilindraje, "Cilindraje");

        this.cilindraje = cilindraje;
    }

    @Override
    public double cobro() {
        return cobro(this.dias);
    }

    @Override
    public double cobro(int dias) {
        validarDias(dias);

        double subtotal = 0;
        subtotal += this.tarifaDiaria * dias;

        if (this.cilindraje > 250) {
            subtotal += 75;
        }

        return validarCobro(subtotal);
    }

    @Override
    public boolean licenciaAdecuada(Cliente cliente) {
        return cliente.autoriza("M");
    }

    @Override
    public String licenciaRequerida() {
        return "M";
    }

    @Override
    public int umbralMantenimiento() {
        return 20;
    }

    @Override
    public String categoria() {
        return "Motocicleta";
    }

    @Override
    public String caracteristicas() {
        return "Cilindraje: " + cilindraje + " cc";
    }
}