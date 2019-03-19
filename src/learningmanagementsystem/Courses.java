package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Courses extends Table {

    // the connection to the database
    private Connection myConn;

    private Label userMessage;

    // the text fields for each data column in the table.
    private TextField courseIdTxtFld;
    private TextField courseNameTxtFld;
    private TextField courseProfTxtFld;
    private TextField courseDescriptionTxtFld;

    // tell us that there's an error in user inputs.
    private boolean inputErrorIndicator;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses(Connection newMyConn) {
        myConn = newMyConn;

        courseIdTxtFld = new TextField();
        courseNameTxtFld = new TextField();
        courseProfTxtFld = new TextField();
        courseDescriptionTxtFld = new TextField();

        userMessage = new Label("");
        userMessage.setFont(Font.font(13));
        userMessage.setTextFill(Color.RED);
    }

    private class AddDashBoard extends GridPane {
        /**
         * Create a grid pane containing elements needed to add courses.
         * @return a GridPane with all the text fields.
         */
        public AddDashBoard() {
            Label functionTitle = new Label("Please enter the new course's data:");
            Label courseIdLbl = new Label("CourseID: ");
            Label courseNameLbl = new Label("Course Name: ");
            Label courseDescriptionLbl = new Label("Course Description: ");
            Label courseProfLbl = new Label("Course Professor ID: ");

            functionTitle.setFont(Font.font(22));
            courseIdLbl.setFont(Font.font(18));
            courseNameLbl.setFont(Font.font(18));
            courseProfLbl.setFont(Font.font(18));
            courseDescriptionLbl.setFont(Font.font(18));

            add(functionTitle, 0, 0, 2, 1);
            add(courseIdLbl, 0, 1);
            add(courseNameLbl, 0, 2);
            add(courseDescriptionLbl, 0, 3);
            add(courseProfLbl, 0, 4);
            add(userMessage, 0, 5, 2, 1);

            add(courseIdTxtFld, 1, 1);
            add(courseNameTxtFld, 1, 2);
            add(courseDescriptionTxtFld, 1, 3);
            add(courseProfTxtFld, 1, 4);

            Button addBtn = new Button("Add Course");
            addBtn.setOnAction(this::checkInputForAddingData);
            add(addBtn, 0, 6);

            final int hGap = 5;
            final int vGap = 10;
            setHgap(hGap);
            setVgap(vGap);
        }

        // try to add data
        // if there are errors, let users know
        private void checkInputForAddingData(ActionEvent event) {
            // turn off the error indicator
            inputErrorIndicator = false;

            // create an error message for user
            String message = "";
            int courseProfId = 0;

            String courseId = courseIdTxtFld.getText().trim();
            if (!checkCourseID(courseId)) {
                message += "Your course id must be six characters long. \n"
                        + "It must start with three letters and end with three digits. \n";
                inputErrorIndicator = true;
                courseIdTxtFld.setStyle("-fx-border-color: red");
            }

            String courseName = courseNameTxtFld.getText().trim();
            if (!checkCourseName(courseName)) {
                message += "Your course name must be less than 40 characters. \n";
                inputErrorIndicator = true;
                courseIdTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                courseProfId = Integer.parseInt(courseProfTxtFld.getText().trim());
                if (!checkProfID(courseProfId)) {
                    message += "The professor ID doesn't exist. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                courseProfTxtFld.setStyle("-fx-border-color: red");
            }

            String courseDescription = courseDescriptionTxtFld.getText().trim();
            if (!checkDescription(courseDescription)) {
                message += "Your course description must be less than 150 characters. \n";
                inputErrorIndicator = true;
                courseIdTxtFld.setStyle("-fx-border-color: red");
            }

            if (inputErrorIndicator) {
                userMessage.setTextFill(Color.RED);
                userMessage.setText(message);
            } else {
                add(courseId, courseName, courseDescription, courseProfId);
            }
        }

        /**
         * Add data to the Courses table.
         * @param courseID
         * @param name
         * @param description
         * @param profID
         */
        public void add(String courseID, String name, String description, int profID){

            String sql = "INSERT INTO Courses VALUES('" + courseID + "', '"+ name + "', '"
                    + description + "', " + profID + ");";

            try {
                Statement newCommand = myConn.createStatement();
                newCommand.executeUpdate(sql);
                newCommand.close();

                // let user know course is added
                userMessage.setTextFill(Color.GREEN);
                userMessage.setText("Course Added.");

                //

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Create a grid pane containing elements needed to search courses.
     * @return a GridPane with all the textfields.
     */
    public GridPane createSearchDashBoard() {

        Label functionTitle = new Label("Search for Courses By CourseId:");
        Label courseIdLbl = new Label("CourseID: ");

        functionTitle.setFont(Font.font(22));
        courseIdLbl.setFont(Font.font(18));

        GridPane gp = new GridPane();

        gp.add(functionTitle, 0, 0, 2, 1);
        gp.add(courseIdLbl, 0, 1);
        gp.add(userMessage, 0, 5, 2, 1);

        gp.add(courseIdTxtFld, 1, 1);

        Button addBtn = new Button("Search Course");
        gp.add(addBtn, 0, 6);

        final int hGap = 5;
        final int vGap = 10;
        gp.setHgap(hGap);
        gp.setVgap(vGap);

        return gp;
    }

    // create the search result area
    private GridPane createSearchResultArea() {
        Label courseIdLbl = new Label("CourseID");
        Label courseNameLbl = new Label("Course Name: ");
        Label courseDescriptionLbl = new Label("Description: ");
        Label courseProfLbl = new Label("Professor ID: ");

        GridPane gp = new GridPane();

        gp.add(courseIdLbl, 0, 0);
        gp.add(courseNameLbl, 1, 0);
        gp.add(courseDescriptionLbl, 2, 0);
        gp.add(courseProfLbl, 3, 0);

        return gp;
    }

    /**
     * Display data in a table with the specified location.
     * @param colName the column name as a String
     * @parem value the value as a String
     */
    public void search(String colName, String value) {
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

    public void delete() {

        try {

            //remind user to enter the user id they want to delete
            System.out.print("Please enter the id of user to delete:");

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

    public void getUpdateData() {
        System.out.println("Please enter the course id: ");




    }

    public void update() {

    }

    /**
     * Check if course id is valid.
     * @param id a String
     * @return true if valid, else false.
     */
    public boolean checkCourseID(String id){
        // check for empty String
        if (id == null || id.strip().equals("")) {
            return false;
        }

        // check for length
        if (id.length() > 6) {
            return false;
        }

        // check that it starts with three letters
        for(int i = 0; i < 3; i++){
            if(!Character.isAlphabetic(id.charAt(i))){
                return false;
            }
        }

        // check that it ends with three letters.
        for(int i = 3; i < 6; i++){
            if(!Character.isDigit(id.charAt(i))){
                return false;
            }
        }

        return true;
    }

    /**
     * Check if course name is valid.
     * @param name a String
     * @return true if name is valid, else false.
     */
    public boolean checkCourseName(String name) {
        final int maxLength = 40;
        return name != null && !(name.strip().equals("")) && name.length() <= maxLength;
    }

    /**
     * Check if course description is valid.
     * @param description a String
     * @return true if description is valid, else false.
     */
    public boolean checkDescription(String description) {
        final int maxLength = 150;
        return description != null && !(description.strip().equals("")) && description.length() <= maxLength;
    }

    /**
     * Check if prof id is valid.
     * @param profID a String
     * @return true if valid, else false.
     */
    public boolean checkProfID(int profID){
        int length = Integer.toString(profID).length();
        return length == 2;
    }

}
