package balls.model;

import balls.physics.PhysicsModel;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Player{
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

    public void moveUp(){
        modeloFisico.up(model.getViewerWidth(), model.getViewerHeight(), DIAMETER);
    }

    public void moveDown() {
        modeloFisico.down(model.getViewerWidth(), model.getViewerHeight(), DIAMETER);
    }

    public void moveLeft() {
        modeloFisico.left(model.getViewerWidth(), model.getViewerHeight(), DIAMETER);
    }

    public void moveRight() {
        modeloFisico.right(model.getViewerWidth(), model.getViewerHeight(), DIAMETER);
    }

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

    public PlayerState getEstadoPlayer() {
        return estadoPlayer;
    }
}
