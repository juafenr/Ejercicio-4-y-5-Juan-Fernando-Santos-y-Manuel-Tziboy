public class ControladorRentaMovil {
    private VistaRentaMovil vista = new VistaRentaMovil();

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            int opcion = vista.mostrarMenu();

            switch (opcion) {
                case 1:
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Ejecucion terminada");
                    break;
                default:
                    System.out.println("Opcion invalida. Intentalo nuevamente");
                    break;
            }
        }
    }
}