package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.DAO.DroneDAO;

/**
 * RealSubject (patrón Proxy).
 *
 * Implementación real de la eliminación de un Drone: delega
 * directamente en {@link DroneDAO#delete(Object)}.
 *
 * No valida la contraseña recibida: esa regla de negocio es
 * responsabilidad exclusiva del {@link Proxy}. Este servicio asume
 * que, si lo están llamando, la operación ya fue autorizada; por eso
 * el cliente (DroneController) nunca debería instanciar esta clase
 * directamente, sino trabajar siempre a través del Proxy.
 */
public class ServicioProxy implements InterfazProxy {

    /**
     * DAO real usado para ejecutar la eliminación contra la base de
     * datos.
     */
    private final DroneDAO droneDAO;

    /**
     * Crea el servicio real, inicializando su propio DroneDAO.
     */
    public ServicioProxy() {
        this.droneDAO = new DroneDAO();
    }

    /**
     * Elimina el drone identificado por {@code id}, sin validar la
     * contraseña recibida (esa validación ya la hizo el Proxy antes
     * de llegar hasta aquí).
     *
     * @param id         identificador del drone a eliminar
     * @param contrasena recibida solo para cumplir el contrato de
     *                   {@link InterfazProxy}; no se valida aquí
     * @return el mensaje que devuelve {@link DroneDAO#delete(Object)}
     * @throws Exception si ocurre un error al eliminar el drone
     */
    @Override
    public String eliminar(String id, String contrasena) throws Exception {
        return droneDAO.delete(id);
    }
}