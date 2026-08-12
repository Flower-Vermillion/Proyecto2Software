package co.edu.poli.sw2.servicios;

import java.util.List;

public interface CRUD<T> {

    String create(T t) throws Exception;

    <K> T readone(K id) throws Exception;

    List<T> readall() throws Exception;

    String update(T t) throws Exception;

    String delete(Object id) throws Exception;
}