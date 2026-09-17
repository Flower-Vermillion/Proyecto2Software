package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Sensor;

/**
 * Leaf / ConcreteComponent (patrón Composite).
 *
 * Envuelve un único {@link Sensor} "plano" del modelo y lo expone
 * como un {@link SensorComponent}, de modo que pueda tratarse de
 * forma uniforme junto a un {@link SensorComposite} dentro del mismo
 * árbol (a través de la interfaz común {@code SensorComponent}). Es
 * el nodo hoja: no tiene hijos ni puede contener otros componentes.
 */
public class SensorWrapper implements SensorComponent {

    /**
     * Sensor "plano" del modelo que este wrapper envuelve.
     */
    private final Sensor sensor;

    /**
     * Crea un wrapper para el sensor indicado.
     *
     * @param sensor el sensor a envolver; no puede ser nulo
     * @throws IllegalArgumentException si {@code sensor} es nulo
     */
    public SensorWrapper(Sensor sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("El sensor no puede ser nulo");
        }
        this.sensor = sensor;
    }

    /**
     * Devuelve el sensor envuelto por este wrapper.
     *
     * @return el {@link Sensor} original
     */
    public Sensor getSensor() {
        return sensor;
    }

    /**
     * Describe el sensor envuelto, incluyendo su id, tipo y
     * fabricante.
     *
     * @return una descripción de una sola línea del sensor
     */
    @Override
    public String obtenerDescripcion() {
        return "Sensor \"" + sensor.getId() + "\" (" + sensor.getTipo() + "), fabricante "
                + sensor.getFabricante();
    }
}