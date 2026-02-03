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

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (comController.isValid(socket)){
                try {
                    socket.close();
                } catch (IOException e) {
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
