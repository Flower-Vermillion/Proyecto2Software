package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.DAO.DroneDAO;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Mision;
import co.edu.poli.sw2.modelo.Sensor;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.servicios.BateriaAdicionalDecorator;
import co.edu.poli.sw2.servicios.Builder;
import co.edu.poli.sw2.servicios.ClienteAdapter;
import co.edu.poli.sw2.servicios.ConcretePrototype;
import co.edu.poli.sw2.servicios.ControlAutonomo;
import co.edu.poli.sw2.servicios.ControlBasico;
import co.edu.poli.sw2.servicios.ControlDron;
import co.edu.poli.sw2.servicios.DroneComponent;
import co.edu.poli.sw2.servicios.DroneCreator;
import co.edu.poli.sw2.servicios.DroneWrapper;
import co.edu.poli.sw2.servicios.InterfazProxy;
import co.edu.poli.sw2.servicios.MisionAdapter;
import co.edu.poli.sw2.servicios.Proxy;
import co.edu.poli.sw2.servicios.SensorComposite;
import co.edu.poli.sw2.servicios.SensorWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * NOTA: este controller ahora maneja tanto Agricultura como Vigilancia
 * a traves de la misma pantalla. En el FXML se deben agregar:
 *
 *   - ComboBox<String> cbTipo   (items: "Agricultura", "Vigilancia")
 *   - TextField txtCapacidadTanque   (solo aplica si tipo = Agricultura)
 *   - CheckBox chkDeteccionTermica   (solo aplica si tipo = Vigilancia)
 *
 * fx:id de esos controles deben coincidir con los campos @FXML de abajo.
 *
 * La construccion de los objetos Agricultura/Vigilancia se delega siempre
 * a DroneCreator (patrón Factory Method): este controller nunca hace
 * "new Agricultura(...)" ni "new Vigilancia(...)" directamente.
 */
public class DroneController {

    @FXML
    private TableView<Drone> tblDrones;

    @FXML
    private TableColumn<Drone, String> colId;

    @FXML
    private TableColumn<Drone, String> colSerial;

    @FXML
    private TableColumn<Drone, String> colModelo;

    @FXML
    private TableColumn<Drone, String> colFabricante;

    @FXML
    private TableColumn<Drone, Double> colPeso;

    @FXML
    private TableColumn<Drone, String> colTipo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtSerial;

    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtFabricante;

    @FXML
    private TextField txtPeso;

    // Nuevo: selector de tipo de drone
    @FXML
    private ComboBox<String> cbTipo;

    // Nuevo: campo especifico de Agricultura
    @FXML
    private TextField txtCapacidadTanque;

    // Nuevo: campo especifico de Vigilancia
    @FXML
    private CheckBox chkDeteccionTermica;

    // Acceso directo al CRUD de Drone para crear/leer/actualizar.
    // NOTA: InterfazProxy ahora solo declara eliminar(...) (ver
    // patrón Proxy simplificado); por eso estas tres operaciones
    // vuelven a usar DroneDAO directamente.
    private DroneDAO droneDAO;

    // Punto único de acceso a la eliminación de Drone (patrón Proxy):
    // exige contraseña antes de delegar en el RealSubject.
    private InterfazProxy proxy;

    // Nuevo: selector de tipo de control (patrón Bridge)
@FXML
private ComboBox<String> cbTipoControl;

// Nuevo: batería adicional (patrón Decorator)
@FXML
private CheckBox chkBateriaAdicional;

@FXML
private TextField txtDescripcionBateria;

    // ========== CAMPOS PARA MISIÓN ==========
    @FXML
    private TextField txtMisionId;
    
    @FXML
    private TextField txtMisionNombre;
    
    @FXML
    private TextField txtMisionUbicacion;
    
    @FXML
    private TextField txtMisionFecha;
    
    @FXML
    private ComboBox<String> cbDroneParaMision;
    
    @FXML
    private Button btnGuardarMision;
    
    @FXML
    private Button btnGuardarMisionJSON;
    
    @FXML
    private TableView<Mision> tblMisiones;
    
