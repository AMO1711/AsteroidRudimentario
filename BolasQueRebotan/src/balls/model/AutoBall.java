package balls.model;

public class AutoBall implements Runnable{
    private final Model model;
    private int numeroMaxBolas, tiempoMinimoAuto, tiempoMaximoAuto, numeroBolas;

    public AutoBall(Model model){
        this.model = model;
        this.numeroBolas = model.getAllBalls().size();
        this.numeroMaxBolas = model.getNumeroMaxBolas();
        this.tiempoMaximoAuto = model.getTiempoMaximoAuto();
        this.tiempoMinimoAuto = model.getTiempoMinimoAuto();
    }

    @Override
    public void run() {
        while (numeroBolas < numeroMaxBolas){
            int miliseconds = (int) ((tiempoMaximoAuto - tiempoMinimoAuto + 1) * Math.random() + tiempoMinimoAuto);
            model.addBall();
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            numeroBolas = model.getAllBalls().size();
        }
    }

    public void setNumeroMaxBolas(int numeroMaxBolas) {
        this.numeroMaxBolas = numeroMaxBolas;
    }

    public void setTiempoMaximoAuto(int tiempoMaximoAuto) {
        this.tiempoMaximoAuto = tiempoMaximoAuto;
    }

    public void setTiempoMinimoAuto(int tiempoMinimoAuto) {
        this.tiempoMinimoAuto = tiempoMinimoAuto;
    }
}
