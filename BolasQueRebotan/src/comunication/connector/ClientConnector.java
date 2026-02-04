package comunication.connector;

import comunication.ComController;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientConnector implements Runnable{
    private final ComController comController;
    private final int mainPort, auxPort;
    private final String HOST;

    public ClientConnector(ComController comController, int mainPort, int auxPort, String ip){
        this.HOST = ip;
        this.comController = comController;
        this.mainPort = mainPort;
        this.auxPort = auxPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        int intentos = 0;

        System.out.println("ClientConnector iniciado - Intentando conectar a: " + HOST);

        while (true){
            // ✅ Solo intentar conectar si NO hay conexión válida
            if (!comController.isValid()){
                intentos++;
                System.out.println("🔄 Intento de conexión #" + intentos + " a " + HOST);

                if (Objects.equals(HOST, "localhost")){
                    try {
                        socket = new Socket(HOST, comController.getAvailablePort());
                        System.out.println("✅ Conectado a localhost");
                    } catch (IOException e) {
                        System.err.println("❌ Error conectando a localhost: " + e.getMessage());
                        // Reintentar después de esperar
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException exc) {
                            throw new RuntimeException(exc);
                        }
                        continue;
                    }
                } else {
                    try {
                        socket = new Socket(HOST, mainPort);
                        System.out.println("✅ Conectado a " + HOST + ":" + mainPort);
                    } catch (IOException e) {
                        System.err.println("❌ Falló puerto " + mainPort + ": " + e.getMessage());
                        try {
                            socket = new Socket(HOST, auxPort);
                            System.out.println("✅ Conectado a " + HOST + ":" + auxPort);
                        } catch (IOException ex) {
                            System.err.println("❌ Falló puerto " + auxPort + ": " + ex.getMessage());
                            System.err.println("⏳ Esperando 5 segundos antes de reintentar...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException exc) {
                                throw new RuntimeException(exc);
                            }
                            continue; // ✅ Reintentar sin setear socket
                        }
                    }
                }

                // ✅ Solo setear si el socket fue creado exitosamente
                if (socket != null && socket.isConnected()) {
                    comController.setSocket(socket);
                    intentos = 0; // Reset contador
                }
            } else {
                // ✅ Ya hay conexión válida, esperar más tiempo
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
