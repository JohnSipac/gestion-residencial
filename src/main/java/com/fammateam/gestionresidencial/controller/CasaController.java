package main.java.com.fammateam.gestionresidencial.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.service.CasaService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class CasaController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        confiTabla();
        ocultarPanelDatosInicial();
        cargarTabla();
    }

    private SceneManager stage;
    private CasaService casaService;
    private Casa casaSelect;

    @FXML
    private TableView<Casa> tvCasas;
    @FXML
    private TableColumn<Casa, String> tvColumnIdCondominio;
    @FXML
    private TableColumn<Casa, String> tvColumnNumeroCasa;
    @FXML
    private TableColumn<Casa, Double> tvColumnAliquota;
    @FXML
    private ComboBox<String> cmbCondominio;
    @FXML
    private VBox panelDatos;
    @FXML
    private TextField txtIdCondominio;
    @FXML
    private TextField txtNumeroCasa;
    @FXML
    private TextField txtAliquota;
    @FXML
    private Button btnMainMenu;

    public CasaController(CasaService casaService, SceneManager stage) {
        this.casaService = casaService;
        this.stage = stage;
    }

    private void confiTabla() {
        tvColumnIdCondominio.setCellValueFactory(new PropertyValueFactory<>("idCondominio"));
        tvColumnNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        tvColumnAliquota.setCellValueFactory(new PropertyValueFactory<>("aliquota"));
        tvCasas.setItems(FXCollections.observableArrayList(casaService.listaCasa()));
    }

    @FXML
    private void handleCreateCasa() {
        casaSelect = null;
        limpiarFormulario();
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateCasa() {
        Casa seleccionado = tvCasas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            stage.showAlert("Casa no seleccionada", "Advertencia", "Debe seleccionar una casa de la tabla", Alert.AlertType.WARNING);
            return;
        }

        casaSelect = seleccionado;
        txtIdCondominio.setText(String.valueOf(seleccionado.getIdCondominio()));
        txtNumeroCasa.setText(seleccionado.getNumeroCasa());
        txtAliquota.setText(String.valueOf(seleccionado.getAliquota()));

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveCasa() {
        try {
            int idCondominio = Integer.parseInt(cmbCondominio.getValue());
            String numeroCasa = txtNumeroCasa.getText().trim();
            double aliquota = Double.parseDouble(txtAliquota.getText().trim());

            if (numeroCasa.isEmpty()) {
                stage.showAlert("Campo vacío", "Advertencia", "El número de casa no puede estar vacío.", Alert.AlertType.WARNING);
                return;
            }

            if (casaSelect == null) {
                Casa nueva = new Casa(0, idCondominio, numeroCasa, aliquota);
                if (casaService.registrarCasa(nueva)) {
                    stage.showAlert("Guardado", "Éxito", "Casa guardada correctamente", Alert.AlertType.INFORMATION);
                }
            } else {
                casaSelect.setIdCondominio(idCondominio);
                casaSelect.setNumeroCasa(numeroCasa);
                casaSelect.setAliquota(aliquota);

                if (casaService.actualizarCasa(casaSelect)) {
                    stage.showAlert("Actualizado", "Éxito", "Casa actualizada correctamente", Alert.AlertType.INFORMATION);
                }
            }
            cargarTabla();
            mostrarFormulario(false);
            limpiarFormulario();

        } catch (NumberFormatException e) {
            stage.showAlert("Datos numéricos inválidos", "Advertencia", "Verifique que el ID de condominio y la alícuota sean números correctos.", Alert.AlertType.WARNING);
        } catch (IllegalArgumentException e) {
            stage.showAlert("Verifique los campos ingresados", "Advertencia", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            stage.showAlert("Error de guardado", "Error", "Ocurrió un error al guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCasa() {
        Casa seleccionado = tvCasas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            stage.showAlert("Casa no seleccionada", "Advertencia", "Debe seleccionar una casa para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = stage.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Casa",
                "¿Desea eliminar la casa número: " + seleccionado.getNumeroCasa() + "?"
        );

        if (confirmado) {
            try {
                if (casaService.eliminarCasa(seleccionado)) {
                    stage.showAlert("Eliminación completada", "Éxito", "Casa eliminada correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla();
                    mostrarFormulario(false);
                }
            } catch (Exception e) {
                stage.showAlert("Error al eliminar", "Error", "No se pudo eliminar la casa: " + e.getMessage(), Alert.AlertType.ERROR);
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
        stage.showMainMenuView();
    }

    @FXML
    private void handleGoToLoginView() {
        stage.showLoginView();
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
        cmbCondominio.getSelectionModel().clearSelection();
        txtNumeroCasa.clear();
        txtAliquota.clear();
        casaSelect = null;
    }

    private void cargarTabla() {
        try {
            ObservableList<Casa> lista = casaService.listaCasa(); // o obtenerTodasLasCasas() según tu servicio
            tvCasas.setItems(lista);
        } catch (Exception e) {
            stage.showAlert("Error de carga", "Error", "No se pudieron cargar las casas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
