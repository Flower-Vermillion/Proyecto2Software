package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Mision;

/**
 * Target (patrón Adapter).
 *
 * Interfaz que espera el cliente (DroneController) para convertir una
 * {@link Mision} a JSON, sin conocer los detalles de cómo se logra
 * esa conversión ni de cómo se guarda el archivo resultante. La clase
 * {@link MisionAdapter} es quien implementa esta interfaz, adaptando
 * el Adaptee ({@link ServicioJson}, que solo sabe guardar texto plano)
 * para que funcione en términos de {@link Mision}.
 */
public interface ClienteAdapter {
    /**
     * Convierte una instancia de Misión a formato JSON
     * @param m Misión a convertir
     * @return String en formato JSON
     */
    String convertir(Mision m);
}