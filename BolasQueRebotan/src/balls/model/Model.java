package balls.model;

import balls.controller.Controller;
import balls.controller.Events;
import balls.physics.DesgloseXY;
import balls.physics.PhysicsModel;
import balls.physics.PhysicsValuesDTO;

import java.util.ArrayList;

public class Model {
    private final Controller controller;
    private ArrayList<Asteroid> asteroidList;
    private ArrayList<ZonaCritica> rooms;
    private ArrayList<Player> jugadores;
    private State estado;
    private int numeroMaxBolas = 200, tamanoMinimoBola = 10, tamanoMaximoBola = 20,
            velocidadMinimaBolaX = 1, velocidadMaximaBolaX = 3, velocidadMinimaBolaY = 1, velocidadMaximaBolaY = 3,
            aceleracionMinimaBolaX = -2, aceleracionMaximaBolaX = 2, aceleracionMinimaBolaY = -2, aceleracionMaximaBolaY = 2,
            tiempoMinimoAuto = 1000, tiempoMaximoAuto = 2000;

    public Model(Controller controller) {
        this.controller = controller;
        this.asteroidList = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.rooms = new ArrayList<>();
        rooms.add(new ZonaCritica());
        estado = State.STOP;
        inicializarJugador1();
    }

    public void addBall(){
        if (asteroidList.size() < numeroMaxBolas && estado == State.PLAY){
            int x,y, velX, velY, accX, accY;
            DesgloseXY posicion, velocidad, aceleracion;
            PhysicsModel pm;

            x = (int) (Math.random() * (getViewerWidth() -2*tamanoMaximoBola) + tamanoMaximoBola);
            y = (int) (Math.random() * (getViewerHeight() - 2*tamanoMaximoBola) + tamanoMaximoBola);
            posicion = new DesgloseXY(x, y);
            velX = (int)(Math.random() * (velocidadMaximaBolaX-velocidadMinimaBolaX+1) + velocidadMinimaBolaX);
            velY = (int)(Math.random() * (velocidadMaximaBolaY-velocidadMinimaBolaY+1) + velocidadMinimaBolaY);
            velocidad = new DesgloseXY(velX, velY);
            accX = (int)(Math.random() * (aceleracionMaximaBolaX-aceleracionMinimaBolaX+1) + aceleracionMinimaBolaX);
            accY = (int)(Math.random() * (aceleracionMaximaBolaY-aceleracionMinimaBolaY+1) + aceleracionMinimaBolaY);
            aceleracion = new DesgloseXY(accX, accY);

            pm = new PhysicsModel(new PhysicsValuesDTO(posicion, velocidad, aceleracion));

            Asteroid asteroid = new Asteroid(this, tamanoMinimoBola, tamanoMaximoBola, pm);
            asteroid.ballActivation();
            asteroidList.add(asteroid);
        }
    }

    public void autoAddBall(){
        AutoBall auto = new AutoBall(this);
        Thread thread = new Thread(auto);
        thread.start();
    }

    public void inicializarJugador1(){
        PhysicsModel modeloFisico = new PhysicsModel(new PhysicsValuesDTO(new DesgloseXY(250, 400), new DesgloseXY(0, 0), new DesgloseXY(0, 0)));
        Player jugador = new Player(this, 50, "src/balls/graphics/Player.png", modeloFisico);
        jugador.playerActivation();

        jugadores.add(jugador);
    }

    public void stop(){
        estado = State.STOP;
        asteroidList.clear();
        rooms.clear();
        rooms.add(new ZonaCritica());
    }

    public void pause(){
        estado = State.PAUSE;
    }

    public void play(){
        estado = State.PLAY;
    }

    public void eventDetector(Asteroid pelota){
        DesgloseXY posicionPropuesta = new DesgloseXY(pelota.getModeloFisico().pv.posicion.x+pelota.getModeloFisico().pv.velocidad.x,
                pelota.getModeloFisico().pv.posicion.y+pelota.getModeloFisico().pv.velocidad.y);
        ArrayList<ZonaCritica> habitacionesEntradas = new ArrayList<>(), habitacionesSalir = new ArrayList<>();

        if (posicionPropuesta.x <= 0){
            controller.eventManager(Events.WEST_LIMIT_REACHED, pelota);
        } else if (posicionPropuesta.x + pelota.getDIAMETER() >= this.getViewerWidth()) {
            controller.eventManager(Events.EAST_LIMIT_REACHED, pelota);
        } else if (posicionPropuesta.y <= 0) {
            controller.eventManager(Events.NORTH_LIMIT_REACHED, pelota);
        } else if (posicionPropuesta.y + pelota.getDIAMETER() >= this.getViewerHeight()) {
            controller.eventManager(Events.SOUTH_LIMIT_REACHED, pelota);
        }

        for (ZonaCritica room: rooms) {
            if (room.getPosicion().x < (posicionPropuesta.x + pelota.getDIAMETER()) &&
                    posicionPropuesta.x < (room.getPosicion().x + room.getDimensiones().x) &&
                    room.getPosicion().y < (posicionPropuesta.y + pelota.getDIAMETER()) &&
                    posicionPropuesta.y < (room.getPosicion().y + room.getDimensiones().y)){
                controller.eventManager(Events.GO_INSIDE_ROOM, pelota, room);
                habitacionesEntradas.add(room);
            }
        }

        if (pelota.getHabitacionesActuales().size() != habitacionesEntradas.size()){
            for (ZonaCritica room: pelota.getHabitacionesActuales()) {
                if (!habitacionesEntradas.contains(room)){
                    habitacionesSalir.add(room);
                }
            }

            for (ZonaCritica room: habitacionesSalir){
                controller.eventManager(Events.GO_OUTSIDE_ROOM, pelota, room);
            }
        }
    }

