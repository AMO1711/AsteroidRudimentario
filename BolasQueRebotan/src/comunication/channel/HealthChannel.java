package comunication.channel;

public class HealthChannel implements Runnable{
    private boolean isHealthy;
    private Channel channel;

    public HealthChannel(Channel channel){
        this.isHealthy = true;
        this.channel = channel;
    }

    @Override
    public void run() {
        while (isHealthy){
            channel.comprobarConexion();
            isHealthy = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        channel.close();
    }

    public void notifyHealthy(){
        isHealthy = true;
    }
}