    @FXML
    private TableColumn<Mision, Integer> colMisionId;
    
    @FXML
    private TableColumn<Mision, String> colMisionNombre;
    
    @FXML
    private TableColumn<Mision, String> colMisionUbicacion;
    
    @FXML
    private TableColumn<Mision, String> colMisionFecha;

    // =========================
    // INICIALIZAR
    // =========================

        @FXML
    public void initialize() {

        droneDAO = new DroneDAO();
        proxy = new Proxy();

        cbTipo.setItems(
                FXCollections.observableArrayList("Agricultura", "Vigilancia")
        );

        cbTipoControl.setItems(
                FXCollections.observableArrayList("Básico", "Autónomo")
        );

        configurarTabla();

        cargarDrones();

        // Seleccionar un drone de la tabla
        tblDrones.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {

                    if (seleccionado != null) {
                        cargarSeleccionEnFormulario(seleccionado);
                    }
                });
    }

    // =========================
    // CONFIGURAR TABLA
    // =========================

    private void configurarTabla() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }

    // =========================
    // CARGAR DRONES
    // =========================

       private void cargarDrones() {

        try {

            ObservableList<Drone> lista =
                    FXCollections.observableArrayList(droneDAO.readall());

            tblDrones.setItems(lista);

            // Refrescar tambien el ComboBox de Misión
            cbDroneParaMision.setItems(
                    FXCollections.observableArrayList(
                            lista.stream()
                                 .map(Drone::getId)
                                 .toList()
                    )
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudieron cargar los drones.\n\n" + e.getMessage()
            );
        }
    }

    private void cargarSeleccionEnFormulario(Drone seleccionado) {

        txtId.setText(seleccionado.getId());
        txtSerial.setText(String.valueOf(seleccionado.getSerial()));
        txtModelo.setText(seleccionado.getModelo());
        txtFabricante.setText(seleccionado.getFabricante());
        txtPeso.setText(String.valueOf(seleccionado.getPeso()));

        if (seleccionado instanceof Agricultura agricultura) {

            cbTipo.setValue("Agricultura");
            txtCapacidadTanque.setText(
                    String.valueOf(agricultura.getCapacidadTanque())
            );
            chkDeteccionTermica.setSelected(false);

        } else if (seleccionado instanceof Vigilancia vigilancia) {

            cbTipo.setValue("Vigilancia");
            chkDeteccionTermica.setSelected(vigilancia.isDeteccionTermica());
            txtCapacidadTanque.clear();
        }
    }

    // =========================
    // GUARDAR
    // =========================

    @FXML
    private void guardarDrone(ActionEvent event) {

        try {

            Drone drone = construirDroneDesdeFormulario(true);

            if (drone == null) {
                return; // El error ya se mostro dentro del metodo
            }

            droneDAO.create(drone);

            mostrarInformacion("Éxito", "El drone se guardó correctamente.");

            limpiarCampos();
            cargarDrones();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "Verifique que peso y el campo específico del tipo "
                    + "sean números válidos."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo guardar el drone.\n\n" + e.getMessage()
            );
        }
    }

    // =========================
    // MODIFICAR
    // =========================

    @FXML
    private void modificarDrone(ActionEvent event) {

        try {

            if (txtId.getText().trim().isEmpty()) {

                mostrarError(
                        "Dato requerido",
                        "Seleccione un drone para modificar."
                );
                return;
            }

            Drone drone = construirDroneDesdeFormulario(true);

            if (drone == null) {
                return;
            }

            droneDAO.update(drone);

            mostrarInformacion("Éxito", "El drone se modificó correctamente.");

            limpiarCampos();
            cargarDrones();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "Verifique que peso y el campo específico del tipo "
                    + "sean números válidos."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo modificar el drone.\n\n" + e.getMessage()
            );
        }
    }

    // =========================
    // ELIMINAR (patrón Proxy)
    // =========================

    /**
     * Antes de eliminar, pide la contraseña mediante {@link
     * #solicitarContrasena()} y se la pasa al {@link Proxy}. El Proxy
     * es quien decide si la contraseña es correcta: si no lo es,
     * lanza una {@link SecurityException} y la eliminación jamás
     * llega al RealSubject ({@link co.edu.poli.sw2.servicios.ServicioProxy}).
     */
    @FXML
    private void eliminarDrone(ActionEvent event) {

        try {

            if (txtId.getText().trim().isEmpty()) {

                mostrarError(
                        "Dato requerido",
                        "Seleccione un drone para eliminar."
                );
                return;
            }

            Optional<String> contrasena = solicitarContrasena();

            if (contrasena.isEmpty()) {
                // El usuario canceló el diálogo: no se intenta eliminar.
                return;
            }

            String id = txtId.getText().trim();

            proxy.eliminar(id, contrasena.get());

            mostrarInformacion("Éxito", "El drone se eliminó correctamente.");

            limpiarCampos();
            cargarDrones();

        } catch (SecurityException e) {

            mostrarError(
                    "Acceso denegado",
                    e.getMessage()
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo eliminar el drone.\n\n" + e.getMessage()
            );
        }
    }

    /**
     * Construye un diálogo simple con un {@link PasswordField} y
     * devuelve la contraseña ingresada (vacío si el usuario cancela).
     * Es el único punto de la UI donde se pide contraseña, ya que es
     * la única operación protegida por el Proxy.
     */
    private Optional<String> solicitarContrasena() {

        Dialog<String> dialogo = new Dialog<>();
        dialogo.setTitle("Confirmar eliminación");
        dialogo.setHeaderText("Ingrese la contraseña para eliminar el drone.");

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Contraseña");

        VBox contenedor = new VBox(10, campoContrasena);
        contenedor.setPadding(new Insets(10));
        dialogo.getDialogPane().setContent(contenedor);

        dialogo.getDialogPane().getButtonTypes().addAll(
                ButtonType.OK, ButtonType.CANCEL);

        dialogo.setResultConverter(boton ->
                boton == ButtonType.OK ? campoContrasena.getText() : null);

        return dialogo.showAndWait();
    }
    
    // =========================
    // CLONAR (patrón Prototype)
    // =========================

    /**
     * Toma el drone seleccionado en la tabla y le pide a un
     * ConcretePrototype que lo clone. El clon es solo en memoria
     * (no se guarda en base de datos); al usuario se le muestra el id
     * del drone copiado junto con la dirección de memoria del
     * original y la del clon recién creado.
     */
    @FXML
    private void clonarDrone(ActionEvent event) {

        Drone seleccionado = tblDrones.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError(
                    "Dato requerido",
                    "Seleccione un drone de la tabla para clonarlo."
            );
            return;
        }

        try {

            ConcretePrototype prototype = new ConcretePrototype(seleccionado);
            Drone clon = prototype.clonar();

            mostrarInformacion("Drone clonado", prototype.mensajeClonado(clon));

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo clonar el drone.\n\n" + e.getMessage()
            );
        }
    }
    
    // =========================
