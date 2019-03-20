package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
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

public class Courses implements Tables {

    // the connection to the database
    private Connection myConn;

    private Label userMessage;

    // the text fields for each data column in the table.
    private TextField courseIdTxtFld;
    private TextField courseNameTxtFld;
    private TextField courseProfTxtFld;
    private TextField courseDescriptionTxtFld;

    // display the result
    private GridPane resultPane;

    // tell us that there's an error in user inputs.
    private boolean inputErrorIndicator;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses(Connection newMyConn) {
        myConn = newMyConn;
        initTextfieldsAndUserMessage();
        userMessage.setFont(Font.font(13));
        userMessage.setTextFill(Color.RED);
        resultPane = new GridPane();
    }

    /**
     * Initiate the textfields and userMessage to new ones.
     */
    public void initTextfieldsAndUserMessage() {
        courseIdTxtFld = new TextField();
        courseNameTxtFld = new TextField();
        courseProfTxtFld = new TextField();
        courseDescriptionTxtFld = new TextField();

        userMessage = new Label("");
    }

    /**
     * Create a grid pane containing elements needed to add courses.
     * @return a GridPane with all the text fields.
     */
    public GridPane createAddDashBoard() {

        Label functionTitle = new Label("Please enter the new course's data:");
        Label courseIdLbl = new Label("CourseID: ");
        Label courseNameLbl = new Label("Course Name: ");
        Label courseProfLbl = new Label("Course Professor ID: ");
        Label courseDescriptionLbl = new Label("Course Description: ");

        functionTitle.setFont(Font.font(22));
        courseIdLbl.setFont(Font.font(18));
        courseNameLbl.setFont(Font.font(18));
        courseProfLbl.setFont(Font.font(18));
        courseDescriptionLbl.setFont(Font.font(18));

        // since the text fields are shared, we have to cleared them first
        initTextfieldsAndUserMessage();

        GridPane gp = new GridPane();

        gp.add(functionTitle, 0, 0, 2, 1);
        gp.add(courseIdLbl, 0, 1);
        gp.add(courseNameLbl, 0, 2);
        gp.add(courseProfLbl, 0, 3);
        gp.add(courseDescriptionLbl, 0, 4);
        gp.add(userMessage, 0, 5, 2, 1);

        gp.add(courseIdTxtFld, 1, 1);
        gp.add(courseNameTxtFld, 1, 2);
        gp.add(courseProfTxtFld, 1, 3);
        gp.add(courseDescriptionTxtFld, 1, 4);

        Button addBtn = new Button("Add Course");
        addBtn.setOnAction(this::checkInputForAddingData);
        gp.add(addBtn, 0, 6);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // try to add data
    // if there are errors, let users know
    private void checkInputForAddingData(ActionEvent event) {
        // turn off the error indicator
        inputErrorIndicator = false;

        // create an error message for user
        String errorMessage = "";
        int courseProfId = 0;

        String courseId = courseIdTxtFld.getText().trim();
        if (!checkCourseID(courseId)) {
            errorMessage += "Your course id must be six characters long. \n"
                    + "It must start with three letters and end with three digits. \n";
            inputErrorIndicator = true;
            courseIdTxtFld.setStyle("-fx-border-color: red");
        }

        String courseName = courseNameTxtFld.getText().trim();
        if (!checkCourseName(courseName)) {
            errorMessage += "Your course name must be less than 40 characters. \n";
            inputErrorIndicator = true;
            courseIdTxtFld.setStyle("-fx-border-color: red");
        }

        try {
            courseProfId = Integer.parseInt(courseProfTxtFld.getText().trim());
            if (!checkProfID(courseProfId)) {
                errorMessage += "The professor ID doesn't exist. \n";
                throw new Exception();
            }
        } catch (Exception e) {
            inputErrorIndicator = true;
            courseProfTxtFld.setStyle("-fx-border-color: red");
        }

        String courseDescription = courseDescriptionTxtFld.getText().trim();
        if (!checkDescription(courseDescription)) {
            errorMessage += "Your course description must be less than 150 characters. \n";
            inputErrorIndicator = true;
            courseIdTxtFld.setStyle("-fx-border-color: red");
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
        } else {
            add(courseId, courseName, courseDescription, courseProfId);
            displaySuccessMessage("Course Added!");
        }

    }

    /**
     * Display an error message to user.
     * @param errorMessage a String.
     */
    public void displayErrorMessage(String errorMessage) {
        userMessage.setTextFill(Color.RED);
        userMessage.setText(errorMessage);
    }

    /**
     * Display an successful operation message to user.
     * @param successMessage a String.
     */
    public void displaySuccessMessage(String successMessage) {
        userMessage.setTextFill(Color.GREEN);
        userMessage.setText(successMessage);
    }

    /**
     * Display a notification message to user.
     * @param notificationMessage a String.
     */
    public void displayNotificationMessage(String notificationMessage) {
        userMessage.setTextFill(Color.BLACK);
        userMessage.setText(notificationMessage);
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

            // let user know course is added
            userMessage.setTextFill(Color.GREEN);
            userMessage.setText("Course Added.");

            //

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a grid pane containing elements needed to search courses.
     * @return a GridPane with all the textfields.
     */
    public GridPane createSearchDashBoard() {

        Label functionTitle = new Label("Search for Courses By CourseId:");
        functionTitle.setFont(Font.font(22));

        // reset the textfields and userMessage since it's shared resources.
        initTextfieldsAndUserMessage();

        GridPane gp = new GridPane();
        gp.add(functionTitle, 0, 0);
        gp.add(courseIdTxtFld, 1, 0);

        Button searchBtn = new Button("Search Courses");
        searchBtn.setOnAction(this::checkInputForSearchData);
        gp.add(searchBtn, 2, 0);

        gp.add(userMessage, 0, 2, 2, 1);
        gp.add(resultPane, 0, 3, 3, 1);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // try to add data
    // if there are errors, let users know
    private void checkInputForSearchData(ActionEvent event) {
        // turn off the error indicator
        inputErrorIndicator = false;

        // create an error message for user
        String errorMessage = "";

        // consider refactoring
        String courseId = courseIdTxtFld.getText().trim();
        if (!checkCourseID(courseId)) {
            errorMessage += "Your course id must be six characters long. \n"
                    + "It must start with three letters and end with three digits. \n";
            inputErrorIndicator = true;
            courseIdTxtFld.setStyle("-fx-border-color: red");
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
        } else {
            search("courseid", courseId);
        }
    }

    /**
     * Display data in a table with the specified location.
     * @param colName the column name as a String
     * @parem value the value as a String
     */
    @Override
    public void search(String colName, String value) {
        String sql = "SELECT * FROM Courses WHERE " + colName + " = '" + value +"';";

        try {
            Statement newCommand = myConn.createStatement();
            ResultSet result = newCommand.executeQuery(sql);
            displaySearchQueryResult(result);
            newCommand.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display the search query result.
     * @param result a ResultSet.
     */
    public void displaySearchQueryResult(ResultSet result) {
        try {

            if (!result.isBeforeFirst()) {
                displayNotificationMessage("No Result Found.");
                return;
            }

            resultPane.getChildren().setAll(createSearchResultArea(result));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the search result area
     */
    public GridPane createSearchResultArea(ResultSet searchResult) {
        GridPane gp = new GridPane();

        Label courseIdLbl = new Label("CourseID");
        Label courseNameLbl = new Label("Course Name");
        Label courseDescriptionLbl = new Label("Description");
        Label courseProfLbl = new Label("Professor ID");

        courseIdLbl.setFont(Font.font(18));
        courseNameLbl.setFont(Font.font(18));
        courseProfLbl.setFont(Font.font(18));
        courseDescriptionLbl.setFont(Font.font(18));

        gp.add(courseIdLbl, 0, 0);
        gp.add(courseNameLbl, 1, 0);
        gp.add(courseDescriptionLbl, 2, 0);
        gp.add(courseProfLbl, 3, 0);

        appendResultToSearchResultArea(gp, searchResult);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // append the search result to the result area.
    private void appendResultToSearchResultArea(GridPane gp, ResultSet searchResult) {
        try {
            // i start at 1 because it represent the row index after
            // the column name.
            for (int i = 1; searchResult.next(); i++) {
                Label courseIdLbl = new Label(searchResult.getString("courseid"));
                Label courseNameLbl = new Label(searchResult.getString("course_name"));
                Label courseDescriptionLbl = new Label(searchResult.getString("description"));
                Label courseProfLbl = new Label(String.valueOf(searchResult.getInt("profid")));

                courseIdLbl.setFont(Font.font(18));
                courseNameLbl.setFont(Font.font(18));
                courseProfLbl.setFont(Font.font(18));
                courseDescriptionLbl.setFont(Font.font(18));

                gp.add(courseIdLbl, 0, i);
                gp.add(courseNameLbl, 1, i);
                gp.add(courseDescriptionLbl, 2, i);
                gp.add(courseProfLbl, 3, i);

                // equal to update command in sql
                Button updateButton = new Button("Update");

                // equal to delete command in sql
                Button deleteButton = new Button("Delete");

                gp.add(updateButton, 4, i);
                gp.add(deleteButton, 5, i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String courseID, String name, String description, int profID) {


        String sql = "DELETE FROM Courses WHERE('" + courseID + "', '" + name + "', '"
                + description + "', " + profID + ");



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
        if (id.length() != 6) {
            return false;
        }

        // check that it starts with three letters
        for (int i = 0; i < 3; i++){
            if(!Character.isAlphabetic(id.charAt(i))){
                return false;
            }
        }

        // check that it ends with three letters.
        for (int i = 3; i < 6; i++){
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
