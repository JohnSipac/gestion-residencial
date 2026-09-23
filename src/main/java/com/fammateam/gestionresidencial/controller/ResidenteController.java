package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import main.java.com.fammateam.gestionresidencial.service.ResidenteService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class ResidenteController implements Initializable {

    private final SceneManager sceneManager;
    private final ResidenteService residenteService;
    private Residente residenteSeleccionado;

    @FXML
    private TableView<Residente> tvResidentes;
    @FXML
    private TableColumn<Residente, String> tcNombre;
    @FXML
    private TableColumn<Residente, String> tcApellido;
    @FXML
    private TableColumn<Residente, String> tcEmail;
    @FXML
    private TableColumn<Residente, String> tcTelefono;
    @FXML
    private TableColumn<Residente, String> tcTipoResidente;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox<String> cmbTipoResidente;

    public ResidenteController(SceneManager sceneManager, ResidenteService residenteService) {
        this.sceneManager = sceneManager;
        this.residenteService = residenteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTipoResidente.getItems().addAll("Propietario", "Inquilino");

        configurarTabla();
        configurarValidacionTelefono();
        ocultarPanelDatosInicial();
        cargarTabla();
    }

    @FXML
    private void handleCreateResidente() {
        residenteSeleccionado = null;
        limpiarFormulario();
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateResidente() {
        Residente seleccionado = tvResidentes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Residente no seleccionado", "Advertencia", "Debe seleccionar un residente para actualizar", Alert.AlertType.WARNING);
            return;
        }

        residenteSeleccionado = seleccionado;
        txtNombre.setText(seleccionado.getNombre());
        txtApellido.setText(seleccionado.getApellido());
        txtEmail.setText(seleccionado.getEmail());
        txtTelefono.setText(seleccionado.getTelefono());
        cmbTipoResidente.setValue(seleccionado.getTipoResidente());

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveResidente() {
        try {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String email = txtEmail.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String tipoResidente = cmbTipoResidente.getValue();

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || tipoResidente == null) {
                sceneManager.showAlert("Campos incompletos", "Advertencia", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
                return;
            }

            if (!email.contains("@")) {
                sceneManager.showAlert("Email inválido", "Advertencia", "El correo electrónico debe contener un caracter '@'.", Alert.AlertType.WARNING);
                return;
            }

            if (telefono.length() != 8) {
                sceneManager.showAlert("Teléfono inválido", "Advertencia", "El teléfono debe contener exactamente 8 números.", Alert.AlertType.WARNING);
                return;
            }

            if (residenteSeleccionado == null) {
                Residente nuevo = new Residente(0, nombre, apellido, email, telefono, tipoResidente);
                if (residenteService.registrarResidente(nuevo)) {
                    sceneManager.showAlert("Guardado", "Éxito", "Residente guardado correctamente.", Alert.AlertType.INFORMATION);
                }
            } else {
                residenteSeleccionado.setNombre(nombre);
                residenteSeleccionado.setApellido(apellido);
                residenteSeleccionado.setEmail(email);
                residenteSeleccionado.setTelefono(telefono);
                residenteSeleccionado.setTipoResidente(tipoResidente);

                if (residenteService.modificarResidente(residenteSeleccionado)) {
                    sceneManager.showAlert("Actualizado", "Éxito", "Residente actualizado correctamente.", Alert.AlertType.INFORMATION);
                }
            }

            cargarTabla();
            mostrarFormulario(false);
            limpiarFormulario();

        } catch (IllegalArgumentException e) {
            sceneManager.showAlert("Verifique los campos ingresados", "Advertencia", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            sceneManager.showAlert("Error de guardado", "Error", "Ocurrió un error al guardar el residente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteResidente() {
        Residente seleccionado = tvResidentes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Residente no seleccionado", "Advertencia", "Debe seleccionar un residente para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Residente",
                "¿Desea eliminar al residente: " + seleccionado.getNombre() + " " + seleccionado.getApellido() + "?"
        );

        if (confirmado) {
            try {
                if (residenteService.eliminarResidente(seleccionado)) {
                    sceneManager.showAlert("Eliminación completada", "Éxito", "Residente eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla();
                    mostrarFormulario(false);
                }
            } catch (Exception e) {
                sceneManager.showAlert("Error al eliminar", "Error", "No se pudo eliminar el residente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleCancel() {
        mostrarFormulario(false);
        limpiarFormulario();
    }

    @FXML
    private void handleGoToMainMenu() {
        sceneManager.showMainMenuView();
    }

    @FXML
    private void handleGoToLoginView() {
        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar cierre de sesión",
                "Cerrar Sesión",
                "¿Desea cerrar sesión?"
        );
        if (confirmado) {
            sceneManager.showLoginView();
        }
    }

    @FXML
    private void handleGoToCondominioView() {
        sceneManager.showCondominioView();
    }

    @FXML
    private void handleGoToCasaView() {
        sceneManager.showCasaView();
    }
    
    @FXML
    private void handleGoToAreaComunView() {
        sceneManager.showAreaComunView();
    }
    
    @FXML
    private void handleGoToReservaView() {
        sceneManager.showReservaView();
    }

    private void configurarTabla() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tcTipoResidente.setCellValueFactory(new PropertyValueFactory<>("tipoResidente"));
    }

    private void configurarValidacionTelefono() {
        txtTelefono.setTextFormatter(new TextFormatter<>(change
                -> change.getControlNewText().matches("\\d{0,8}") ? change : null
        ));
    }

    private void ocultarPanelDatosInicial() {
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        panelDatos.setPrefWidth(0);
        panelDatos.setMinWidth(0);
        panelDatos.setMaxWidth(0);
    }

    private void cargarTabla() {
        try {
            ObservableList<Residente> lista = residenteService.listarResidentes();
            tvResidentes.setItems(lista);
        } catch (Exception e) {
            sceneManager.showAlert("Error de carga", "Error", "No se pudieron cargar los residentes", Alert.AlertType.ERROR);
        }
    }

    private void mostrarFormulario(boolean mostrar) {
        final double ANCHO_EXPANDIDO = 287.0;
        double anchoObjetivo = mostrar ? ANCHO_EXPANDIDO : 0.0;

        if (mostrar) {
            panelDatos.setVisible(true);
            panelDatos.setManaged(true);
        }

        Timeline timeline = new Timeline();
        KeyValue kvPref = new KeyValue(panelDatos.prefWidthProperty(), anchoObjetivo);
        KeyValue kvMin = new KeyValue(panelDatos.minWidthProperty(), anchoObjetivo);
        KeyValue kvMax = new KeyValue(panelDatos.maxWidthProperty(), anchoObjetivo);

        KeyFrame kf = new KeyFrame(Duration.millis(250), kvPref, kvMin, kvMax);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(e -> {
            if (!mostrar) {
                panelDatos.setVisible(false);
                panelDatos.setManaged(false);
            }
        });

        timeline.play();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtTelefono.clear();
        cmbTipoResidente.getSelectionModel().clearSelection();
        residenteSeleccionado = null;
    }
}
