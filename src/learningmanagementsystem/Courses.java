package learningmanagementsystem;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Courses extends Command implements Tables {

    // the connection to the database
    private Connection myConn;

    private Label errorMessage;

    // the text fields for each data column in the table.
    private TextField courseIdTxtFld;
    private TextField courseNameTxtFld;
    private TextField courseProfTxtFld;
    private TextField courseDescriptionTxtFld;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses(Connection newMyConn) {
        myConn = newMyConn;
        errorMessage = new Label("");
        errorMessage.setFont(Font.font(13));
        errorMessage.setTextFill(Color.RED);
    }

    /**
     * Create a grid pane containing elements needed to add courses.
     * @return a GridPane with all the text fields.
     */
    public GridPane createAddDashBoard() {

        Label functionTitle = new Label("Please enter the new course's data below:");
        Label courseIdLbl = new Label("CourseID: ");
        Label courseNameLbl = new Label("Course Name: ");
        Label courseProfLbl = new Label("Course Professor: ");
        Label courseDescriptionLbl = new Label("Course Description: ");

        functionTitle.setFont(Font.font(22));
        courseIdLbl.setFont(Font.font(18));
        courseNameLbl.setFont(Font.font(18));
        courseProfLbl.setFont(Font.font(18));
        courseDescriptionLbl.setFont(Font.font(18));

        courseIdTxtFld = new TextField();
        courseNameTxtFld = new TextField();
        courseProfTxtFld = new TextField();
        courseDescriptionTxtFld = new TextField();

        GridPane gp = new GridPane();

        gp.add(functionTitle, 0, 0, 2, 1);
        gp.add(courseIdLbl, 0, 1);
        gp.add(courseNameLbl, 0, 2);
        gp.add(courseProfLbl, 0, 3);
        gp.add(courseDescriptionLbl, 0, 4);
        gp.add(errorMessage, 0, 5, 2, 1);

        gp.add(courseIdTxtFld, 1, 1);
        gp.add(courseNameTxtFld, 1, 2);
        gp.add(courseProfTxtFld, 1, 3);
        gp.add(courseDescriptionTxtFld, 1, 4);

        Button saveBtn = new Button("Add Course");
        gp.add(saveBtn, 0, 6);

        final int hGap = 5;
        final int vGap = 10;
        gp.setHgap(hGap);
        gp.setVgap(vGap);

        return gp;
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
        final int maxLength = 40;
        return name != null && !(name.strip().equals("")) && name.length() <= maxLength;
    }

    private boolean checkDescription(String description) {
        final int maxLength = 150;
        return description != null && !(description.strip().equals("")) && description.length() <= maxLength;
    }

    private boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        return length == 2;
    }

}
