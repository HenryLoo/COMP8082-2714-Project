package learningmanagementsystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Drive the functions
 */
public class Driver extends Application {

    // The session that'll run when we open the app.
    private Session mySession;

    public void start(Stage primaryStage) {

        // start a session
        mySession = new Session();
        GridPane gui = new LearningSysGUI(mySession);

        // Set up the scene, which is like staging for a commit
        // in git but for JavaFX

        final int appHeight = 600;
        final int appWidth = 900;
        Scene scene = new Scene(gui, appWidth, appHeight);

        // Stage the scene aka show it to user.
        primaryStage.setScene(scene);
        primaryStage.setTitle("Learning System");
        primaryStage.show();
    }

    /**
     * End the session when application closes.
     */
    @Override
    public void stop() {
        mySession.endSession();
    }

    public static void main(String[] args) {
        // start the application when click start
        launch(args);
    }
}
