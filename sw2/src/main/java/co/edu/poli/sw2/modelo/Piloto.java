package co.edu.poli.sw2.modelo;

/**
 * Representa a un piloto habilitado para operar drones dentro del
 * sistema.
 *
 * Es una entidad simple del modelo (datos + getters/setters), usada por
 * el DAO/Controller correspondiente para registrar y consultar pilotos.
 */
public class Piloto {

    /** Identificador unico del piloto. */
    private int id;

    /** Nombre completo del piloto. */
    private String nombre;

    /** Descripcion o nivel de experiencia del piloto. */
    private String experiencia;

    /** Numero de telefono de contacto del piloto. */
    private int telefono;

    /**
     * Constructor vacio, requerido por los DAO que arman el objeto
     * campo a campo a partir del resultado de una consulta.
     */
    public Piloto() {
    }

    /**
     * Crea un piloto con todos sus datos.
     *
     * @param id           identificador del piloto
     * @param nombre       nombre del piloto
     * @param experiencia  experiencia del piloto
     * @param telefono     telefono de contacto del piloto
     */
    public Piloto(int id, String nombre, String experiencia, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.telefono = telefono;
    }

    /**
     * @return el identificador del piloto
     */
    public int getId() {
        return id;
    }

    /**
     * @param id nuevo identificador del piloto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return el nombre del piloto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre nuevo nombre del piloto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return la experiencia del piloto
     */
    public String getExperiencia() {
        return experiencia;
    }

    /**
     * @param experiencia nueva experiencia del piloto
     */
    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    /**
     * @return el telefono de contacto del piloto
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     * @param telefono nuevo telefono de contacto del piloto
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Piloto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", experiencia='" + experiencia + '\'' +
                ", telefono=" + telefono +
                '}';
    }
}