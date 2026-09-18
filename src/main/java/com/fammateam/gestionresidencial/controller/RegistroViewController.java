package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import main.java.com.fammateam.gestionresidencial.model.Usuario;
import main.java.com.fammateam.gestionresidencial.service.AuthService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;



public class RegistroViewController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private Label lblMensaje;

    private final AuthService authService;
    private final SceneManager sceneManager;

    //CLAVE DE SEGURIDAD    
    private static final String ADMIN_SECRET_KEY = "ADMIN2026";

    public RegistroViewController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        cmbRol.setItems(FXCollections.observableArrayList("Secretaria", "Administrador"));
        cmbRol.setValue("Secretaria"); 

        cmbRol.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Administrador".equals(newVal)) {
                boolean autorizado = solicitarCodigoAutorizacion();
                if (!autorizado) {
                    cmbRol.setValue("Secretaria"); 
                }
            }
        });
    }

    private boolean solicitarCodigoAutorizacion() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verificación de Seguridad");
        dialog.setHeaderText("Acceso Restringido: Rol Administrador");
        dialog.setContentText("Ingrese el código de autorización:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (ADMIN_SECRET_KEY.equals(result.get().trim())) {
                sceneManager.showAlert("AUTORIZADO", "Código Correcto", "Se ha asignado el rol de Administrador.", AlertType.INFORMATION);
                return true;
            } else {
                sceneManager.showAlert("DENEGADO", "Código Incorrecto", "El código ingresado no es válido.", AlertType.ERROR);
                return false;
            }
        }
        return false;
    }

    @FXML
    private void handleRegistrar(ActionEvent event) {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String rol = cmbRol.getValue();

        if (nombre.isEmpty() || apellido.isEmpty() || username.isEmpty() || password.isEmpty() || rol == null) {
            lblMensaje.setText("Por favor complete todos los campos obligatorios.");
            return;
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellido(apellido);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setRol(rol);

        try {
            if (authService.registerUser(nuevoUsuario, password)) {
                sceneManager.showAlert("ÉXITO", "Registro Completado", "El usuario ha sido creado correctamente.", AlertType.INFORMATION);
                sceneManager.showLoginView();
            } else {
                lblMensaje.setText("No se pudo guardar el usuario.");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al registrar: " + e.getMessage());
        }
    }

    @FXML
    private void handleVolverLogin(ActionEvent event) {
        sceneManager.showLoginView();
    }
}
