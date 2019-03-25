package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.*;

public class LearningSysGUI extends GridPane {

    public static final int HGAP = 5;
    public static final int VGAP = 10;

    // The connection that connects the GUI to the database.
    private Connection databaseConnection;

    // The page we are displaying to the user
    private GridPane currentPage;


    public LearningSysGUI(Connection newDatabaseConnection) {
        databaseConnection = newDatabaseConnection;
        currentPage = new SignInPage();

        add(currentPage, 0, 0);

        setHgap(HGAP);
        setVgap(VGAP);
    }

    private class SignInPage extends GridPane {
        public SignInPage() {
            Text title = new Text("Log In");
            Text usernameTxt = new Text("Username:");
            Text passwordTxt = new Text("Password:");

            title.setFont(Font.font(30));
            usernameTxt.setFont(Font.font(22));
            passwordTxt.setFont(Font.font(22));

            TextField usernameTxtFld = new TextField();
            TextField passwordTxtFld = new TextField();

            Button signInBtn = new Button("Sign In");
            signInBtn.setOnAction(this::signIn);

            add(title, 0, 0 , 2, 1);
            add(usernameTxt, 0, 2);
            add(passwordTxt, 0, 3);

            add(usernameTxtFld, 1, 2);
            add(passwordTxtFld, 1, 3);

            add(signInBtn, 0, 4);
            setHgap(HGAP);
            setVgap(VGAP);

        }

        // check if user credential are correct and change page
        private void signIn(ActionEvent event) {
            // for testing purpose, only changing page for now.
            currentPage.getChildren().setAll(new HomePage());
        }
    }

    private class HomePage extends GridPane{

        // The bar that displays table functions.
        private HBox functionOptions;

        // The current pane being displayed.
        private Pane currentPane;

        // The current table that user is looking at.
        private Tables currentTable;

        public HomePage() {
            functionOptions = new HBox();
            currentPane = new GridPane();

            add(createTableNameBar(), 0, 0);
            add(functionOptions, 0, 1);
            add(currentPane, 0, 2);

            setHgap(HGAP);
            setVgap(VGAP);
        }

        /**
         * Create the buttons at the top of the program
         */
        public HBox createTableNameBar() {
            // create each button individually
            Button courseTable = createCoursesButton();
            Button gradeItemTable = createGradeItemsButton();

            final int gap = 10;
            HBox hbox = new HBox(courseTable, gradeItemTable);
            hbox.setSpacing(gap);

            return hbox;
        }

        private Button createCoursesButton() {
            Button myButton = new Button("Courses");

            Tooltip tip = new Tooltip("Access the school's courses");
            myButton.setTooltip(tip);

            // all table name buttons will have the same onAction.
            myButton.setOnAction(this::createFunctionOptions);
            return myButton;
        }

        private Button createGradeItemsButton() {
            Button myButton = new Button("Grade Items");

            Tooltip tip = new Tooltip("Access the school's grade items");
            myButton.setTooltip(tip);

            // all table name buttons will have the same onAction.
            myButton.setOnAction(this::createFunctionOptions);
            return myButton;
        }

        // when a table is chosen, displays the functions
        private void createFunctionOptions(ActionEvent event) {
            // find which button was clicked
            String buttonName = findButtonText(event);

            // set the currentTable instance variable to the correct table.
            setCurrentTable(buttonName);

            // open the search dashboard
            openSearchDashBoard();

            // create an add button
            Button addButton = new Button("Add " + buttonName);
            addButton.setOnAction(this::openAddDashBoard);
            addButton.setTooltip(new Tooltip("Add New Data"));

            final int gap = 5;
            HBox hbox = new HBox(addButton);
            hbox.setSpacing(gap);

            // get the functionOptions that's currently empty and set it to hbox.
            functionOptions.getChildren().setAll(hbox);
        }

        private VBox greetingPane() {
            Text welcome = new Text("Welcome to the Learning System!");
            Text subtext = new Text("Choose a table above to get started.");

            welcome.setFont(Font.font(40));
            subtext.setFont(Font.font(30));

            VBox vbox = new VBox(welcome, subtext);
            vbox.setAlignment(Pos.CENTER);
            return vbox;
        }

        /**
         * Find the button text based on the event created by clicking on that button.
         *
         * @param event an ActionEvent.
         * @return the name of the button as a String.
         */
        public String findButtonText(ActionEvent event) {
            // to check why this work, you can use System.out.println(event.getSource()).
            Button buttonObj = (Button) event.getSource();
            return buttonObj.getText();
        }

        /**
         * Set the current table to a new instance of that table class.
         *
         * @param tableName a String
         */
        public void setCurrentTable(String tableName) {
            switch (tableName) {
                case "Courses":
                    currentTable = new Courses(databaseConnection, currentPane);
                    break;
                case "Grade Items":
                    currentTable = new GradeItems(databaseConnection, currentPane);
                    break;
                default:
                    System.out.println("There's no table with that name");
            }
        }

        /**
         * Open the add dashboard.
         *
         * @param event an Action event.
         */
        public void openAddDashBoard(ActionEvent event) {
            currentPane.getChildren().setAll(currentTable.createAddDashBoard());
        }

        /**
         * Open the search dashboard.
         */
        public void openSearchDashBoard() {
            currentPane.getChildren().setAll(currentTable.createSearchDashBoard());
        }


    }
}

