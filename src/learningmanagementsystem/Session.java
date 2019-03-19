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

public class Session {
    private Connection myConnection;

    public Session() {
        super();
        // Create connection and initialize it
        MyDBConnection mdbc = new MyDBConnection();
        mdbc.init();

        // Get database connection
        myConnection = mdbc.getMyConnection();

        // We are now connected to the database.
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    /**
     * Exit the program by ending the session.
     * @return true if program exit successfully, else false.
     */
    public void endSession() {
        try {
            System.out.println("Ending Connection...");
            myConnection.close();
            System.out.println("Goodbye.");
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
