package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Drone;

/**
 * Facade (patrón Facade).
 *
 * Simplifica la creación de un {@link Drone} concreto (Agricultura o
 * Vigilancia) ocultando al cliente los detalles del subsistema
 * involucrado: no necesita conocer {@link DroneCreator}, sus
 * constantes de tipo ({@code TIPO_AGRICULTURA}/{@code TIPO_VIGILANCIA})
 * ni el hecho de que {@link DroneCreator#crearDrone} recibe el
 * atributo específico como un {@code Object} genérico (que hay que
 * convertir manualmente a {@code String}).
 *
 * En su lugar, el cliente llama a un método por tipo de drone
 * ({@link #crearAgricultura} o {@link #crearVigilancia}) con
 * parámetros ya tipados correctamente ({@code double},
 * {@code boolean}), y esta clase se encarga de traducirlos y
 * delegarlos en el subsistema real ({@link DroneCreator}).
 */
public class DroneFacade {

    /**
     * Crea un drone de tipo Agricultura, delegando en
     * {@link DroneCreator} sin que el cliente necesite conocerlo.
     *
     * @param id               identificador del drone
     * @param serial           serial del drone
     * @param modelo           modelo del drone
     * @param fabricante       fabricante del drone
     * @param peso             peso del drone
     * @param capacidadTanque  capacidad del tanque, propia de
     *                         Agricultura
     * @return el {@link Drone} (en concreto, una instancia de
     *         {@code Agricultura}) ya construido
     */
    public Drone crearAgricultura(String id, String serial, String modelo, String fabricante, double peso, double capacidadTanque) {

        return DroneCreator.paraTipo(DroneCreator.TIPO_AGRICULTURA).crearDrone(id, serial, modelo, fabricante, peso,
                String.valueOf(capacidadTanque));

    }


    /**
     * Crea un drone de tipo Vigilancia, delegando en
     * {@link DroneCreator} sin que el cliente necesite conocerlo.
     *
     * @param id                 identificador del drone
     * @param serial             serial del drone
     * @param modelo             modelo del drone
     * @param fabricante         fabricante del drone
     * @param peso               peso del drone
     * @param deteccionTermica   si el drone cuenta con detección
     *                           térmica, propia de Vigilancia
     * @return el {@link Drone} (en concreto, una instancia de
     *         {@code Vigilancia}) ya construido
     */
    public Drone crearVigilancia(String id, String serial, String modelo,
			String fabricante, double peso, boolean deteccionTermica) {

		return DroneCreator.paraTipo(DroneCreator.TIPO_VIGILANCIA).crearDrone(id, serial, modelo, fabricante, peso,
				String.valueOf(deteccionTermica));
		}
}