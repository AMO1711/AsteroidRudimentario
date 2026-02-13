package comunication;

import comunication.helpers.BolaDTO;
import comunication.channel.Channel;
import comunication.connector.ClientConnector;
import comunication.connector.ServerConnector;

import java.net.Socket;

public class ComController {
    private final MasterController controladorMaestro;
    private final Channel channel;
    private final ClientConnector clientConnector;
    private final ServerConnector serverConnector;
    private final String ip;
    private final int localPort1 = 10000, localPort2 = 10001;

    public ComController(MasterController controladorMaestro, String ip, int mainPort, int auxPort){
        this.controladorMaestro = controladorMaestro;
        this.ip = ip;
        channel = new Channel(ip, this);
        clientConnector = new ClientConnector(this, mainPort, auxPort, ip);
        serverConnector = new ServerConnector(this, mainPort, auxPort);

        inicializar();
    }

    public boolean isValid(){return channel.isValid();}

    public boolean isValid(Socket socket){
        boolean isValid = channel.isValid();

        if (!isValid){
            setSocket(socket);
        }

        return isValid;
    }

    public void setSocket(Socket socket){channel.setSocket(socket);}

    public int getAvailablePort(){
        int port;

        while (!serverConnector.isConected()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        port = serverConnector.getActualPort();

        if (port == localPort2){
            return localPort1;
        } else {
            return localPort2;
        }
    }

    private void inicializar(){
        new Thread(serverConnector).start();
        new Thread(clientConnector).start();
    }

    public void introducirBola (BolaDTO bolaDTO){
        controladorMaestro.introducirBola(bolaDTO);
    }

    public void lanzarBola (BolaDTO bolaDTO){
        channel.lanzarBola(bolaDTO);
    }
}
