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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.service.CasaService;
import main.java.com.fammateam.gestionresidencial.service.CondominioService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class CasaController implements Initializable {

    private final SceneManager sceneManager;
    private final CondominioService condominioService;
    private final CasaService casaService;

    private Condominio condominioSeleccionado;
    private Casa casaSeleccionada;

    @FXML
    private TableView<Casa> tvCasas;
    @FXML
    private TableColumn<Casa, String> tcNumeroCasa;
    @FXML
    private TableColumn<Casa, Double> tcAlicuota;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private TextField txtNumeroCasa;
    @FXML
    private TextField txtAlicuota;

    @FXML
    private ComboBox<String> cmbCondominio;

    @FXML
    private ComboBox<String> cmbCondominios;

    @FXML
    private Label lblDatos;
    @FXML
    private HBox hbDatosCondominio;
    @FXML
    private Label lblCasas;
    @FXML
    private Label lblDireccion;
    @FXML
    private Label lblTelefono;

    public CasaController(SceneManager sceneManager, CondominioService condominioService, CasaService casaService) {
        this.sceneManager = sceneManager;
        this.condominioService = condominioService;
        this.casaService = casaService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tcNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        tcAlicuota.setCellValueFactory(new PropertyValueFactory<>("aliquota"));

        ObservableList<String> opciones = condominioService.encontrarNombres();
        cmbCondominio.setItems(opciones);
        cmbCondominios.setItems(opciones);

        cmbCondominio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarVistaPorCondominio(newVal);
            }
        });

        tvCasas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                casaSeleccionada = newSelection;
            }
        });

        configurarValidacionAlicuota();
        ocultarPanelDatosInicial();
    }

    private void cargarVistaPorCondominio(String nombreCondominio) {
        Condominio cond = condominioService.obtenerCondominioPorNombre(nombreCondominio);
        if (cond != null) {
            condominioSeleccionado = cond;
            lblDireccion.setText(cond.getDireccion());
            lblTelefono.setText(cond.getTelefono());

            mostrarElementosCentrales(true);

            ObservableList<Casa> casas = casaService.listarCasasPorCondominio(nombreCondominio);
            tvCasas.setItems(casas);
        }
    }

    private void mostrarElementosCentrales(boolean visible) {
        lblDatos.setVisible(visible);
        hbDatosCondominio.setVisible(visible);
        lblCasas.setVisible(visible);
        tvCasas.setVisible(visible);
    }

    @FXML
    private void handleCreateCasa() {
        casaSeleccionada = null;
        limpiarFormulario();
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateCasa() {
        Casa seleccionada = tvCasas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            sceneManager.showAlert("Casa no seleccionada", "Advertencia", "Debe seleccionar una casa de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        casaSeleccionada = seleccionada;
        txtNumeroCasa.setText(seleccionada.getNumeroCasa());
        txtAlicuota.setText(String.valueOf(seleccionada.getAliquota()));

        if (cmbCondominio.getValue() != null) {
            cmbCondominios.setValue(cmbCondominio.getValue());
        }

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveCasa() {
        try {
            String numeroCasa = txtNumeroCasa.getText().trim();
            String alicuotaStr = txtAlicuota.getText().trim();
            String condominioNombre = cmbCondominios.getValue();

            if (numeroCasa.isEmpty() || alicuotaStr.isEmpty()) {
                sceneManager.showAlert("Campos vacíos", "Advertencia", "Por favor llene todos los campos obligatorios.", Alert.AlertType.WARNING);
                return;
            }

            if (condominioNombre == null || condominioNombre.isEmpty()) {
                sceneManager.showAlert("Sin Condominio", "Advertencia", "Debe seleccionar un condominio en el formulario.", Alert.AlertType.WARNING);
                return;
            }

            Condominio cond = condominioService.obtenerCondominioPorNombre(condominioNombre);
            if (cond == null) {
                sceneManager.showAlert("Error de Condominio", "Error", "No se encontró el condominio seleccionado.", Alert.AlertType.ERROR);
                return;
            }

            double aliquota = Double.parseDouble(alicuotaStr);

            if (aliquota < 0.0 || aliquota >= 10.0) {
                sceneManager.showAlert(
                        "Alícuota fuera de rango",
                        "Valor no permitido",
                        "La alícuota debe ser un valor entre 0.0000 y 9.9999",
                        Alert.AlertType.WARNING
                );
                return;
            }

            if (casaSeleccionada == null) {
                Casa nuevaCasa = new Casa(0, cond.getIdCondominio(), numeroCasa, aliquota);
                if (casaService.registrarCasa(nuevaCasa)) {
                    sceneManager.showAlert("Guardado", "Éxito", "Casa registrada correctamente.", Alert.AlertType.INFORMATION);
                }
            } else {
                casaSeleccionada.setNumeroCasa(numeroCasa);
                casaSeleccionada.setAliquota(aliquota);
                casaSeleccionada.setIdCondominio(cond.getIdCondominio());

                if (casaService.modificarCasa(casaSeleccionada)) {
                    sceneManager.showAlert("Actualizado", "Éxito", "Casa actualizada correctamente.", Alert.AlertType.INFORMATION);
                }
            }

            mostrarFormulario(false);
            limpiarFormulario();

            cmbCondominio.setValue(condominioNombre);
            cargarVistaPorCondominio(condominioNombre);

        } catch (NumberFormatException e) {
            sceneManager.showAlert("Valor inválido", "Advertencia", "Ingrese un valor numérico válido para la alícuota.", Alert.AlertType.WARNING);
        } catch (IllegalArgumentException e) {
            sceneManager.showAlert("Datos inválidos", "Advertencia", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            sceneManager.showAlert("Error de guardado", "Error", "Ocurrió un error al guardar la casa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCasa() {
        Casa seleccionada = tvCasas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            sceneManager.showAlert("Casa no seleccionada", "Advertencia", "Debe seleccionar una casa para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Casa",
                "¿Desea eliminar la casa número: " + seleccionada.getNumeroCasa() + "?"
        );

        if (confirmado) {
            try {
                if (casaService.eliminarCasa(seleccionada)) {
                    sceneManager.showAlert("Eliminación completada", "Éxito", "Casa eliminada correctamente.", Alert.AlertType.INFORMATION);

                    if (cmbCondominio.getValue() != null) {
                        cargarVistaPorCondominio(cmbCondominio.getValue());
                    }
                    mostrarFormulario(false);
                    limpiarFormulario();
                }
            } catch (Exception e) {
                sceneManager.showAlert("Error al eliminar", "Error", "No se pudo eliminar la casa: " + e.getMessage(), Alert.AlertType.ERROR);
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
    private void handleGoToCondominioView() {
        sceneManager.showCondominioView();
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

    private void configurarValidacionAlicuota() {
        txtAlicuota.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,4})?")) {
                return change;
            }
            return null;
        }));
    }

    private void ocultarPanelDatosInicial() {
        panelDatos.setVisible(false);
        panelDatos.setManaged(false);
        panelDatos.setPrefWidth(0);
        panelDatos.setMinWidth(0);
        panelDatos.setMaxWidth(0);
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
        txtNumeroCasa.clear();
        txtAlicuota.clear();
        cmbCondominios.getSelectionModel().clearSelection();
        casaSeleccionada = null;
    }
}
