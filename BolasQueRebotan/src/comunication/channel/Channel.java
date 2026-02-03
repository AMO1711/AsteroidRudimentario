package comunication.channel;

import comunication.ComController;

import java.io.*;
import java.net.Socket;

public class Channel implements Runnable{
    private ComController comController;
    private Socket socket;
    private String ip;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private HealthChannel healthChannel;

    public Channel(String ip, ComController comController){
        this.comController = comController;
        this.socket = null;
        this.ip = ip;
        this.healthChannel = new HealthChannel(this);
    }

    public boolean isValid(){
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized void setSocket(Socket socket){
        if(this.socket != null && !this.socket.isClosed()){
            return; // ya hay conexión activa
        }

        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // importante

            in = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
            new Thread(healthChannel).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void send(MsgDTO msg){
        if(out != null){
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                close();
            }
        }
    }

    private void procesarMensaje(MsgDTO msg){
        int codigo = msg.getCode();

        switch (codigo){
            case 0: //El mensaje es una bola
                comController.introducirBola(msg.getBall());
                break;
            case 1: //El mensaje ha sido enviado para comprobar la conexión
                send(new MsgDTO(2, null));
                break;
            case 2: //El mensaje ha sido enviado para decir que la conexión va bien
                healthChannel.notifyHealthy();
                break;
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void comprobarConexion(){
        send(new MsgDTO(1, null));
    }

    public void lanzarBola(BolaDTO bolaDTO){
        send(new MsgDTO(0, bolaDTO));
    }

    @Override
    public void run() {
        MsgDTO msg;

        while (socket != null && !socket.isClosed()){
            try {
                msg = (MsgDTO) in.readObject();  // 🔥 AQUÍ ESTÁ EL CAMBIO

                if (msg == null) {
                    socket.close();
                    break;
                }

                procesarMensaje(msg);

            } catch (IOException | ClassNotFoundException e) {
                close();
                break;
            }
        }
    }
}
