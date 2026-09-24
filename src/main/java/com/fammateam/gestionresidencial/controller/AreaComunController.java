package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
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
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import main.java.com.fammateam.gestionresidencial.model.AreaComun;
import main.java.com.fammateam.gestionresidencial.model.Condominio;
import main.java.com.fammateam.gestionresidencial.service.AreaComunService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;

public class AreaComunController implements Initializable {

    @FXML
    private TableView<AreaComun> tblAreasComunes;
    @FXML
    private TableColumn<AreaComun, Integer> colIdArea;
    @FXML
    private TableColumn<AreaComun, String> colNombre;
    @FXML
    private TableColumn<AreaComun, Integer> colCapacidad;
    @FXML
    private TableColumn<AreaComun, Double> colCosto;
    @FXML
    private TableColumn<AreaComun, String> colCondominio;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCapacidad;
    @FXML
    private TextField txtCosto;
    @FXML
    private ComboBox<Condominio> cmbCondominio;

    @FXML
    private AnchorPane formContainer;
    @FXML
    private VBox sidebar;

    private final AreaComunService areaComunService;
    private final SceneManager sceneManager;
    private AreaComun areaSeleccionada;

    public AreaComunController(AreaComunService areaComunService, SceneManager sceneManager) {
        this.areaComunService = areaComunService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();
        setupComboBoxConverter();
        setupTextFieldValidation();
        loadTableData();
        loadCondominios();
        setupTableSelection();
    }

    private void setupColumns() {
        colIdArea.setCellValueFactory(new PropertyValueFactory<>("idArea"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidadMaxima"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costoReserva"));
        colCondominio.setCellValueFactory(new PropertyValueFactory<>("nombreCondominio"));
    }

