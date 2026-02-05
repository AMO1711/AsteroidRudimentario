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
            }

            if (!comController.isValid()) {
                try {
                    socket = serverSocket.accept();

                    if (!comController.isValid()) {
                        comController.setSocket(socket);
                    } else {
                        socket.close();
                    }

                } catch (IOException e) {
                    System.err.println("Error en ServerSocket: " + e.getMessage());
                    e.printStackTrace();
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
