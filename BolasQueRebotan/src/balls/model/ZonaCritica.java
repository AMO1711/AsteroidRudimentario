package balls.model;

import balls.physics.DesgloseXY;

public class ZonaCritica {
    private Asteroid asteroidInside;
    private boolean free;
    private final DesgloseXY posicion, dimensiones;

    public ZonaCritica(){
        posicion = new DesgloseXY(100, 100);
        dimensiones = new DesgloseXY(100, 200); // Primero anchura y segundo altura
        free = true;
        asteroidInside = null;
    }

    synchronized public void goInside(Asteroid asteroid){
        while (!free && asteroid != asteroidInside){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        asteroidInside = asteroid;
        free = false;
        asteroid.roomEntered(this);
    }

    synchronized public void goOutside(Asteroid asteroid){
        asteroidInside = null;
        free = true;
        asteroid.roomLeaved(this);
        notifyAll();
    }

    public Asteroid getBallInside() {
        return asteroidInside;
    }

    public boolean isFree() {
        return free;
    }

    public DesgloseXY getPosicion() {
        return posicion;
    }

    public DesgloseXY getDimensiones() {
        return dimensiones;
    }
}