    //Metodo de comprobacion de formato
    private void setupTextFieldValidation() {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        txtCapacidad.setTextFormatter(new TextFormatter<>(integerFilter));

        UnaryOperator<TextFormatter.Change> decimalFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            }
            return null;
        };
        txtCosto.setTextFormatter(new TextFormatter<>(decimalFilter));
    }

    private void setupComboBoxConverter() {
        cmbCondominio.setConverter(new StringConverter<Condominio>() {
            @Override
            public String toString(Condominio condominio) {
                return (condominio != null) ? condominio.getNombre() : "";
            }

            @Override
            public Condominio fromString(String string) {
                return cmbCondominio.getItems().stream()
                        .filter(c -> c.getNombre().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void loadCondominios() {
        try {
            ObservableList<Condominio> listaCondominios = areaComunService.getAllCondominios();
            cmbCondominio.setItems(listaCondominios);
        } catch (Exception e) {
            sceneManager.showAlert("ERROR DE BASE DE DATOS", "Error al cargar condominios",
                    "No se pudieron cargar los condominios: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadTableData() {
        try {
            ObservableList<AreaComun> lista = areaComunService.getAllAreasComunes();
            tblAreasComunes.setItems(lista);
        } catch (Exception e) {
            sceneManager.showAlert("ERROR DE BASE DE DATOS", "Error al cargar",
                    "No se pudieron obtener los registros: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupTableSelection() {
        tblAreasComunes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                areaSeleccionada = newSelection;
                txtNombre.setText(newSelection.getNombre());
                txtCapacidad.setText(String.valueOf(newSelection.getCapacidadMaxima()));
                txtCosto.setText(String.valueOf(newSelection.getCostoReserva()));

                if (cmbCondominio.getItems() != null) {
                    for (Condominio c : cmbCondominio.getItems()) {
                        if (c.getIdCondominio() == newSelection.getIdCondominio()) {
                            cmbCondominio.setValue(c);
                            break;
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void handleShowCreateForm() {
        clearFields();
        if (formContainer != null) {
            formContainer.setVisible(true);
            formContainer.setManaged(true);
        }
    }

    @FXML
    private void handleUpdate() {
        if (areaSeleccionada == null) {
            sceneManager.showAlert("SELECCIÓN REQUERIDA", "Atención",
                    "Debe seleccionar un área común de la tabla para editar.", Alert.AlertType.WARNING);
            return;
        }

        if (formContainer != null && !formContainer.isVisible()) {
            formContainer.setVisible(true);
            formContainer.setManaged(true);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            areaSeleccionada.setIdCondominio(cmbCondominio.getValue().getIdCondominio());
            areaSeleccionada.setNombre(txtNombre.getText().trim());
            areaSeleccionada.setCapacidadMaxima(Integer.parseInt(txtCapacidad.getText().trim()));
            areaSeleccionada.setCostoReserva(Double.parseDouble(txtCosto.getText().trim()));

            if (areaComunService.updateAreaComun(areaSeleccionada)) {
                sceneManager.showAlert("ÉXITO DE ACTUALIZACIÓN", "Registro Modificado",
                        "Se actualizaron los datos del área común.", Alert.AlertType.INFORMATION);
                loadTableData();
                handleCancel();
            }
        } catch (Exception e) {
            sceneManager.showAlert("ERROR EN OPERACIÓN", "Error al Actualizar",
                    "No se pudieron actualizar los datos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSave() {
        if (!validarCampos()) {
            return;
        }

        try {
            int idCondominio = cmbCondominio.getValue().getIdCondominio();
            String nombre = txtNombre.getText().trim();
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            double costo = Double.parseDouble(txtCosto.getText().trim());

            AreaComun area = new AreaComun(idCondominio, nombre, capacidad, costo);

            if (areaComunService.saveAreaComun(area)) {
                sceneManager.showAlert("ÉXITO DE REGISTRO", "Área Guardada",
                        "El área común se ha registrado correctamente.", Alert.AlertType.INFORMATION);
                loadTableData();
                handleCancel();
            }
        } catch (Exception e) {
            sceneManager.showAlert("ERROR EN OPERACIÓN", "Error al Guardar",
                    "No se pudo registrar en el sistema: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        if (areaSeleccionada == null) {
            sceneManager.showAlert("SELECCIÓN REQUERIDA", "Atención",
                    "Debe seleccionar un área común de la tabla para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (areaComunService.removeAreaComun(areaSeleccionada.getIdArea())) {
                sceneManager.showAlert("ÉXITO DE ELIMINACIÓN", "Registro Eliminado",
                        "El área común ha sido removida del sistema.", Alert.AlertType.INFORMATION);
                loadTableData();
                handleCancel();
            }
        } catch (Exception e) {
            sceneManager.showAlert("ERROR EN OPERACIÓN", "Error al Eliminar",
                    "No se pudo eliminar el registro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        clearFields();
        if (formContainer != null) {
            formContainer.setVisible(false);
            formContainer.setManaged(false);
        }
    }

    private boolean validarCampos() {
        if (cmbCondominio.getValue() == null) {
            sceneManager.showAlert("VALIDACIÓN DE DATOS", "Campo Obligatorio",
                    "Debe seleccionar un condominio.", Alert.AlertType.WARNING);
            return false;
        }
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            sceneManager.showAlert("VALIDACIÓN DE DATOS", "Campo Obligatorio",
                    "El nombre del área común no puede estar vacío.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            if (capacidad <= 0) {
                sceneManager.showAlert("VALIDACIÓN DE DATOS", "Valor Inválido",
                        "La capacidad máxima debe ser mayor a 0.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            sceneManager.showAlert("VALIDACIÓN DE DATOS", "Formato Incorrecto",
                    "La capacidad debe ser un entero válido.", Alert.AlertType.WARNING);
            return false;
        }

        try {
            double costo = Double.parseDouble(txtCosto.getText().trim());
            if (costo < 0) {
                sceneManager.showAlert("VALIDACIÓN DE DATOS", "Valor Inválido",
                        "El costo de reserva no puede ser negativo.", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            sceneManager.showAlert("VALIDACIÓN DE DATOS", "Formato Incorrecto",
                    "El costo debe ser un valor numérico decimal.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    @FXML
    private void clearFields() {
        txtNombre.clear();
        txtCapacidad.clear();
        txtCosto.clear();
        cmbCondominio.setValue(null);
        areaSeleccionada = null;
        tblAreasComunes.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleLogin() {
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
    private void handleReturn() {
        sceneManager.showMainMenuView();
    }

    @FXML
    private void handleResidentes() {
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
    private void handleGoToCasaResidente() {
        sceneManager.showCasaResidente();
    }

    @FXML
    private void handleGoToCuota() {
        sceneManager.showCuotaView();
    }

    @FXML
    private void handleGoToReserva() {
        sceneManager.showReservaView();
    }
    
    @FXML
    private void handleGoToPagoView() {
        sceneManager.showPagoView();
    }
}
