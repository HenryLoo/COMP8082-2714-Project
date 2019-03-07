package learningmanagementsystem;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Represent an user session
 * @author Thomas
 * @author Ricky (based on template)
 * @version 4_mar_19
 */

public class Session extends Command{
    private MyDBConnection mdbc;
    private boolean inSession;
    private Connection myConnection;

    private enum TableNames {
        Courses, GradeItems
    }

    public Session() {
        // Create connection and initialize it
        mdbc = new MyDBConnection();
        mdbc.init();

        // Get database connection
        myConnection = mdbc.getMyConnection();
        inSession = true;

    }

    public void runMenu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the Learning System Management. \n");

        while (inSession) {
            System.out.println("Enter the name of the table would you like to use. \n"
                    + "You can choose from the following tables: Courses or GradeItems. \n"
                    + "Type 'exit' to exit");

            String choice = scan.next();

            if (!accessCMDList(choice)) {
                accessTable(choice);
            }
        }
    }
    /*
        Choose which scenario the computer initialize for the user.
     */
    private void accessTable(String choice) {
        TableNames name = TableNames.valueOf(choice);
        switch (name) {
            case Courses:
                promptUserAction(name);
                break;
            case GradeItems:
                promptUserAction(name);
                break;
            default:
                System.out.println("The table name is incorrect. Try again.");
        }
    }

    private void promptUserAction(TableNames tableName) {

    }

    /**
     * Exit the program by ending the session.
     * @return true if program exit successfully, else false.
     */
    protected boolean exitProgram() {
        try {
            inSession = false;
            System.out.println("Program shutting down. Goodbye.");
            myConnection.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
