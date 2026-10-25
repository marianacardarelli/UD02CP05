public class Main {
    public static void main(String[] args) {
        Ascensor ascensor = new Ascensor();

        Usuario u1 = new Usuario(1, 0, 3, ascensor);
        Usuario u2 = new Usuario(2, 2, -1, ascensor);
        Usuario u3 = new Usuario(3, 1, 5, ascensor);

        // Primero se cargan los usuarios en la lista, antes de arrancar ningún hilo
        ascensor.agregarUsuario(u1);
        ascensor.agregarUsuario(u2);
        ascensor.agregarUsuario(u3);

        new Thread(ascensor, "Ascensor").start();
        new Thread(u1).start();
        new Thread(u2).start();
        new Thread(u3).start();
    }
}
