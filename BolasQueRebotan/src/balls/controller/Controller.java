package balls.controller;

import balls.model.Asteroid;
import balls.model.Player;
import balls.model.ZonaCritica;
import balls.view.View;
import balls.model.Model;

import java.util.ArrayList;

public class Controller {
    private final Model model;
    private final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
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
        } else if (evento == Events.EAST_LIMIT_REACHED) {
            model.eastRebound(pelota);
        } else if (evento == Events.WEST_LIMIT_REACHED) {
            model.westRebound(pelota);
        }
    }

    public void eventManager(Events evento, Asteroid pelota, ZonaCritica room){
        if (evento == Events.GO_INSIDE_ROOM){
            model.goInside(pelota, room);
        } else if (evento == Events.GO_OUTSIDE_ROOM) {
            model.goOutside(pelota, room);
        }
    }

    public ArrayList<ZonaCritica> getAllRooms(){
         return model.getAllRooms();
    }

    public ArrayList<Player> getAllPlayers(){return model.getAllPlayers();}

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
}
