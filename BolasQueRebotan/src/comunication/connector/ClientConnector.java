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

        while (true){
            // ✅ Solo intentar conectar si NO hay conexión válida
            if (!comController.isValid()){
                intentos++;
                if (Objects.equals(HOST, "localhost")){
                    try {
                        socket = new Socket(HOST, comController.getAvailablePort());
                        System.out.println("Conectado a localhost");
                    } catch (IOException e) {
                        System.err.println("Error conectando a localhost: " + e.getMessage());
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
                    } catch (IOException e) {
                        try {
                            socket = new Socket(HOST, auxPort);
                        } catch (IOException ex) {
                            System.err.println("Esperando 5 segundos antes de reintentar...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException exc) {
                                throw new RuntimeException(exc);
                            }
                            continue;
                        }
                    }
                }

                if (socket != null && socket.isConnected()) {
                    comController.setSocket(socket);
                    intentos = 0;
                }
            } else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
