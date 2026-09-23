package co.edu.poli.sw2.servicios;

/**
 * Proxy de protección (patrón Proxy).
 *
 * Actúa como intermediario entre el cliente (DroneController) y el
 * servicio real {@link ServicioProxy}, implementando
 * {@link InterfazProxy} para que el cliente use la eliminación de
 * drones sin conocer directamente la implementación del servicio real.
 *
 * Antes de permitir la eliminación de un drone, el Proxy verifica que
 * la contraseña proporcionada coincida con la contraseña esperada. Si
 * la contraseña es incorrecta, lanza una {@link SecurityException} y
 * la operación nunca llega al servicio real.
 *
 * La contraseña esperada se obtiene mediante
 * {@link #obtenerContrasenaEsperada()}: primero intenta con la
 * variable de entorno {@code DRONE_DELETE_PASSWORD}; si esta no está
 * configurada, usa el valor por defecto definido en
 * {@link #contrasena}.
 *
 * @see InterfazProxy
 * @see ServicioProxy
 */
public class Proxy implements InterfazProxy {

    /**
     * Contraseña por defecto usada cuando la variable de entorno
     * {@code DRONE_DELETE_PASSWORD} no está configurada.
     */
    private static final String contrasena = "1234";

    /**
     * Servicio real (RealSubject) al que el Proxy delega la
     * eliminación una vez validada la contraseña.
     */
    private final InterfazProxy servicioReal;

    /**
     * Crea el Proxy, inicializando su propio servicio real.
     */
    public Proxy() {
        this.servicioReal = new ServicioProxy();
    }

    /**
     * Valida la contraseña recibida y, solo si es correcta, delega la
     * eliminación en el servicio real.
     *
     * @param id         identificador del drone a eliminar
     * @param contrasena contraseña a validar antes de eliminar
     * @return el mensaje devuelto por el servicio real, si la
     *         contraseña es correcta
     * @throws SecurityException si la contraseña no coincide con la
     *                           esperada
     * @throws Exception         si ocurre un error al eliminar el
     *                           drone
     */
    @Override
    public String eliminar(String id, String contrasena) throws Exception {

        if (!contrasenaValida(contrasena)) {
            throw new SecurityException(
                    "Contraseña incorrecta: no se permite eliminar el drone.");
        }

        return servicioReal.eliminar(id, contrasena);
    }

    // =========================
    // VALIDACIÓN DE CONTRASEÑA
    // =========================

    /**
     * Compara la contraseña recibida con la contraseña esperada.
     *
     * @param contrasena contraseña a validar
     * @return {@code true} si coincide con la contraseña esperada,
     *         {@code false} en caso contrario (incluyendo cuando es
     *         {@code null})
     */
    private boolean contrasenaValida(String contrasena) {
        return contrasena != null && contrasena.equals(obtenerContrasenaEsperada());
    }

    /**
     * Obtiene la contraseña esperada: primero intenta con la variable
     * de entorno {@code DRONE_DELETE_PASSWORD}; si no está configurada
     * o está en blanco, usa el valor por defecto de {@link #contrasena}.
     *
     * @return la contraseña esperada para autorizar la eliminación
     */
    private String obtenerContrasenaEsperada() {

        String valor = contrasena;

        if (valor == null || valor.isBlank()) {
            valor = System.getenv("DRONE_DELETE_PASSWORD");
        }

        return (valor == null || valor.isBlank()) ? contrasena : valor;
    }
}