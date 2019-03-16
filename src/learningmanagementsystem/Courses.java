package learningmanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Courses extends Command implements Tables {

    private Connection myConn;
    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses(Connection newMyConn) throws ExitProgramException{
        try {
            myConn = newMyConn;
            runDashboard();
        } catch (ExitProgramException exit) {
            throw exit;
        }
    }

    @Override
    public void runDashboard() throws ExitProgramException {
        while (true) {
            System.out.println("You are now in the Courses Dashboard. \n"
                    + "Depends on your privilege, you can add, update, or delete data. \n"
                    + "Press 'menu' to return to main menu, 'exit' to quit the program.");

            try {
                String input = scanner.next();
                accessCMDList(input, myConn);

            } catch (ExitProgramException exit) {
                throw exit;
            }
        }

    }

    /**
     * Display data in a table with the specified location.
     * @param colName the column name as a String
     * @parem value the value as a String
     */
    @Override
    public void select(String colName, String value) {
        String sql = "SELECT * FROM Courses WHERE " + colName + " = " + value;
        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();
            System.out.println("Your data has been successfully added to Courses. \n"
                    + "Returning to Courses Dashboard...");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get data from user so we can add it to Courses.
     */
    @Override
    public void getAddData(){
        System.out.println("Please enter a course id: ");
        String courseID = scanner.next();
        System.out.println("Please enter the course name: ");
        String name = scanner.next();
        System.out.println("Please enter the profID: ");
        int profID = scanner.nextInt();
        System.out.println("Please enter a description: ");
        String description = scanner.next();


        if (checkCourseID(courseID)
                && checkCourseName(name)
                && checkDescription(description)
                && checkProfID(profID)) {
            add (courseID,name,description,profID);
        }
    }

    /**
     * Add data to the Courses table.
     * @param courseID
     * @param name
     * @param description
     * @param profID
     */
    @Override
    public void add(String courseID, String name, String description, int profID){

        String sql = "INSERT INTO Courses VALUES('" + courseID + "', '"+ name + "', '"
                + description + "', " + profID + ");";

        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();
            System.out.println("Your data has been successfully added to Courses. \n"
                            + "Returning to Courses Dashboard...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String courseID, String name, String description, int profID) {

        String sql = "DELETE FROM Courses WHERE('" + courseID + "', '" + name + "', '"
                + description + "', " + profID + ");";

        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();
            System.out.println("Your data has been successfully deleted to Courses. \n"
                    + "Returning to Courses Dashboard...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getUpdateData() {
        System.out.println("Please enter the course id: ");
        String courseid = scanner.next();

        if (!checkCourseID(courseid)) {
            System.out.println("Your course id format is invalid, please try again.");
            return;
        }


    }

    @Override
    public void update() {

    }

    private boolean checkCourseID(String id){
        if (id == null || id.strip().equals("")) {
            return false;
        }

        if (id.length() > 6) {
            return false;
        }

        for(int i = 0; i < 3; i++){
            if(!(Character.isAlphabetic(id.charAt(i)))){
                return false;
            }
        }

        for(int i = 3; i < 6; i++){
            if(!(Character.isDigit(id.charAt(i)))){
                return false;
            }
        }

        return true;
    }

    private boolean checkCourseName(String name) {
        return name != null && !(name.strip().equals(""));
    }

    private boolean checkDescription(String description) {
        return description != null && !(description.strip().equals("")) && description.length() <= 150;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        return length == 2;
    }

}
