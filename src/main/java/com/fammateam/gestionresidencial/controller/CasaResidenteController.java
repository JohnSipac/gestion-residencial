package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.java.com.fammateam.gestionresidencial.model.Casa;
import main.java.com.fammateam.gestionresidencial.model.CasaResidente;
import main.java.com.fammateam.gestionresidencial.model.Residente;
import main.java.com.fammateam.gestionresidencial.service.CasaResidenteService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class CasaResidenteController implements Initializable {

    private final SceneManager sceneManager;
    private final CasaResidenteService casaResidenteService;
    private CasaResidente asignacionSeleccionada;

    @FXML
    private TableView<CasaResidente> tvCasaResidente;
    @FXML
    private TableColumn<CasaResidente, Integer> tcIdCasa;
    @FXML
    private TableColumn<CasaResidente, Integer> tcIdResidente;
    @FXML
    private TableColumn<CasaResidente, String> tcFechaInicio;

    @FXML
    private AnchorPane panelDatos;
    @FXML
    private ComboBox<Casa> cmbCasa;
    @FXML
    private ComboBox<Residente> cmbResidente;
    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private Button btnMainMenu;

    public CasaResidenteController(SceneManager sceneManager, CasaResidenteService casaResidenteService) {
        this.sceneManager = sceneManager;
        this.casaResidenteService = casaResidenteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarCombos();
        ocultarPanelDatosInicial();
        cargarTabla();
        cargarCombos();
    }

    //AÑADIR CONTENIDO
    private void configurarCombos() {
        cmbCasa.setConverter(new StringConverter<Casa>() {
            @Override
            public String toString(Casa c) {
                if (c == null) {
                    return null;
                }

                return "Casa No." + c.getNumeroCasa();
            }

            @Override
            public Casa fromString(String string) {
                return null;
            }
        });

        cmbResidente.setConverter(new StringConverter<Residente>() {
            @Override
            public String toString(Residente r) {
                if (r == null) {
                    return null;
                }
                return r.getNombre() + " " + r.getApellido();
            }

            @Override
            public Residente fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleCreateCasaResidente() {
        asignacionSeleccionada = null;
        limpiarFormulario();
        cmbCasa.setDisable(false);
        cmbResidente.setDisable(false);
        mostrarFormulario(true);
    }

    @FXML
    private void handleUpdateCasaResidente() {
        CasaResidente seleccionado = tvCasaResidente.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Asignación no seleccionada", "Advertencia", "Debe seleccionar una asignación de la tabla", Alert.AlertType.WARNING);
            return;
        }

        asignacionSeleccionada = seleccionado;

        cmbCasa.setDisable(true);
        cmbResidente.setDisable(true);

        if (cmbCasa.getItems() != null) {
            cmbCasa.getItems().stream()
                    .filter(c -> c.getIdCasa() == seleccionado.getIdCasa())
                    .findFirst()
                    .ifPresent(cmbCasa::setValue);
        }

        if (cmbResidente.getItems() != null) {
            cmbResidente.getItems().stream()
                    .filter(r -> r.getIdResidente() == seleccionado.getIdResidente())
                    .findFirst()
                    .ifPresent(cmbResidente::setValue);
        }

        if (seleccionado.getFechaInicio() != null && !seleccionado.getFechaInicio().isEmpty()) {
            try {
                dpFechaInicio.setValue(LocalDate.parse(seleccionado.getFechaInicio()));
            } catch (Exception e) {
                dpFechaInicio.setValue(null);
            }
        }

        mostrarFormulario(true);
    }

    @FXML
    private void handleSaveCasaResidente() {
        try {
            Casa casa = cmbCasa.getValue();
            Residente residente = cmbResidente.getValue();
            LocalDate fecha = dpFechaInicio.getValue();

            if (casa == null || residente == null || fecha == null) {
                sceneManager.showAlert("Campos incompletos", "Advertencia", "Debe seleccionar una casa, un residente y la fecha de inicio.", Alert.AlertType.WARNING);
                return;
            }

            String fechaStr = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (asignacionSeleccionada == null) {
                CasaResidente nuevo = new CasaResidente(casa.getIdCasa(), residente.getIdResidente(), fechaStr);
                if (casaResidenteService.registerCasaResidente(nuevo)) {
                    sceneManager.showAlert("Guardado", "Éxito", "Asignación guardada correctamente", Alert.AlertType.INFORMATION);
                }
            } else {
                asignacionSeleccionada.setFechaInicio(fechaStr);
                if (casaResidenteService.updateCasaResidente(asignacionSeleccionada)) {
                    sceneManager.showAlert("Actualizado", "Éxito", "Asignación actualizada correctamente", Alert.AlertType.INFORMATION);
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
    private void handleDeleteCasaResidente() {
        CasaResidente seleccionado = tvCasaResidente.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showAlert("Asignación no seleccionada", "Advertencia", "Debe seleccionar una asignación para eliminar", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar eliminación",
                "Eliminación de Asignación",
                "¿Desea desasignar el residente de la casa seleccionada?"
        );

        if (confirmado) {
            try {
                if (casaResidenteService.deleteCasaResidente(seleccionado.getIdCasa(), seleccionado.getIdResidente())) {
                    sceneManager.showAlert("Eliminación completada", "Éxito", "Asignación eliminada correctamente.", Alert.AlertType.INFORMATION);
                    cargarTabla();
                    mostrarFormulario(false);
                }
            } catch (Exception e) {
                sceneManager.showAlert("Error al eliminar", "Error", "No se pudo eliminar la asignación: " + e.getMessage(), Alert.AlertType.ERROR);
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

    @FXML
    private void handleGoToCondominioView() {
        sceneManager.showCondominioView();
    }

    @FXML
    private void handleGoToAreaComunView() {
        sceneManager.showAreaComunView();
    }

    private void configurarTabla() {
        tcIdCasa.setCellValueFactory(new PropertyValueFactory<>("idCasa"));
        tcIdResidente.setCellValueFactory(new PropertyValueFactory<>("idResidente"));
        tcFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
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
            ObservableList<CasaResidente> lista = casaResidenteService.findAll();
            tvCasaResidente.setItems(lista);
        } catch (Exception e) {
            sceneManager.showAlert("Error de carga", "Error", "No se pudieron cargar las asignaciones", Alert.AlertType.ERROR);
        }
    }

    private void cargarCombos() {
        try {
            cmbCasa.setItems(casaResidenteService.getCasas());
            cmbResidente.setItems(casaResidenteService.getResidentes());
        } catch (Exception e) {
            sceneManager.showAlert("Error de carga", "Error", "No se pudieron cargar las listas desplegables", Alert.AlertType.ERROR);
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
        cmbCasa.setValue(null);
        cmbResidente.setValue(null);
        dpFechaInicio.setValue(null);
        cmbCasa.setDisable(false);
        cmbResidente.setDisable(false);
        asignacionSeleccionada = null;
    }
}
