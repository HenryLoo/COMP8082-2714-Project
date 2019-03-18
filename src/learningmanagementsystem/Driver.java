package learningmanagementsystem;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Drive the functions
 */
public class Driver extends Application {

    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) {
        Session mySession = new Session();
        mySession.runMenu();
    }
}
