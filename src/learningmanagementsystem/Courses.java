package learningmanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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

    @Override
    public void select() {

    }

    @Override
    public void getAddData(){
        System.out.println("Please enter a course id");
        String courseID = scanner.next();
        System.out.println("Please enter the course name");
        String name = scanner.next();
        System.out.println("Please enter the profID");
        int profID = scanner.nextInt();
        System.out.println("Please enter a description");
        String description = scanner.next();


        if(checkID(courseID) && checkCourseName(name) && checkDescription(description) && checkProfID(profID)) {
            add(courseID,name,description,profID);
        }
    }

    @Override
    public void add(String courseID, String name, String description, int profID){
        Statement newCommand = null;
            String sql = "INSERT INTO Courses VALUES('" + courseID + "', '"+ name + "', '"
                    + description + "', " + profID + ");";
            try {
                newCommand = myConn.createStatement();
                newCommand.executeUpdate(sql);
                newCommand.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void delete() {

        try {

            //remind user to enter the user id they want to delete
            System.out.print("Please enter the id of user to delete:");
            int userid = scanner.nextInt();

            //define the Sql statement
            // String deleteSql = "DELETE FROM login WHERE CourseName ="+CourseName+";";

            //acquire the statement object

//            stt = conn.createStatement();
//
//            //execute the Sql statement
//            stt.executeUpdate(deleteSql);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        }

    }

    @Override
    public void update() {

    }

    private boolean checkID(String id){
        boolean digitTest  = false;
        boolean alphaTest = false;
        for(int i = 0; i < 3; i++){
            if(id!= null && Character.isAlphabetic(id.charAt(i))){
                digitTest = true;
            }
        }
        for(int i = 3; i < 6; i++){
            if(id!= null && Character.isDigit(id.charAt(i))){
                alphaTest = true;
            }
        }
        if (digitTest && alphaTest && id.length() == 6 ){
            return true;
        }else {
            return false;

        }
    }

    private boolean checkCourseName(String name) {
        if (name != null){
            return true;
        }

        return false;
    }

    private boolean checkDescription(String description) {
        if(description != null && description.length() > 150){
            return false;
        }

        return true;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        if(length != 2){
            return false;
        }
        return true;
    }

}
