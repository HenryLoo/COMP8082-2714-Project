package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.*;

public class LearningSysGUI extends GridPane {

    // The option bar at the top of the screen.
    private OptionBar topBar;

    // The bar that displays table functions.
    private HBox functionOptions;

    // The current pane being displayed.
    private Pane currentPane;

    // The current table that user is looking at.
    private Tables currentTable;

    // The session that's connected to the GUI.
    private Session mySession;


    public LearningSysGUI(Session newSession) {
        mySession = newSession;
        topBar = new OptionBar();
        currentPane = greetingPane();
        functionOptions = new HBox();

        add(topBar.createTableNameBar(), 0, 0);
        add(functionOptions, 0, 1);
        add(currentPane, 0, 2);

        final int Hgap = 5;
        final int Vgap = 20;
        setHgap(Hgap);
        setVgap(Vgap);
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

    private class OptionBar {

        /**
         * Create the buttons at the top of the program
          */
        public HBox createTableNameBar() {
            // create each button individually
            Button courseTable = createCoursesButton();

            final int gap = 10;
            HBox hbox = new HBox(courseTable);
            hbox.setSpacing(gap);

            return hbox;
        }

        private Button createCoursesButton() {
            Button myButton = new Button("Courses");

            Tooltip tip = new Tooltip("View and edit the courses");
            myButton.setTooltip(tip);

            // all table name buttons will have the same onAction.
            myButton.setOnAction(this::createFunctionOptions);
            return myButton;
        }

        // when a table is chosen, displays the functions
        private void createFunctionOptions(ActionEvent event) {
            // find which button was clicked
            String buttonName = findButtonText(event);

            // equal to select command in sql
            Button viewButton = new Button("View Table");
            viewButton.setOnAction(this::openSearchDashBoard);

            // equal to insert into command in sql
            Button addButton = new Button("Add " + buttonName);
            addButton.setOnAction(this::openAddDashBoard);

            final int gap = 5;
            HBox hbox = new HBox(viewButton, addButton);
            hbox.setSpacing(gap);

            // get the functionOptions that's currently empty and set it to hbox.
            functionOptions.getChildren().setAll(hbox);
        }

        /**
         * Find the button text based on the event created by clicking on that button.
         * @param event an ActionEvent.
         * @return the name of the button as a String.
         */
        public String findButtonText(ActionEvent event) {
            // to check why this work, you can use System.out.println(event.getSource()).
            Button buttonObj = (Button) event.getSource();
            String name = buttonObj.getText();

            // set the currentTable instance variable to the correct table.
            setCurrentTable(name);
            return name;
        }

        /**
         * Set the current table to a new instance of that table class.
         * @param tableName a String
         */
        public void setCurrentTable(String tableName) {
            switch (tableName) {
                case "Courses":
                    currentTable = new Courses(mySession.getMyConnection());
                    break;
                default:
                    System.out.println("There's no table with that name");
            }
        }

        /**
         * Open the add dashboard.
         * @param event an Action event.
         */
        public void openAddDashBoard(ActionEvent event) {
            currentPane.getChildren().setAll(currentTable.createAddDashBoard());
        }

        /**
         * Open the search dashboard.
         * @param event an Action Event.
         */
        public void openSearchDashBoard(ActionEvent event) {
            currentPane.getChildren().setAll(currentTable.createSearchDashBoard());
        }


    }



}

