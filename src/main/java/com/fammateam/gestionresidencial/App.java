package main.java.com.fammateam.gestionresidencial;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import main.java.com.fammateam.gestionresidencial.util.SceneManager;


public class App extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginView();
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }

}
