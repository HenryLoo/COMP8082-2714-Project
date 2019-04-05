package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Grades extends Tables {

    // the connection to the database
    private Connection myConn;

    // the text fields for each data column in the table.
    private TextField itemIdTxtFld;
    private TextField stuIdTextFld;
    private TextField gradeTxtFld;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public Grades(Connection newMyConn, Pane newCurrentPane) {
        myConn = newMyConn;
        initTextfieldsAndUserMessage();
        resultPane = new ScrollPane();
        currentPane = newCurrentPane;
    }

    /**
     * Initiate the textfields and userMessage to new ones.
     */
    public void initTextfieldsAndUserMessage() {
        itemIdTxtFld = new TextField();
        stuIdTextFld = new TextField();
        gradeTxtFld = new TextField();

        userMessage = new Label("");
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

        // create the dashboard title
        Label functionTitle = new Label("Please enter the new grade item's data:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        Button addBtn = new Button("Add GradeItem");
        addBtn.setOnAction(this::checkInputForAddingData);
        gp.add(addBtn, 0, 6);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    /**
     * Create a dashboard template that can be used by other dashboard.
     * @return a grid pane containing all the needed elements.
     */
    public GridPane createDashBoardTemplate() {
        GridPane gp = new GridPane();

        Label studentLbl = new Label("Student ID: ");
        Label itemLbl = new Label("Item ID: ");
        Label markLbl = new Label("Mark: ");

        studentLbl.setFont(Font.font(18));
        itemLbl.setFont(Font.font(18));
        markLbl.setFont(Font.font(18));

        gp.add(studentLbl, 0, 1);
        gp.add(itemLbl, 0, 2);
        gp.add(markLbl, 0, 3);

        // since the text fields are shared, we have to clear them first
        initTextfieldsAndUserMessage();

        gp.add(stuIdTextFld, 1, 1);
        gp.add(itemIdTxtFld, 1, 2);
        gp.add(gradeTxtFld, 1,3);
        gp.add(userMessage, 0, 5, 2, 1);

        return gp;
    }

    // check input before adding data
    private void checkInputForAddingData(ActionEvent event) {
        // create an error message for user
        String errorMessage = testAllTextFld();

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
            // turn off the error indicator
            inputErrorIndicator = false;
        } else {
            int stuid = Integer.parseInt(stuIdTextFld.getText().trim());
            int itemId = Integer.parseInt(itemIdTxtFld.getText().trim());
            double grade = Double.parseDouble(gradeTxtFld.getText().trim());

            String query = prepareAddQuery(stuid, itemId, grade);
            String message = "Item Added!";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    // test all the textfields and return an error message if any
    private String testAllTextFld() {
        String errorMessage = "";

        if (!checkUserOrItemID(stuIdTextFld.getText().trim())) {
            errorMessage += markStuIdtxtFldWrong();
        }

        if (!checkGrade(gradeTxtFld.getText().trim())) {
            errorMessage += markGradeTxtFldWrong();
        }

        if (!checkUserOrItemID(itemIdTxtFld.getText().trim())) {
            errorMessage += markItemIdTxtFldWrong();
        }
//
//        if (!checkTotalMarkOrWeight(weightTxtFld.getText().trim())) {
//            errorMessage += markWeightTxtFldWrong();
//        }

        return errorMessage;
    }

    // mark that the itemIdTxtFld was wrong
    private String markItemIdTxtFldWrong() {
        inputErrorIndicator = true;
        itemIdTxtFld.setStyle("-fx-border-color: red");
        return "Your item id must be six digits long. \n";
    }

    // mark that the stuIdTextFld was wrong
    private String markStuIdtxtFldWrong() {
        inputErrorIndicator = true;
        stuIdTextFld.setStyle("-fx-border-color: red");
        return "Your student id is wrong!\n";
    }

    // mark that the gradeTxtFld was wrong
    private String markGradeTxtFldWrong() {
        inputErrorIndicator = true;
        gradeTxtFld.setStyle("-fx-border-color: red");
        return "Your grade must be between 0 and 100 \n";
    }

//    // mark that the totalMarkTxtFld was wrong
//    private String markTotalMarkTxtFldWrong() {
//        inputErrorIndicator = true;
//        totalMarkTxtFld.setStyle("-fx-border-color: red");
//        return "The total mark must be between 0 and 100 inclusively. \n";
//    }
//
//    // mark that the weightTxtFld was wrong
//    private String markWeightTxtFldWrong() {
//        inputErrorIndicator = true;
//        weightTxtFld.setStyle("-fx-border-color: red");
//        return "The weight must be between 0 and 100 inclusively. \n";
//    }

    /**
     * Add data to the GradeItems table.
     *
     * @param stuid an int
     * @param itemid an int
     * @param grade a double
     */
    public String prepareAddQuery(int stuid, int itemid, double grade) {
        return "INSERT INTO StuGrades(stuid, itemid, grade) VALUES('" + stuid + "', '" + itemid + "', '"
                + grade + "');";
    }

    /**
     * Create a grid pane containing elements needed to search courses.
     *
     * @return a GridPane with all the text fields.
     */
    public GridPane createSearchDashBoard() {
        Label functionTitle = new Label("Search for Grade By stuid:");
        functionTitle.setFont(Font.font(22));

        // reset the textfields and userMessage since it's shared resources.
        initTextfieldsAndUserMessage();

        GridPane gp = new GridPane();
        gp.add(functionTitle, 0, 0);
        gp.add(itemIdTxtFld, 1, 0);

        Button searchBtn = new Button("Search Items");
        searchBtn.setOnAction(this::checkInputForSearchData);
        gp.add(searchBtn, 2, 0);

        gp.add(userMessage, 0, 1, 2, 1);
        gp.add(resultPane, 0, 2, 3, 1);

        Button seeAllBtn = new Button("See all grades");
        seeAllBtn.setOnAction(this::displayAllData);
        gp.add(seeAllBtn, 0, 3);

        // automatically displays all data in the table.
        displaySearchQueryResult(search("StuGrades", myConn));

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // display all the data in the table
    private void displayAllData(ActionEvent event) {
        displaySearchQueryResult(search("StuGrades", myConn));
    }

    // check inputs before searching for data
    private void checkInputForSearchData(ActionEvent event) {
        // create an error message for user
        String errorMessage = "";

        String itemid = itemIdTxtFld.getText().trim();
        if (!checkUserOrItemID(itemid)) {
            errorMessage += markItemIdTxtFldWrong();
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);

            // turn error indicator off
            inputErrorIndicator = false;
        } else {
            String tableName = "StuGrades";
            String columnName = "stuid";
            ResultSet result = search(tableName, columnName, itemid, myConn);

            // call a method in the Tables class
            displaySearchQueryResult(result);
        }
    }

    /**
     * Create the search result area.
     */
    @Override
    public GridPane createSearchResultArea(ResultSet searchResult) {
        GridPane gp = new GridPane();
        Label studentid = new Label("Student Id");
        Label itemid = new Label("Item Name");
        Label grade = new Label("Grade");

        studentid.setFont(Font.font(18));
        itemid.setFont(Font.font(18));
        grade.setFont(Font.font(18));

        gp.add(studentid, 0, 0);
        gp.add(itemid, 1, 0);
        gp.add(grade, 2, 0);

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
                Label stuidLbl = new Label(searchResult.getString("stuid"));
                Label itemNamelbl = new Label(searchResult.getString("itemid"));
                Label gradeNameLbl = new Label(searchResult.getString("Grade"));

                stuidLbl.setFont(Font.font(18));
                itemNamelbl.setFont(Font.font(18));
                gradeNameLbl.setFont(Font.font(18));

                gp.add(stuidLbl, 0, i);
                gp.add(itemNamelbl, 1, i);
                gp.add(gradeNameLbl, 2, i);

                // create an edit button
                Button editButton = createEditButtonWithGraphic();

                // put the itemid of the current row into this button's id
                editButton.setId(searchResult.getString("itemid"));
                editButton.setOnAction(this::openEditDashBoard);

                // create a delete button
                Button deleteButton = createDeleteButtonWithGraphic();

                // put the itemid of the current row into this button's id
                deleteButton.setId(searchResult.getString("itemid"));
                deleteButton.setOnAction(this::handleDeleteEvent);

                gp.add(editButton, 5, i);
                gp.add(deleteButton, 6, i);
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
    public GridPane createEditDashBoard(String itemid) {
        // create the dashboard based on the template
        GridPane gp = createDashBoardTemplate();

        // create the dashboard title
        Label functionTitle = new Label("You can change any of the current data below:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        // set the text values of the text fields to the search result
        String tableName = "GradeItems";
        String columnName = "stuid";
        ResultSet result = search(tableName, columnName, itemid, myConn);
        setTextBoxToValueOfResultSet(result);

        Button editBtn = new Button("Edit Item");

        // add the current itemid into the edit button
        editBtn.setId(itemid);
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
            itemIdTxtFld.setText(result.getString("itemid"));
            stuIdTextFld.setText(result.getString("stuid"));
            gradeTxtFld.setText(result.getString("grade"));

            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // check that the input are correct before updating the table
    private void checkInputForEditingData(ActionEvent event) {
        String errorMessage = testAllTextFld();

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
            // turn off the error indicator
            inputErrorIndicator = false;
        } else {
            String currentItemId = findButtonId(event);
            int newItemId = Integer.parseInt(itemIdTxtFld.getText().trim());
            String stuId = stuIdTextFld.getText().trim();
            int grade = Integer.parseInt(gradeTxtFld.getText().trim());

            String query = prepareEditQuery(stuId,newItemId,grade);
            String message = "Grade inserted";
            runQueryWithNoReturnValue(query, myConn, message);
        }
    }

    /**
     * Prepare an UPDATE query for GradeItems table.
     * @param stuId a String.
     * @param itemId an int.
     * @param grade an int.
     */
    public String prepareEditQuery(String stuId, int itemId,
                                    int grade) {
        return "UPDATE StuGrades SET grade = \'" + grade + "\'  WHERE studId = " + stuId + " AND itemid = \'" + itemId + "\' ;";
    }

    /**
     * Handle a delete event.
     * @param event an ActionEvent.
     */
    public void handleDeleteEvent(ActionEvent event){
        if (getUserConfirmation()) {
            String stuid = findButtonId(event);
            int itemid = Integer.parseInt(itemIdTxtFld.getText());
            String query = prepareDeleteQuery(stuid,itemid);
            String message = "Item Deleted!";
            runQueryWithNoReturnValue(query, myConn, message);

            // clear the resultPane and display notification
            resultPane.setContent(new GridPane());
            displayNotificationMessage("");
        }
    }

    /**
     * Prepare a DELETE query based on the uniquePrimaryKeyValue and secondPrimnaryKey.
     * @param uniquePrimaryKeyValue a String.
     * @param secondPrimaryKeyValue an int.
     * @return a String query.
     */
    public String prepareDeleteQuery(String uniquePrimaryKeyValue, int secondPrimaryKeyValue) {
        return "DELETE FROM StuGrades WHERE stuid = '" + uniquePrimaryKeyValue + "' AND itemid = \'"
                + secondPrimaryKeyValue + "\' ;";
    }
}
