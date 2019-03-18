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

public class LearningSysGUI extends GridPane {

    // The option bar at the top of the screen.
    private OptionBar topBar;

    // The bar that displays table functions.
    private HBox functionOptions;

    // The current pane being displayed.
    private Pane currentPane;

    // The current table that user is looking at.
    private Tables currentTable;

    // The ecosystem.
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

        // working on layout of the page

        // need to set up a myPool page
        // then the starting var editor
        // then the run simulation page
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

            myButton.setOnAction(this::createFunctionOptions);
            return myButton;
        }

        // when a table is chosen, displays the functions
        private void createFunctionOptions(ActionEvent event) {
            String buttonName = findButtonName(event);

            // equal to select command in sql
            Button viewButton = new Button("View Table");

            // equal to add command in sql
            Button addButton = new Button("Add" + buttonName);

            // equal to update command in sql
            Button updateButton = new Button("Update" + buttonName);

            // equal to delete command in sql
            Button deleteButton = new Button("Delete" + buttonName);

            final int gap = 5;
            HBox hbox = new HBox(viewButton, addButton, updateButton, deleteButton);
            hbox.setSpacing(gap);
            hbox.setStyle("-fx-background-color: gray");

            // get the functionOptions that's currently empty and set it to hbox.
            functionOptions.getChildren().setAll(hbox);
        }

        public String findButtonName(ActionEvent event) {
            // split the object text into two parts by separating using the quote symbol.
            // to check why this work, you can use System.out.println(event.getSource()).
            String[] buttonObj = event.getSource().toString().split("'");

            final int indexContainingTheName = 1;
            return buttonObj[indexContainingTheName];
        }

    }



}

