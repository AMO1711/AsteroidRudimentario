package balls.physics;

public class PhysicsValuesDTO {
    public final DesgloseXY posicion, velocidad, aceleracion;

    public PhysicsValuesDTO(DesgloseXY posicion, DesgloseXY velocidad, DesgloseXY aceleracion){
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.aceleracion = aceleracion;
    }
}
