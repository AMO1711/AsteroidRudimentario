package balls.model;

import balls.physics.PhysicsModel;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Asteroid implements Runnable {
    private final Model model;
    private final int DIAMETER;
    private final Color COLOR;
    private Thread hilo;
    private AsteroidState estadoBola;
    private final ArrayList<ZonaCritica> habitacionesActuales;
    private final PhysicsModel modeloFisico;

    public Asteroid(Model model, int tamanoMinimo, int tamanoMaximo, PhysicsModel modeloFisico){
        this.model = model;
        this.modeloFisico = modeloFisico;

        DIAMETER = (int)((tamanoMaximo-tamanoMinimo+1) * Math.random() + tamanoMinimo);
        COLOR = new Color((int)(256 * Math.random()), (int)(256 * Math.random()), (int)(256 * Math.random()));
        estadoBola = AsteroidState.INICIALIZING;
        habitacionesActuales = new ArrayList<>();
    }

    public Asteroid(Model model, int diametro, Color color, PhysicsModel modeloFisico){
        this.model = model;
        this.modeloFisico = modeloFisico;

        DIAMETER = diametro;
        COLOR = color;
        estadoBola = AsteroidState.INICIALIZING;
        habitacionesActuales = new ArrayList<>();
    }

    public void ballActivation(){
        estadoBola = AsteroidState.ALIVE;
        hilo = new Thread(this);
        hilo.start();
    }

    public void play(){
        estadoBola = AsteroidState.ALIVE;
    }

    public void pause(){
        estadoBola = AsteroidState.PAUSED;
    }

    public void stop(){
        estadoBola = AsteroidState.DEAD;
    }

    public void northRebound(){
        modeloFisico.northRebound();
    }

    public void southRebound(){
        modeloFisico.southRebound(model.getViewerHeight() - getDIAMETER());
    }

    public void eastRebound(){modeloFisico.eastRebound(model.getViewerWidth() - getDIAMETER());}

    public void westRebound(){modeloFisico.westRebound();}

    public void roomEntered(ZonaCritica room){
        habitacionesActuales.add(room);
    }

    public void roomLeaved(ZonaCritica room){
        habitacionesActuales.remove(room);
    }

    public Color getCOLOR() {
        return this.COLOR;
    }

    public int getDIAMETER() {
        return this.DIAMETER;
    }

    public Thread getHilo() {
        return hilo;
    }

    public PhysicsModel getModeloFisico() {
        return modeloFisico;
    }

    public ArrayList<ZonaCritica> getHabitacionesActuales() {
        return habitacionesActuales;
    }

    @Override
    public void run() {
        while (model.getEstado() != State.STOP) {
            if (model.getEstado() != State.PAUSE && estadoBola == AsteroidState.ALIVE){
                model.eventDetector(this);

                modeloFisico.move();
            } else if (estadoBola == AsteroidState.DEAD) {
                break;
            }

            try {
                sleep(10); // Cada cuanto se aumenta la velocidad. Default: 1-3px/10ms -> 100-300px/s
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
