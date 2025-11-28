package balls.physics;

public class PhysicsModel {
    public PhysicsValuesDTO pv;

    public PhysicsModel(PhysicsValuesDTO pv){
        this.pv = pv;
    }

    //funciones de fisicas
    public void northRebound(){
        DesgloseXY posicion, velocidad;
        posicion = new DesgloseXY(pv.posicion.x, 0);
        velocidad = new DesgloseXY(pv.velocidad.x, -pv.velocidad.y);

        pv = new PhysicsValuesDTO(posicion, velocidad, pv.aceleracion);
    }

    public void southRebound(int margen){
        DesgloseXY posicion, velocidad;
        posicion = new DesgloseXY(pv.posicion.x, margen);
        velocidad = new DesgloseXY(pv.velocidad.x, -pv.velocidad.y);

        pv = new PhysicsValuesDTO(posicion, velocidad, pv.aceleracion);
    }

    public void eastRebound(int margen){
        DesgloseXY posicion, velocidad;
        posicion = new DesgloseXY(margen, pv.posicion.y);
        velocidad = new DesgloseXY(-pv.velocidad.x, pv.velocidad.y);

        pv = new PhysicsValuesDTO(posicion, velocidad, pv.aceleracion);
    }

    public void westRebound(){
        DesgloseXY posicion, velocidad;
        posicion = new DesgloseXY(0, pv.posicion.y);
        velocidad = new DesgloseXY(-pv.velocidad.x, pv.velocidad.y);

        pv = new PhysicsValuesDTO(posicion, velocidad, pv.aceleracion);
    }

    public void move(){
        DesgloseXY posicion, velocidad;

        posicion = new DesgloseXY(pv.posicion.x+pv.velocidad.x, pv.posicion.y+pv.velocidad.y);
        velocidad = new DesgloseXY(pv.velocidad.x+pv.aceleracion.x, pv.velocidad.y+pv.aceleracion.y);

        pv = new PhysicsValuesDTO(posicion, velocidad, pv.aceleracion);
    }

    public void up(int maxW, int maxH, int diameter){
        int newX = pv.posicion.x;
        int newY = pv.posicion.y - 10;

        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        if (newX + diameter > maxW) newX = maxW - diameter;
        if (newY + diameter > maxH) newY = maxH - diameter;

        pv = new PhysicsValuesDTO(new DesgloseXY(newX, newY), pv.velocidad, pv.aceleracion);
    }

    public void down(int maxW, int maxH, int diameter){
        int newX = pv.posicion.x;
        int newY = pv.posicion.y + 10;

        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        if (newX + diameter > maxW) newX = maxW - diameter;
        if (newY + diameter > maxH) newY = maxH - diameter;

        pv = new PhysicsValuesDTO(new DesgloseXY(newX, newY), pv.velocidad, pv.aceleracion);
    }

    public void right(int maxW, int maxH, int diameter){
        int newX = pv.posicion.x + 10;
        int newY = pv.posicion.y;

        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        if (newX + diameter > maxW) newX = maxW - diameter;
        if (newY + diameter > maxH) newY = maxH - diameter;

        pv = new PhysicsValuesDTO(new DesgloseXY(newX, newY), pv.velocidad, pv.aceleracion);
    }

    public void left(int maxW, int maxH, int diameter){
        int newX = pv.posicion.x - 10;
        int newY = pv.posicion.y;

        if (newX < 0) newX = 0;
        if (newY < 0) newY = 0;
        if (newX + diameter > maxW) newX = maxW - diameter;
        if (newY + diameter > maxH) newY = maxH - diameter;

        pv = new PhysicsValuesDTO(new DesgloseXY(newX, newY), pv.velocidad, pv.aceleracion);
    }
}
