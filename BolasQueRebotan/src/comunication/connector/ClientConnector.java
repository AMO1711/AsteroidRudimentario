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

        while (true){
            if (!comController.isValid()){
                if (Objects.equals(HOST, "localhost")){
                    try {
                        socket = new Socket(HOST, comController.getAvailablePort());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        socket = new Socket(HOST, mainPort);
                    } catch (IOException e) {
                        try {
                            socket = new Socket(HOST, auxPort);
                        } catch (IOException ex) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException exc) {
                                throw new RuntimeException(exc);
                            }
                            continue;
                        }
                    }
                }

                comController.setSocket(socket);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
