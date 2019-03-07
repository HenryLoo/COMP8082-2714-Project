package learningmanagementsystem;

/**
 * An interfaces for all tables in the database
 *
 * @author Bosco
 * @version 06_mar_2019
 */
public interface Tables {
    void runDashboard() throws ExitProgramException;
    void add();
    void update();
    void delete();
    void select(); // might change to display
}
