package learningmanagementsystem;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Code of updating course using CLI
 * @author Thomas
 * @author Ricky (based on template)
 * @version 4_mar_19
 */

public class Session {
    private MyDBConnection mdbc;
    private boolean inSession;
    private Connection myConnection;
    private Commands commandList;

    private enum TableNames {
        Courses, GradeItems
    }

    private enum CommandsChoices {
        add, update, delete, exit
    }

    public Session() {
        // Create connection and initialize it
        mdbc = new MyDBConnection();
        mdbc.init();

        // Get database connection
        myConnection = mdbc.getMyConnection();
        inSession = true;
        commandList = new Commands();
    }

    public void runMenu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to the Learning System Management. \n");

        while (inSession) {
            System.out.println("Enter the name of the table would you like to use. \n"
                    + "You can choose from the following tables: Courses or GradeItems. \n"
                    + "Type 'exit' to exit");

            String choice = scan.next();

            if (choice.equals("exit")) {
                commandList.accessCMDList(choice);
            } else {
                accessTable(choice);
            }
        }
    }
    /*
        Choose which scenario the computer initialize for the user.
     */
    private void accessTable(String choice) {

        try {
            TableNames name = TableNames.valueOf(choice);
            switch (name) {
                case Courses:
                    promptUserAction(name);
                    break;
                case GradeItems:
                    promptUserAction(name);
                    break;
            }
        } catch (Exception e) {
            System.out.println("This table doesn't exist. Please retry");
        }

    }

    private void promptUserAction(TableNames tableName) {

    }

    public ResultSet executeQuery(Connection db, String query) {

        ResultSet rs=null;

        try{
            // Create statement
            Statement stmt = db.createStatement();

            // Execute query on DB
            rs=stmt.executeQuery(query);

        }
        catch(SQLException e){}

        return rs;
    }

    private void endSession() {
        try {
            myConnection.close();
        } catch (SQLException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private class Commands {
        public void accessCMDList(String choice) {
            try {
                CommandsChoices cmd = CommandsChoices.valueOf(choice);
                switch (cmd) {
                    case add:
                        runCommand(cmd);
                        break;
                    case update:
                        runCommand(cmd);
                        break;
                    case delete:
                        runCommand(cmd);
                        break;
                    case exit:
                        exitProgram();
                        break;
                }
            } catch (Exception e) {
                System.out.println("This command doesn't exist. Please retry");
            }
        }

        // Holder for now
        public void runCommand(CommandsChoices cmd) {
            System.out.println("Please come back later");
        }

        private void exitProgram() {
            inSession = false;
            System.out.println("Program shutting down. Goodbye.");
            endSession();
        }
    }
}
