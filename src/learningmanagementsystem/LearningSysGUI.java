package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        currentPage = new SignInPage(databaseConnection);

        add(currentPage, 0, 0);

        setHgap(HGAP);
        setVgap(VGAP);
    }

    private class SignInPage extends GridPane {
        private TextField usernameTxtFld;
        private TextField passwordTxtFld;
        private Label errorMessage;

        private Connection myConn;

        public SignInPage(Connection myConn) {
            this.myConn = myConn;
            errorMessage = new Label();

            Text title = new Text("Log In");
            Text usernameTxt = new Text("Username:");
            Text passwordTxt = new Text("Password:");

            title.setFont(Font.font(30));
            usernameTxt.setFont(Font.font(22));
            passwordTxt.setFont(Font.font(22));

            usernameTxtFld = new TextField();
            passwordTxtFld = new TextField();

            Button signInBtn = new Button("Sign In");
            signInBtn.setOnAction(this::signIn);
            signInBtn.setTooltip(new Tooltip("Click here to go to dashboard."));

            add(title, 0, 0 , 2, 1);
            add(usernameTxt, 0, 2);
            add(passwordTxt, 0, 3);

            add(usernameTxtFld, 1, 2);
            add(passwordTxtFld, 1, 3);

            add(errorMessage, 0, 4);

            add(signInBtn, 0, 5);
            setHgap(HGAP);
            setVgap(VGAP);

        }

        // check if user credential are correct and change page
        private void signIn(ActionEvent event) {
            String username = usernameTxtFld.getText();
            String password = passwordTxtFld.getText();
            String message = "";

            if (username.isEmpty()) {
                message = markUserNameTxtFldWrong();
            }

            if (password.isEmpty()) {
                message += markPasswordTxtFldWrong();
            }

            if (!message.isEmpty()) {
                errorMessage.setText(message);
                return;
            }

            if (searchForUser(username, password)) {
                currentPage.getChildren().setAll(new HomePage("admin"));
            } else {
                errorMessage.setText("Your username and password pair are incorrect");
            }

        }

        // search if user is in the Users table
        private boolean searchForUser(String name, String password) {
            String sql = "SELECT * FROM Users WHERE username='" + name + "' AND password='" + password + "';";
            try {
                Statement newCommand = myConn.createStatement();
                ResultSet result = newCommand.executeQuery(sql);

                boolean answer = result.next();
                newCommand.close();
                return answer;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        // mark that the userNameTxtFld was wrong
        private String markUserNameTxtFldWrong() {
            usernameTxtFld.setStyle("-fx-border-color: red");
            return "The username must be made of alphabetical characters only. \n";
        }

        // mark the passworddTxtFld was wrong
        private String markPasswordTxtFldWrong() {
            passwordTxtFld.setStyle("-fx-border-color: red");
            return "The password must be at least 6 characters long. \n";
        }
    }

    private class HomePage extends GridPane{

        // The bar that displays table functions.
        private HBox functionOptions;

        // The current pane being displayed.
        private Pane currentPane;

        // The current table that user is looking at.
        private Tables currentTable;

        // The user role;
        private String userRole;

        public HomePage(String userRole) {
            functionOptions = new HBox();
            currentPane = new GridPane();
            this.userRole = userRole;

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
            Button courseTable = createNameButton("Courses", "Access the school's courses");
            Button gradeItemTable = createNameButton("Grade Items","Access the school's grade items");
            Button gradesTable = createNameButton("Grades","Access the students' grades");
            Button userTable = createNameButton("Users","Access the school's user");

            HBox hbox = new HBox(courseTable, gradeItemTable, gradesTable, userTable);
            hbox.setSpacing(HGAP);

            return hbox;
        }

        private Button createNameButton(String name, String tooltip) {
            Button myButton = new Button(name);
            myButton.setTooltip(new Tooltip(tooltip));

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
                case "Grades":
                    currentTable = new Grades(databaseConnection, currentPane);
                    break;
                case "Users":
                    currentTable = new Users(databaseConnection, currentPane);
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

