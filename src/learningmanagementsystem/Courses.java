package learningmanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Courses implements Tables {

    @Override
    public void select() {

    }

    @Override
    public void add(String name, String id, String description, int profID){
        try {
            checkCourseName(name);
        } catch(IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        try {
            checkDescription(description);
        } catch (IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        try {
            checkProfID(profID);
        } catch (IllegalArgumentException e){
            System.out.println("Please re enter");
        }
        

    }

    @Override
    public void delete() {
        try {
            //getting connection

            conn = MyDBConnection.getConnection();
            if (conn = null)
                return;

            //remind user to enter the user id they want to delete
            System.out.print("Please enter the id of user to delete:");
            Scanner input;
            input = new Scanner(System.in);
            int userid = input.nextInt();

            //define the Sql statement
            String deleteSql = "DELETE FROM login WHERE userid ="+userid+";";

            //acquire the statement object
            stt = conn.createStatement();

            //execute the Sql statement
            stt.executeUpdate(deleteSql);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                conn.close();
            } catch (Exception e2){
                // TODO: handle exception
            }
        }

    }

    @Override
    public void update() {

    }

    private boolean checkID(String id){

    }

    private boolean checkCourseName(String name) {
        if(name != null){
            return true;
        }
        return false;
    }

    private boolean checkDescription(String description) {
        if(description != null && description.length() > 150){
            throw new IllegalArgumentException("Sorry the description is too long!");
        }
        return true;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        if(length != 2){
            throw new IllegalArgumentException("Your professor id is invalid");
        }
        return true;
    }
}
