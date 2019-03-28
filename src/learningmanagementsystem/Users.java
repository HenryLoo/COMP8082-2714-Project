package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users extends Tables {

    // the connection to the database
    private Connection myConn;

    // the text fields for each data column in the table.
    private TextField firstNameTxtFld;
    private TextField lastNameTxtFld;
    private TextField passwordTxtFld;

    /**
     * Create a Users instance and run the dashboard.
     */
    public Users(Connection newMyConn, Pane newCurrentPane) {
        myConn = newMyConn;
        initTextfieldsAndUserMessage();
        userMessage.setFont(Font.font(13));
        userMessage.setTextFill(Color.RED);
        currentPane = newCurrentPane;
        resultPane = new GridPane();
    }

    /**
     * Initiate the textfields and userMessage to new ones.
     */
    public void initTextfieldsAndUserMessage() {
        firstNameTxtFld = new TextField();
        lastNameTxtFld = new TextField();
        passwordTxtFld = new TextField();

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

        Label firstNameLbl = new Label("User First Name: ");
        Label lastNameLbl = new Label("User Last Name: ");
        Label passwordLbl = new Label("Password: ");

        firstNameLbl.setFont(Font.font(18));
        lastNameLbl.setFont(Font.font(18));
        passwordLbl.setFont(Font.font(18));

        gp.add(firstNameLbl, 0, 1);
        gp.add(lastNameLbl, 0, 2);
        gp.add(passwordLbl, 0, 3);

        // since the text fields are shared, we have to cleared them first
        initTextfieldsAndUserMessage();

        gp.add(firstNameTxtFld, 1, 1);
        gp.add(lastNameTxtFld, 1, 2);
        gp.add(passwordTxtFld, 1, 3);
        gp.add(userMessage, 0, 4, 2, 1);

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
            String firstName = firstNameTxtFld.getText().trim();
            String lastName = lastNameTxtFld.getText().trim();
            String password = passwordTxtFld.getText().trim();

            String query = prepareAddQuery(firstName, lastName, password);
            String message = "User Added!";
            runQueryWithNoReturnValue(query, myConn, message);
        }

    }

    // test all the textfields and return an error message if any
    private String testAllTextFld() {
        String errorMessage = "";

        if (!checkUserID(firstNameTxtFld.getText().trim())) {
            errorMessage += markFirstNameTxtFldWrong();
        }

        if (!checkCourseName(lastNameTxtFld.getText().trim())) {
            errorMessage += markLastNameTxtFldWrong();
        }

        if (!checkUserID(passwordTxtFld.getText().trim())) {
            errorMessage += markPasswordTxtFldWrong();
        }

        return errorMessage;
    }

    // mark that the firstNameTxtFld was wrong
    private String markFirstNameTxtFldWrong() {
        inputErrorIndicator = true;
        firstNameTxtFld.setStyle("-fx-border-color: red");
        return "The user's first name must be made of alphabetical characters. \n";
    }

    // mark the coursenameTxtFld was wrong
    private String markLastNameTxtFldWrong() {
        inputErrorIndicator = true;
        firstNameTxtFld.setStyle("-fx-border-color: red");
        return "The user's last name must be made of alphabetical characters. \n";
    }

    // mark the courseProfidTxtFld was wrong
    private String markPasswordTxtFldWrong() {
        inputErrorIndicator = true;
        passwordTxtFld.setStyle("-fx-border-color: red");
        return "The password must be at least 6 characters long. \n"
                + "It also must have at least: \n"
                + "- One alphabetical character. \n"
                + "- One numerical character. \n";
    }

    /**
     * Create the query to data to the Courses table.
     *
     * @param firstName a String.
     * @param lastName a String.
     * @param password a String.
     * @return a SQL String.
     */
    public String prepareAddQuery(String firstName, String lastName, String password) {
        return "INSERT INTO Users VALUES('" + firstName + "', '" + lastName + "', '"
                + password + "');";
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
        gp.add(firstNameTxtFld, 1, 0);

        Button searchBtn = new Button("Search Courses");
        searchBtn.setOnAction(this::checkInputForSearchData);
        gp.add(searchBtn, 2, 0);

        gp.add(userMessage, 0, 1, 2, 1);
        gp.add(resultPane, 0, 2, 3, 1);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // try to add data
    // if there are errors, let users know
    private void checkInputForSearchData(ActionEvent event) {
        // create an error message for user
        String errorMessage = "";

        String courseId = firstNameTxtFld.getText().trim();
        if (!checkCourseID(courseId)) {
            errorMessage += "Your course id must be six characters long. \n"
                    + "It must start with three letters and end with three digits. \n";
            inputErrorIndicator = true;
            firstNameTxtFld.setStyle("-fx-border-color: red");
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
        Label userIdLbl = new Label("UserID");
        Label firstNameLbl = new Label("First Name");
        Label lastNameLbl = new Label("Last Name");
        Label roleLbl = new Label("Role");

        userIdLbl.setFont(Font.font(18));
        firstNameLbl.setFont(Font.font(18));
        lastNameLbl.setFont(Font.font(18));
        roleLbl.setFont(Font.font(18));

        gp.add(userIdLbl, 0, 0);
        gp.add(firstNameLbl, 1, 0);
        gp.add(lastNameLbl, 2, 0);
        gp.add(roleLbl, 3, 0);

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
                Label userIdLbl = new Label(searchResult.getString("userid"));
                Label firstNameLbl = new Label(searchResult.getString("firstname"));
                Label lastNameLbl = new Label(searchResult.getString("lastname"));
                Label roleLbl = new Label(String.valueOf(searchResult.getInt("role")));

                userIdLbl.setFont(Font.font(18));
                firstNameLbl.setFont(Font.font(18));
                lastNameLbl.setFont(Font.font(18));
                roleLbl.setFont(Font.font(18));

                gp.add(userIdLbl, 0, i);
                gp.add(firstNameLbl, 1, i);
                gp.add(lastNameLbl, 2, i);
                gp.add(roleLbl, 3, i);

                // create an edit button
                Button editButton = createEditButtonWithGraphic();

                // put the userid of the current row into this button's id
                editButton.setId(searchResult.getString("userid"));
                editButton.setOnAction(this::openEditDashBoard);

                // create a delete button
                Button deleteButton = createDeleteButtonWithGraphic();

                // put the userid of the current row into this button's id
                deleteButton.setId(searchResult.getString("userid"));
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
    public GridPane createEditDashBoard(String userid) {
        // create the dashboard based on the template
        GridPane gp = createDashBoardTemplate();

        // create things unique to the add dashboard.
        Label functionTitle = new Label("You can change any of the current data below:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        String tableName = "Courses";
        String columnName = "userid";
        ResultSet result = search(tableName, columnName, userid, myConn);
        setTextBoxToValueOfResultSet(result);

        Button editBtn = new Button("Edit Course");

        // add the current userid into the update button
        editBtn.setId(userid);
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
            firstNameTxtFld.setText(result.getString("courseid"));
            lastNameTxtFld.setText(result.getString("course_name"));
            passwordTxtFld.setText(result.getString("profid"));

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

            String newCourseId = firstNameTxtFld.getText().trim();
            String courseName = lastNameTxtFld.getText().trim();
            String courseDescription = .getText().trim();
            String courseProfId = passwordTxtFld.getText().trim();

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

