public class Camioneta extends Vehiculo {
    private double capacidad;

    public Camioneta(String placa, String marca, String modelo, double tarifaDiaria, int dias, double capacidad) {
        super(placa, marca, modelo, tarifaDiaria, dias);

        positivo(capacidad, "Capacidad");

        this.capacidad = capacidad;
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
        subtotal += 100 * this.capacidad * dias;

        return validarCobro(subtotal);
    }

    @Override
    public boolean licenciaAdecuada(Cliente cliente) {
        return cliente.autoriza("B");
    }

    @Override
    public String licenciaRequerida() {
        return "B o A";
    }

    @Override
    public int umbralMantenimiento() {
        return 15;
    }

    @Override
    public String categoria() {
        return "Camioneta";
    }

    @Override
    public String caracteristicas() {
        return "Capacidad maxima: " + capacidad + " toneladas";
    }
}