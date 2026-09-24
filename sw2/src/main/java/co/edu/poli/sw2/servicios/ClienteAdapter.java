package co.edu.poli.sw2.servicios;

import co.edu.poli.sw2.modelo.Mision;

public interface ClienteAdapter {
    /**
     * Convierte una instancia de Misión a formato JSON
     * @param m Misión a convertir
     * @return String en formato JSON
     */
    String convertir(Mision m);
}