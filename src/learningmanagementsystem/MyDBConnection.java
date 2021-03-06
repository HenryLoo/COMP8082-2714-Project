/*
 * MyDBConnection.java
 *
 * Created on 2005/01/16, 10:50
 */

package learningmanagementsystem;

import java.sql.*;

/**
 * Base code from NetBeans database connection tutorial
 * @author noniko
 */
public class MyDBConnection {
   
    private Connection myConnection;
    
    /** Creates a new instance of MyDBConnection */
    public MyDBConnection() {

    }

    public void init(){
       try {
           String url = "jdbc:mysql://51.38.19.86:3306/munimoe_LMS?zeroDateTimeBehavior" +
                   "=CONVERT_TO_NULL&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver");
            myConnection=DriverManager.getConnection(url,
                    "munimoe_admin", "a9D4^x1Uy8AV");

           System.out.println("Database connection success!");
        }
        catch (Exception e){
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
        }
    }
    
    
    public Connection getMyConnection(){
        return myConnection;
    }

     
  public void destroy(){
  
    if(myConnection !=null){
    
         try{
               myConnection.close();
               System.out.println("Connection destroyed");
            }
            catch(Exception e){}
        
        
    }
  }
    
}
