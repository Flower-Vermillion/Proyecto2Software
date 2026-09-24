package co.edu.poli.sw2.servicios;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptee (patrón Adapter).
 *
 * Servicio genérico que solo sabe guardar un texto en formato JSON a
 * un archivo, sin conocer nada sobre {@link co.edu.poli.sw2.modelo.Mision}
 * ni sobre ningún otro modelo del dominio. Es {@link MisionAdapter}
 * quien lo adapta para que el cliente pueda trabajar en términos de
 * {@link ClienteAdapter#convertir(co.edu.poli.sw2.modelo.Mision)}.
 */
public class ServicioJson {

    /**
     * Guarda un JSON en un archivo
     * @param json String en formato JSON a guardar
     * @return String con la ruta del archivo guardado o mensaje de error
     */
    public String guardarJson(String json) {
        try {
            // Crear nombre de archivo con timestamp
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String nombreArchivo = "mision_" + timestamp + ".json";
            String rutaArchivo = "src/main/resources/" + nombreArchivo;
            
            // Crear directorio si no existe
            Files.createDirectories(Paths.get("src/main/resources"));
            
            // Escribir el archivo
            FileWriter fileWriter = new FileWriter(rutaArchivo);
            fileWriter.write(json);
            fileWriter.close();
            
            return "Archivo guardado exitosamente en: " + rutaArchivo;
        } catch (IOException e) {
            return "Error al guardar el JSON: " + e.getMessage();
        }
    }
}