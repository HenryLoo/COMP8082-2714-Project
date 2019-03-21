package learningmanagementsystem;

import javafx.scene.layout.GridPane;

import java.sql.ResultSet;

/**
 * An interfaces for all tables in the database
 *
 * @author Bosco
 * @version 06_mar_2019
 */
public abstract class Tables {
    int HGAP = 5;
    int VGAP = 10;

    abstract GridPane createAddDashBoard();
    abstract void add(String courseID, String name, String description, int profID);
    abstract void edit();
    abstract void delete(String courseID);
    abstract GridPane createSearchDashBoard();
    abstract void search(String colName, String value); // might change to display

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


