package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GradeItems extends Tables {

    // the connection to the database
    private Connection myConn;

    // the text fields for each data column in the table.
    private TextField itemIdTxtFld;
    private TextField courseIdTxtFld;
    private TextField itemNameTxtFld;
    private TextField totalMarkTxtFld;
    private TextField weightTxtFld;

    /**
     * Create a Courses instance and run the dashboard.
     */
    public GradeItems(Connection newMyConn, Pane newCurrentPane) {
        myConn = newMyConn;
        initTextfieldsAndUserMessage();
        resultPane = new GridPane();
        currentPane = newCurrentPane;
    }

    /**
     * Initiate the textfields and userMessage to new ones.
     */
    public void initTextfieldsAndUserMessage() {
        itemIdTxtFld = new TextField();
        courseIdTxtFld = new TextField();
        itemNameTxtFld = new TextField();
        totalMarkTxtFld = new TextField();
        weightTxtFld = new TextField();

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
        GridPane gp = createSingleCourseInfoDashBoard();

        // create things unique to the add dashboard.
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
     * Create a single course info dashboard generic for add and update dashboard.
     * @return a grid pane containing all the needed elements.
     */
    public GridPane createSingleCourseInfoDashBoard() {
        GridPane gp = new GridPane();

        Label courseIdLbl = new Label("CourseID: ");
        Label nameLbl = new Label("Item Name: ");
        Label markLbl = new Label("Total Mark: ");
        Label weightLbl = new Label("Item Weight: ");

        courseIdLbl.setFont(Font.font(18));
        nameLbl.setFont(Font.font(18));
        markLbl.setFont(Font.font(18));
        weightLbl.setFont(Font.font(18));

        gp.add(courseIdLbl, 0, 1);
        gp.add(nameLbl, 0, 2);
        gp.add(markLbl, 0, 3);
        gp.add(weightLbl, 0, 4);

        // since the text fields are shared, we have to clear them first
        initTextfieldsAndUserMessage();

        gp.add(courseIdTxtFld, 1, 1);
        gp.add(itemNameTxtFld, 1, 2);
        gp.add(totalMarkTxtFld, 1, 3);
        gp.add(weightTxtFld, 1, 4);
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
            String itemName = itemNameTxtFld.getText().trim();
            int totalMark = Integer.parseInt(totalMarkTxtFld.getText().trim();
            int itemWeight = Integer.parseInt(weightTxtFld.getText().trim());
            add(courseId, itemName, totalMark, itemWeight);

        }
    }

    // test all the textfields and return an error message if any
    private String testAllTextFld() {
        String errorMessage = "";

        if (!checkCourseID(courseIdTxtFld.getText().trim())) {
            errorMessage += markCourseIdTxtFldWrong();
        }

        if (!checkItemName(itemNameTxtFld.getText().trim())) {
            errorMessage += markItemNameTxtFldWrong();
        }

        try {
            if (!checkTotalMarkOrWeight(Integer.parseInt(totalMarkTxtFld.getText().trim()))) {
                // do this so the catch block handles everything
                throw new Exception();
            }
        } catch (Exception e) {
            errorMessage += markTotalMarkTxtFldWrong();
        }

        try {
            if (!checkTotalMarkOrWeight(Integer.parseInt(weightTxtFld.getText().trim()))) {
                // do this so the catch block handles everything
                throw new Exception();
            }
        } catch (Exception e) {
            errorMessage += markWeightTxtFldWrong();
        }

        return errorMessage;
    }

    // mark that the itemIdTxtFld was wrong
    private String markItemIdTxtFldWrong() {
        inputErrorIndicator = true;
        itemIdTxtFld.setStyle("-fx-border-color: red");
        return "Your item id must be six digits long. \n";
    }

    // mark that the courseIdTxtFld was wrong
    private String markCourseIdTxtFldWrong() {
        inputErrorIndicator = true;
        courseIdTxtFld.setStyle("-fx-border-color: red");
        return "Your course id must be six characters long. \n"
                + "It must start with three letters and end with three digits. \n";
    }

    // mark that the itemNameTxtFld was wrong
    private String markItemNameTxtFldWrong() {
        inputErrorIndicator = true;
        itemNameTxtFld.setStyle("-fx-border-color: red");
        return "Your item name must be less than 40 characters. \n";
    }

    // mark that the totalMarkTxtFld was wrong
    private String markTotalMarkTxtFldWrong() {
        inputErrorIndicator = true;
        totalMarkTxtFld.setStyle("-fx-border-color: red");
        return "The total mark must be less than 100 and more than 0. \n";
    }

    // mark that the weightTxtFld was wrong
    private String markWeightTxtFldWrong() {
        inputErrorIndicator = true;
        weightTxtFld.setStyle("-fx-border-color: red");
        return "The weight. \n";
    }

    /**
     * Add data to the GradeItems table.
     *
     * @param courseID a String
     * @param name a String
     * @param totalMark an int
     * @param weight an int
     */
    @Override
    public void add(String courseID, String name, int totalMark, int weight) {

        String sql = "INSERT INTO Courses VALUES('" + courseID + "', '" + name + "', '"
                + description + "', " + profID + ");";

        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();

            displaySuccessMessage("Course Added!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a grid pane containing elements needed to search courses.
     *
     * @return a GridPane with all the text fields.
     */
    public GridPane createSearchDashBoard() {
        Label functionTitle = new Label("Search for Gradeitem By GradeitemId:");
        functionTitle.setFont(Font.font(22));

        // reset the textfields and userMessage since it's shared resources.
        initTextfieldsAndUserMessage();

        GridPane gp = new GridPane();
        gp.add(functionTitle, 0, 0);
        gp.add(courseIdTxtFld, 1, 0);

        Button searchBtn = new Button("Search Gradeitems");
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
        // turn off the error indicator
        inputErrorIndicator = false;

        // create an error message for user
        String errorMessage = "";

        // consider refactoring
        String GradeitemId = GradeitemIdTxtFld.getText().trim();
        if (!checkGradeitemID(GradeitemId)) {
            errorMessage += "Your Gradeitem id must be six characters long. \n"
                    + "It must start with three letters and end with three digits. \n";
            inputErrorIndicator = true;
            GradeitemIdTxtFld.setStyle("-fx-border-color: red");
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
        } else {
            ResultSet result = search("Gradeitemid", GradeitemId);
            displaySearchQueryResult(result);
        }
    }

    /**
     * Display data in a table with the specified location.
     *
     * @param colName the column name as a String
     * @parem value the value as a String
     */
    @Override
    public ResultSet search(String colName, String value) {
        String sql = "SELECT * FROM Gradeitems WHERE " + colName + " = '" + value + "';";

        try {
            Statement newCommand = myConn.createStatement();
            ResultSet result = newCommand.executeQuery(sql);

            // will closed when ResultSet is closed
            newCommand.closeOnCompletion();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Display the search query result.
     *
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
        Label GradeitemIdLbl = new Label("GradeitemID");
        Label GradeitemNameLbl = new Label("Gradeitem Name");
        Label GradeitemDescriptionLbl = new Label("Description");
        Label GradeitemProfLbl = new Label("Professor ID");

        courseIdLbl.setFont(Font.font(18));
        courseNameLbl.setFont(Font.font(18));
        courseProfLbl.setFont(Font.font(18));
        courseDescriptionLbl.setFont(Font.font(18));

        gp.add(GradeitemIdLbl, 0, 0);
        gp.add(GradeitemNameLbl, 1, 0);
        gp.add(GradeitemDescriptionLbl, 2, 0);
        gp.add(GradeitemProfLbl, 3, 0);

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
                Label GradeitemIdLbl = new Label(searchResult.getString("Gradeitemid"));
                Label GradeitemNameLbl = new Label(searchResult.getString("Gradeitem_name"));
                Label GradeitemDescriptionLbl = new Label(searchResult.getString("description"));
                Label GradeitemProfLbl = new Label(String.valueOf(searchResult.getInt("profid")));

                GradeitemIdLbl.setFont(Font.font(18));
                GradeitemNameLbl.setFont(Font.font(18));
                GradeitemProfLbl.setFont(Font.font(18));
                GradeitemDescriptionLbl.setFont(Font.font(18));

                gp.add(GradeitemIdLbl, 0, i);
                gp.add(GradeitemNameLbl, 1, i);
                gp.add(GradeitemDescriptionLbl, 2, i);
                gp.add(GradeitemProfLbl, 3, i);

                // create an edit button
                ImageView editPencil = new ImageView(new Image("img/Pencil-icon.png"));
                Button editButton = new Button();
                editButton.setGraphic(editPencil);
                editButton.setId(searchResult.getString("Gradeitemid"));
                editButton.setOnAction(this::openEditDashBoard);
                editButton.setTooltip(new Tooltip("Edit"));

                // create a delete button
                ImageView deleteSign = new ImageView(new Image("img/delete-1-icon.png"));
                Button deleteButton = new Button();
                deleteButton.setId(searchResult.getString("Gradeitemid"));
                deleteButton.setTooltip(new Tooltip("Delete"));
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
     * Find the button id.
     *
     * @param event an ActionEvent.
     * @return the button id as a String.
     */
    public String findButtonId(ActionEvent event) {
        Button buttonObj = (Button) event.getSource();
        return buttonObj.getId();
    }

    public GridPane createEditDashBoard(String courseid) {
        // create the dashboard based on the template
        GridPane gp = createSingleCourseInfoDashBoard();

        // create things unique to the add dashboard.
        Label functionTitle = new Label("You can change any of the current data below:");
        functionTitle.setFont(Font.font(22));
        gp.add(functionTitle, 0, 0, 2, 1);

        setTextBoxToValueOfResultSet(Gradeitemid);

        Button editBtn = new Button("Edit Gradeitem");

        // add the current courseid into the update button
        editBtn.setId(Gradeitemid);
        editBtn.setOnAction(this::checkInputForEditingData);
        gp.add(editBtn, 0, 6);

        gp.setHgap(HGAP);
        gp.setVgap(VGAP);

        return gp;
    }

    // set textbox to values of the 'select' statement based on
    // the courseid
    private void setTextBoxToValueOfResultSet(String Gradeitemid) {
        try {
            // find the result associated with the courseid passed to this method.
            // courseid is guaranteed to work
            ResultSet result = search("Gradeitemid", Gradeitemid);
            result.next();

            // set textboxes to current value of the specified course
            GradeitemIdTxtFld.setText(result.getString("Gradeitemid"));
            GradeitemNameTxtFld.setText(result.getString("Gradeitem_name"));
            GradeitemDescriptionTxtFld.setText(result.getString("description"));
            GradeitemProfTxtFld.setText(result.getString("profid"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void checkInputForEditingData(ActionEvent event) {
        String errorMessage = "";
        int courseProfId = 0;

        String newCourseId = courseIdTxtFld.getText().trim();
        if (!checkCourseID(newCourseId)) {
            errorMessage += markCourseIdTxtFldWrong();
        }

        String courseName = courseNameTxtFld.getText().trim();
        if (!checkCourseName(courseName)) {
            errorMessage += markCourseNameTxtFldWrong();
        }

        try {
            courseProfId = Integer.parseInt(courseProfTxtFld.getText().trim());
            if (!checkUserID(courseProfId)) {
                // do this so the catch block handles everything
                throw new Exception();
            }
        } catch (Exception e) {
            errorMessage += markCourseProfIdTxtFldWrong();
        }

        String courseDescription = courseDescriptionTxtFld.getText().trim();
        if (!checkDescription(courseDescription)) {
            errorMessage += markCourseDescriptionTxtFld();
        }

        if (inputErrorIndicator) {
            displayErrorMessage(errorMessage);
            // turn off the error indicator
            inputErrorIndicator = false;
        } else {
            String currentCourseId = findButtonId(event);
            edit(currentCourseId, newCourseId, courseName, courseDescription, courseProfId);
        }
    }

    @Override
    public void edit(String currentCourseID, String newCourseID, String courseName, String courseDescription,
                     int courseProfId) {
        String sql = "UPDATE Courses SET courseid = '" + newCourseID + "', course_name = '"
                + courseName + "', description = '" + courseDescription + "', profid = "
                + courseProfId + " WHERE courseid = '" + currentCourseID + "';";

        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();
            displaySuccessMessage("Course Updated!");

            // reset the result pane
            resultPane = new GridPane();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void putForDelete(ActionEvent event){
        delete(findButtonId(event));
        displaySuccessMessage("You successfully deleted the course!");
        System.out.println(findButtonId(event));
    }

    public void delete(String GradeitemID) {
        String sql = "DELETE FROM Gradeitems WHERE Gradeitemid = '" + GradeitemID + "';";

        try {
            Statement newCommand = myConn.createStatement();
            newCommand.executeUpdate(sql);
            newCommand.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if item name is valid.
     * @param itemName a String
     * @return true if valid, false if else
     */
    public boolean checkItemName(String itemName){
        int length = String.valueOf(itemName).length();
        if(length > 40) {
            return false;
        }
        return true;
    }

    /**
     * Check if total mark or weight is valid.
     * @param markOrWeight an int
     * @return true if valid, false if else
     */
    public boolean checkTotalMarkOrWeight(int markOrWeight) {
        if (markOrWeight > 100 || markOrWeight < 0) {
            return false;
        }
        return true;
    }
}


