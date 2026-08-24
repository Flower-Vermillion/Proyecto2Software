package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;

/**
 * ConcreteCreator del patrón Factory Method.
 * Sobrescribe crearDrone() para devolver siempre un Vigilancia.
 */
public class VigilanciaCreator extends DroneCreator {

    @Override
    public Drone crearDrone(String id, String serial, String modelo,
                             String fabricante, double peso,
                             Object atributoEspecifico) {

        boolean deteccionTermica = (atributoEspecifico instanceof Boolean)
                ? (Boolean) atributoEspecifico
                : Boolean.parseBoolean(String.valueOf(atributoEspecifico));

        return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
    }
}
