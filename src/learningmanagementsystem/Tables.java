package learningmanagementsystem;
import java.util.Scanner;
/**
 * An interfaces for all tables in the database
 *
 * @author Bosco
 * @version 06_mar_2019
 */
public interface Tables {
    Scanner scanner = new Scanner(System.in);
    void runDashboard();
    void add(String name, String id, String description, int profID);
    void update();
    void delete();
    void select(); // might change to display
}
