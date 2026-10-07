public class Alquiler {
    private int numero;
    private final Vehiculo vehiculo;
    private final int dias;
    private final double subtotal;
    private final double descuento;
    private final double total;
    private boolean confirmado;
    private boolean activo;
    private boolean previo;

    public Alquiler(Vehiculo vehiculo, int dias, double subtotal, double descuento) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehiculo inexistente.");
        }

        Vehiculo.validarDias(dias);

        if (!Double.isFinite(subtotal) || subtotal < 0 || subtotal > 1000000000000.0
                || !Double.isFinite(descuento) || descuento < 0 || descuento > subtotal) {
            throw new IllegalArgumentException("Montos de alquiler invalidos.");
        }

        this.vehiculo = vehiculo;
        this.dias = dias;
        this.subtotal = Vehiculo.redondear(subtotal);
        this.descuento = Vehiculo.redondear(descuento);
        this.total = Vehiculo.redondear(this.subtotal - this.descuento);
    }

    public void confirmar(int numero) {
        if (confirmado) {
            throw new IllegalStateException("El alquiler ya fue confirmado.");
        }

        if (numero <= 0) {
            throw new IllegalArgumentException("Numero de alquiler invalido.");
        }

        if ((long) vehiculo.getDiasDesdeMantenimiento() + dias > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Los dias exceden el acumulado permitido.");
        }

        vehiculo.alquilar();

        this.numero = numero;
        confirmado = true;
        activo = true;
    }

    public void finalizar() {
        if (!activo) {
            throw new IllegalStateException("El alquiler no esta activo.");
        }

        vehiculo.devolver(dias);

        activo = false;
    }

    void marcarComoPrevio() {
        if (!confirmado || activo) {
            throw new IllegalStateException("El registro previo debe estar finalizado.");
        }

        previo = true;
    }

    public int getNumero() {
        return this.numero;
    }

    public Vehiculo getVehiculo() {
        return this.vehiculo;
    }

    public int getDias() {
        return this.dias;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public double getDescuento() {
        return this.descuento;
    }

    public double getTotal() {
        return this.total;
    }

    public boolean estaActivo() {
        return this.activo;
    }

    public boolean estaConfirmado() {
        return this.confirmado;
    }

    public boolean esPrevio() {
        return this.previo;
    }
}