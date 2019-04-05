package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Courses extends Tables {

    // the connection to the database
    private Connection myConn;

    // the text fields for each data column in the table.
    private TextField courseIdTxtFld;
    private TextField courseNameTxtFld;
    private TextField courseProfTxtFld;
    private TextField courseDescriptionTxtFld;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Courses(Connection newMyConn, Pane newCurrentPane) {
        myConn = newMyConn;
        initTextfieldsAndUserMessage();
        userMessage.setFont(Font.font(13));
        userMessage.setTextFill(Color.RED);
        currentPane = newCurrentPane;
        resultPane = new ScrollPane();
    }

    /**
     * Initiate the textfields and userMessage to new ones.
     */
    public void initTextfieldsAndUserMessage() {
        courseIdTxtFld = new TextField();
        courseNameTxtFld = new TextField();
        courseProfTxtFld = new TextField();
        courseDescriptionTxtFld = new TextField();

        userMessage = new Label();
        userMessage.setFont(Font.font(15));

    }

    /**
     * Create a grid pane containing elements needed to add courses.
     *
     * @return a GridPane with all the text fields.
     */
    public GridPane createAddDashBoard() {
        // get the generic look of a course info dashboard
        GridPane gp = createDashBoardTemplate();

        // create things unique to the add dashboard.
        Label functionTitle = new Label("Please enter the new course's data:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        Button addBtn = new Button("Add Course");
        addBtn.setOnAction(this::checkInputForAddingData);
        gp.add(addBtn, 0, 6);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    /**
     * Create a dashboard template that can be used by other templates.
     * @return a grid pane containing all the needed elements.
     */
    public GridPane createDashBoardTemplate() {
        GridPane gp = new GridPane();

        Label courseIdLbl = new Label("CourseID: ");
        Label courseNameLbl = new Label("Course Name: ");
        Label courseProfLbl = new Label("Course Professor ID: ");
        Label courseDescriptionLbl = new Label("Course Description: ");

        courseIdLbl.setFont(Font.font(18));
        courseNameLbl.setFont(Font.font(18));
        courseProfLbl.setFont(Font.font(18));
        courseDescriptionLbl.setFont(Font.font(18));

        gp.add(courseIdLbl, 0, 1);
        gp.add(courseNameLbl, 0, 2);
        gp.add(courseProfLbl, 0, 3);
        gp.add(courseDescriptionLbl, 0, 4);

        // since the text fields are shared, we have to cleared them first
        initTextfieldsAndUserMessage();

        gp.add(courseIdTxtFld, 1, 1);
        gp.add(courseNameTxtFld, 1, 2);
        gp.add(courseProfTxtFld, 1, 3);
        gp.add(courseDescriptionTxtFld, 1, 4);
        gp.add(userMessage, 0, 5, 2, 1);

        return gp;

    }

    // try to add data
    // if there are errors, let users know
    private void checkInputForAddingData(ActionEvent event) {
        // create an error message for user
        String errorMessage = testAllTextFld();

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
            // turn off the error indicator
            inputErrorIndicator = false;
        } else {
            String courseId = courseIdTxtFld.getText().trim();
            String courseName = courseNameTxtFld.getText().trim();
            int courseProfId = Integer.parseInt(courseProfTxtFld.getText().trim());
            String courseDescription = courseDescriptionTxtFld.getText().trim();

            String query = prepareAddQuery(courseId, courseName, courseDescription, courseProfId);
            String message = "Course Added!";
            runQueryWithNoReturnValue(query, myConn, message);
        }

    }

    // test all the textfields and return an error message if any
    private String testAllTextFld() {
        String errorMessage = "";

        if (!checkCourseID(courseIdTxtFld.getText().trim())) {
            errorMessage += markCourseIdTxtFldWrong();
        }

        if (!checkCourseName(courseNameTxtFld.getText().trim())) {
            errorMessage += markCourseNameTxtFldWrong();
        }

        if (!checkUserOrItemID(courseProfTxtFld.getText().trim())) {
            errorMessage += markCourseProfIdTxtFldWrong();
        }

        if (!checkDescription(courseDescriptionTxtFld.getText().trim())) {
            errorMessage += markCourseDescriptionTxtFld();
        }

        return errorMessage;
    }

    // mark that the courseIdTxtFld was wrong
    private String markCourseIdTxtFldWrong() {
        inputErrorIndicator = true;
        courseIdTxtFld.setStyle("-fx-border-color: red");
        return "Your course id must be six characters long. \n"
                + "It must start with three letters and end with three digits. \n";
    }

    // mark the coursenameTxtFld was wrong
    private String markCourseNameTxtFldWrong() {
        inputErrorIndicator = true;
        courseIdTxtFld.setStyle("-fx-border-color: red");
        return "Your course name must be less than 40 characters. \n";
    }

    // mark the courseProfidTxtFld was wrong
    private String markCourseProfIdTxtFldWrong() {
        inputErrorIndicator = true;
        courseProfTxtFld.setStyle("-fx-border-color: red");
        return "The professor ID doesn't exist. \n";
    }

    // mark the courseDescriptionTxtFld was wrong
    private String markCourseDescriptionTxtFld() {
        inputErrorIndicator = true;
        courseIdTxtFld.setStyle("-fx-border-color: red");
        return "Your course description must be less than 150 characters. \n";
    }

    /**
     * Create the query to data to the Courses table.
     *
     * @param courseID a String.
     * @param name a String.
     * @param description a String.
     * @param profID an int.
     * @return a SQL String.
     */
    public String prepareAddQuery(String courseID, String name, String description, int profID) {

        return "INSERT INTO Courses VALUES('" + courseID + "', '" + name + "', '"
                + description + "', " + profID + ");";
    }

    /**
     * Create a grid pane containing elements needed to search courses.
     *
     * @return a GridPane with all the text fields.
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

        gp.add(userMessage, 0, 1, 2, 1);
        gp.add(resultPane, 0, 2, 3, 1);

        Button seeAllBtn = new Button("See all courses");
        seeAllBtn.setOnAction(this::displayAllData);
        gp.add(seeAllBtn, 0, 3);

        // automatically displays all data in the table.
        displaySearchQueryResult(search("Courses", myConn));
        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // display all the data in the table
    private void displayAllData(ActionEvent event) {
        displaySearchQueryResult(search("Courses", myConn));
    }

    // try to add data
    // if there are errors, let users know
    private void checkInputForSearchData(ActionEvent event) {
        // create an error message for user
        String errorMessage = "";

        String courseId = courseIdTxtFld.getText().trim();
        if (!checkCourseID(courseId)) {
            errorMessage += "Your course id must be six characters long. \n"
                    + "It must start with three letters and end with three digits. \n";
            inputErrorIndicator = true;
            courseIdTxtFld.setStyle("-fx-border-color: red");
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);

            // turn off error indicator
            inputErrorIndicator = false;
        } else {
            String tableName = "Courses";
            String columnName = "courseid";
            ResultSet result = search(tableName, columnName, courseId, myConn);
            // call a method in the Tables class
            displaySearchQueryResult(result);
        }
    }

    /**
     * Create the search result area.
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

                // create an edit button
                Button editButton = createEditButtonWithGraphic();

                // put the courseid of the current row into this button's id
                editButton.setId(searchResult.getString("courseid"));
                editButton.setOnAction(this::openEditDashBoard);

                // create a delete button
                Button deleteButton = createDeleteButtonWithGraphic();

                // put the courseid of the current row into this button's id
                deleteButton.setId(searchResult.getString("courseid"));
                deleteButton.setOnAction(this::putForDelete);

                gp.add(editButton, 4, i);
                gp.add(deleteButton, 5, i);
            }
            searchResult.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the update dashboard.
     * @param event an ActionEvent.
     */
    public void openEditDashBoard(ActionEvent event) {
        currentPane.getChildren().setAll(createEditDashBoard(findButtonId(event)));
    }

    /**
     * Create an edit dashboard.
     * @return the dashboard as a GridPane.
     */
    @Override
    public GridPane createEditDashBoard(String courseid) {
        // create the dashboard based on the template
        GridPane gp = createDashBoardTemplate();

        // create things unique to the add dashboard.
        Label functionTitle = new Label("You can change any of the current data below:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        String tableName = "Courses";
        String columnName = "courseid";
        ResultSet result = search(tableName,columnName, courseid, myConn);
        setTextBoxToValueOfResultSet(result);

        Button editBtn = new Button("Edit Course");

        // add the current courseid into the update button
        editBtn.setId(courseid);
        editBtn.setOnAction(this::checkInputForEditingData);
        gp.add(editBtn, 0, 6);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // set textbox to values of the result set from searching the table
    private void setTextBoxToValueOfResultSet(ResultSet result) {
        try {
            result.next();

            // set textboxes to current value of the specified course
            courseIdTxtFld.setText(result.getString("courseid"));
            courseNameTxtFld.setText(result.getString("course_name"));
            courseDescriptionTxtFld.setText(result.getString("description"));
            courseProfTxtFld.setText(result.getString("profid"));

            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void checkInputForEditingData(ActionEvent event) {
        String errorMessage = testAllTextFld();

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);

            // turn off the error indicator
            inputErrorIndicator = false;
        } else {
            // get the current course set in the button id of the source
            String currentCourseId = findButtonId(event);

            String newCourseId = courseIdTxtFld.getText().trim();
            String courseName = courseNameTxtFld.getText().trim();
            String courseDescription = courseDescriptionTxtFld.getText().trim();
            String courseProfId = courseProfTxtFld.getText().trim();

            String query = prepareEditQuery(currentCourseId, newCourseId,
                    courseName, courseDescription, courseProfId);
            String message = "Course Updated!";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    /**
     * Prepare an UPDATE query for Courses table.
     * @param currentCourseID a String.
     * @param newCourseID a String.
     * @param courseName a String.
     * @param courseDescription a String.
     * @param courseProfId a String.
     * @return
     */
    public String prepareEditQuery(String currentCourseID, String newCourseID,
                                 String courseName, String courseDescription,
                                 String courseProfId) {
        return "UPDATE Courses SET courseid = \"" + newCourseID + "\", course_name = \""
                + courseName + "\", description = \"" + courseDescription + "\", profid = "
                + courseProfId + " WHERE courseid = '" + currentCourseID + "';";
    }

    public void putForDelete(ActionEvent event){
        if (getUserConfirmation()) {
            String courseid = findButtonId(event);
            String query = prepareDeleteQuery(courseid);
            String message = "Course Deleted!";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    /**
     * Prepare a DELETE query based on the uniquePrimaryKeyValue.
     * @param uniquePrimaryKeyValue a String
     * @return a String query.
     */
    public String prepareDeleteQuery(String uniquePrimaryKeyValue) {
        return "DELETE FROM Courses WHERE courseid = '" + uniquePrimaryKeyValue + "';";
    }

    /**
     * Check if course name is valid.
     *
     * @param name a String
     * @return true if name is valid, else false.
     */
    public boolean checkCourseName(String name) {
        final int maxLength = 40;
        return name != null && !(name.strip().equals("")) && name.length() <= maxLength;
    }

    /**
     * Check if course description is valid.
     *
     * @param description a String
     * @return true if description is valid, else false.
     */
    public boolean checkDescription(String description) {
        final int maxLength = 150;
        return description != null && !(description.strip().equals("")) && description.length() <= maxLength;
    }
}

