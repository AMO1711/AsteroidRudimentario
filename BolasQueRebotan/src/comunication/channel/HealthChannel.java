package comunication.channel;

public class HealthChannel implements Runnable{
    private boolean isHealthy;
    private Channel channel;
    private long ultimaRespuesta;
    private static final long TIMEOUT = 10000; // 10 segundos de timeout
    private static final long CHECK_INTERVAL = 3000; // Verificar cada 3 segundos

    public HealthChannel(Channel channel){
        this.isHealthy = true;
        this.channel = channel;
        this.ultimaRespuesta = System.currentTimeMillis();
    }

    @Override
    public void run() {
        System.out.println("🏥 HealthChannel iniciado");

        while (true){
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Enviar ping
            channel.comprobarConexion();
            System.out.println("📡 Ping enviado, esperando respuesta...");

            // Esperar un momento para la respuesta
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Verificar si recibimos respuesta a tiempo
            long tiempoSinRespuesta = System.currentTimeMillis() - ultimaRespuesta;

            if (tiempoSinRespuesta > TIMEOUT) {
                System.err.println("❌ Sin respuesta por " + (tiempoSinRespuesta/1000) + " segundos. Cerrando conexión.");
                channel.close();
                break;
            } else {
                System.out.println("✅ Conexión saludable (última respuesta hace " + (tiempoSinRespuesta/1000) + "s)");
            }
        }

        System.out.println("🛑 HealthChannel terminado");
    }

    public synchronized void notifyHealthy(){
        this.ultimaRespuesta = System.currentTimeMillis();
        System.out.println("💚 Pong recibido");
    }
}