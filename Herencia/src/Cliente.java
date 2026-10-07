import java.util.ArrayList;

public abstract class Cliente {
    protected String identificador;
    protected String nombre;
    protected ArrayList<String> licencias;
    private ArrayList<Alquiler> alquileres;

    public Cliente(String identificador, String nombre, ArrayList<String> licencias) {
        this.identificador = Vehiculo.textoObligatorio(identificador, "Identificador")
                .toUpperCase(java.util.Locale.ROOT);
        this.nombre = Vehiculo.textoObligatorio(nombre, "Nombre");

        if (licencias == null || licencias.isEmpty()) {
            throw new IllegalArgumentException("Debe presentar al menos una licencia.");
        }

        this.licencias = new ArrayList<>();

        for (String licencia : licencias) {
            String normalizada = Vehiculo.textoObligatorio(licencia, "Licencia").toUpperCase(java.util.Locale.ROOT);

            if (!normalizada.equals("A") && !normalizada.equals("B") && !normalizada.equals("C")
                    && !normalizada.equals("M")) {
                throw new IllegalArgumentException("Solo se aceptan licencias A, B, C o M.");
            }

            if (!this.licencias.contains(normalizada)) {
                this.licencias.add(normalizada);
            }
        }

        this.alquileres = new ArrayList<>();
    }

    public abstract double calcularDescuento(double subtotal);

    public abstract int getLimiteAlquileresActivos();

    public abstract String descripcion();

    public String getIdentificador() {
        return this.identificador;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<String> getLicencias() {
        return new ArrayList<>(licencias);
    }

    public ArrayList<Alquiler> getAlquileres() {
        return new ArrayList<>(alquileres);
    }

    public int getCantidadAlquileresConfirmados() {
        return this.alquileres.size();
    }

    public int getCantidadAlquileresActivos() {
        int cantidad = 0;

        for (Alquiler alquiler : alquileres) {
            if (alquiler.estaActivo()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public boolean puedeAlquilar() {
        return getCantidadAlquileresActivos() < getLimiteAlquileresActivos();
    }

    public boolean autoriza(String requerida) {
        if (requerida.equals("C")) {
            return licencias.contains("C") || licencias.contains("B") || licencias.contains("A");
        }

        if (requerida.equals("B")) {
            return licencias.contains("B") || licencias.contains("A");
        }

        return licencias.contains(requerida);
    }

    public ArrayList<String> razonesRechazo(Vehiculo vehiculo) {
        ArrayList<String> razones = new ArrayList<>();

        if (!vehiculo.disponible()) {
            razones.add("Vehiculo no disponible: " + vehiculo.getEstado());
        }

        if (!vehiculo.licenciaAdecuada(this)) {
            razones.add("Licencia inadecuada. Se requiere: " + vehiculo.licenciaRequerida());
        }

        if (!puedeAlquilar()) {
            razones.add("Limite de alquileres activos alcanzado: " + getLimiteAlquileresActivos());
        }

        return razones;
    }

    public Alquiler cotizar(Vehiculo vehiculo, int dias) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehiculo inexistente.");
        }

        double subtotal = vehiculo.cobro(dias);

        return new Alquiler(vehiculo, dias, subtotal, calcularDescuento(subtotal));
    }

    public void confirmarAlquiler(Alquiler propuesta, int numero) {
        if (propuesta == null) {
            throw new IllegalArgumentException("No existe una propuesta.");
        }

        ArrayList<String> razones = razonesRechazo(propuesta.getVehiculo());

        if (!razones.isEmpty()) {
            throw new IllegalStateException(String.join("; ", razones));
        }

        double subtotal = propuesta.getVehiculo().cobro(propuesta.getDias());

        if (Double.compare(subtotal, propuesta.getSubtotal()) != 0
                || Double.compare(calcularDescuento(subtotal), propuesta.getDescuento()) != 0) {
            throw new IllegalStateException("Las condiciones cambiaron. Debe cotizar nuevamente.");
        }

        propuesta.confirmar(numero);
        alquileres.add(propuesta);
    }

    public double calcularTotalPagado() {
        double total = 0;

        for (Alquiler alquiler : alquileres) {
            total += alquiler.getTotal();
        }

        return Vehiculo.redondear(total);
    }
}