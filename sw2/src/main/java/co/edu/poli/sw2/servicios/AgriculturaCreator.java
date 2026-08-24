package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;

/**
 * ConcreteCreator del patrón Factory Method.
 * Sobrescribe crearDrone() para devolver siempre un Agricultura.
 */
public class AgriculturaCreator extends DroneCreator {

@Override
    public Drone crearDrone(String id, String serial, String modelo,
                             String fabricante, double peso,
                             Object atributoEspecifico) {

        double capacidadTanque = (atributoEspecifico instanceof Number)
                ? ((Number) atributoEspecifico).doubleValue()
                : 0.0;

        return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
    }
}
