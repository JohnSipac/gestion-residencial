package main.java.com.fammateam.gestionresidencial.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.model.Cuota;
import main.java.com.fammateam.gestionresidencial.model.Pago;
import main.java.com.fammateam.gestionresidencial.service.CondominioService;
import main.java.com.fammateam.gestionresidencial.service.CuotaService;
import main.java.com.fammateam.gestionresidencial.service.PagoService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class PagoController implements Initializable {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final BigDecimal MONTO_MAXIMO = new BigDecimal("99999999.99");

    private SceneManager stage;
    private CondominioService condominioService;
    private CuotaService cuotaService;
    private PagoService pagoService;

    private Condominio condominioSeleccionado;
    private Pago pagoSeleccionado;

    @FXML
    private ComboBox<Condominio> cmbCondominio;
    @FXML
    private Label lblDatos;
    @FXML
    private HBox hbDatosCondominio;
    @FXML
    private Label lblNombreCondominio;
    @FXML
    private Label lblTelefono;
    @FXML
    private Label lblPagos;
    @FXML
    private TableView<Pago> tvPagos;
    @FXML
    private TableColumn<Pago, String> tcNumeroCasa;
    @FXML
    private TableColumn<Pago, String> tcMontoPagado;
    @FXML
    private TableColumn<Pago, String> tcFechaPago;
    @FXML
    private TableColumn<Pago, String> tcMetodoPago;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private ComboBox<Cuota> cmbCasas;
    @FXML
    private TextField txtMontoPagado;
    @FXML
    private DatePicker dpFechaPago;
    @FXML
    private ComboBox<String> cmbHoraPago;
    @FXML
    private ComboBox<String> cmbMinutoPago;
    @FXML
    private ComboBox<String> cmbMetodoPago;

    public PagoController(SceneManager stage, CondominioService condominioService,
                           CuotaService cuotaService, PagoService pagoService) {
        this.stage = stage;
        this.condominioService = condominioService;
        this.cuotaService = cuotaService;
        this.pagoService = pagoService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnasTabla();
        configurarComboBoxConverters();
        configurarDatePicker();
        cargarDatosComboBox();
        configurarListeners();
        configurarValidacionMonto();
        ocultarPanelDatosInicial();
        ocultarElementosCentralesInicial();
    }

    private void configurarColumnasTabla() {
        tcNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));

        tcMontoPagado.setCellValueFactory(cellData -> {
            BigDecimal monto = cellData.getValue().getMontoPagado();
            return new SimpleStringProperty(monto != null ? monto.toPlainString() : "");
        });

        tcFechaPago.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaPago();
            return new SimpleStringProperty(fecha != null ? fecha.format(FORMATO_FECHA) : "");
        });

        tcMetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
    }

    private void configurarComboBoxConverters() {
        cmbCasas.setConverter(new StringConverter<Cuota>() {
            @Override
            public String toString(Cuota cuota) {
                return (cuota != null) ? cuota.getNumeroCasa() : "";
            }

            @Override
            public Cuota fromString(String string) {
                return cmbCasas.getItems().stream()
                        .filter(c -> c.getNumeroCasa().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void configurarDatePicker() {
        dpFechaPago.setEditable(false);
    }

    private void cargarDatosComboBox() {
        ObservableList<Condominio> condominios = condominioService.listarCondominios();
        cmbCondominio.setItems(condominios);

        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            horas.add(String.format("%02d", i));
        }
        cmbHoraPago.setItems(horas);

        cmbMinutoPago.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));

        cmbMetodoPago.setItems(FXCollections.observableArrayList("Transferencia", "Efectivo", "Tarjeta"));
    }

    private void configurarListeners() {
        cmbCondominio.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarVistaPorCondominio(newVal);
            }
        });

        tvPagos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                pagoSeleccionado = newSelection;
            }
        });
    }

    private void cargarVistaPorCondominio(Condominio condominio) {
        try {
            condominioSeleccionado = condominio;
            lblNombreCondominio.setText(condominio.getNombre());
            lblTelefono.setText(condominio.getTelefono());

            mostrarElementosCentrales(true);

            ObservableList<Pago> pagos = pagoService.listarPagosPorCondominio(condominio.getIdCondominio());
            tvPagos.setItems(pagos);

            ObservableList<Cuota> cuotas = cuotaService.listaCuotaConNumeroCasa();
            cmbCasas.setItems(cuotas);

        } catch (SQLException e) {
            stage.showAlert("Error al cargar datos", "Error", "No se pudieron cargar los datos del condominio: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void ocultarElementosCentralesInicial() {
        mostrarElementosCentrales(false);
    }

    private void mostrarElementosCentrales(boolean visible) {
        lblDatos.setVisible(visible);
        hbDatosCondominio.setVisible(visible);
        lblPagos.setVisible(visible);
        tvPagos.setVisible(visible);
    }

    @FXML
    private void handleShowCreateForm() {
        pagoSeleccionado = null;
        limpiarFormulario();
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdate() {
        Pago seleccionado = tvPagos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            stage.showAlert("Pago no seleccionado", "Advertencia", "Debe seleccionar un pago de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        pagoSeleccionado = seleccionado;
        txtMontoPagado.setText(seleccionado.getMontoPagado().toPlainString());

        LocalDateTime fecha = seleccionado.getFechaPago();
        if (fecha != null) {
            dpFechaPago.setValue(fecha.toLocalDate());
            cmbHoraPago.setValue(String.format("%02d", fecha.getHour()));
            cmbMinutoPago.setValue(String.format("%02d", fecha.getMinute()));
        }

        cmbMetodoPago.setValue(seleccionado.getMetodoPago());

        cmbCasas.getItems().stream()
                .filter(c -> c.getIdCuota() == seleccionado.getIdCuota())
                .findFirst()
                .ifPresent(cmbCasas::setValue);

        mostrarFormulario(true);
    }

    @FXML
    private void handleSave() {
        try {
            String montoStr = txtMontoPagado.getText().trim();
            Cuota cuotaSeleccionada = cmbCasas.getValue();
            LocalDate fecha = dpFechaPago.getValue();
            String horaStr = cmbHoraPago.getValue();
            String minutoStr = cmbMinutoPago.getValue();
            String metodoPago = cmbMetodoPago.getValue();

            if (montoStr.isEmpty() || cuotaSeleccionada == null || fecha == null
                    || horaStr == null || minutoStr == null || metodoPago == null) {
                stage.showAlert("Campos vacíos", "Advertencia", "Por favor llene todos los campos obligatorios.", Alert.AlertType.WARNING);
                return;
            }

            BigDecimal monto = new BigDecimal(montoStr);

            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                stage.showAlert("Monto inválido", "Advertencia", "El monto pagado debe ser mayor a cero.", Alert.AlertType.WARNING);
                return;
            }

            if (monto.compareTo(MONTO_MAXIMO) > 0) {
                stage.showAlert("Monto inválido", "Advertencia", "El monto pagado no puede superar 99,999,999.99.", Alert.AlertType.WARNING);
                return;
            }

            LocalDateTime fechaPago = LocalDateTime.of(
                    fecha,
                    LocalTime.of(Integer.parseInt(horaStr), Integer.parseInt(minutoStr))
            );

            Condominio condominioDelPago = condominioSeleccionado;

            if (pagoSeleccionado == null) {
                Pago nuevoPago = new Pago(
                        cuotaSeleccionada.getIdCuota(),
                        monto,
                        fechaPago,
                        metodoPago
                );
                if (pagoService.registrarPago(nuevoPago)) {
                    stage.showAlert("Guardado", "Éxito", "Pago registrado correctamente.", Alert.AlertType.INFORMATION);
                }
            } else {
                Pago pagoActualizado = new Pago(
                        pagoSeleccionado.getIdPago(),
                        cuotaSeleccionada.getIdCuota(),
                        monto,
                        fechaPago,
                        metodoPago,
                        pagoSeleccionado.getIdCondominio(),
                        pagoSeleccionado.getNombreCondominio(),
                        pagoSeleccionado.getIdCasa(),
                        pagoSeleccionado.getNumeroCasa()
                );
                if (pagoService.modificarPago(pagoActualizado)) {
                    stage.showAlert("Actualizado", "Éxito", "Pago actualizado correctamente.", Alert.AlertType.INFORMATION);
                }
            }

            mostrarFormulario(false);
            limpiarFormulario();

            if (condominioDelPago != null) {
                cmbCondominio.setValue(condominioDelPago);
                cargarVistaPorCondominio(condominioDelPago);
            }

        } catch (NumberFormatException e) {
            stage.showAlert("Valor inválido", "Advertencia", "Ingrese un valor numérico válido para el monto.", Alert.AlertType.WARNING);
        } catch (IllegalArgumentException e) {
            stage.showAlert("Datos inválidos", "Advertencia", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            stage.showAlert("Error de guardado", "Error", "Ocurrió un error al guardar el pago: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        Pago seleccionado = tvPagos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            stage.showAlert("Pago no seleccionado", "Advertencia", "Debe seleccionar un pago para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = stage.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Pago",
                "¿Desea eliminar el pago de la casa: " + seleccionado.getNumeroCasa() + "?"
        );

        if (confirmado) {
            try {
                if (pagoService.eliminarPago(seleccionado)) {
                    stage.showAlert("Eliminación completada", "Éxito", "Pago eliminado correctamente.", Alert.AlertType.INFORMATION);

                    if (condominioSeleccionado != null) {
                        cargarVistaPorCondominio(condominioSeleccionado);
                    }
                    mostrarFormulario(false);
                    limpiarFormulario();
                }
            } catch (Exception e) {
                stage.showAlert("Error al eliminar", "Error", "No se pudo eliminar el pago: " + e.getMessage(), Alert.AlertType.ERROR);
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
    private void handleGoToCondominioView() {
        stage.showCondominioView();
    }

    @FXML
    private void handleGoToCasaView() {
        stage.showCasaView();
    }

    @FXML
    private void handleGoToResidenteView() {
        stage.showResidenteView();
    }

    @FXML
    private void handleCasaResidente() {
        stage.showCasaResidente();
    }

    @FXML
    private void handleAreaComun() {
        stage.showAreaComunView();
    }

    @FXML
    private void handleGoToCuota() {
        stage.showCuotaView();
    }
    
     @FXML
    private void handleGoToReserva() {
        stage.showReservaView();
    }

    @FXML
    private void handleGoToLoginView() {
        boolean confirmado = stage.showConfirmation(
                "Confirmar cierre de sesión",
                "Cerrar Sesión",
                "¿Desea cerrar sesión?"
        );
        if (confirmado) {
            stage.showLoginView();
        }
    }

    // --- Métodos de Utilidad y Animación ---
    private void configurarValidacionMonto() {
        txtMontoPagado.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,8}(\\.\\d{0,2})?")) {
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
        final double ANCHO_EXPANDIDO = 294.0;
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
        txtMontoPagado.clear();
        dpFechaPago.setValue(null);
        cmbHoraPago.getSelectionModel().clearSelection();
        cmbMinutoPago.getSelectionModel().clearSelection();
        cmbMetodoPago.getSelectionModel().clearSelection();
        cmbCasas.getSelectionModel().clearSelection();
        pagoSeleccionado = null;
    }
}