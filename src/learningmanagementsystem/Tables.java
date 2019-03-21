package learningmanagementsystem;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.sql.ResultSet;

/**
 * An interfaces for all tables in the database
 *
 * @author Bosco
 * @version 06_mar_2019
 */
public abstract class Tables {
    public static final int HGAP = 5;
    public static final int VGAP = 10;

    // display the result of the search function
    protected GridPane resultPane;

    // tell us that there's an error in user inputs.
    protected boolean inputErrorIndicator;

    // the current pane being displayed to user.
    protected Pane currentPane;

    protected Label userMessage;


    abstract GridPane createAddDashBoard();
    abstract void add(String courseID, String name, String description, int profID);
    abstract void edit(String currentCourseid, String newCourseid, String courseName,
                       String courseDescription, int courseProfId);
    abstract void delete(String courseID);
    abstract GridPane createSearchDashBoard();
    abstract ResultSet search(String colName, String value); // might change to display

    /**
     * Display an error message to user.
     *
     * @param errorMessage a String.
     */
    public void displayErrorMessage(String errorMessage) {
        userMessage.setTextFill(Color.RED);
        userMessage.setText(errorMessage);
    }

    /**
     * Display an successful operation message to user.
     *
     * @param successMessage a String.
     */
    public void displaySuccessMessage(String successMessage) {
        userMessage.setTextFill(Color.GREEN);
        userMessage.setText(successMessage);
    }

    /**
     * Display a notification message to user.
     *
     * @param notificationMessage a String.
     */
    public void displayNotificationMessage(String notificationMessage) {
        userMessage.setTextFill(Color.BLACK);
        userMessage.setText(notificationMessage);
    }


    /**
     * Check if prof id is valid.
     * @param profID a String
     * @return true if valid, else false.
     */
    public boolean checkUserID(int profID){
        int length = Integer.toString(profID).length();
        return length == 2;
    }

    /**
     * Check if course id is valid.
     *
     * @param id a String
     * @return true if valid, else false.
     */
    public boolean checkCourseID(String id) {
        // check for empty String
        if (id == null || id.strip().equals("")) {
            return false;
        }

        // check for length
        if (id.length() != 6) {
            return false;
        }

        // check that it starts with three letters
        for (int i = 0; i < 3; i++) {
            if (!Character.isAlphabetic(id.charAt(i))) {
                return false;
            }
        }

        // check that it ends with three letters.
        for (int i = 3; i < 6; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}


