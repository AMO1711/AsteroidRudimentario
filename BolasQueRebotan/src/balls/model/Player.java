package balls.model;

import balls.physics.PhysicsModel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player implements KeyListener {
    private final Model model;
    private final int DIAMETER;
    private String imagen;
    private final Color COLOR;
    private PlayerState estadoPlayer;
    private PhysicsModel modeloFisico;

    public Player(Model modelo, int diametro, String imagen, PhysicsModel modeloFisico){
        this.model = modelo;
        this.COLOR = new Color(0, 0, 0);
        this.DIAMETER = diametro;
        this.imagen = imagen;
        this.estadoPlayer = PlayerState.INICIALIZING;
        this.modeloFisico = modeloFisico;
    }

    public void playerActivation(){
        estadoPlayer = PlayerState.ALIVE;
    }

    public void play(){
        estadoPlayer = PlayerState.ALIVE;
    }

    public void pause(){
        estadoPlayer = PlayerState.PAUSED;
    }

    public void stop(){
        estadoPlayer = PlayerState.DEAD;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (estadoPlayer == PlayerState.ALIVE && model.getEstado() == State.PLAY){
            int tecla = e.getKeyCode();

            int w = model.getViewerWidth();
            int h = model.getViewerHeight();

            if (tecla == KeyEvent.VK_UP){
                modeloFisico.up(w, h, DIAMETER);
            } else if (tecla == KeyEvent.VK_DOWN) {
                modeloFisico.down(w, h, DIAMETER);
            } else if (tecla == KeyEvent.VK_RIGHT) {
                modeloFisico.right(w, h, DIAMETER);
            } else if (tecla == KeyEvent.VK_LEFT) {
                modeloFisico.left(w, h, DIAMETER);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}


    public String getImagen() {
        return imagen;
    }

    public int getDIAMETER() {
        return DIAMETER;
    }

    public Color getCOLOR() {
        return COLOR;
    }

    public PhysicsModel getModeloFisico() {
        return modeloFisico;
    }
}
