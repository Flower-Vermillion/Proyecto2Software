package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Creator (patrón Factory Method).
 *
 * Declara el método fábrica {@link #crearDrone} que cada subclase
 * concreta (ConcreteCreator) debe implementar para producir su propio
 * tipo de Drone (el "Product" de este patrón).
 *
 * El cliente (por ejemplo DroneDAO) no necesita conocer las clases
 * concretas Agricultura o Vigilancia: solo trabaja con un DroneCreator
 * y con el tipo Drone que este devuelve.
 */
public abstract class DroneCreator {

    public static final String TIPO_AGRICULTURA = "AGRICULTURA";
    public static final String TIPO_VIGILANCIA = "VIGILANCIA";

    /**
     * Factory Method. Cada ConcreteCreator decide qué subclase de
     * Drone instanciar y cómo construirla.
     *
     * @param atributoEspecifico capacidadTanque (Number) si el creator
     *                           es de Agricultura, o deteccionTermica
     *                           (Boolean) si es de Vigilancia.
     */
    public abstract Drone crearDrone(String id, String serial, String modelo,
                                      String fabricante, double peso,
                                      Object atributoEspecifico);

    /**
     * Punto único donde se decide qué ConcreteCreator usar según el
     * tipo. Esto no es parte estricta del GoF (el patrón asume que el
     * cliente ya sabe qué Creator concreto usar), pero evita repetir
     * este switch en cada lugar del DAO/Controller que necesite crear
     * un drone a partir de un texto "AGRICULTURA"/"VIGILANCIA".
     */
    public static DroneCreator paraTipo(String tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de drone no puede ser nulo");
        }

        switch (tipo.trim().toUpperCase()) {

            case TIPO_AGRICULTURA:
                return new AgriculturaCreator();

            case TIPO_VIGILANCIA:
                return new VigilanciaCreator();

            default:
                throw new IllegalArgumentException("Tipo de drone no soportado: " + tipo);
        }
    }
}