package helpers;

import balls.controller.Controller;
import comunication.ComController;
import comunication.channel.BolaDTO;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MasterController {
    private final Controller controlador;
    private final ComController comunicaciones;

    public MasterController(){
        String ip = "localhost";

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        this.controlador = new Controller(this);
        this.comunicaciones = new ComController(this, ip, 11000, 11001);
    }

    public void introducirBola (BolaDTO bolaDTO){
        controlador.introducirBola(bolaDTO);
    }

    public void lanzarBola (BolaDTO bolaDTO){
        comunicaciones.lanzarBola (bolaDTO);
    }
}
