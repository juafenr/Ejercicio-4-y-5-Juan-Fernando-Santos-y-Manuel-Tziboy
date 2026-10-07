public abstract class Vehiculo {
    protected String placa;
    protected String marca;
    protected String modelo;
    protected double tarifaDiaria;
    protected String estado;
    protected int dias;

    public Vehiculo(String placa, String marca, String modelo, double tarifaDiaria, int dias) {
        this.placa = textoObligatorio(placa, "Placa").toUpperCase(java.util.Locale.ROOT);
        this.marca = textoObligatorio(marca, "Marca");
        this.modelo = textoObligatorio(modelo, "Modelo");

        positivo(tarifaDiaria, "Tarifa diaria");
        validarDias(dias);

        this.tarifaDiaria = tarifaDiaria;
        this.estado = "Disponible";
        this.dias = dias;
    }

    public abstract double cobro();

    public abstract double cobro(int dias);

    public abstract boolean licenciaAdecuada(Cliente cliente);

    public abstract String licenciaRequerida();

    public abstract int umbralMantenimiento();

    public abstract String categoria();

    public abstract String caracteristicas();

    private int diasDesdeMantenimiento;

    public String getPlaca() {
        return placa;
    }

    public String getEstado() {
        return estado;
    }

    public int getDiasDesdeMantenimiento() {
        return diasDesdeMantenimiento;
    }

    public boolean disponible() {
        return estado.equals("Disponible");
    }

    public void alquilar() {
        if (!disponible()) {
            throw new IllegalStateException("El vehiculo no esta disponible.");
        }

        estado = "Alquilado";
    }

    public void devolver(int diasAlquilado) {
        if (!estado.equals("Alquilado")) {
            throw new IllegalStateException("El vehiculo no esta alquilado.");
        }

        validarDias(diasAlquilado);

        int nuevoAcumulado = Math.addExact(diasDesdeMantenimiento, diasAlquilado);
        diasDesdeMantenimiento = nuevoAcumulado;
        estado = nuevoAcumulado >= umbralMantenimiento() ? "En mantenimiento" : "Disponible";
    }

    public void finalizarMantenimiento() {
        if (!estado.equals("En mantenimiento")) {
            throw new IllegalStateException("El vehiculo no esta en mantenimiento.");
        }

        diasDesdeMantenimiento = 0;
        estado = "Disponible";
    }

    public String descripcion() {
        return placa + " | " + categoria() + " | " + marca + " " + modelo
                + " | " + caracteristicas() + " | Tarifa Q" + String.format(java.util.Locale.US, "%.2f", tarifaDiaria)
                + " | " + estado + " | Dias desde mantenimiento: " + diasDesdeMantenimiento;
    }

    public static String textoObligatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio.");
        }

        return texto.trim();
    }

    public static void positivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser positivo y finito.");
        }
    }

    public static void validarDias(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias deben ser enteros positivos.");
        }
    }

    protected double validarCobro(double monto) {
        positivo(monto, "Subtotal");

        if (monto > 1000000000000.0) {
            throw new IllegalArgumentException("El monto excede el limite del sistema.");
        }

        return redondear(monto);
    }

    public static double redondear(double monto) {
        return Math.round(monto * 100.0) / 100.0;
    }
}