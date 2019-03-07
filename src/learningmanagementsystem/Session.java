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

    public Session() {
        super();
        // Create connection and initialize it
        mdbc = new MyDBConnection();
        mdbc.init();

        // Get database connection
        myConnection = mdbc.getMyConnection();
        inSession = true;

    }

    public void runMenu() {
        System.out.println("Welcome to the Learning System Management. \n");

        while (inSession) {
            try {
                System.out.println("Enter the name of the table would you like to use. \n"
                        + "You can choose from the following tables: Courses or GradeItems. \n"
                        + "Type 'exit' to exit");

                String input = scanner.next();

                accessCMDList(input);

            } catch (ExitProgramException exit) {
                endSession();
            }
        }
    }

    /**
     * Exit the program by ending the session.
     * @return true if program exit successfully, else false.
     */
    protected void endSession() {
        try {
            inSession = false;
            System.out.println("Ending Connection...");
            myConnection.close();
            scanner.close();
            System.out.println("Goodbye.");
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
