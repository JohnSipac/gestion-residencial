package main.java.com.fammateam.gestionresidencial.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.java.com.fammateam.gestionresidencial.controller.LoginViewController;
import main.java.com.fammateam.gestionresidencial.controller.RegistroViewController;
import main.java.com.fammateam.gestionresidencial.repository.UsuarioRepository;
import main.java.com.fammateam.gestionresidencial.service.AuthService;


public class SceneManager {

    private Stage primaryStage;
    private String FXML_PATH = "/main/resources/view/";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    //Metodo para poder sobreescribir el FXMLLoader y el setControllerFactory en cada método
    @FunctionalInterface
    public interface ControllerFactory {
        Object create(Class<?> clazz);
    }

    //MÉTODOS DE CARGA
    private void loadView(String fxmlFile, String title, ControllerFactory customFactory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATH + fxmlFile));

            loader.setControllerFactory(clazz -> {
                Object controller = customFactory.create(clazz);
                if (controller != null) {
                    return controller;
                }
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Error al instanciar el controlador " + clazz.getName() + ": " + e.getMessage(), e);
                }
            });

            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
            showAlert("ERROR DE SISTEMA", "Error de Carga", "No se pudo abrir la vista: " + fxmlFile, Alert.AlertType.ERROR);
        }
    }

    public void showAlert(String header, String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(this.primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //NAVEGACION ENTRE VISTAS
    public void showLoginView() {
        loadView("login-view.fxml", "Gestión Residencial - Inicio de Sesión", clazz -> {
            if (clazz == LoginViewController.class) {
                UsuarioRepository usuarioRepository = new UsuarioRepository();
                AuthService authService = new AuthService(usuarioRepository);
                return new LoginViewController(authService, this);
            }
            return null;
        });
    }

    public void showRegistroView() {
        loadView("registro-view.fxml", "Gestión Residencial - Registro de Usuario", clazz -> {
            if (clazz == RegistroViewController.class) {
                UsuarioRepository usuarioRepository = new UsuarioRepository();
                AuthService authService = new AuthService(usuarioRepository);
                return new RegistroViewController(authService, this);
            }
            return null;
        });
    }

}
