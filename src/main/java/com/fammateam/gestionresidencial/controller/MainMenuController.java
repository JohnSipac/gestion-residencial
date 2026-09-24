package main.java.com.fammateam.gestionresidencial.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import main.java.com.fammateam.gestionresidencial.model.Usuario;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;
import main.java.com.fammateam.gestionresidencial.util.UserSession;

public class MainMenuController implements Initializable {

    @FXML
    private Label lblUsername;
    @FXML
    private VBox sidebarPane;
    private boolean isExpanded = false;

    private final SceneManager sceneManager;

    public MainMenuController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosUsuario();
    }

    @FXML
    public void backToLogin() {
        boolean confirmado = sceneManager.showConfirmation(
                "Confirmar cierre de sesión",
                "Cerrar Sesión",
                "¿Desea cerrar sesión?"
        );
        if (confirmado) {
            sceneManager.showLoginView();
        }
    }

    private void cargarDatosUsuario() {
        UserSession session = UserSession.getInstance();

        if (session != null && session.getUsuario() != null) {
            Usuario usuario = session.getUsuario();
            lblUsername.setText(usuario.getUsername().toUpperCase());
        } else {
            lblUsername.setText("INVITADO");
        }
    }

    @FXML
    private void toggleSidebar() {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(250),
                new KeyValue(sidebarPane.prefWidthProperty(), isExpanded ? 85 : 210)
        );
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        isExpanded = !isExpanded;
    }

    @FXML
    private void handleCasa() {
        sceneManager.showCasaView();
    }

    @FXML
    private void handleAreaComun() {
        sceneManager.showAreaComunView();
    }

    @FXML
    private void handleCondominio() {
        sceneManager.showCondominioView();
    }

    @FXML
    private void handleResidentes() {
        sceneManager.showResidenteView();
    }

    @FXML
    private void handleCuotas() {
        sceneManager.showCuotaView();
    }

    @FXML
    private void handleCasaResidente() {
        sceneManager.showCasaResidente();
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
