package learningmanagementsystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Courses implements Tables {

    @Override
    public void select() {

    }

    @Override
    public void add(String ){


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
        }}

    }

    @Override
    public void update() {

    }
}
