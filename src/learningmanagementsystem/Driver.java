package learningmanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Drive the functions
 */
public class Driver extends Application {
        private MyDBConnection connection;
    public void start(Stage primaryStage) {
        connection = new MyDBConnection();
        connection.init();
        GridPane gui = new LearningSysGUI(connection.getMyConnection());

        // Set up the scene, which is like staging for a commit
        // in git but for JavaFX

        final int appHeight = 600;
        final int appWidth = 1000;
        Scene scene = new Scene(gui, appWidth, appHeight);

        // Stage the scene aka show it to user.
        primaryStage.setScene(scene);
        primaryStage.setTitle("Learning System");
        primaryStage.show();
    }

    /**
     * Destroy the connection when application closes.
     */
    @Override
    public void stop() {
        connection.destroy();
    }

    public static void main(String[] args) {
        // start the application when click start
        launch(args);
    }
}
