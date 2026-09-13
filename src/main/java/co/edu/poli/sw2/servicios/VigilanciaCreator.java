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
                             String atributoEspecifico) {

        boolean deteccionTermica = Boolean.parseBoolean(
                atributoEspecifico != null ? atributoEspecifico.trim() : "false");

        return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
    }
}