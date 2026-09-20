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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.service.CondominioService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class CondominioController implements Initializable {

    private final SceneManager sceneManager;
    private final CondominioService condominioService;
    private Condominio condominioSeleccionado;

    @FXML
    private TableView<Condominio> tvCondominios;
    @FXML
    private TableColumn<Condominio, String> tcNombre;
    @FXML
    private TableColumn<Condominio, String> tcDireccion;
    @FXML
    private TableColumn<Condominio, String> tcTelefono;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;

    @FXML
    private Button btnMainMenu;

    public CondominioController(SceneManager sceneManager, CondominioService condominioService) {
        this.sceneManager = sceneManager;
        this.condominioService = condominioService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarValidacionTelefono();
        ocultarPanelDatosInicial();
        cargarTabla();
    }

    @FXML
    private void handleCreateCondominio() {
        condominioSeleccionado = null;
        limpiarFormulario();
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateCondominio() {
        Condominio seleccionado = tvCondominios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Condominio no seleccionado", "Advertencia", "Debe seleccionar un condominio de la tabla", Alert.AlertType.WARNING);
            return;
        }

        condominioSeleccionado = seleccionado;
        txtNombre.setText(seleccionado.getNombre());
        txtDireccion.setText(seleccionado.getDireccion());
        txtTelefono.setText(seleccionado.getTelefono());

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveCondominio() {
        try {
            String nombre = txtNombre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String telefono = txtTelefono.getText().trim();

            if (telefono.length() != 8) {
                sceneManager.showAlert("Teléfono inválido", "Advertencia", "El teléfono debe contener exactamente 8 números.", Alert.AlertType.WARNING);
                return;
            }

            if (condominioSeleccionado == null) {
                Condominio nuevo = new Condominio(0, nombre, direccion, telefono);
                if (condominioService.registrarCondominio(nuevo)) {
                    sceneManager.showAlert("Guardado", "Éxito", "Condominio guardado correctamente", Alert.AlertType.INFORMATION);
                }
            } else {
                condominioSeleccionado.setNombre(nombre);
                condominioSeleccionado.setDireccion(direccion);
                condominioSeleccionado.setTelefono(telefono);

                if (condominioService.modificarCondominio(condominioSeleccionado)) {
                    sceneManager.showAlert("Actualizado", "Éxito", "Condominio actualizado correctamente", Alert.AlertType.INFORMATION);
                }
            }

            cargarTabla();
            mostrarFormulario(false);
            limpiarFormulario();

        } catch (IllegalArgumentException e) {
            sceneManager.showAlert("Advertencia", "Verifique los datos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            sceneManager.showAlert("Error de guardado", "Error", "Ocurrió un error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCondominio() {
        Condominio seleccionado = tvCondominios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Condominio no seleccionado", "Advertencia", "Debe seleccionar un condominio para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Condominio",
                "¿Desea eliminar el condominio: " + seleccionado.getNombre() + "?"
        );

        if (confirmado) {
            try {
                if (condominioService.eliminarCondominio(seleccionado)) {
                    sceneManager.showAlert("Eliminación completada", "Éxito", "Condominio eliminado correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla();
                    mostrarFormulario(false);
                }
            } catch (Exception e) {
                sceneManager.showAlert("Error al eliminar", "Error", "No se pudo eliminar el condominio: " + e.getMessage(), Alert.AlertType.ERROR);
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
    private void handleGoToResidenteView() {
        sceneManager.showResidenteView();
    }

    @FXML
    private void handleGoToCasaView() {
        sceneManager.showCasaView();
    }

    private void configurarTabla() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
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
            ObservableList<Condominio> lista = condominioService.listarCondominios();
            tvCondominios.setItems(lista);
        } catch (Exception e) {
            sceneManager.showAlert("Error de carga", "Error", "No se pudieron cargar los condominios", Alert.AlertType.ERROR);
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
        txtDireccion.clear();
        txtTelefono.clear();
        condominioSeleccionado = null;
    }
}
