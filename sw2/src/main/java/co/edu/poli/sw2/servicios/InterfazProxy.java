package co.edu.poli.sw2.servicios;

/**
 * Subject (patrón Proxy).
 *
 * Declara la única operación que interesa proteger con este patrón:
 * eliminar un Drone. Tanto el RealSubject ({@link ServicioProxy}) como
 * el {@link Proxy} implementan esta interfaz, de forma que el cliente
 * (DroneController) trabaje siempre contra ella sin necesitar saber si
 * está hablando directamente con el servicio real o con el Proxy que
 * lo protege.
 */
public interface InterfazProxy {

    /**
     * Elimina el drone identificado por {@code id}.
     *
     * @param id         identificador del drone a eliminar
     * @param contrasena contraseña que el Proxy valida antes de
     *                   permitir la eliminación; el RealSubject la
     *                   recibe pero no la valida (esa responsabilidad
     *                   es solo del Proxy)
     * @return un mensaje indicando el resultado de la eliminación
     * @throws Exception si ocurre un error al eliminar el drone
     */
    String eliminar(String id, String contrasena) throws Exception;
}