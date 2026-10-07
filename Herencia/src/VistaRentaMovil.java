import java.util.ArrayList;
import java.util.Scanner;
import java.util.Locale;

public class VistaRentaMovil {
    private Scanner sc;

    public VistaRentaMovil() {
        this(new Scanner(System.in));
    }

    public VistaRentaMovil(Scanner sc) {
        this.sc = sc;
    }

    public int mostrarMenu() {
        mostrarMensaje("\n=== RentaMovil ===\n1 Registrar vehiculo\n2 Registrar cliente"
                + "\n3 Consultar flota\n4 Consultar clientes\n5 Cotizar (sin confirmar)"
                + "\n6 Alquilar (confirmar o cancelar)\n7 Registrar devolucion"
                + "\n8 Finalizar mantenimiento\n9 Reportes\n0 Salir");

        return leerOpcion("Opcion: ", 0, 9);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            mostrarMensaje("El dato no puede estar vacio.");
        }
    }

    public int leerOpcion(String mensaje, int minimo, int maximo) {
        while (true) {
            try {
                int valor = Integer.parseInt(leerTexto(mensaje));

                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException ex) {
                // Se vuelve a pedir la linea; no se mezcla nextInt con nextLine.
            }

            mostrarMensaje("Ingrese un entero entre " + minimo + " y " + maximo + ".");
        }
    }

    public int leerEnteroPositivo(String mensaje) {
        return leerOpcion(mensaje, 1, Integer.MAX_VALUE);
    }

    public double leerPositivo(String mensaje) {
        while (true) {
            try {
                double valor = Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
                if (Double.isFinite(valor) && valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException ex) {
                // El siguiente intento lee una linea nueva.
            }

            mostrarMensaje("Ingrese un numero positivo y finito, sin separadores de miles.");
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);

            if (respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("si")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n") || respuesta.equalsIgnoreCase("no")) {
                return false;
            }

            mostrarMensaje("Responda s o n.");
        }
    }

    public ArrayList<String> leerLicencias() {
        while (true) {
            String entrada = leerTexto("Licencias A B C M (separadas por espacios o comas): ").toUpperCase(Locale.ROOT);
            ArrayList<String> licencias = new ArrayList<>();
            boolean validas = true;

            for (String licencia : entrada.split("[,\\s]+")) {
                if (!licencia.equals("A") && !licencia.equals("B") && !licencia.equals("C") && !licencia.equals("M")) {
                    validas = false;
                }

                if (!licencias.contains(licencia)) {
                    licencias.add(licencia);
                }
            }

            if (validas && !licencias.isEmpty()) {
                return licencias;
            }

            mostrarMensaje("Debe ingresar al menos una licencia, usando solamente A, B, C o M.");
        }
    }

    public void mostrarVehiculos(ArrayList<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) {
            mostrarMensaje("No hay vehiculos registrados.");
        }

        for (Vehiculo vehiculo : vehiculos) {
            mostrarMensaje(vehiculo.descripcion());
        }
    }

    public void mostrarClientes(ArrayList<Cliente> clientes) {
        if (clientes.isEmpty()) {
            mostrarMensaje("No hay clientes registrados.");
        }

        for (Cliente cliente : clientes) {
            mostrarMensaje(cliente.descripcion());
        }
    }

    public void mostrarCotizacion(Cliente cliente, Alquiler propuesta, ArrayList<String> razones) {
        mostrarMensaje("\nCliente: " + cliente.getNombre() + " | " + cliente.getIdentificador());
        mostrarMensaje(propuesta.getVehiculo().descripcion());
        mostrarMensaje("Licencia requerida: " + propuesta.getVehiculo().licenciaRequerida());
        mostrarMensaje("Dias: " + propuesta.getDias());

        System.out.printf(Locale.US, "Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f%n",
                propuesta.getSubtotal(), propuesta.getDescuento(), propuesta.getTotal());

        if (razones.isEmpty()) {
            mostrarMensaje("Puede alquilar en este momento.");
        } else {
            for (String razon : razones) {
                mostrarMensaje("No puede alquilar: " + razon);
            }
        }
    }

    public void mostrarConteo(String categoria, int[] cantidades) {
        System.out.printf("%s | Total %d | Disponibles %d | Alquilados %d | Mantenimiento %d%n",
                categoria, cantidades[0], cantidades[1], cantidades[2], cantidades[3]);
    }

    public void mostrarIngresos(String categoria, double[] montos) {
        System.out.printf(Locale.US, "%s | Ingresos Q%.2f | Descuentos Q%.2f%n", categoria, montos[0], montos[1]);
    }

    private void mostrarAlquiler(Cliente cliente, Alquiler alquiler) {
        System.out.printf(Locale.US,
                "#%d | Cliente %s (%s) | Vehiculo %s | Dias %d | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f | %s%s%n",
                alquiler.getNumero(), cliente.getNombre(), cliente.getIdentificador(),
                alquiler.getVehiculo().getPlaca(),
                alquiler.getDias(), alquiler.getSubtotal(), alquiler.getDescuento(), alquiler.getTotal(),
                alquiler.estaActivo() ? "Activo" : "Finalizado", alquiler.esPrevio() ? " | Historico previo" : "");
    }

    public void mostrarAlquileresActivos(ArrayList<Cliente> clientes) {
        boolean encontrado = false;

        for (Cliente cliente : clientes) {
            for (Alquiler alquiler : cliente.getAlquileres()) {
                if (alquiler.estaActivo()) {
                    mostrarAlquiler(cliente, alquiler);
                    encontrado = true;
                }
            }
        }

        if (!encontrado) {
            mostrarMensaje("No hay alquileres activos.");
        }
    }

    public void mostrarHistorial(Cliente cliente) {
        mostrarMensaje(cliente.descripcion());

        if (cliente.getAlquileres().isEmpty()) {
            mostrarMensaje("Sin alquileres confirmados.");
        }

        for (Alquiler alquiler : cliente.getAlquileres()) {
            mostrarAlquiler(cliente, alquiler);
        }

        System.out.printf(Locale.US, "Total pagado (incluye historial previo): Q%.2f%n", cliente.calcularTotalPagado());
    }
}