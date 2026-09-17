package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.java.com.fammateam.gestionresidencial.service.AuthService;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;


public class LoginViewController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMensaje;

    private final AuthService authService;
    private final SceneManager sceneManager;

    public LoginViewController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("Por favor, ingrese usuario y contraseña.");
            return;
        }

        if (authService.autenticator(username, password)) {
            lblMensaje.setText("BIENVENIDO");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void handleRegistro(ActionEvent event) {
        sceneManager.showRegistroView();
    }

}
