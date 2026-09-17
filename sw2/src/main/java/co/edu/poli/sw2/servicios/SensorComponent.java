package co.edu.poli.sw2.servicios;


/**
 * Component (patrón Composite).
 *
 * Define el contrato común entre una hoja individual
 * ({@link SensorWrapper}) y una composición de varias
 * ({@link SensorComposite}), de modo que ambas se puedan tratar de
 * forma uniforme sin que el código cliente necesite distinguir si
 * está trabajando con un sensor suelto o con todo un árbol de
 * sensores.
 */
public interface SensorComponent {

	/**
	 * Describe este componente (un sensor individual, o la
	 * combinación de todos los componentes que contenga, si se trata
	 * de un composite).
	 *
	 * @return la descripción del componente
	 */
	String obtenerDescripcion();
}