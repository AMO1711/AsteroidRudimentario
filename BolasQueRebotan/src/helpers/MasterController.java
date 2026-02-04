package helpers;

import balls.controller.Controller;
import comunication.ComController;
import comunication.channel.BolaDTO;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MasterController {
    private final Controller controlador;
    private final ComController comunicaciones;

    private static final String IP_EQUIPO_1 = "172.26.224.1"; //Wifiwire Adrián
    private static final String IP_EQUIPO_2 = "192.168.1.101"; //

    public MasterController(){
        String miIP = obtenerMiIP();
        String ipRemota = obtenerIPRemota(miIP);

        this.controlador = new Controller(this);
        this.comunicaciones = new ComController(this, ipRemota, 11000, 11001);
    }

    private String obtenerMiIP(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    private String obtenerIPRemota(String miIP){
        if (miIP.equals(IP_EQUIPO_1)) {
            return IP_EQUIPO_2; // Soy equipo 1, conecto a equipo 2
        } else if (miIP.equals(IP_EQUIPO_2)) {
            return IP_EQUIPO_1; // Soy equipo 2, conecto a equipo 1
        } else {
            // Para desarrollo en localhost
            System.out.println("⚠️ MODO DESARROLLO: IP no reconocida, usando localhost");
            return "localhost";
        }
    }

    public void introducirBola (BolaDTO bolaDTO){
        controlador.introducirBola(bolaDTO);
    }

    public void lanzarBola (BolaDTO bolaDTO){
        comunicaciones.lanzarBola (bolaDTO);
    }
}
