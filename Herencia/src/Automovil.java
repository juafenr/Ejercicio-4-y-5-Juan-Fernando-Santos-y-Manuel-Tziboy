public class Automovil extends Vehiculo {
    private int pasajeros;
    private boolean automatica;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int dias, int pasajeros,
            boolean automatica) {
        super(placa, marca, modelo, tarifaDiaria, dias);

        positivo(pasajeros, "Pasajeros");

        this.pasajeros = pasajeros;
        this.automatica = automatica;
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

        if (this.automatica) {
            subtotal += dias * 50;
        }

        return validarCobro(subtotal);
    }

    @Override
    public boolean licenciaAdecuada(Cliente cliente) {
        return cliente.autoriza("C");
    }

    @Override
    public String licenciaRequerida() {
        return "C, B o A";
    }

    @Override
    public int umbralMantenimiento() {
        return 30;
    }

    @Override
    public String categoria() {
        return "Automovil";
    }

    @Override
    public String caracteristicas() {
        return pasajeros + " pasajeros, transmision " + (automatica ? "automatica" : "manual");
    }
}