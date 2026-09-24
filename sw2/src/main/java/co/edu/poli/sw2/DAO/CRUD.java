package co.edu.poli.sw2.DAO;

import java.util.List;

/**
 * Contrato generico de las operaciones basicas de persistencia
 * (Crear, Leer, Actualizar, Eliminar) para cualquier entidad {@code T}.
 *
 * Los DAO concretos del proyecto (por ejemplo, {@code DroneDAO})
 * implementan esta interfaz, de modo que el resto de la aplicacion
 * (Controller/servicios) puede depender de {@code CRUD<T>} en lugar de
 * una implementacion especifica de acceso a datos.
 *
 * @param <T> tipo de entidad del modelo sobre la que opera este DAO
 */
public interface CRUD<T> {

    /**
     * Inserta una nueva entidad en la fuente de datos.
     *
     * @param t entidad a crear
     * @return un mensaje o identificador resultante de la operacion
     * @throws Exception si ocurre un error de acceso a datos
     */
    String create(T t) throws Exception;

    /**
     * Busca una entidad a partir de su identificador.
     *
     * @param id   identificador de la entidad a buscar
     * @param <K>  tipo del identificador
     * @return la entidad encontrada
     * @throws Exception si ocurre un error de acceso a datos
     */
    <K> T readone(K id) throws Exception;

    /**
     * Obtiene todas las entidades disponibles en la fuente de datos.
     *
     * @return lista con todas las entidades
     * @throws Exception si ocurre un error de acceso a datos
     */
    List<T> readall() throws Exception;

    /**
     * Actualiza una entidad existente en la fuente de datos.
     *
     * @param t entidad con los datos actualizados
     * @return un mensaje resultante de la operacion
     * @throws Exception si ocurre un error de acceso a datos
     */
    String update(T t) throws Exception;

    /**
     * Elimina una entidad a partir de su identificador.
     *
     * @param id identificador de la entidad a eliminar
     * @return un mensaje resultante de la operacion
     * @throws Exception si ocurre un error de acceso a datos
     */
    String delete(Object id) throws Exception;
}