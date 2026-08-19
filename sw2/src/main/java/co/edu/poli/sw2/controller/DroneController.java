package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.modelo.Agricultura;
import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.modelo.Vigilancia;
import co.edu.poli.sw2.servicios.DroneDAO;

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

    // =========================
    // INICIALIZAR
    // =========================

    @FXML
    public void initialize() {

        droneDAO = new DroneDAO();

        cbTipo.setItems(
                FXCollections.observableArrayList("Agricultura", "Vigilancia")
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
    // CONSTRUIR DRONE (Agricultura o Vigilancia) DESDE EL FORMULARIO
    // =========================

    /**
     * Valida los campos comunes + el campo especifico segun cbTipo,
     * y arma el objeto Agricultura o Vigilancia correspondiente.
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

            double capacidadTanque = Double.parseDouble(
                    txtCapacidadTanque.getText().trim()
            );

            return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);

        } else { // "Vigilancia"

            boolean deteccionTermica = chkDeteccionTermica.isSelected();

            return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
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