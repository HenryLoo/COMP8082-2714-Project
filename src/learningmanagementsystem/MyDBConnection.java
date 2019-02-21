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
       try{
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        myConnection=DriverManager.getConnection(
                "jdbc:mysql://51.38.19.86:3306/munimoe_LMS","munimoe_admin", "a9D4^x1Uy8AV"
                );
        }
        catch(Exception e){
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
        }
       System.out.println("Database connection success!");
    }
    
    
    public Connection getMyConnection(){
        return myConnection;
    }
    
    
    public void close(ResultSet rs){
        
        if(rs !=null){
            try{
               rs.close();
            }
            catch(Exception e){}
        
        }
    }
    
     public void close(java.sql.Statement stmt){
        
        if(stmt !=null){
            try{
               stmt.close();
            }
            catch(Exception e){}
        
        }
    }
     
  public void destroy(){
  
    if(myConnection !=null){
    
         try{
               myConnection.close();
            }
            catch(Exception e){}
        
        
    }
  }
    
}
