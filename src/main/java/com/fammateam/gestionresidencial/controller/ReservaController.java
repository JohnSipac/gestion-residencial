package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.java.com.fammateam.gestionresidencial.model.AreaComun;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import main.java.com.fammateam.gestionresidencial.model.Reserva;
import main.java.com.fammateam.gestionresidencial.service.AreaComunService;
import main.java.com.fammateam.gestionresidencial.service.ResidenteService;
import main.java.com.fammateam.gestionresidencial.service.ReservaService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class ReservaController implements Initializable {

    @FXML
    private TableView<Reserva> tvReservas;
    @FXML
    private TableColumn<Reserva, String> tcNombreResidente;
    @FXML
    private TableColumn<Reserva, String> tcFechaReserva;
    @FXML
    private TableColumn<Reserva, LocalTime> tcHoraInicio;
    @FXML
    private TableColumn<Reserva, LocalTime> tcHoraFin;
    @FXML
    private TableColumn<Reserva, String> tcEstado;

    @FXML
    private ComboBox<AreaComun> cmbArea;
    @FXML
    private Label lblNombreArea;
    @FXML
    private Label lblCostoReserva;
    @FXML
    private Label lblDatos;
    @FXML
    private HBox hbDatosAreaComun;
    @FXML
    private Label lblReservas;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private ComboBox<AreaComun> cmbAreas;
    @FXML
    private ComboBox<Residente> cmbResidente;
    @FXML
    private DatePicker dpFechaReserva;
    @FXML
    private ComboBox<String> cmbHoraInicio;
    @FXML
    private ComboBox<String> cmbMinutoInicio;
    @FXML
    private ComboBox<String> cmbHoraFin;
    @FXML
    private ComboBox<String> cmbMinutoFin;
    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private VBox sidebar;

    private final ReservaService reservaService;
    private final AreaComunService areaComunService;
    private final ResidenteService residenteService;
    private final SceneManager sceneManager;

    private Reserva reservaSeleccionada;

    public ReservaController(ReservaService reservaService, AreaComunService areaComunService, ResidenteService residenteService, SceneManager sceneManager) {
        this.reservaService = reservaService;
        this.areaComunService = areaComunService;
        this.residenteService = residenteService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        setupComboBoxConverters();
        cargarDatosHorasYMinutos();
        loadAreasComunes();
        loadResidentes();
        setupTableSelection();
        configurarDatePicker();

        cmbArea.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarVistaPorArea(newVal);
            }
        });

        ocultarPanelDatosInicial();
        ocultarElementosCentralesInicial();
    }

    private void setupColumns() {
        tcNombreResidente.setCellValueFactory(new PropertyValueFactory<>("nombreResidente"));
        tcFechaReserva.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));
        tcHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        tcHoraFin.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void setupComboBoxConverters() {
        StringConverter<AreaComun> areaConverter = new StringConverter<AreaComun>() {
            @Override
            public String toString(AreaComun area) {
                return (area != null) ? area.getNombre() : "";
            }

            @Override
            public AreaComun fromString(String string) {
                return null;
            }
        };

        if (cmbArea != null) {
            cmbArea.setConverter(areaConverter);
        }
        if (cmbAreas != null) {
            cmbAreas.setConverter(areaConverter);
        }

        if (cmbResidente != null) {
            cmbResidente.setConverter(new StringConverter<Residente>() {
                @Override
                public String toString(Residente residente) {
                    return (residente != null) ? residente.getNombre() : "";
                }

                @Override
                public Residente fromString(String string) {
                    return null;
                }
            });
        }
    }

    private void configurarDatePicker() {
        if (dpFechaReserva != null) {
            dpFechaReserva.setEditable(false);
        }
    }

    private void cargarDatosHorasYMinutos() {
        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            horas.add(String.format("%02d", i));
        }

        ObservableList<String> minutos = FXCollections.observableArrayList("00", "15", "30", "45");

        if (cmbHoraInicio != null) {
            cmbHoraInicio.setItems(horas);
        }
        if (cmbMinutoInicio != null) {
            cmbMinutoInicio.setItems(minutos);
        }
        if (cmbHoraFin != null) {
            cmbHoraFin.setItems(horas);
        }
        if (cmbMinutoFin != null) {
            cmbMinutoFin.setItems(minutos);
        }

        if (cmbEstado != null) {
            cmbEstado.setItems(FXCollections.observableArrayList("Pendiente", "Aprobada", "Cancelada"));
        }
    }

    private void loadAreasComunes() {
        try {
            ObservableList<AreaComun> lista = areaComunService.getAllAreasComunes();
            if (cmbArea != null) {
                cmbArea.setItems(lista);
            }
            if (cmbAreas != null) {
                cmbAreas.setItems(lista);
            }
        } catch (Exception e) {
            sceneManager.showAlert("ERROR", "Error al cargar áreas", "No se pudieron obtener las áreas comunes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadResidentes() {
        try {
            ObservableList<Residente> lista = residenteService.listarResidentes();
            if (cmbResidente != null) {
                cmbResidente.setItems(lista);
            }
        } catch (Exception e) {
            sceneManager.showAlert("ERROR", "Error al cargar residentes", "No se pudieron obtener los residentes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarVistaPorArea(AreaComun areaComun) {
        if (areaComun == null) {
            return;
        }

        if (lblNombreArea != null) {
            lblNombreArea.setText(areaComun.getNombre());
        }
        if (lblCostoReserva != null) {
            lblCostoReserva.setText(String.valueOf(areaComun.getCostoReserva()));
        }

        mostrarElementosCentrales(true);

        try {
            ObservableList<Reserva> reservas = reservaService.buscarReservasPorArea(areaComun.getIdArea());
            tvReservas.setItems(reservas);
        } catch (Exception e) {
            sceneManager.showAlert("Error de carga", "Error", "No se pudo cargar las reservas", Alert.AlertType.ERROR);
        }
    }

    private void ocultarElementosCentralesInicial() {
        mostrarElementosCentrales(false);
    }

    private void mostrarElementosCentrales(boolean visible) {
        if (lblDatos != null) {
            lblDatos.setVisible(visible);
        }
        if (hbDatosAreaComun != null) {
            hbDatosAreaComun.setVisible(visible);
        }
        if (lblReservas != null) {
            lblReservas.setVisible(visible);
        }
        if (tvReservas != null) {
            tvReservas.setVisible(visible);
        }
    }

    private void setupTableSelection() {
        tvReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                return;
            }

            reservaSeleccionada = newSelection;

            dpFechaReserva.setValue(newSelection.getFechaReserva());
            cmbEstado.setValue(newSelection.getEstado());

            if (newSelection.getHoraInicio() != null) {
                cmbHoraInicio.setValue(String.format("%02d", newSelection.getHoraInicio().getHour()));
                cmbMinutoInicio.setValue(String.format("%02d", newSelection.getHoraInicio().getMinute()));
            }

            if (newSelection.getHoraFin() != null) {
                cmbHoraFin.setValue(String.format("%02d", newSelection.getHoraFin().getHour()));
                cmbMinutoFin.setValue(String.format("%02d", newSelection.getHoraFin().getMinute()));
            }

            if (cmbAreas.getItems() != null) {
                for (AreaComun a : cmbAreas.getItems()) {
                    if (a.getIdArea() == newSelection.getIdArea()) {
                        cmbAreas.setValue(a);
                        break;
                    }
                }
            }

            if (cmbResidente.getItems() != null) {
                for (Residente r : cmbResidente.getItems()) {
                    if (r.getIdResidente() == newSelection.getIdResidente()) {
                        cmbResidente.setValue(r);
                        break;
                    }
                }
            }
        });
    }

    @FXML
    private void handleShowCreateForm() {
        reservaSeleccionada = null;
        clearFields();
        mostrarFormulario(true);
    }

    @FXML
    private void handleSave() {
        if (!validarCampos()) {
            return;
        }

        try {
            int idArea = cmbAreas.getValue().getIdArea();
            int idResidente = cmbResidente.getValue().getIdResidente();
            LocalDate fecha = dpFechaReserva.getValue();

            LocalTime horaInicio = LocalTime.of(
                    Integer.parseInt(cmbHoraInicio.getValue()),
                    Integer.parseInt(cmbMinutoInicio.getValue())
            );

            LocalTime horaFin = LocalTime.of(
                    Integer.parseInt(cmbHoraFin.getValue()),
                    Integer.parseInt(cmbMinutoFin.getValue())
            );

            String estado = cmbEstado.getValue();
            AreaComun areaGuardada = cmbAreas.getValue();
            boolean esNueva = (reservaSeleccionada == null);

            boolean exito = reservaService.guardarReserva(reservaSeleccionada, idArea, idResidente, fecha, horaInicio, horaFin, estado);

            if (exito) {
                if (esNueva) {
                    sceneManager.showAlert("Creado correctamente", "Creado", "Reserva creada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    sceneManager.showAlert("Actualizado correctamente", "Actualizado", "Reserva actualizada correctamente", Alert.AlertType.INFORMATION);
                }
            } else {
                if (esNueva) {
                    sceneManager.showAlert("Error", "No se pudo crear", "No se pudo registrar la reserva.", Alert.AlertType.ERROR);
                } else {
                    sceneManager.showAlert("Error", "No se pudo actualizar", "No se pudo actualizar la reserva.", Alert.AlertType.ERROR);
                }
            }

            mostrarFormulario(false);
            clearFields();

            if (exito && areaGuardada != null) {
                cmbArea.setValue(areaGuardada);
                cargarVistaPorArea(areaGuardada);
            } else if (cmbArea.getValue() != null) {
                cargarVistaPorArea(cmbArea.getValue());
            }

        } catch (NumberFormatException e) {
            sceneManager.showAlert("ERROR", "Formato inválido", "La hora u otro campo numérico es inválido.", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException e) {
            sceneManager.showAlert("Datos inválidos", "Advertencia", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            sceneManager.showAlert("ERROR", "Error al guardar", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        mostrarFormulario(false);
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        if (reservaSeleccionada == null) {
            sceneManager.showAlert("AVISO", "Selección requerida", "Debe seleccionar una reserva de la tabla para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        mostrarFormulario(true);
    }

    @FXML
    private void handleDelete() {
        if (reservaSeleccionada == null) {
            sceneManager.showAlert("AVISO", "Selección requerida", "Debe seleccionar una reserva para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Reserva",
                "¿Desea eliminar la reserva seleccionada?"
        );

        if (confirmado) {
            try {
                if (reservaService.eliminarReserva(reservaSeleccionada)) {
                    sceneManager.showAlert("Éxito", "Eliminación completada", "Reserva eliminada correctamente.", Alert.AlertType.INFORMATION);

                    if (cmbArea.getValue() != null) {
                        cargarVistaPorArea(cmbArea.getValue());
                    }
                    mostrarFormulario(false);
                    clearFields();
                }
            } catch (Exception e) {
                sceneManager.showAlert("Error", "Error al eliminar", "No se pudo eliminar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
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
    private void handleGoToResidenteView() {
        sceneManager.showResidenteView();
    }
    
    @FXML
    private void handleGoToCasaView() {
        sceneManager.showCasaView();
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

    private void ocultarPanelDatosInicial() {
        if (panelDatos != null) {
            panelDatos.setVisible(false);
            panelDatos.setManaged(false);
            panelDatos.setPrefWidth(0);
            panelDatos.setMinWidth(0);
            panelDatos.setMaxWidth(0);
        }
    }

    private void mostrarFormulario(boolean mostrar) {
        if (panelDatos == null) {
            return;
        }

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

    private void clearFields() {
        reservaSeleccionada = null;
        if (cmbAreas != null) {
            cmbAreas.getSelectionModel().clearSelection();
        }
        if (cmbResidente != null) {
            cmbResidente.getSelectionModel().clearSelection();
        }
        if (dpFechaReserva != null) {
            dpFechaReserva.setValue(null);
        }
        if (cmbHoraInicio != null) {
            cmbHoraInicio.getSelectionModel().clearSelection();
        }
        if (cmbMinutoInicio != null) {
            cmbMinutoInicio.getSelectionModel().clearSelection();
        }
        if (cmbHoraFin != null) {
            cmbHoraFin.getSelectionModel().clearSelection();
        }
        if (cmbMinutoFin != null) {
            cmbMinutoFin.getSelectionModel().clearSelection();
        }
        if (cmbEstado != null) {
            cmbEstado.getSelectionModel().clearSelection();
        }
    }

    private boolean validarCampos() {
        if (cmbAreas.getValue() == null || cmbResidente.getValue() == null || dpFechaReserva.getValue() == null
                || cmbHoraInicio.getValue() == null || cmbMinutoInicio.getValue() == null
                || cmbHoraFin.getValue() == null || cmbMinutoFin.getValue() == null || cmbEstado.getValue() == null) {
            sceneManager.showAlert("Campos vacíos", "Advertencia", "Por favor complete todos los campos del formulario.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}