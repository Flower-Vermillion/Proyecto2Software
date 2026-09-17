package co.edu.poli.sw2.servicios;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.sw2.modelo.Sensor;

/**
 * Composite (patrón Composite).
 *
 * Mantiene una LISTA de {@link SensorComponent}, que puede contener
 * tanto hojas ({@link SensorWrapper}) como OTROS SensorComposite,
 * permitiendo anidar categorías dentro de categorías indefinidamente
 * (por ejemplo: Sensor General -> Sensor Sonido -> Sensor Digital ->
 * SPI/UART). Como implementa la misma interfaz que sus hijos, el
 * árbol completo se puede tratar de forma uniforme.
 */
public class SensorComposite implements SensorComponent {

    /**
     * Nombre de este composite (por ejemplo "Sensor General" o
     * "Sensor Sonido"), usado para identificarlo dentro de la
     * descripción combinada del árbol.
     */
    private final String nombre;

    /**
     * Hijos directos de este composite. Cada elemento puede ser una
     * hoja ({@link SensorWrapper}) o, a su vez, otro
     * {@code SensorComposite}, lo que permite anidar niveles.
     */
    private final List<SensorComponent> hijos = new ArrayList<>();

    /**
     * Crea un composite vacío con el nombre indicado.
     *
     * @param nombre nombre identificador del composite; no puede ser
     *               nulo ni estar en blanco
     * @throws IllegalArgumentException si {@code nombre} es nulo o
     *                                  está en blanco
     */
    public SensorComposite(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El composite debe tener un nombre");
        }

        this.nombre = nombre.trim();
    }

    /**
     * Devuelve el nombre de este composite.
     *
     * @return el nombre con el que se creó este composite
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Agrega cualquier componente (una hoja SensorWrapper u otro
     * SensorComposite) como hijo directo de este composite. Este es
     * el método que permite anidar categorías dentro de categorías.
     *
     * @param componente el {@link SensorComponent} (hoja o
     *                   sub-composite) a agregar; no puede ser nulo
     * @return un mensaje confirmando que el componente fue agregado
     * @throws IllegalArgumentException si {@code componente} es nulo
     */
    public String agregar(SensorComponent componente) {

        if (componente == null) {
            throw new IllegalArgumentException("El componente a agregar no puede ser nulo");
        }

        hijos.add(componente);

        return "Componente agregado a \"" + nombre + "\".";
    }

    /**
     * Conveniencia: agrega un Sensor "crudo" envolviéndolo
     * automáticamente en un SensorWrapper (hoja).
     *
     * @param sensor el sensor a envolver y agregar como hoja; no
     *               puede ser nulo
     * @return un mensaje confirmando que el sensor fue agregado
     * @throws IllegalArgumentException si {@code sensor} es nulo
     */
    public String agregar(Sensor sensor) {

        if (sensor == null) {
            throw new IllegalArgumentException("El sensor no puede ser nulo");
        }

        return agregar(new SensorWrapper(sensor));
    }

    /**
     * Elimina, si existe entre los hijos DIRECTOS, la hoja cuyo
     * sensor tenga el id indicado. No busca recursivamente dentro de
     * sub-composites (mantiene la operación simple para la demo).
     *
     * @param sensor sensor cuyo id se usa para buscar la hoja a
     *               eliminar; no puede ser nulo
     * @return un mensaje indicando si el sensor fue eliminado o si
     *         no se encontró entre los hijos directos
     * @throws IllegalArgumentException si {@code sensor} es nulo
     */
    public String eliminar(Sensor sensor) {

        if (sensor == null) {
            throw new IllegalArgumentException("El sensor no puede ser nulo");
        }

        boolean eliminado = hijos.removeIf(componente ->
                componente instanceof SensorWrapper wrapper
                        && wrapper.getSensor().getId() == sensor.getId()
        );

        return eliminado
                ? "Sensor \"" + sensor.getId() + "\" eliminado de \"" + nombre + "\"."
                : "No se encontró el sensor \"" + sensor.getId() + "\" en \"" + nombre + "\".";
    }

    /**
     * Recorre TODOS los hijos (hojas o sub-composites) y combina sus
     * descripciones, indentando cada nivel para que la jerarquía se
     * note en el texto plano.
     *
     * @return el nombre de este composite seguido de la descripción
     *         de cada hijo (recursivamente, si son sub-composites),
     *         indentada según su profundidad en el árbol
     */
    @Override
    public String obtenerDescripcion() {

        StringBuilder sb = new StringBuilder(nombre);

        if (hijos.isEmpty()) {
            sb.append(" (sin componentes)");
            return sb.toString();
        }

        for (SensorComponent hijo : hijos) {

            String descripcionHijo = hijo.obtenerDescripcion();
            String indentada = descripcionHijo.replace("\n", "\n  ");

            sb.append("\n  - ").append(indentada);
        }

        return sb.toString();
    }
}