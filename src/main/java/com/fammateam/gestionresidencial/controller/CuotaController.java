package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.model.Cuota;
import main.java.com.fammateam.gestionresidencial.model.enums.EstadoCuota;
import main.java.com.fammateam.gestionresidencial.service.CasaService;
import main.java.com.fammateam.gestionresidencial.service.CuotaService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;
import java.sql.SQLException;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.service.CondominioService;

public class CuotaController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnasTabla();
        cargarDatosComboBox();
        configurarListeners();
        configurarValidacionNumerica();
        ocultarPanelDatosInicial();
    }

    private SceneManager stage;
    private Condominio condominioSelect;
    private Casa casaSelect;
    private Cuota cuotaSelect;
    private CuotaService cuotaService;
    private CasaService casaService;
    private CondominioService condominioService;

    @FXML
    private TableView<Cuota> tvCuotas;
    @FXML
    private TableColumn<Cuota, Integer> tvColumnMes;
    @FXML
    private TableColumn<Cuota, Integer> tvColumnAnio;
    @FXML
    private TableColumn<Cuota, Double> tvColumnMonto;
    @FXML
    private TableColumn<Cuota, EstadoCuota> tvColumnEstado;
    @FXML
    private TableColumn<Cuota, LocalDate> tvColumnFechaVencimiento;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private ComboBox<Condominio> cmbCondominio;
    @FXML
    private ComboBox<Casa> cmbCasa;

    @FXML
    private TextField txtMes;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtMonto;
    @FXML
    private ComboBox<EstadoCuota> cmbEstado;
    @FXML
    private DatePicker dpFechaVencimiento;

    @FXML
    private Label lblDatos;
    @FXML
    private HBox hbDatosCasa;
    @FXML
    private Label lblTituloTabla;
    @FXML
    private Label lblDireccionCasa;
    @FXML
    private Button btnMainMenu;

    public CuotaController(SceneManager stage, CuotaService cuotaService, CasaService casaService, CondominioService condominioService) {
        this.stage = stage;
        this.cuotaService = cuotaService;
        this.casaService = casaService;
        this.condominioService = condominioService;
    }

    private void configurarColumnasTabla() {
        tvColumnMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        tvColumnAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        tvColumnMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        tvColumnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tvColumnFechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
    }

    private void cargarDatosComboBox() {
        if (condominioService != null) {
            ObservableList<Condominio> listaCondominios = FXCollections.observableArrayList(condominioService.listarCondominios());
            cmbCondominio.setItems(listaCondominios);
        }
        cmbEstado.setItems(FXCollections.observableArrayList(EstadoCuota.values()));
    }

    private void configurarListeners() {
        cmbCondominio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                this.condominioSelect = newVal;
                this.casaSelect = null;

                cmbCasa.setVisible(true);
                cmbCasa.setManaged(true);

                mostrarElementosCentrales(false);
                mostrarFormulario(false);

                Casa casaFiltro = new Casa();
                casaFiltro.setIdCondominio(newVal.getIdCondominio());
                List<Casa> listaCasas = casaService.findCasasByCondominio(casaFiltro);
                cmbCasa.setItems(FXCollections.observableArrayList(listaCasas));
            } else {
                cmbCasa.setVisible(false);
                cmbCasa.setManaged(false);
                cmbCasa.getItems().clear();
            }
        });

        cmbCasa.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarVistaPorCasa(newVal);
            }
        });

        tvCuotas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cuotaSelect = newSelection;
            }
        });
    }

    private void cargarVistaPorCasa(Casa casa) {
        try {
            this.casaSelect = casa;
            lblDireccionCasa.setText("Casa número: " + casa.getNumeroCasa());
            mostrarElementosCentrales(true);
            ObservableList<Cuota> cuotas = FXCollections.observableArrayList(
                    cuotaService.findCuotaByCasa(casa)
            );
            tvCuotas.setItems(cuotas);

        } catch (SQLException e) {
            stage.showAlert("Error de datos", "No se pudieron cargar las cuotas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarElementosCentrales(boolean visible) {
        lblDatos.setVisible(visible);
        hbDatosCasa.setVisible(visible);
        lblTituloTabla.setVisible(visible);
        tvCuotas.setVisible(visible);
    }

    private void configurarValidacionNumerica() {
        txtMes.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        txtAnio.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
        txtMonto.setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*(\\.\\d{0,2})?") ? change : null));
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
        txtMes.clear();
        txtAnio.clear();
        txtMonto.clear();
        cmbEstado.getSelectionModel().clearSelection();
        dpFechaVencimiento.setValue(null);
        cuotaSelect = null;
    }

    @FXML
    private void handleCreateCuota() {
        cuotaSelect = null;
        limpiarFormulario();

        if (casaSelect != null) {
            cmbCasa.setValue(casaSelect);
        }

        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateCuota() {
        Cuota seleccionada = tvCuotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            stage.showAlert("Cuota no seleccionada", "Advertencia", "Debe seleccionar una cuota de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        cuotaSelect = seleccionada;

        txtMes.setText(String.valueOf(seleccionada.getMes()));
        txtAnio.setText(String.valueOf(seleccionada.getAnio()));
        txtMonto.setText(String.valueOf(seleccionada.getMonto()));
        cmbEstado.setValue(seleccionada.getEstado());
        dpFechaVencimiento.setValue(seleccionada.getFechaVencimiento());

        for (Casa c : cmbCasa.getItems()) {
            if (c.getIdCasa() == seleccionada.getIdCasa()) {
                cmbCasa.setValue(c);
                break;
            }
        }

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveCuota() {
        try {
            if (txtMes.getText().isEmpty() || txtAnio.getText().isEmpty()
                    || txtMonto.getText().isEmpty() || cmbEstado.getValue() == null
                    || dpFechaVencimiento.getValue() == null || cmbCasa.getValue() == null) {

                stage.showAlert("Campos vacíos", "Advertencia", "Por favor llene todos los campos obligatorios.", Alert.AlertType.WARNING);
                return;
            }

            Casa casaForm = cmbCasa.getValue();
            int mes = Integer.parseInt(txtMes.getText().trim());
            int anio = Integer.parseInt(txtAnio.getText().trim());
            double monto = Double.parseDouble(txtMonto.getText().trim());
            EstadoCuota estado = cmbEstado.getValue();
            LocalDate fechaVenc = dpFechaVencimiento.getValue();

            if (mes < 1 || mes > 12) {
                stage.showAlert("Mes inválido", "Advertencia", "El mes debe estar entre 1 y 12.", Alert.AlertType.WARNING);
                return;
            }

            if (cuotaSelect == null) {
                Cuota nuevaCuota = new Cuota(0, casaForm.getIdCasa(), mes, anio, monto, estado, fechaVenc);
                if (cuotaService.crearCuota(nuevaCuota)) {
                    stage.showAlert("Guardado", "Éxito", "Cuota registrada correctamente.", Alert.AlertType.INFORMATION);
                }
            } else {
                cuotaSelect.setIdCasa(casaForm.getIdCasa());
                cuotaSelect.setMes(mes);
                cuotaSelect.setAnio(anio);
                cuotaSelect.setMonto(monto);
                cuotaSelect.setEstado(estado);
                cuotaSelect.setFechaVencimiento(fechaVenc);

                if (cuotaService.actualizarCuota(cuotaSelect)) {
                    stage.showAlert("Actualizado", "Éxito", "Cuota actualizada correctamente.", Alert.AlertType.INFORMATION);
                }
            }

            mostrarFormulario(false);
            limpiarFormulario();

            if (casaSelect != null && casaSelect.getIdCasa() == casaForm.getIdCasa()) {
                cargarVistaPorCasa(casaSelect);
            } else {
                cmbCasa.setValue(casaForm);
            }

        } catch (NumberFormatException e) {
            stage.showAlert("Valor inválido", "Advertencia", "Verifique que Mes, Año y Monto sean números válidos.", Alert.AlertType.WARNING);
        } catch (IllegalArgumentException e) {
            stage.showAlert("Datos inválidos", "No se pudo procesar", e.getMessage(), Alert.AlertType.WARNING);
        } catch (SQLException e) {
            stage.showAlert("Error de base de datos", "Fallo técnico", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCuota() {
        Cuota seleccionada = tvCuotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            stage.showAlert("Cuota no seleccionada", "Advertencia", "Debe seleccionar una cuota para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = stage.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Cuota",
                "¿Desea eliminar la cuota del mes " + seleccionada.getMes() + " / " + seleccionada.getAnio() + "?"
        );

        if (confirmado) {
            try {
                if (cuotaService.eliminarCuota(seleccionada)) {
                    stage.showAlert("Eliminación completada", "Éxito", "Cuota eliminada correctamente.", Alert.AlertType.INFORMATION);

                    if (casaSelect != null) {
                        cargarVistaPorCasa(casaSelect);
                    }
                    mostrarFormulario(false);
                    limpiarFormulario();
                }
            } catch (IllegalArgumentException e) {
                stage.showAlert("Aviso", "No se puede eliminar", e.getMessage(), Alert.AlertType.WARNING);
            } catch (SQLException e) {
                stage.showAlert("Error al eliminar", "Fallo técnico", e.getMessage(), Alert.AlertType.ERROR);
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
        if (stage.showConfirmation("Confirmar cierre", "Cerrar Sesión", "¿Desea cerrar sesión?")) {
            stage.showLoginView();
        }
    }

    @FXML
    private void handleGoToCondominioView() {
        stage.showCondominioView();
    }

    @FXML
    private void handleResidentes() {
        stage.showResidenteView();
    }

    @FXML
    private void handleGoToCasaResidente() {
        stage.showCasaResidente();
    }

    @FXML
    private void handleGoToAreaComunView() {
        stage.showAreaComunView();
    }

    @FXML
    private void handleGoToReserva() {
        stage.showReservaView();
    }

    @FXML
    private void handleGoToCasaView() {
        stage.showCasaView();
    }
}
