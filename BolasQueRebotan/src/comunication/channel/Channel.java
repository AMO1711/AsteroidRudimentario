package comunication.channel;

import comunication.ComController;
import comunication.helpers.BolaDTO;
import comunication.helpers.MsgDTO;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Channel implements Runnable {
    private ComController comController;
    private Socket socket;
    private String ip;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private HealthChannel healthChannel;
    private Thread readerThread;
    private Thread healthThread;

    public Channel(String ip, ComController comController){
        this.comController = comController;
        this.socket = null;
        this.ip = ip;
        this.healthChannel = null;
        this.readerThread = null;
        this.healthThread = null;
    }

    public boolean isValid(){
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized void setSocket(Socket socket){
        // Si ya hay un socket válido, cerrar el nuevo y salir
        if(this.socket != null && !this.socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Limpiar socket anterior si existe pero está cerrado
        if (this.socket != null && this.socket.isClosed()) {
            limpiarSocket();
        }

        // Verificar que el socket entrante es válido
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            System.err.println("Socket inválido recibido en setSocket()");
            return;
        }

        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());

            this.healthChannel = new HealthChannel(this);

            readerThread = new Thread(this, "ChannelReader");
            readerThread.start();

            healthThread = new Thread(healthChannel, "HealthChannel");
            healthThread.start();

        } catch (EOFException e) {
            System.err.println("EOFException: El socket remoto se cerró antes de completar el handshake");
            limpiarSocket();
        } catch (IOException e) {
            System.err.println("Error creando streams: " + e.getMessage());
            e.printStackTrace();
            limpiarSocket();
        }
    }

    public synchronized void send(MsgDTO msg){
        if(out != null && socket != null && !socket.isClosed()){
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                System.err.println("Error enviando mensaje: " + e.getMessage());
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
                if (healthChannel != null) {
                    healthChannel.notifyHealthy();
                }
                break;
        }
    }

    public synchronized void close(){
        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        readerThread = null;
        healthThread = null;
    }

    private void limpiarSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.socket = null;
        this.out = null;
        this.in = null;

        System.out.println("Socket limpiado después de error");
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
                msg = (MsgDTO) in.readObject();

                if (msg == null) {
                    close();
                    break;
                }

                procesarMensaje(msg);

            } catch (EOFException e) {
                System.err.println("Conexión cerrada por el otro extremo (EOFException)");
                close();
                break;
            } catch (IOException e) {
                System.err.println("Error de I/O: " + e.getMessage());
                close();
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Clase no encontrada: " + e.getMessage());
                close();
                break;
            }
        }

        System.out.println("Thread de lectura del Channel terminado");
    }
}