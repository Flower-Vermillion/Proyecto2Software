package co.edu.poli.sw2.vista;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX de gestion de drones.
 *
 * Se encarga de crear la {@link Stage} y la {@link Scene} principal,
 * cargando la vista inicial ({@code droneVista.fxml}), y expone
 * {@link #setRoot(String)} para que el resto de la aplicacion pueda
 * cambiar de vista dentro de la misma escena.
 */
public class App extends Application {

    /** Escena unica de la aplicacion, reutilizada al cambiar de vista. */
    private static Scene scene;

    /**
     * Inicializa la ventana principal: crea la escena a partir de la
     * vista {@code droneVista}, configura titulo y tamaños de la
     * ventana, y la muestra maximizada.
     *
     * @param stage ventana principal proporcionada por JavaFX
     * @throws IOException si no se puede cargar el archivo FXML inicial
     */
      @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("droneVista"), 1280, 800);
        stage.setScene(scene);
        stage.setTitle("Gestión de Drones - Proyecto 2 Software 2");
        stage.setMinWidth(1100);
        stage.setMinHeight(650);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Cambia la vista actual dentro de la misma {@link Scene}, cargando
     * el FXML indicado como nueva raiz.
     *
     * @param fxml nombre del archivo FXML (sin extension) a cargar
     * @throws IOException si no se puede cargar el archivo FXML indicado
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga un archivo FXML ubicado en el paquete de vistas y devuelve
     * su nodo raiz.
     *
     * @param fxml nombre del archivo FXML (sin extension) a cargar
     * @return el nodo raiz ({@link Parent}) definido en el FXML
     * @throws IOException si el archivo FXML no existe o no puede
     *                      cargarse
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource("/sw2/co/edu/poli/sw2/vista/" + fxml + ".fxml")
        );

        return fxmlLoader.load();
    }

    /**
     * Metodo principal: lanza la aplicacion JavaFX.
     *
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        launch();
    }
}