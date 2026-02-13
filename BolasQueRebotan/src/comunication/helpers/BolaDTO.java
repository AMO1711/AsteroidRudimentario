package comunication.helpers;

import java.awt.*;
import java.io.Serializable;

public class BolaDTO implements Serializable {
    public int DIAMETER, posicionX, posicionY, velocidadX, velocidadY, aceleracionX, aceleracionY;
    public Color color;

    public BolaDTO (int diametro, int posicionX, int posicionY, int velocidadX, int velocidadY,
                    int aceleracionX, int aceleracionY, Color color){
        this.DIAMETER = diametro;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.aceleracionX = aceleracionX;
        this.aceleracionY = aceleracionY;
        this.color = color;
    }

}
