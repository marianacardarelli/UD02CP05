import java.util.LinkedList;

    public class Ascensor implements Runnable {
        private int pisoActual = 0;
        private final LinkedList<Usuario> pendientes = new LinkedList<>();
        private Usuario atendiendo = null;     // usuario al que se sirve ahora
        private boolean usuarioDentro = false; // true si ese usuario ya está dentro

        // ---------- Métodos que llaman los usuarios ----------

        public synchronized void agregarUsuario(Usuario u) {
            pendientes.add(u);
        }

        public synchronized void subir(Usuario u) {
            while (atendiendo != u || pisoActual != u.getOrigen()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("Usuario " + u.getId() + " sube al ascensor en piso " + pisoActual + ".");
            usuarioDentro = true;
            notifyAll();
        }

        public synchronized void bajar(Usuario u) {
            while (!usuarioDentro || atendiendo != u || pisoActual != u.getDestino()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("Usuario " + u.getId() + " baja en piso " + pisoActual);
            usuarioDentro = false;
            notifyAll();
        }

        // ---------- Métodos internos del ascensor ----------

        // Solo el hilo del ascensor modifica pisoActual, por eso puede leerlo en el while
        private void irAPiso(int destino) {
            while (pisoActual != destino) {
                try {
                    Thread.sleep(500);           // fuera del synchronized
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                synchronized (this) {
                    pisoActual += (destino > pisoActual) ? 1 : -1;
                    System.out.println("Ascensor en piso " + pisoActual);
                    notifyAll();                 // avisa de que cambió el piso
                }
            }
        }

        private synchronized void recoger(Usuario u) {
            atendiendo = u;
            notifyAll();
            while (!usuarioDentro) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        private synchronized void dejar(Usuario u) {
            notifyAll();
            while (usuarioDentro) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            pendientes.removeFirst();
            atendiendo = null;
        }

        // ---------- Hilo del ascensor ----------

        @Override
        public void run() {
            while (true) {
                Usuario siguiente;
                synchronized (this) {
                    siguiente = pendientes.peekFirst(); // null si no quedan usuarios
                }
                if (siguiente == null) {
                    break;
                }
                irAPiso(siguiente.getOrigen());
                recoger(siguiente);
                irAPiso(siguiente.getDestino());
                dejar(siguiente);
            }

            irAPiso(-1);
            System.out.println("Ascensor llegó a piso -1. Fin del programa.");
        }
    }
