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
        // ✅ Si ya hay un socket válido, cerrar el nuevo y salir
        if(this.socket != null && !this.socket.isClosed()){
            try {
                System.out.println("⚠️ Ya existe socket válido, cerrando el nuevo");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // ✅ Limpiar socket anterior si existe pero está cerrado
        if (this.socket != null && this.socket.isClosed()) {
            System.out.println("🧹 Limpiando socket anterior cerrado");
            this.socket = null;
            this.in = null;
            this.out = null;
        }

        // ✅ Verificar que el socket entrante es válido
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            System.err.println("❌ Socket inválido recibido en setSocket()");
            return;
        }

        this.socket = socket;

        try {
            System.out.println("🔧 Creando ObjectOutputStream...");
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            System.out.println("🔧 Creando ObjectInputStream...");
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("✅ Streams creados exitosamente");

            // ✅ Iniciar threads solo si todo salió bien
            new Thread(this).start();
            new Thread(healthChannel).start();

        } catch (EOFException e) {
            System.err.println("❌ EOFException: El socket remoto se cerró antes de completar el handshake");
            limpiarSocket();
        } catch (IOException e) {
            System.err.println("❌ Error creando streams: " + e.getMessage());
            e.printStackTrace();
            limpiarSocket();
        }
    }

    /**
     * ✅ NUEVO: Método para limpiar el socket cuando falla
     */
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

        System.out.println("🧹 Socket limpiado después de error");
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

    public synchronized void close(){
        System.out.println("🛑 Cerrando Channel...");

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

        System.out.println("✅ Channel cerrado completamente");
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
                    System.out.println("⚠️ Mensaje nulo recibido, cerrando conexión");
                    close();
                    break;
                }

                procesarMensaje(msg);

            } catch (EOFException e) {
                System.err.println("❌ Conexión cerrada por el otro extremo (EOFException)");
                close();
                break;
            } catch (IOException e) {
                System.err.println("❌ Error de I/O: " + e.getMessage());
                close();
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Clase no encontrada: " + e.getMessage());
                close();
                break;
            }
        }

        System.out.println("🛑 Thread de lectura del Channel terminado");
    }
}
