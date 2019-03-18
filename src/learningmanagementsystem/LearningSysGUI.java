package learningmanagementsystem;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Hashtable;

public class LearningSysGUI extends GridPane {

    // The options bar at the top of the screen.
    private OptionsBar topBar;

    // The current pane being displayed.
    private Pane currentPane;

    // The ecosystem.
    private Session mySession;


    public LearningSysGUI(Session newSession) {
        mySession = newSession;
        topBar = new OptionsBar();
        currentPane = greetingPane();

        add(topBar.createOptionsBar(), 0, 0);
        add(currentPane, 0, 1);

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
        Text subtext = new Text("Choose an option above to get started.");

        welcome.setFont(Font.font(40));
        subtext.setFont(Font.font(30));

        VBox vbox = new VBox(welcome, subtext);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private class OptionsBar {

        // create the buttons at the top of the program
        private HBox createOptionsBar() {
            Button courseTable = createMyPoolButton();

            final int gap = 10;
            HBox hbox = new HBox(courseTable);
            hbox.setSpacing(gap);

            return hbox;
        }

        private Button createMyPoolButton() {
            Button myButton = new Button("Courses");

            Tooltip tip = new Tooltip("View and edit the courses");
            myButton.setTooltip(tip);
            return myButton;
        }

    }




}

