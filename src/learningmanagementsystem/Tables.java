package learningmanagementsystem;

import javafx.scene.layout.GridPane;

import java.sql.ResultSet;

/**
 * An interfaces for all tables in the database
 *
 * @author Bosco
 * @version 06_mar_2019
 */
public interface Tables {
    int HGAP = 5;
    int VGAP = 10;
    GridPane createAddDashBoard();
    void add(String courseID, String name, String description, int profID);
    void edit();
    void delete();
    GridPane createSearchDashBoard();
    void search(String colName, String value); // might change to display
}