// APLICAR CONTROL (patrón Bridge)
// =========================

/**
 * Toma el drone seleccionado en la tabla y le aplica el modo de
 * control elegido en cbTipoControl (Básico o Autónomo), delegando
 * la ejecución al DroneControlImplementor correspondiente. Es una
 * demostración funcional en memoria: no se guarda en base de datos.
 */
@FXML
private void aplicarControl(ActionEvent event) {

    Drone seleccionado = tblDrones.getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        mostrarError(
                "Dato requerido",
                "Seleccione un drone de la tabla para aplicarle un control."
        );
        return;
    }

    if (cbTipoControl.getValue() == null) {
        mostrarError(
                "Dato requerido",
                "Seleccione el tipo de control (Básico o Autónomo)."
        );
        return;
    }

    try {

        ControlDron.DroneControlImplementor implementor = "Autónomo".equals(cbTipoControl.getValue())
                ? new ControlAutonomo()
                : new ControlBasico();

        ControlDron controlDron = new ControlDron(implementor);

        mostrarInformacion("Control aplicado", controlDron.activar(seleccionado));

    } catch (Exception e) {

        mostrarError(
                "Error",
                "No se pudo aplicar el control.\n\n" + e.getMessage()
        );
    }
}

// =========================
// VER DESCRIPCIÓN CON BATERÍA ADICIONAL (patrón Decorator)
// =========================

