import java.util.ArrayList;

public class Individual extends Cliente {
    public Individual(String identificador, String nombre, ArrayList<String> licencias) {
        super(identificador, nombre, licencias);

        if (!this.identificador.matches("[0-9]{13}")) {
            throw new IllegalArgumentException("El DPI debe contener exactamente 13 digitos.");
        }
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return getCantidadAlquileresConfirmados() >= 3 ? Vehiculo.redondear(subtotal * 0.05) : 0;
    }

    @Override
    public int getLimiteAlquileresActivos() {
        return 1;
    }

    @Override
    public String descripcion() {
        return "Individual | DPI " + identificador + " | " + nombre + " | Licencias " + licencias
                + " | Activos " + getCantidadAlquileresActivos() + "/1 | Confirmados "
                + getCantidadAlquileresConfirmados();
    }
}