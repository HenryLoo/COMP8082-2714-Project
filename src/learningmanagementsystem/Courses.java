package learningmanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Courses extends Command implements Tables {

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses() throws ExitProgramException{
        try {
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
                accessCMDList(input);

            } catch (ExitProgramException exit) {
                throw exit;
            }
        }

    }

    @Override
    public void select() {

    }

    @Override
    public void add(String name, String id, String description, int profID) {
        if (checkCourseName(name) && checkID(id) && checkDescription(description) && checkProfID(profID)) {
            String command = "INSERT INTO courses VALUES ( '" + name + "', '" + id
                    + "' , '" + description + "'," + profID + ");";

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
            if(id!= null && Character.isDigit(id.charAt(i))){
                digitTest = true;
            }
        }
        for(int i = 3; i < 6; i++){
            if(id!= null && Character.isAlphabetic(i)){
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
        if(length != 6){
            return false;
        }
        return true;
    }

}