/**
 * Toma el drone seleccionado en la tabla y arma su descripción;
 * si chkBateriaAdicional está marcado, la envuelve con
 * BateriaAdicionalDecorator usando el texto de
 * txtDescripcionBateria. Es una demostración funcional en
 * memoria: no se guarda en base de datos.
 */
@FXML
private void verDescripcionDecorator(ActionEvent event) {

    Drone seleccionado = tblDrones.getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        mostrarError(
                "Dato requerido",
                "Seleccione un drone de la tabla para ver su descripción."
        );
        return;
    }

    try {

        DroneComponent componente = new DroneWrapper(seleccionado);

        if (chkBateriaAdicional.isSelected()) {

            String descripcionBateria = txtDescripcionBateria.getText().trim();

            if (descripcionBateria.isEmpty()) {
                mostrarError(
                        "Dato requerido",
                        "Ingrese la descripción de la batería adicional."
                );
                return;
            }

            componente = new BateriaAdicionalDecorator(componente, descripcionBateria);
        }

        mostrarInformacion("Descripción del drone", componente.getDescripcion());

    } catch (Exception e) {

        mostrarError(
                "Error",
                "No se pudo generar la descripción.\n\n" + e.getMessage()
        );
    }
}
    @FXML
    private void construirConBuilder(ActionEvent event) {

        if (cbTipo.getValue() == null) {

            mostrarError(
                    "Datos incompletos",
                    "Debe seleccionar el tipo de drone."
            );
            return;
        }

        String id = txtId.getText().trim();
        String serial = txtSerial.getText().trim();
        double peso = txtPeso.getText().trim().isEmpty()
                ? 0.0
                : Double.parseDouble(txtPeso.getText().trim());
        String modelo = txtModelo.getText().trim();
        String fabricante = txtFabricante.getText().trim();
        String tipo = cbTipo.getValue();

        String atributoEspecifico;

        if ("Agricultura".equals(tipo)) {

            atributoEspecifico = txtCapacidadTanque.getText().trim();

        } else {
            atributoEspecifico = String.valueOf(chkDeteccionTermica.isSelected());
        }

        try {

            Drone drone = new Builder()
                    .conId(id)
                    .conSerial(serial)
                    .conModelo(modelo)
                    .conFabricante(fabricante)
                    .conPeso(peso)
                    .conTipo(tipo)
                    .conAtributoEspecifico(atributoEspecifico)
                    .build();
            
            String detalleEspecifico;

            if (drone instanceof Agricultura agricultura) {
                detalleEspecifico = "Capacidad tanque: " + agricultura.getCapacidadTanque() + " L";
            } else if (drone instanceof Vigilancia vigilancia) {
                detalleEspecifico = vigilancia.isDeteccionTermica()
                        ? "Detección térmica: Sí"
                        : "Detección térmica: No especificado";
            } else {
                detalleEspecifico = "";
            }

            mostrarInformacion(
                    "Drone creado",
                    "Se construyó el drone \"" + valorOMostrar(drone.getId()) + "\" (" + drone.getTipo() + ").\n\n"
                            + "Serial: " + valorOMostrar(drone.getSerial()) + "\n"
                            + "Modelo: " + valorOMostrar(drone.getModelo()) + "\n"
                            + "Fabricante: " + valorOMostrar(drone.getFabricante()) + "\n"
                            + "Peso: " + (drone.getPeso() > 0 ? drone.getPeso() + " kg" : "No especificado") + "\n"
                            + detalleEspecifico
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo construir el drone.\n\n" + e.getMessage()
            );
        }
    }

    // =========================
    // VER SENSORES (patrón Composite)
    // =========================

    /**
     * Abre una ventana emergente (Stage independiente) donde se puede
     * armar un SensorComposite en memoria: agregar varios Sensor,
     * eliminarlos por id, y ver la descripción combinada de todos los
     * que estén agregados en ese momento. No se persiste en base de
     * datos; es una demostración funcional del patrón Composite,
     * igual de "en memoria" que Prototype, Bridge y Decorator.
     */
    @FXML
    private void abrirVentanaComposite(ActionEvent event) {

        SensorComposite composite = new SensorComposite("Sensores agregados");

        TextField txtSensorId = new TextField();
        txtSensorId.setPromptText("ID (número)");

        TextField txtSensorTipo = new TextField();
        txtSensorTipo.setPromptText("Tipo");

        TextField txtSensorFabricante = new TextField();
        txtSensorFabricante.setPromptText("Fabricante");

        TextArea txtDescripcionComposite = new TextArea();
        txtDescripcionComposite.setEditable(false);
        txtDescripcionComposite.setWrapText(true);
        txtDescripcionComposite.setPrefHeight(220);
        txtDescripcionComposite.setText(composite.obtenerDescripcion());

        Button btnAgregar = new Button("Agregar sensor");
        btnAgregar.setOnAction(e -> {

            try {

                if (txtSensorId.getText().trim().isEmpty()
                        || txtSensorTipo.getText().trim().isEmpty()
                        || txtSensorFabricante.getText().trim().isEmpty()) {

                    mostrarError(
                            "Datos incompletos",
                            "Debe completar id, tipo y fabricante del sensor."
                    );
                    return;
                }

                int idSensor = Integer.parseInt(txtSensorId.getText().trim());

                Sensor sensor = new Sensor(
                        idSensor,
                        txtSensorTipo.getText().trim(),
                        txtSensorFabricante.getText().trim()
                );

                String resultado = composite.agregar(sensor);
                txtDescripcionComposite.setText(composite.obtenerDescripcion());

                txtSensorId.clear();
                txtSensorTipo.clear();
                txtSensorFabricante.clear();

                mostrarInformacion("Sensor agregado", resultado);

            } catch (NumberFormatException ex) {

                mostrarError(
                        "Dato inválido",
                        "El id del sensor debe ser un número entero."
                );

            } catch (Exception ex) {

                mostrarError(
                        "Error",
                        "No se pudo agregar el sensor.\n\n" + ex.getMessage()
                );
            }
        });

        Button btnEliminar = new Button("Eliminar sensor");
        btnEliminar.setOnAction(e -> {

            try {

                if (txtSensorId.getText().trim().isEmpty()) {

                    mostrarError(
                            "Dato requerido",
                            "Ingrese el id del sensor a eliminar."
                    );
                    return;
                }

                int idSensor = Integer.parseInt(txtSensorId.getText().trim());

                Sensor sensor = new Sensor(idSensor, "", "");

                String resultado = composite.eliminar(sensor);
                txtDescripcionComposite.setText(composite.obtenerDescripcion());

                mostrarInformacion("Eliminar sensor", resultado);

            } catch (NumberFormatException ex) {

                mostrarError(
                        "Dato inválido",
                        "El id del sensor debe ser un número entero."
                );

            } catch (Exception ex) {

                mostrarError(
                        "Error",
                        "No se pudo eliminar el sensor.\n\n" + ex.getMessage()
                );
            }
        });

        HBox filaCampos = new HBox(10, txtSensorId, txtSensorTipo, txtSensorFabricante);
        HBox filaBotones = new HBox(10, btnAgregar, btnEliminar);

        VBox contenedor = new VBox(
                10,
                new Label("Agregar / eliminar sensores del composite:"),
                filaCampos,
                filaBotones,
                new Label("Descripción combinada (Composite):"),
                txtDescripcionComposite
        );
        contenedor.setPadding(new Insets(15));

        Stage ventana = new Stage();
        ventana.setTitle("Sensores (patrón Composite)");
        ventana.setScene(new Scene(contenedor, 480, 400));
        ventana.show();
    }

    // =========================
    // DEMO: ÁRBOL DE SENSORES DEL DIAGRAMA (patrón Composite)
    // =========================

    /**
     * Construye EXACTAMENTE el árbol de sensores pedido por el
     * profesor:
     *
     *   Sensor General
     *     - Sensor Temperatura
     *         - Sensor Infrarrojo
     *         - RTD
     *     - Sensor Cámara
     *         - Sensor CMOS
     *         - Sensor CCD
     *     - Sensor Sonido
     *         - Sensor Analógico
     *         - Sensor Digital
     *             - SPI
     *             - UART
     *     - Sensor Inteligente
     *
     * Es una demostración puramente en memoria (no se guarda en base
     * de datos) y la salida es texto plano ("crudo"): el objetivo es
     * demostrar la LÓGICA del patrón (un Composite puede contener
     * hojas y otros Composites, y todos se tratan de forma uniforme
     * a través de obtenerDescripcion()), no una representación
     * gráfica del árbol.
     */
    @FXML
    private void mostrarArbolSensoresDemo(ActionEvent event) {

        try {

            int contadorId = 1;

            SensorComposite sensorGeneral = new SensorComposite("Sensor General");

            SensorComposite sensorTemperatura = new SensorComposite("Sensor Temperatura");
            sensorTemperatura.agregar(new Sensor(contadorId++, "Sensor Infrarrojo", "N/A"));
            sensorTemperatura.agregar(new Sensor(contadorId++, "RTD", "N/A"));

            SensorComposite sensorDigital = new SensorComposite("Sensor Digital");
            sensorDigital.agregar(new Sensor(contadorId++, "SPI", "N/A"));
            sensorDigital.agregar(new Sensor(contadorId++, "UART", "N/A"));

            SensorComposite sensorSonido = new SensorComposite("Sensor Sonido");
            sensorSonido.agregar(new Sensor(contadorId++, "Sensor Analógico", "N/A"));
            sensorSonido.agregar(sensorDigital); // Composite dentro de Composite

            SensorComposite sensorInteligente = new SensorComposite( "Sensor Inteligente");
            
                    sensorInteligente.agregar(new Sensor(contadorId++, "Sensor Camara", "N/A"));
                    sensorInteligente.agregar(new Sensor(contadorId++, "Sensor CMOS", "N/A"));
                    sensorInteligente.agregar(new Sensor(contadorId++, "Sensor CCD", "N/A"));
            
            

            sensorGeneral.agregar(sensorTemperatura);
            sensorGeneral.agregar(sensorSonido);
            sensorGeneral.agregar(sensorInteligente);

            TextArea txtArbol = new TextArea(sensorGeneral.obtenerDescripcion());
            txtArbol.setEditable(false);
            txtArbol.setWrapText(false);
            txtArbol.setPrefSize(420, 320);

            VBox contenedor = new VBox(
                    10,
                    new Label("Árbol de sensores construido con el patrón Composite:"),
                    txtArbol
            );
            contenedor.setPadding(new Insets(15));

            Stage ventana = new Stage();
            ventana.setTitle("Demo Composite: árbol de sensores");
            ventana.setScene(new Scene(contenedor, 460, 400));
            ventana.show();

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo construir el árbol de sensores.\n\n" + e.getMessage()
            );
        }
    }

    // =========================
    // CONSTRUIR DRONE (Agricultura o Vigilancia) DESDE EL FORMULARIO
    // =========================

    /**
     * Valida los campos comunes + el campo especifico segun cbTipo,
     * y le pide al DroneCreator correspondiente que arme el objeto
     * Agricultura o Vigilancia. Nunca hace "new Agricultura(...)" ni
     * "new Vigilancia(...)" directamente: eso queda encapsulado en el

     * Factory Method (DroneCreator / AgriculturaCreator / VigilanciaCreator).
     *
     * Devuelve null (y muestra alerta) si algo obligatorio falta.
     */
    private Drone construirDroneDesdeFormulario(boolean validarId) {

        if ((validarId && txtId.getText().trim().isEmpty())
                || txtSerial.getText().trim().isEmpty()
                || txtModelo.getText().trim().isEmpty()
                || txtFabricante.getText().trim().isEmpty()
                || txtPeso.getText().trim().isEmpty()
                || cbTipo.getValue() == null) {

            mostrarError(
                    "Datos incompletos",
                    "Debe completar todos los campos, incluyendo el tipo de drone."
            );
            return null;
        }

        String id = txtId.getText().trim();
        String serial = txtSerial.getText().trim();
        double peso = Double.parseDouble(txtPeso.getText().trim());
        String modelo = txtModelo.getText().trim();
        String fabricante = txtFabricante.getText().trim();
        String tipo = cbTipo.getValue();

        if ("Agricultura".equals(tipo)) {

            if (txtCapacidadTanque.getText().trim().isEmpty()) {
                mostrarError(
                        "Datos incompletos",
                        "Debe indicar la capacidad del tanque para un drone de Agricultura."
                );
                return null;
            }

            String capacidadTanque = txtCapacidadTanque.getText().trim();

            return DroneCreator.paraTipo(DroneCreator.TIPO_AGRICULTURA)
                    .crearDrone(id, serial, modelo, fabricante, peso, capacidadTanque);

        } else { // "Vigilancia"

            String deteccionTermica = String.valueOf(chkDeteccionTermica.isSelected());

            return DroneCreator.paraTipo(DroneCreator.TIPO_VIGILANCIA)
                    .crearDrone(id, serial, modelo, fabricante, peso, deteccionTermica);
        }
    }

    // =========================
    // LIMPIAR CAMPOS
    // =========================

    private void limpiarCampos() {

        txtId.clear();
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
        txtCapacidadTanque.clear();
        chkDeteccionTermica.setSelected(false);
        cbTipo.setValue(null);

        tblDrones.getSelectionModel().clearSelection();
    }

    // =========================
    // ALERTA INFORMACIÓN
    // =========================

    private void mostrarInformacion(String titulo, String mensaje) {
    	
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    private String valorOMostrar(String valor) {
        return (valor == null || valor.trim().isEmpty()) ? "No especificado" : valor;
    }

    // =========================
    // ALERTA ERROR
    // =========================

    private void mostrarError(String titulo, String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

        // =========================
    // GUARDAR MISIÓN COMO JSON
    // =========================

    @FXML
    private void guardarMisionComoJSON(ActionEvent event) {
        
        try {
            // Validar campos de Misión
            if (txtMisionNombre.getText().trim().isEmpty()
                    || txtMisionUbicacion.getText().trim().isEmpty()
                    || txtMisionFecha.getText().trim().isEmpty()
                    || cbDroneParaMision.getValue() == null) {
                
                mostrarError(
                        "Datos incompletos",
                        "Debe completar todos los campos de la Misión."
                );
                return;
            }
            
            // Crear instancia de Misión
            Mision mision = new Mision();
            mision.setNombre(txtMisionNombre.getText().trim());
            mision.setUbicacion(txtMisionUbicacion.getText().trim());
            mision.setFecha(txtMisionFecha.getText().trim());
            
            // Buscar el drone seleccionado
            String droneSeleccionado = cbDroneParaMision.getValue();
            for (Drone drone : tblDrones.getItems()) {
                if (drone.getId().equals(droneSeleccionado)) {
                    mision.setDrone(drone);
                    break;
                }
            }
            
            // Usar el Adapter para convertir a JSON y guardar
            ClienteAdapter adapter = new MisionAdapter();
            String resultado = adapter.convertir(mision);
            
            mostrarInformacion("Éxito", resultado);
            
            limpiarCamposMision();
            
        } catch (Exception e) {
            mostrarError(
                    "Error",
                    "No se pudo guardar la Misión como JSON.\n\n" + e.getMessage()
            );
        }
    }
    
    // =========================
    // LIMPIAR CAMPOS DE MISIÓN
    // =========================
    
    private void limpiarCamposMision() {
        txtMisionId.clear();
        txtMisionNombre.clear();
        txtMisionUbicacion.clear();
        txtMisionFecha.clear();
        cbDroneParaMision.setValue(null);
    }
}