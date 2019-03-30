package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

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

    /**
     * Run a query that expects no return value (INSERT INTO, UPDATE, and DELETE).
     * @param query a String.
     * @param myConn a Connection.
     * @param successMessage a String
     */
    protected void runQueryWithNoReturnValue(String query, Connection myConn, String successMessage) {
        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(query);
            newCommand.close();
            displaySuccessMessage(successMessage);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Child class must create a search dashboard.
     * @return the dashboard as a GridPane.
     */
    abstract GridPane createSearchDashBoard();

    /**
     * Child class must create a search result area.
     * @return the result area as a GridPane.
     */
    abstract GridPane createSearchResultArea(ResultSet result);

    /**
     * Display data in a table with the specified location.
     *
     * @param tableName a String.
     * @param colName a String.
     * @parem value a String.
     * @param myConn a Connection.
     */
    protected ResultSet search(String tableName, String colName,
                               String value, Connection myConn) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + colName + " = '" + value + "';";
        try {
            Statement newCommand = myConn.createStatement();
            ResultSet result = newCommand.executeQuery(sql);

            // will closed when ResultSet is closed
            newCommand.closeOnCompletion();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Child class must create an edit dashboard.
     * @return the dashboard as a GridPane.
     */
    abstract GridPane createEditDashBoard(String primaryKeyValue);

    /**
     * Confirmation message to user.
     */
    public boolean getUserConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Box");
        alert.setHeaderText("Are you sure you want to delete?");
        alert.setResizable(false);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find the button id.
     *
     * @param event an ActionEvent.
     * @return the button id as a String.
     */
    public String findButtonId(ActionEvent event) {
        Button buttonObj = (Button) event.getSource();
        return buttonObj.getId();
    }

    /**
     * Create a button with the edit graphics.
     * @return an edit Button.
     */
    public Button createEditButtonWithGraphic() {
        ImageView editPencil = new ImageView(new Image("Pencil-icon.png"));
        editPencil.setFitHeight(20);
        editPencil.setPreserveRatio(true);
        editPencil.setSmooth(true);

        Button editButton = new Button();
        editButton.setGraphic(editPencil);
        editButton.setTooltip(new Tooltip("Edit the Row"));
        return editButton;
    }

    /**
     * Create a button with the delete graphics.
     * @return a delete Button.
     */
    public Button createDeleteButtonWithGraphic() {
        ImageView deleteSign = new ImageView(new Image("delete-1-icon.png"));
        deleteSign.setFitHeight(20);
        deleteSign.setPreserveRatio(true);
        deleteSign.setSmooth(true);

        Button delButton = new Button();
        delButton.setGraphic(deleteSign);
        delButton.setTooltip(new Tooltip("Delete the Row"));
        return delButton;
    }

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
     * Display the search query result.
     *
     * @param result a ResultSet.
     */
    public void displaySearchQueryResult(ResultSet result) {
        try {
            // if there are no result
            if (!result.isBeforeFirst()) {
                displayNotificationMessage("No Result Found.");
                resultPane.getChildren().setAll(new GridPane());
                return;
            }

            // clear the userMessage area
            userMessage.setText("");
            resultPane.getChildren().setAll(createSearchResultArea(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if prof or Student id or item id is valid.
     *
     * @param iD a String
     * @return true if valid, else false.
     */
    public boolean checkUserOrItemID(String iD) {
        if (iD.length() > 6) {
            return false;
        }

        try {
            Integer.parseInt(iD);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if grades are valid.
     *
     * @param grade a String
     * @return true if valid, else false.
     */
    public boolean checkGrade(String grade) {
        try {
          Double d = Double.parseDouble(grade);
          if (d < 100 && d > 0){
              Double.parseDouble(grade);
              return true;
          }
        } catch (Exception e) {
            return false;
        }
        return false;
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

    /**
     * Check if item id is valid.
     * @param itemID a String
     * @return true if valid, false if else
     */
    public boolean checkItemID(String itemID) {
        if (itemID.length() > 6) {
            return false;
        }

        try {
            Integer.parseInt(itemID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Capitalize the first letter of a String.
     * @param s must not be null or full of white spaces.
     * @return s but capitalize.
     */
    public static String capitalizeFirstLetter(String s) {
        s = s.trim();
        String firstLetter = s.substring(0, 1).toUpperCase();
        String remainingLetters = s.substring(1).toLowerCase();
        s = firstLetter + remainingLetters;

        return s;
    }


}


