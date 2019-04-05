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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users extends Tables {

    // the connection to the database
    private Connection myConn;

    // the text fields for each data column in the table.
    private TextField userIdTxtFld;
    private TextField userNameTxtFld;
    private TextField passwordTxtFld;
    private TextField confirmPasswordTxtFld;

    /**
     * Create a Users instance and run the dashboard.
     */
    public Users(Connection newMyConn, Pane newCurrentPane) {
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
        userIdTxtFld = new TextField();
        userNameTxtFld = new TextField();
        passwordTxtFld = new TextField();
        confirmPasswordTxtFld = new TextField();

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
        Label functionTitle = new Label("Please enter the new user's data:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        Button addBtn = new Button("Add User");
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

        Label firstNameLbl = new Label("Username: ");
        Label passwordLbl = new Label("Password: ");
        Label confirmPasswordLbl = new Label("Confirm Password: ");

        firstNameLbl.setFont(Font.font(18));
        passwordLbl.setFont(Font.font(18));
        confirmPasswordLbl.setFont(Font.font(18));

        gp.add(firstNameLbl, 0, 1);
        gp.add(passwordLbl, 0, 2);
        gp.add(confirmPasswordLbl, 0, 3);

        // since the text fields are shared, we have to cleared them first
        initTextfieldsAndUserMessage();

        gp.add(userNameTxtFld, 1, 1);
        gp.add(passwordTxtFld, 1, 2);
        gp.add(confirmPasswordTxtFld, 1, 3);
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
            String userName = userNameTxtFld.getText().trim();
            String password = passwordTxtFld.getText().trim();

            String query = prepareAddQuery(userName, password);
            String message = "User Added!";
            runQueryWithNoReturnValue(query, myConn, message);
        }

    }

    // test all the textfields and return an error message if any
    private String testAllTextFld() {
        String errorMessage = "";

        if (!checkUserName(userNameTxtFld.getText().trim())) {
            errorMessage += markUserNameTxtFldWrong();
        }

        if (!checkPassword(passwordTxtFld.getText().trim())) {
            errorMessage += markPasswordTxtFldWrong();
        }

        if (!passwordTxtFld.getText().trim().equals(confirmPasswordTxtFld.getText().trim())) {
            errorMessage += markConfirmPasswordTxtFldWrong();
        }
        return errorMessage;
    }

    // mark that the userIdTxtFld was wrong
    private String markUserIdTxtFldWrong() {
        inputErrorIndicator = true;
        userNameTxtFld.setStyle("-fx-border-color: red");
        return "The userid must be made an integer. \n";
    }

    // mark that the userNameTxtFld was wrong
    private String markUserNameTxtFldWrong() {
        inputErrorIndicator = true;
        userNameTxtFld.setStyle("-fx-border-color: red");
        return "The username must be made of alphabetical characters. \n";
    }

    // mark the passworddTxtFld was wrong
    private String markPasswordTxtFldWrong() {
        inputErrorIndicator = true;
        passwordTxtFld.setStyle("-fx-border-color: red");
        return "The password must be at least 6 characters long. \n"
                + "It also must have at least: \n"
                + "- One alphabetical character. \n"
                + "- One numerical character. \n";
    }

    // mark the confirmPasswordTxtFld was wrong
    private String markConfirmPasswordTxtFldWrong() {
        inputErrorIndicator = true;
        confirmPasswordTxtFld.setStyle("-fx-border-color: red");
        return "The passwords must match each other\n";
    }


    /**
     * Create the query to data to the Courses table.
     *
     * @param userName a String.
     * @param password a String.
     * @return a SQL String.
     */
    public String prepareAddQuery(String userName, String password) {
        return "INSERT INTO Users (username, password, role, salt) "
                + "VALUES('" + userName + "', '" + password + "', 'student', 'asdo3iq1');";
    }

    /**
     * Create a grid pane containing elements needed to search courses.
     *
     * @return a GridPane with all the text fields.
     */
    public GridPane createSearchDashBoard() {

        Label functionTitle = new Label("Search for Users By userid:");
        functionTitle.setFont(Font.font(22));

        // reset the textfields and userMessage since it's shared resources.
        initTextfieldsAndUserMessage();

        GridPane gp = new GridPane();
        gp.add(functionTitle, 0, 0);
        gp.add(userIdTxtFld, 1, 0);

        Button searchBtn = new Button("Search Users");
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

        String userId = userIdTxtFld.getText().trim();
        if (!checkUserOrItemID(userId)) {
            errorMessage += markUserIdTxtFldWrong();
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);

            // turn off error indicator
            inputErrorIndicator = false;
        } else {
            String tableName = "Users";
            String columnName = "userid";
            ResultSet result = search(tableName, columnName, userId, myConn);
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
        Label firstNameLbl = new Label("Username");
        Label roleLbl = new Label("Role");

        userIdLbl.setFont(Font.font(18));
        firstNameLbl.setFont(Font.font(18));
        roleLbl.setFont(Font.font(18));

        gp.add(userIdLbl, 0, 0);
        gp.add(firstNameLbl, 1, 0);
        gp.add(roleLbl, 2, 0);

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
                Label userNameLbl = new Label(searchResult.getString("username"));
                Label roleLbl = new Label(String.valueOf(searchResult.getString("role")));

                userIdLbl.setFont(Font.font(18));
                userNameLbl.setFont(Font.font(18));
                roleLbl.setFont(Font.font(18));

                gp.add(userIdLbl, 0, i);
                gp.add(userNameLbl, 1, i);
                gp.add(roleLbl, 2, i);

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

                gp.add(editButton, 3, i);
                gp.add(deleteButton, 4, i);
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

        String tableName = "Users";
        String columnName = "userid";
        ResultSet result = search(tableName, columnName, userid, myConn);
        setTextBoxToValueOfResultSet(result);

        Button editBtn = new Button("Edit User");

        // add the current userid into the update button
        editBtn.setId(userid);
        editBtn.setOnAction(this::checkInputForEditingData);
        gp.add(editBtn, 0, 5);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // set textbox to values of the result set from searching the table
    private void setTextBoxToValueOfResultSet(ResultSet result) {
        try {
            result.next();

            // set textboxes to current value of the specified course
            userNameTxtFld.setText(result.getString("username"));

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
            String userId = findButtonId(event);

            String userName = userNameTxtFld.getText().trim();
            String password = passwordTxtFld.getText().trim();

            String query = prepareEditQuery(userId, userName, password);
            String message = "User Updated!";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    /**
     * Prepare an UPDATE query for Courses table.
     * @param userId a String.
     * @param userName a String.
     * @param password a String.
     * @return an edit query as a String.
     */
    public String prepareEditQuery(String userId, String userName, String password) {
        return "UPDATE Users SET username = \"" + userName + "\", password = \"" + password
                + " WHERE userid = '" + userId + "';";
    }

    public void putForDelete(ActionEvent event){
        if (getUserConfirmation()) {
            String userid = findButtonId(event);
            String query = prepareDeleteQuery(userid);
            String message = "User Deleted!";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    /**
     * Prepare a DELETE query based on the uniquePrimaryKeyValue.
     * @param uniquePrimaryKeyValue a String
     * @return a String query.
     */
    public String prepareDeleteQuery(String uniquePrimaryKeyValue) {
        return "DELETE FROM Users WHERE userid = '" + uniquePrimaryKeyValue + "';";
    }

    /**
     * Check if name is valid.
     *
     * @param name a String
     * @return true if name is valid, else false.
     */
    public boolean checkUserName(String name) {
        final int maxLength = 40;
        return name != null && !(name.strip().equals("")) && name.length() <= maxLength;
    }

    /**
     * Check if password is valid.
     *
     * @param password a String
     * @return true if description is valid, else false.
     */
    public boolean checkPassword(String password) {
        final int maxLength = 40;
        return password != null && !(password.strip().equals("")) && password.length() <= maxLength;
    }
}