    public void northRebound(Asteroid pelota){
        pelota.northRebound();
    }

    public void southRebound(Asteroid pelota){
        pelota.southRebound();
    }

    public void eastRebound(Asteroid pelota){pelota.eastRebound();}

    public void westRebound(Asteroid pelota){pelota.westRebound();}

    public void goInside(Asteroid pelota, ZonaCritica room){
        room.goInside(pelota);
    }

    public void goOutside(Asteroid pelota, ZonaCritica room){
        room.goOutside(pelota);
    }

    public ArrayList<Asteroid> getAllBalls() {
        return new ArrayList<>(asteroidList);
    }

    public ArrayList<ZonaCritica> getAllRooms(){return new ArrayList<>(rooms);}

    public ArrayList<Player> getAllPlayers(){return new ArrayList<>(jugadores);}

    public int getViewerWidth() {
        return controller.getViewerWidth();
    }

    public int getViewerHeight() {
        return controller.getViewerHeight();
    }

    public Controller getController() {
        return controller;
    }

    public int getTamanoMinimoBola() {
        return tamanoMinimoBola;
    }

    public int getTamanoMaximoBola() {
        return tamanoMaximoBola;
    }

    public int getVelocidadMinimaBolaX() {
        return velocidadMinimaBolaX;
    }

    public int getVelocidadMaximaBolaX() {
        return velocidadMaximaBolaX;
    }

    public int getVelocidadMinimaBolaY() {
        return velocidadMinimaBolaY;
    }

    public int getVelocidadMaximaBolaY() {
        return velocidadMaximaBolaY;
    }

    public int getAceleracionMinimaBolaX() {
        return aceleracionMinimaBolaX;
    }

    public int getAceleracionMaximaBolaX() {
        return aceleracionMaximaBolaX;
    }

    public int getAceleracionMinimaBolaY() {
        return aceleracionMinimaBolaY;
    }

    public int getAceleracionMaximaBolaY() {
        return aceleracionMaximaBolaY;
    }

    public int getNumeroMaxBolas() {
        return numeroMaxBolas;
    }

    public int getTiempoMinimoAuto() {
        return tiempoMinimoAuto;
    }

    public int getTiempoMaximoAuto() {
        return tiempoMaximoAuto;
    }

    public State getEstado() {
        return estado;
    }

    public void setVelocidadMaximaBolaY(int velocidadMaximaBolaY) {
        this.velocidadMaximaBolaY = velocidadMaximaBolaY;
    }

    public void setVelocidadMinimaBolaY(int velocidadMinimaBolaY) {
        this.velocidadMinimaBolaY = velocidadMinimaBolaY;
    }

    public void setVelocidadMaximaBolaX(int velocidadMaximaBolaX) {
        this.velocidadMaximaBolaX = velocidadMaximaBolaX;
    }

    public void setVelocidadMinimaBolaX(int velocidadMinimaBolaX) {
        this.velocidadMinimaBolaX = velocidadMinimaBolaX;
    }

    public void setAceleracionMinimaBolaX(int aceleracionMinimaBolaX) {
        this.aceleracionMinimaBolaX = aceleracionMinimaBolaX;
    }

    public void setAceleracionMaximaBolaX(int aceleracionMaximaBolaX) {
        this.aceleracionMaximaBolaX = aceleracionMaximaBolaX;
    }

    public void setAceleracionMinimaBolaY(int aceleracionMinimaBolaY) {
        this.aceleracionMinimaBolaY = aceleracionMinimaBolaY;
    }

    public void setAceleracionMaximaBolaY(int aceleracionMaximaBolaY) {
        this.aceleracionMaximaBolaY = aceleracionMaximaBolaY;
    }

    public void setTamanoMaximoBola(int tamanoMaximoBola) {
        this.tamanoMaximoBola = tamanoMaximoBola;
    }

    public void setTamanoMinimoBola(int tamanoMinimoBola) {
        this.tamanoMinimoBola = tamanoMinimoBola;
    }

    public void setNumeroMaxBolas(int numeroMaxBolas) {
        this.numeroMaxBolas = numeroMaxBolas;
    }

    public void setTiempoMinimoAuto(int tiempoMinimoAuto) {
        this.tiempoMinimoAuto = tiempoMinimoAuto;
    }

    public void setTiempoMaximoAuto(int tiempoMaximoAuto) {
        this.tiempoMaximoAuto = tiempoMaximoAuto;
    }
}
