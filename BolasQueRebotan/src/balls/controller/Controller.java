package balls.controller;

import balls.model.Asteroid;
import balls.model.Player;
import balls.model.ZonaCritica;
import balls.view.View;
import balls.model.Model;
import comunication.channel.BolaDTO;
import helpers.MasterController;

import java.util.ArrayList;

public class Controller {
    private final MasterController controladorMaestro;
    private final Model model;
    private final View view;

    public Controller(MasterController controladorMaestro) {
        this.controladorMaestro = controladorMaestro;
        this.model = new Model(this);
        this.view = new View(this);

        view.viewerActivation();
    }

    public void playAnimation(){
        model.play();
    }

    public void pauseAnimation(){
        model.pause();
    }

    public void stopAnimation(){
        model.stop();
    }

    public void eventManager(Events evento, Asteroid pelota){
        if (evento == Events.NORTH_LIMIT_REACHED){
            model.northRebound(pelota);
        } else if (evento == Events.SOUTH_LIMIT_REACHED) {
            model.southRebound(pelota);
        } else if (evento == Events.EAST_LIMIT_REACHED) { //todo pasar a la otra pantalla
            lanzarBola(pelota);
            pelota.stop();
        } else if (evento == Events.WEST_LIMIT_REACHED) { //todo pasar a la otra pantalla
            lanzarBola(pelota);
            pelota.stop();
        }
    }

    public void eventManager(Events evento, Asteroid pelota, ZonaCritica room){
        if (evento == Events.GO_INSIDE_ROOM){
            model.goInside(pelota, room);
        } else if (evento == Events.GO_OUTSIDE_ROOM) {
            model.goOutside(pelota, room);
        }
    }

    private void lanzarBola (Asteroid bola){
        int posX = getViewerWidth() - bola.getModeloFisico().pv.posicion.x;

        BolaDTO bolaDTO = new BolaDTO(bola.getDIAMETER(),
                posX, bola.getModeloFisico().pv.posicion.y,
                bola.getModeloFisico().pv.velocidad.x, bola.getModeloFisico().pv.velocidad.y,
                bola.getModeloFisico().pv.aceleracion.x, bola.getModeloFisico().pv.aceleracion.y,
                bola.getCOLOR());

        controladorMaestro.lanzarBola(bolaDTO);
    }

    public void moveUp(Player player){model.moveUp(player);}

    public void moveDown(Player player){model.moveDown(player);}

    public void moveLeft(Player player){model.moveLeft(player);}

    public void moveRight(Player player){model.moveRight(player);}

    public ArrayList<ZonaCritica> getAllRooms(){
         return model.getAllRooms();
    }

    public ArrayList<Player> getAllPlayers(){return model.getAllPlayers();}

    public void introducirBola (BolaDTO bolaDTO){
        model.addBall(bolaDTO);
    }

    public void addBall() {
        model.addBall();
    }

    public void autoAddBall(){
        model.autoAddBall();
    }

    public ArrayList<Asteroid> getAllBalls() {
        return model.getAllBalls();
    }

    public int getViewerWidth() {
        return view.getViewerWidth();
    }

    public int getViewerHeight() {
        return view.getViewerHeight();
    }

    public Model getModel() {
        return model;
    }
}
