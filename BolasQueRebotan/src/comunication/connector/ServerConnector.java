package comunication.connector;

import comunication.ComController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnector implements Runnable{
    private final ComController comController;
    private ServerSocket serverSocket;
    private final int mainPort, auxPort;
    private int actualPort;

    public ServerConnector(ComController comController, int mainPort, int auxPort){
        this.comController = comController;
        this.serverSocket = null;
        this.mainPort = mainPort;
        this.auxPort = auxPort;
    }

    @Override
    public void run() {
        Socket socket;

        while (true){
            if (serverSocket == null){
                conectarPuerto();
                System.out.println("✅ ServerConnector escuchando en puerto: " + actualPort);
            }

            // ✅ Solo aceptar conexiones si NO hay una válida
            if (!comController.isValid()) {
                try {
                    System.out.println("⏳ Esperando conexión en puerto " + actualPort + "...");
                    socket = serverSocket.accept();
                    System.out.println("🔔 Conexión recibida desde: " + socket.getInetAddress().getHostAddress());

                    // ✅ Verificar nuevamente antes de setear (por si cambió durante accept)
                    if (!comController.isValid()) {
                        comController.setSocket(socket);
                    } else {
                        System.out.println("⚠️ Ya hay conexión, cerrando nueva");
                        socket.close();
                    }

                } catch (IOException e) {
                    System.err.println("❌ Error en ServerSocket: " + e.getMessage());
                    e.printStackTrace();
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

    private void conectarPuerto(){
        try {
            serverSocket = new ServerSocket(mainPort);
            actualPort = mainPort;
        } catch (IOException e) {
            try {
                serverSocket = new ServerSocket(auxPort);
                actualPort = auxPort;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean isConected(){return serverSocket != null;}

    public int getActualPort(){
        return actualPort;
    }
}
