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
                             String atributoEspecifico) {

        double capacidadTanque;
        try {
            capacidadTanque = Double.parseDouble(atributoEspecifico.trim());
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException(
                    "capacidadTanque inválida: \"" + atributoEspecifico + "\"", e);
        }

        return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
    }
}