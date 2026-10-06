import java.util.ArrayList;
public abstract class Cliente {
    protected String identificador;
    protected String nombre;
    protected ArrayList<String> licencias;
    private ArrayList<Alquiler> alquileres;

    public Cliente(String identificador, String nombre, ArrayList<String> licencias) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.licencias = licencias;
        this.alquileres = new ArrayList<>();
    }
}