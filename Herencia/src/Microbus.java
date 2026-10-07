public class Microbus extends Vehiculo {
    private int pasajeros;
    private boolean piloto;

    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int dias, int pasajeros,
            boolean piloto) {
        super(placa, marca, modelo, tarifaDiaria, dias);
        positivo(pasajeros, "Pasajeros");

        this.pasajeros = pasajeros;
        this.piloto = piloto;
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

        if (this.piloto == true) {
            subtotal += dias * 250.0;
        }

        return validarCobro(subtotal);
    }

    @Override
    public boolean licenciaAdecuada(Cliente cliente) {
        return piloto || cliente.autoriza("B");
    }

    @Override
    public String licenciaRequerida() {
        return piloto ? "Ninguna (incluye piloto)" : "B o A";
    }

    @Override
    public int umbralMantenimiento() {
        return 25;
    }

    @Override
    public String categoria() {
        return "Microbus";
    }

    @Override
    public String caracteristicas() {
        return pasajeros + " pasajeros, " + (piloto ? "con piloto" : "sin piloto");
    }
}