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


}
