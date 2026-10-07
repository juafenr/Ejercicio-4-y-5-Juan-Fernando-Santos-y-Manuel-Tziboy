import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class ControladorRentaMovil {
    private VistaRentaMovil vista;
    private ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private int siguienteNumero = 1;

    public ControladorRentaMovil() {
        this(new VistaRentaMovil(), true);
    }

    public ControladorRentaMovil(VistaRentaMovil vista, boolean datosIniciales) {
        if (vista == null) {
            throw new IllegalArgumentException("Se necesita una vista.");
        }

        this.vista = vista;

        if (datosIniciales) {
            cargarDatosIniciales();
        }
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            try {
                int opcion = vista.mostrarMenu();
                switch (opcion) {
                    case 1:
                        registrarVehiculo();
                        break;

                    case 2:
                        registrarCliente();
                        break;

                    case 3:
                        vista.mostrarVehiculos(vehiculos);
                        break;

                    case 4:
                        vista.mostrarClientes(clientes);
                        break;

                    case 5:
                        gestionarAlquiler(false);
                        break;

                    case 6:
                        gestionarAlquiler(true);
                        break;

                    case 7:
                        Vehiculo devuelto = devolverVehiculo(vista.leerTexto("Placa a devolver: "));
                        vista.mostrarMensaje("Devolucion registrada. Estado: " + devuelto.getEstado());
                        break;

                    case 8:
                        finalizarMantenimiento(vista.leerTexto("Placa: "));
                        vista.mostrarMensaje("Mantenimiento finalizado. Disponible; acumulado en cero.");
                        break;

                    case 9:
                        mostrarReportes();
                        break;

                    case 0:
                        continuar = false;
                        vista.mostrarMensaje("Ejecucion terminada.");
                        break;

                    default:
                        vista.mostrarMensaje("Opcion invalida.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                vista.mostrarMensaje("Error: " + ex.getMessage());
            } catch (NoSuchElementException ex) {
                vista.mostrarMensaje("Entrada cerrada. Programa finalizado sin confirmar operaciones pendientes.");
                continuar = false;
            }
        }
    }

    private void registrarVehiculo() {
        int tipo = vista.leerOpcion("Categoria: 1 Automovil, 2 Motocicleta, 3 Camioneta, 4 Microbus, 0 Cancelar: ", 0,
                4);
        if (tipo == 0) {
            return;
        }

        String placa = vista.leerTexto("Placa: ");
        String marca = vista.leerTexto("Marca: ");
        String modelo = vista.leerTexto("Modelo: ");
        double tarifa = vista.leerPositivo("Tarifa diaria Q: ");
        Vehiculo nuevo;

        switch (tipo) {
            case 1:
                nuevo = new Automovil(placa, marca, modelo, tarifa, 1,
                        vista.leerEnteroPositivo("Pasajeros: "), vista.leerSiNo("Automatica? (s/n): "));
                break;

            case 2:
                nuevo = new Motocicleta(placa, marca, modelo, tarifa, 1,
                        vista.leerEnteroPositivo("Cilindraje cc: "));
                break;

            case 3:
                nuevo = new Camioneta(placa, marca, modelo, tarifa, 1,
                        vista.leerPositivo("Capacidad maxima en toneladas: "));
                break;

            default:
                nuevo = new Microbus(placa, marca, modelo, tarifa, 1,
                        vista.leerEnteroPositivo("Pasajeros: "), vista.leerSiNo("Incluye piloto? (s/n): "));
        }

        registrarVehiculo(nuevo);
        vista.mostrarMensaje("Vehiculo registrado como disponible.");
    }

    private void registrarCliente() {
        int tipo = vista.leerOpcion("Cliente: 1 Individual, 2 Corporativo, 0 Cancelar: ", 0, 2);

        if (tipo == 0) {
            return;
        }

        String id = vista.leerTexto("Identificador (DPI o NIT): ");
        String nombre = vista.leerTexto("Nombre de persona o empresa: ");
        ArrayList<String> licencias = vista.leerLicencias();
        Cliente nuevo;

        if (tipo == 1) {
            nuevo = new Individual(id, nombre, licencias);
        } else {
            nuevo = new Corporativo(id, nombre, licencias, vista.leerTexto("Nombre del contacto: "));
        }

        registrarCliente(nuevo);
        vista.mostrarMensaje("Cliente registrado.");
    }

    private void gestionarAlquiler(boolean solicitarConfirmacion) {
        String placa = vista.leerTexto("Placa: ");
        String id = vista.leerTexto("Identificador del cliente: ");
        int dias = vista.leerEnteroPositivo("Dias del alquiler: ");
        Cliente cliente = buscarCliente(id);
        Alquiler propuesta = cotizar(placa, id, dias);
        ArrayList<String> razones = cliente.razonesRechazo(propuesta.getVehiculo());

        vista.mostrarCotizacion(cliente, propuesta, razones);

        if (!solicitarConfirmacion) {
            return;
        }

        if (!razones.isEmpty()) {
            vista.mostrarMensaje("No se puede confirmar. No se modifico informacion.");
            return;
        }

        boolean acepta = vista.leerSiNo("Acepta el cobro y confirma? (s/n; n cancela): ");
        Alquiler confirmado = confirmarAlquiler(id, propuesta, acepta);

        if (confirmado == null) {
            vista.mostrarMensaje("Alquiler cancelado sin cambios.");
        } else {
            vista.mostrarMensaje("Alquiler confirmado con numero " + confirmado.getNumero() + ". Cobro registrado.");
        }
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehiculo invalido.");
        }

        for (Vehiculo actual : vehiculos) {
            if (actual.getPlaca().equalsIgnoreCase(vehiculo.getPlaca())) {
                throw new IllegalArgumentException("La placa ya esta registrada.");
            }
        }

        vehiculos.add(vehiculo);
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente invalido.");
        }

        for (Cliente actual : clientes) {
            if (actual.getIdentificador().equalsIgnoreCase(cliente.getIdentificador())) {
                throw new IllegalArgumentException("El identificador ya esta registrado.");
            }
        }

        clientes.add(cliente);
    }

    public Vehiculo buscarVehiculo(String placa) {
        placa = Vehiculo.textoObligatorio(placa, "Placa");

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa)) {
                return vehiculo;
            }
        }

        throw new IllegalArgumentException("Placa inexistente: " + placa);
    }

    public Cliente buscarCliente(String id) {
        id = Vehiculo.textoObligatorio(id, "Identificador");

        for (Cliente cliente : clientes) {
            if (cliente.getIdentificador().equalsIgnoreCase(id)) {
                return cliente;
            }
        }

        throw new IllegalArgumentException("Cliente inexistente: " + id);
    }

    public Alquiler cotizar(String placa, String id, int dias) {
        return buscarCliente(id).cotizar(buscarVehiculo(placa), dias);
    }

    public Alquiler confirmarAlquiler(String id, Alquiler propuesta, boolean acepta) {
        if (!acepta) {
            return null;
        }

        Cliente cliente = buscarCliente(id);

        if (propuesta == null || !vehiculos.contains(propuesta.getVehiculo())) {
            throw new IllegalArgumentException("La propuesta no corresponde a la flota.");
        }

        if (siguienteNumero == Integer.MAX_VALUE) {
            throw new IllegalStateException("Se agoto el correlativo.");
        }

        cliente.confirmarAlquiler(propuesta, siguienteNumero);
        siguienteNumero++;

        return propuesta;
    }

    public Vehiculo devolverVehiculo(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);

        for (Cliente cliente : clientes) {
            for (Alquiler alquiler : cliente.getAlquileres()) {
                if (alquiler.getVehiculo() == vehiculo && alquiler.estaActivo()) {
                    alquiler.finalizar();
                    return vehiculo;
                }
            }
        }

        throw new IllegalStateException("El vehiculo no tiene un alquiler activo.");
    }

    public void finalizarMantenimiento(String placa) {
        buscarVehiculo(placa).finalizarMantenimiento();
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return new ArrayList<>(vehiculos);
    }

    public ArrayList<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public ArrayList<String> getCategorias() {
        ArrayList<String> categorias = new ArrayList<>();

        for (Vehiculo vehiculo : vehiculos) {
            if (!categorias.contains(vehiculo.categoria())) {
                categorias.add(vehiculo.categoria());
            }
        }

        return categorias;
    }

    public int[] contarFlota(String categoria) {
        int[] cantidades = new int[4]; // Total, disponibles, alquilados, mantenimiento.

        for (Vehiculo vehiculo : vehiculos) {
            if (categoria == null || vehiculo.categoria().equals(categoria)) {
                cantidades[0]++;

                if (vehiculo.disponible()) {
                    cantidades[1]++;
                } else if (vehiculo.getEstado().equals("Alquilado")) {
                    cantidades[2]++;
                } else {
                    cantidades[3]++;
                }
            }
        }

        return cantidades;
    }

    public double[] ingresos(String categoria) {
        double[] montos = new double[2]; // Cobrado y descuentos de esta ejecucion.

        for (Cliente cliente : clientes) {
            for (Alquiler alquiler : cliente.getAlquileres()) {
                if (!alquiler.esPrevio()
                        && (categoria == null || alquiler.getVehiculo().categoria().equals(categoria))) {
                    montos[0] += alquiler.getTotal();
                    montos[1] += alquiler.getDescuento();
                }
            }
        }

        montos[0] = Vehiculo.redondear(montos[0]);
        montos[1] = Vehiculo.redondear(montos[1]);

        return montos;
    }

    private void mostrarReportes() {
        int opcion = vista.leerOpcion("Reporte: 1 Flota, 2 Ingresos y descuentos, 3 Activos, 4 Historial, 0 Volver: ",
                0, 4);

        switch (opcion) {
            case 1:
                for (String categoria : getCategorias()) {
                    vista.mostrarConteo(categoria, contarFlota(categoria));
                }

                vista.mostrarConteo("TOTAL", contarFlota(null));
                break;

            case 2:
                for (String categoria : getCategorias()) {
                    vista.mostrarIngresos(categoria, ingresos(categoria));
                }

                vista.mostrarIngresos("TOTAL DE ESTA EJECUCION", ingresos(null));
                break;

            case 3:
                vista.mostrarAlquileresActivos(clientes);
                break;

            case 4:
                vista.mostrarHistorial(buscarCliente(vista.leerTexto("Identificador del cliente: ")));
                break;

            default:
                break;
        }
    }

    private void cargarDatosIniciales() {
        registrarVehiculo(new Automovil("P001AAA", "Toyota", "Corolla", 200, 1, 5, false));
        registrarVehiculo(new Automovil("P002AAA", "Honda", "Civic", 250, 1, 5, true));
        registrarVehiculo(new Motocicleta("M001AAA", "Honda", "CB250", 100, 1, 250));
        registrarVehiculo(new Motocicleta("M002AAA", "Yamaha", "MT03", 150, 1, 321));
        registrarVehiculo(new Camioneta("C001AAA", "Toyota", "Hilux", 200, 1, 1.5));
        registrarVehiculo(new Camioneta("C002AAA", "Isuzu", "DMax", 300, 1, 2.0));
        registrarVehiculo(new Microbus("B001AAA", "Toyota", "Hiace", 450, 1, 15, true));
        registrarVehiculo(new Microbus("B002AAA", "Hyundai", "H1", 350, 1, 12, false));
        registrarCliente(new Individual("1234567890101", "Ana Lopez", new ArrayList<>(Arrays.asList("C"))));
        registrarCliente(new Individual("1234567890102", "Luis Perez", new ArrayList<>(Arrays.asList("A", "M"))));
        registrarCliente(new Corporativo("1234567-8", "Transportes Centro", new ArrayList<>(Arrays.asList("B", "M")),
                "Maria Diaz"));
        registrarCliente(
                new Corporativo("7654321-0", "Servicios Norte", new ArrayList<>(Arrays.asList("C")), "Pedro Ruiz"));

        for (int duracion : new int[] { 10, 10, 9 }) {
            Alquiler previo = cotizar("P001AAA", "1234567890101", duracion);

            confirmarAlquiler("1234567890101", previo, true);
            devolverVehiculo("P001AAA");

            previo.marcarComoPrevio();
        }
    }
}