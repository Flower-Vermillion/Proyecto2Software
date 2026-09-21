package co.edu.poli.sw2.servicios;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.edu.poli.sw2.modelo.Mision;

public class MisionAdapter implements ClienteAdapter {

    private ServicioJson adapMision;
    private Gson gson;

    /**
     * Constructor del MisionAdapter
     */
    public MisionAdapter() {
        this.adapMision = new ServicioJson();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Convierte una Misión a formato JSON y la guarda
     * @param m Misión a convertir
     * @return String del JSON guardado
     */
    @Override
    public String convertir(Mision m) {
        // Convertir la Misión a JSON usando Gson
        String jsonString = gson.toJson(m);
        
        // Guardar el JSON usando ServicioJson
        return adapMision.guardarJson(jsonString);
    }

    /**
     * Obtiene la instancia de ServicioJson
     * @return ServicioJson
     */
    public ServicioJson getAdapMision() {
        return adapMision;
    }

    /**
     * Establece la instancia de ServicioJson
     * @param adapMision Nueva instancia de ServicioJson
     */
    public void setAdapMision(ServicioJson adapMision) {
        this.adapMision = adapMision;
    }
}