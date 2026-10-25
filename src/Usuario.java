 public class Usuario implements Runnable {
        private int id;
        private int origen;
        private int destino;
        private Ascensor ascensor;

        public Usuario(int id, int origen, int destino, Ascensor ascensor) {
            this.id = id;
            this.origen = origen;
            this.destino = destino;
            this.ascensor = ascensor;
        }

        public int getId() {
            return id;
        }

        public int getOrigen() {
            return origen;
        }

        public int getDestino() {
            return destino;
        }

        @Override
        public void run() {
            System.out.println("Usuario " + id + " espera en piso " + origen);
            ascensor.subir(this);   // espera a que el ascensor llegue a su piso y entra
            ascensor.bajar(this);   // espera a que el ascensor llegue a su destino y sale
        }
    }
