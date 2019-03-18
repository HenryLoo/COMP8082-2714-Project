package learningmanagementsystem;
import java.util.Scanner;
import java.sql.*;

public class Command {
    /*
        Declare all the commands available in the program.
     */
    private enum CommandsChoices {
        add, update, delete, exit, menu, access
    }

    /*
        Declare all the tables available in the database.
     */
    private enum TableNames {
        Courses, GradeItems
    }

    Scanner scanner;

    public Command() {
        scanner = new Scanner(System.in);
    }

    /**
     * Test whether the parameter is a command.
     * If it is, run appropriate action.
     *
     * @param input a String.
     */
    public void accessCMDList(String input, Connection myConn) {
        try {
            CommandsChoices cmd = CommandsChoices.valueOf(input);
            switch (cmd) {
                case add:
                    getAddData();
                    break;
                case update:
                    getUpdateData();
                    break;
                case delete:
                    runCommand(cmd);
                    break;
                case access:
                    String tableName = scanner.next();
                    accessTable(tableName, myConn);
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("The command syntax is incorrect. Please try again.");
        }
    }

    /*
        Choose which table dashboard to initialize for the user.
     */
    private void accessTable(String choice, Connection myConn) {
        try {
            TableNames name = TableNames.valueOf(choice);
            switch (name) {
                case Courses:
                    new Courses(myConn);
                    break;
            }

        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("This table does not exist. Try again");
        }

    }

    // Holder for now
    public void runCommand(CommandsChoices cmd) {
        System.out.println("Please come back later");
    }

    /**
     * Get add data from user.
     * This is different for each user.
     */
    public void getAddData() {
        System.out.println("Please enter data to add to the table.");
    }

    public void getUpdateData() {
        System.out.println("Please enter data so we can update the course table.");
    }

}
