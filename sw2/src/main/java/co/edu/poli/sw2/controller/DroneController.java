package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.modelo.Drone;
import co.edu.poli.sw2.servicios.DroneDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DroneController {

    @FXML
    private TableView<Drone> tblDrones;

    @FXML
    private TableColumn<Drone, Integer> colId;

    @FXML
    private TableColumn<Drone, Integer> colSerial;

    @FXML
    private TableColumn<Drone, String> colModelo;

    @FXML
    private TableColumn<Drone, String> colFabricante;

    @FXML
    private TableColumn<Drone, Integer> colPeso;

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

    private DroneDAO droneDAO;

    // =========================
    // INICIALIZAR
    // =========================

    @FXML
    public void initialize() {

        droneDAO = new DroneDAO();

        configurarTabla();

        cargarDrones();

        // Seleccionar un drone de la tabla
        tblDrones.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {

                    if (seleccionado != null) {

                        txtId.setText(
                                String.valueOf(
                                        seleccionado.getId()
                                )
                        );

                        txtSerial.setText(
                                String.valueOf(
                                        seleccionado.getSerial()
                                )
                        );

                        txtModelo.setText(
                                seleccionado.getModelo()
                        );

                        txtFabricante.setText(
                                seleccionado.getFabricante()
                        );

                        txtPeso.setText(
                                String.valueOf(
                                        seleccionado.getPeso()
                                )
                        );
                    }
                });
    }

    // =========================
    // CONFIGURAR TABLA
    // =========================

    private void configurarTabla() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colSerial.setCellValueFactory(
                new PropertyValueFactory<>("serial")
        );

        colModelo.setCellValueFactory(
                new PropertyValueFactory<>("modelo")
        );

        colFabricante.setCellValueFactory(
                new PropertyValueFactory<>("fabricante")
        );

        colPeso.setCellValueFactory(
                new PropertyValueFactory<>("peso")
        );
    }

    // =========================
    // CARGAR DRONES
    // =========================

    private void cargarDrones() {

        try {

            ObservableList<Drone> lista =
                    FXCollections.observableArrayList(
                            droneDAO.readall()
                    );

            tblDrones.setItems(lista);

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudieron cargar los drones.\n\n"
                    + e.getMessage()
            );
        }
    }

    // =========================
    // GUARDAR
    // =========================

    @FXML
    private void guardarDrone(ActionEvent event) {

        try {

            if (txtSerial.getText().trim().isEmpty()
                    || txtModelo.getText().trim().isEmpty()
                    || txtFabricante.getText().trim().isEmpty()
                    || txtPeso.getText().trim().isEmpty()) {

                mostrarError(
                        "Datos incompletos",
                        "Debe completar todos los campos."
                );

                return;
            }

            int serial = Integer.parseInt(
                    txtSerial.getText().trim()
            );

            int peso = Integer.parseInt(
                    txtPeso.getText().trim()
            );

            String modelo =
                    txtModelo.getText().trim();

            String fabricante =
                    txtFabricante.getText().trim();

            Drone drone = new Drone(
                    0,
                    serial,
                    modelo,
                    fabricante,
                    peso
            );

            droneDAO.create(drone);

            mostrarInformacion(
                    "Éxito",
                    "El drone se guardó correctamente."
            );

            limpiarCampos();

            cargarDrones();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "El serial y el peso deben ser números enteros."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo guardar el drone.\n\n"
                    + e.getMessage()
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

            if (txtSerial.getText().trim().isEmpty()
                    || txtModelo.getText().trim().isEmpty()
                    || txtFabricante.getText().trim().isEmpty()
                    || txtPeso.getText().trim().isEmpty()) {

                mostrarError(
                        "Datos incompletos",
                        "Debe completar todos los campos."
                );

                return;
            }

            int id = Integer.parseInt(
                    txtId.getText().trim()
            );

            int serial = Integer.parseInt(
                    txtSerial.getText().trim()
            );

            int peso = Integer.parseInt(
                    txtPeso.getText().trim()
            );

            String modelo =
                    txtModelo.getText().trim();

            String fabricante =
                    txtFabricante.getText().trim();

            Drone drone = new Drone(
                    id,
                    serial,
                    modelo,
                    fabricante,
                    peso
            );

            droneDAO.update(drone);

            mostrarInformacion(
                    "Éxito",
                    "El drone se modificó correctamente."
            );

            limpiarCampos();

            cargarDrones();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "El ID, serial y peso deben ser números enteros."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo modificar el drone.\n\n"
                    + e.getMessage()
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

            int id = Integer.parseInt(
                    txtId.getText().trim()
            );

            droneDAO.delete(id);

            mostrarInformacion(
                    "Éxito",
                    "El drone se eliminó correctamente."
            );

            limpiarCampos();

            cargarDrones();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Dato inválido",
                    "El ID debe ser un número entero."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo eliminar el drone.\n\n"
                    + e.getMessage()
            );
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

        tblDrones.getSelectionModel()
                .clearSelection();
    }

    // =========================
    // ALERTA INFORMACIÓN
    // =========================

    private void mostrarInformacion(
            String titulo,
            String mensaje) {

        Alert alerta =
                new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    // =========================
    // ALERTA ERROR
    // =========================

    private void mostrarError(
            String titulo,
            String mensaje) {

        Alert alerta =
                new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}