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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.service.CondominioService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class CondominioController implements Initializable {

    private final SceneManager sceneManager;
    private final CondominioService condominioService;

    public CondominioController(SceneManager sceneManager, CondominioService condominioService) {
        this.sceneManager = sceneManager;
        this.condominioService = condominioService;
    }

    @FXML private TableView<Condominio> tvCondominios;
    @FXML private TableColumn<Condominio, String> tcNombre;
    @FXML private TableColumn<Condominio, String> tcDireccion;
    @FXML private TableColumn<Condominio, String> tcTelefono;

    @FXML private AnchorPane panelDatos;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;

    private Condominio condominioSeleccionado;

    public CondominioController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.condominioService = new CondominioService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        panelDatos.setPrefWidth(0);
        panelDatos.setMinWidth(0);
        panelDatos.setMaxWidth(0);

        cargarTabla();
    }

    private void cargarTabla() {
        try {
            ObservableList<Condominio> lista = condominioService.listarCondominios();
            tvCondominios.setItems(lista);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los condominios: " + e.getMessage());
        }
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
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un condominio de la tabla.");
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
            String nombre = txtNombre.getText();
            String direccion = txtDireccion.getText();
            String telefono = txtTelefono.getText();

            if (condominioSeleccionado == null) {
                Condominio nuevo = new Condominio(0, nombre, direccion, telefono);
                if (condominioService.registrarCondominio(nuevo)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Condominio guardado correctamente.");
                }
            } else {
                condominioSeleccionado.setNombre(nombre);
                condominioSeleccionado.setDireccion(direccion);
                condominioSeleccionado.setTelefono(telefono);

                if (condominioService.modificarCondominio(condominioSeleccionado)) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Condominio actualizado correctamente.");
                }
            }

            cargarTabla();
            mostrarFormulario(false);
            limpiarFormulario();

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteCondominio() {
        Condominio seleccionado = tvCondominios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Debe seleccionar un condominio para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el condominio: " + seleccionado.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    if (condominioService.eliminarCondominio(seleccionado)) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Condominio eliminado correctamente.");
                        cargarTabla();
                        mostrarFormulario(false);
                    }
                } catch (Exception e) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el condominio: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleCancel() {
        mostrarFormulario(false);
        limpiarFormulario();
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}