package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.DAO.DroneDAO;
import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.servicios.BateriaAdicionalDecorator;
import co.edu.poli.sw2.servicios.Builder;
import co.edu.poli.sw2.servicios.ConcretePrototype;
import co.edu.poli.sw2.servicios.ControlAutonomo;
import co.edu.poli.sw2.servicios.ControlBasico;
import co.edu.poli.sw2.servicios.ControlDrone;
import co.edu.poli.sw2.servicios.DroneComponent;
import co.edu.poli.sw2.servicios.DroneCreator;
import co.edu.poli.sw2.servicios.DroneWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private DroneDAO droneDAO;

    // Nuevo: selector de tipo de control (patrón Bridge)
@FXML
private ComboBox<String> cbTipoControl;

// Nuevo: batería adicional (patrón Decorator)
@FXML
private CheckBox chkBateriaAdicional;

@FXML
private TextField txtDescripcionBateria;

    // =========================
    // INICIALIZAR
    // =========================

    @FXML
    public void initialize() {

        droneDAO = new DroneDAO();

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
    // ELIMINAR
    // =========================

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

            String id = txtId.getText().trim();

            droneDAO.delete(id);

            mostrarInformacion("Éxito", "El drone se eliminó correctamente.");

            limpiarCampos();
            cargarDrones();

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo eliminar el drone.\n\n" + e.getMessage()
            );
        }
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

        ControlDrone.DroneControlImplementor implementor = "Autónomo".equals(cbTipoControl.getValue())
                ? new ControlAutonomo()
                : new ControlBasico();

        ControlDrone controlDron = new ControlDrone(implementor);

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
}