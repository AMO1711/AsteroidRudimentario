package comunication;

import balls.controller.Controller;
import comunication.helpers.BolaDTO;

import java.net.*;
import java.util.Enumeration;

public class MasterController {
    private final Controller controlador;
    private final ComController comunicaciones;

    private static final String IP_EQUIPO_1 = "172.16.8.54"; //Wifiwire Adrián - conseguido mediante ipconfig
    private static final String IP_EQUIPO_2 = "172.16.8.13"; //Wifiwire Thomas - conseguido mediante ipconfig

    public MasterController(){
        String miIP = obtenerMiIP();
        String ipRemota = obtenerIPRemota(miIP);

        System.out.println("Mi ip: " + miIP + "\tIp remota: " + ipRemota);

        this.controlador = new Controller(this);
        this.comunicaciones = new ComController(this, ipRemota, 11000, 11001);
    }

    private String obtenerIPRemota(String miIP){
        if (miIP.equals(IP_EQUIPO_1)) {
            return IP_EQUIPO_2;
        } else if (miIP.equals(IP_EQUIPO_2)) {
            return IP_EQUIPO_1;
        } else {
            // Para desarrollo en localhost
            System.out.println("MODO DESARROLLO: IP no reconocida, usando localhost");
            return "localhost";
        }
    }

    private String obtenerMiIP(){
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                // Saltar interfaces inactivas, loopback y virtuales
                if (!iface.isUp() || iface.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // Solo procesar IPv4
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();

                        // Buscar específicamente IPs de la red WifiWire (172.16.8.x)
                        if (ip.startsWith("172.16.8.")) {
                            return ip;
                        }
                    }
                }
            }

            System.out.println("No se encontró IP 172.16.8.x, usando getLocalHost()");
            return InetAddress.getLocalHost().getHostAddress();

        } catch (SocketException e) {
            System.err.println("Error obteniendo interfaces de red: " + e.getMessage());
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                return "localhost";
            }
        } catch (UnknownHostException e) {
            System.err.println("Error obteniendo host local: " + e.getMessage());
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
