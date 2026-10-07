import java.util.ArrayList;

public class Corporativo extends Cliente {
    private String nombreContacto;

    public Corporativo(String identificador, String nombre, ArrayList<String> licencias, String nombreContacto) {
        super(identificador, nombre, licencias);
        this.nombreContacto = Vehiculo.textoObligatorio(nombreContacto, "Contacto");
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return Vehiculo.redondear(subtotal * 0.10);
    }

    @Override
    public int getLimiteAlquileresActivos() {
        return 3;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    @Override
    public String descripcion() {
        return "Corporativo | NIT " + identificador + " | Empresa " + nombre + " | Contacto " + nombreContacto
                + " | Licencias " + licencias + " | Activos " + getCantidadAlquileresActivos() + "/3";
    }
}