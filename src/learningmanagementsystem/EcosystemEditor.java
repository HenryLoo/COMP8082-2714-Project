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

    // The pane that displays My Pools.
    private MyPoolsPane poolsPane;

    // The pane that displays the SpawnVariablePane.
    private SpawnVariablePane variablePane;

    // The pane that displays the SimulationZone.
    private SimulationZone simulationPane;

    // The ecosystem's pools.
    private ArrayList<Pool> pools;

    // The ecosystem.
    private Ecosystem myEcosystem;


    public LearningSysGUI() {
        myEcosystem = new Ecosystem();
        myEcosystem.setupPools();
        pools = myEcosystem.getPools();

        poolsPane = new MyPoolsPane();
        variablePane = new SpawnVariablePane();
        simulationPane = new SimulationZone();
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
        Text welcome = new Text("Welcome to the Ecosystem Editor!");
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
            Button myPools = createMyPoolButton();
            Button classesVariables = createVariablesButton();
            Button simulationZone = createSimulationZone();

            final int gap = 10;
            HBox hbox = new HBox(myPools, classesVariables, simulationZone);
            hbox.setSpacing(gap);

            return hbox;
        }

        private Button createMyPoolButton() {
            Button myButton = new Button("My Pools");
            myButton.setOnAction(this::displayMyPoolPane);

            Tooltip tip = new Tooltip("View and change your pool variables");
            myButton.setTooltip(tip);
            return myButton;
        }

        private void displayMyPoolPane(ActionEvent event) {
            currentPane.getChildren().setAll(poolsPane.createMyPoolPane());
        }

        private Button createVariablesButton() {
            Button myButton = new Button("Creature Variables");
            Tooltip tip = new Tooltip("View and change your starting creature variables");
            myButton.setTooltip(tip);

            myButton.setOnAction(this::displayCreatures);
            return myButton;
        }

        private void displayCreatures(ActionEvent event) {
            currentPane.getChildren().setAll(variablePane.createSpawnVariablesPane());
        }

        private Button createSimulationZone() {
            Button myButton = new Button("Simulation Zone");
            Tooltip tip = new Tooltip("Run simulation of your ecosystem");
            myButton.setTooltip(tip);

            myButton.setOnAction(this::displaySimulationZone);
            return myButton;
        }

        private void displaySimulationZone(ActionEvent event) {
            currentPane.getChildren().setAll(simulationPane.createSimulationZonePane());
        }
    }

    private class MyPoolsPane {

        // The current pool editor pane being displayed.
        private Pane currentPoolEditorPane;

        /** The current creature editor pane being displayed.
         */
        protected Pane currentCreatureEditorPane;

        // The text fields in the My Pools Pane.
        private TextField volumeTxtFld;
        private TextField pHTxtFld;
        private TextField nutrientTxtFld;
        private TextField temperatureTxtFld;
        private TextField femChanceTxtFld;
        private TextField minAgeTxtFld;
        private TextField maxAgeTxtFld;
        private TextField minHealthTxtFld;
        private TextField maxHealthTxtFld;
        private TextField amountTxtFld;


        // The pool that is currently being displayed to user
        private Pool currentPool;

        /** The current creature that is being selected.
         */
        protected String currentCreature;

        /** Indicator that there's an error in user input.
         */
        protected boolean inputErrorIndicator;

        // The creatureNamesButtons will show up after user click on a pool.
        private HBox creatureNamesButtons;

        // The error message in the pool editor.
        private Label poolErrorMessage;

        /** The error message in the creature editor.
         */
        protected Label creatureErrorMessage;


        /**
         * Create the MyPool Pane.
         * @return a GridPane containing all the needed elements.
         */
        public GridPane createMyPoolPane() {

            // create the buttons with the pool names
            GridPane pane = new GridPane();
            pane.add(createPoolButtons(), 0, 0);

            // add the empty inner pane to the right place for now
            currentPoolEditorPane = new Pane();
            pane.add(currentPoolEditorPane, 0, 1);

            // add the currently empty buttons with creature names for now
            creatureNamesButtons = new HBox();
            pane.add(creatureNamesButtons, 1, 0);

            // create a blank creature editor pane for now
            currentCreatureEditorPane = new Pane();
            pane.add(currentCreatureEditorPane, 1, 1);

            final int hGap = 70;
            pane.setHgap(hGap);

            return pane;
        }

        private HBox createPoolButtons() {
            final int gap = 5;
            HBox hbox = new HBox();

            hbox.setSpacing(gap);
            hbox.setStyle("-fx-background-color: gray");

            for (Pool p : pools) {
                Button myButton = new Button(p.getName());
                myButton.setOnAction(this::openPoolEditor);
                hbox.getChildren().addAll(myButton);
            }
            return hbox;
        }

        private void openPoolEditor(ActionEvent event) {
            // find which button was clicked
            String[] buttonObj = event.getSource().toString().split("'");

            final int indexContainingTheName = 1;
            String buttonName = buttonObj[indexContainingTheName];

            // pass the name to the createInnerPane and add it to the currentPoolEditorPane
            // the pool editor is now displayed
            currentPoolEditorPane.getChildren().setAll(createPoolEditor(buttonName));

            // reveal the creatureNamesButton
            // it's attached to the currentPoolEditorPane but is currently invisible
            creatureNamesButtons.getChildren().setAll(createCreatureButtons());
        }

        private GridPane createPoolEditor(String name) {
            Label poolName = new Label("Pool Name: " + name);
            Label volume = new Label("Water Volume in Litres: ");
            Label pH = new Label("pH Level: ");
            Label nutrientCoefficient = new Label("Nutrient Coefficient: ");
            Label temperature = new Label("Temperature in Celsius: ");
            poolErrorMessage = new Label("");

            poolName.setFont(Font.font(24));
            volume.setFont(Font.font(18));
            pH.setFont(Font.font(18));
            nutrientCoefficient.setFont(Font.font(18));
            temperature.setFont(Font.font(18));
            poolErrorMessage.setFont(Font.font(13));
            poolErrorMessage.setTextFill(Color.RED);

            currentPool = findPoolByName(name);
            volumeTxtFld = new TextField(String.valueOf(currentPool.getVolumeLitres()));
            pHTxtFld = new TextField(String.valueOf(currentPool.getpH()));
            nutrientTxtFld = new TextField(String.valueOf(currentPool.getNutrientCoefficient()));
            temperatureTxtFld = new TextField(String.valueOf(currentPool.getTemperatureCelsius()));

            GridPane gp = new GridPane();

            gp.add(poolName, 0, 0, 2, 1);
            gp.add(volume, 0, 1);
            gp.add(pH, 0, 2);
            gp.add(nutrientCoefficient, 0, 3);
            gp.add(temperature, 0, 4);
            gp.add(poolErrorMessage, 0, 5, 2, 1);

            gp.add(volumeTxtFld, 1, 1);
            gp.add(pHTxtFld, 1, 2);
            gp.add(nutrientTxtFld, 1, 3);
            gp.add(temperatureTxtFld, 1, 4);

            Button saveBtn = new Button("Save Changes");
            saveBtn.setOnAction(this::saveNewPoolValues);
            gp.add(saveBtn, 0, 6);

            final int hGap = 5;
            final int vGap = 10;
            gp.setHgap(hGap);
            gp.setVgap(vGap);

            return gp;
        }

        private Pool findPoolByName(String name) {
            for (Pool p : pools) {
                if (p.getName().equals(name)) {
                    return p;
                }
            }
            return null;
        }

        /**
         * Create a HBox containings buttons with creature names on it.
         * @return creature names button in a HBox.
         */
        protected HBox createCreatureButtons() {
            HBox hbox = new HBox();

            final int gap = 5;
            hbox.setSpacing(gap);
            hbox.setStyle("-fx-background-color: gray");

            Button guppyBtn = new Button("Guppy");
            guppyBtn.setOnAction(this::openCreatureEditor);

            Button swordTailsBtn = new Button("Swordtail");
            swordTailsBtn.setOnAction(this::openCreatureEditor);

            Button pondSnailBtn = new Button("PondSnail");
            pondSnailBtn.setOnAction(this::openCreatureEditor);

            hbox.getChildren().addAll(guppyBtn, swordTailsBtn, pondSnailBtn);

            return hbox;
        }

        /**
         * Open the creature editor to user.
         * @param event an ActionEvent.
         */
        protected void openCreatureEditor(ActionEvent event) {
            // find which button was clicked
            String[] buttonObj = event.getSource().toString().split("'");

            final int indexContainingTheName = 1;
            String buttonName = buttonObj[indexContainingTheName];

            // pass the name to the createCreatureEditor and add it to the currentPoolEditorPane
            currentCreatureEditorPane.getChildren().setAll(createCreatureEditor(buttonName));
        }

        /**
         * Create the creature editor.
         * @return a GridPane containing all the needed element.
         */
        protected GridPane createCreatureEditor(String name) {
            Label creatureName = new Label("Initial " + name + " Variables: ");
            creatureName.setFont(Font.font(24));

            Label amount = new Label("Amount: ");
            amount.setFont(Font.font(18));

            Label minAge = new Label("Minimum Age (inclusive): ");
            minAge.setFont(Font.font(18));

            Label maxAge = new Label("Maximum Age (inclusive): ");
            maxAge.setFont(Font.font(18));

            Label minHealth = new Label("Minimum Health (inclusive): ");
            minHealth.setFont(Font.font(18));

            Label maxHealth = new Label("Maximum Health (inclusive): ");
            maxHealth.setFont(Font.font(18));

            Label femaleChance = new Label("Female Chance: ");
            femaleChance.setFont(Font.font(18));

            creatureErrorMessage = new Label();
            creatureErrorMessage.setFont(Font.font(13));
            creatureErrorMessage.setTextFill(Color.RED);

            Hashtable<String, Double> variables = findCreatureVariableByName(name);

            if (variables == null) {
                return null;
            }

            amountTxtFld = new TextField(variables.get("amount").toString());
            minAgeTxtFld = new TextField(variables.get("minAge").toString());
            maxAgeTxtFld = new TextField(variables.get("maxAge").toString());
            minHealthTxtFld = new TextField(variables.get("minHealth").toString());
            maxHealthTxtFld = new TextField(variables.get("maxHealth").toString());

            // if name is PondSnail, set femChanceTxtFld to 0 and disable it.
            if (name.equals("PondSnail")) {
                femChanceTxtFld = new TextField("0");
                femChanceTxtFld.setEditable(false);
                femaleChance.setText("Female Chance (N/A)");

            } else {
                femChanceTxtFld = new TextField(variables.get("initialFemaleChance").toString());
            }

            GridPane gp = new GridPane();

            gp.add(creatureName, 0, 0, 2, 1);
            gp.add(amount, 0, 1);
            gp.add(minAge, 0, 2);
            gp.add(maxAge, 0, 3);
            gp.add(minHealth, 0, 4);
            gp.add(maxHealth, 0, 5);
            gp.add(femaleChance, 0, 6);

            gp.add(amountTxtFld, 1, 1);
            gp.add(minAgeTxtFld, 1, 2);
            gp.add(maxAgeTxtFld, 1, 3);
            gp.add(minHealthTxtFld, 1, 4);
            gp.add(maxHealthTxtFld, 1, 5);
            gp.add(femChanceTxtFld, 1, 6);

            Button saveBtn = new Button("Save Changes");
            saveBtn.setOnAction(this::saveNewCreatureValues);
            gp.add(saveBtn, 0, 7);

            gp.add(creatureErrorMessage, 0, 8, 2, 1);

            final int hGap = 5;
            final int vGap = 10;
            gp.setHgap(hGap);
            gp.setVgap(vGap);

            currentCreature = name;
            return gp;
        }


        private Hashtable<String, Double> findCreatureVariableByName(String name) {
            switch (name) {
              case "Guppy":
                  return currentPool.getInitialGuppyVariables();

              case "Swordtail":
                  return currentPool.getInitialSwordTailVariables();

              case "PondSnail":
                  return currentPool.getInitialPondSnailVariables();

              default:
                  System.out.println("There's no such name. Please try again.");
                  return null;
            }
        }

        // try to save new pool values
        // if there are errors, let the user know.
        private void saveNewPoolValues(ActionEvent event) {
            // turn off the error indicator
            inputErrorIndicator = false;

            // create an error message for user
            String message = "";

            double newVolume = 0;
            double newPH = 0;
            double newNutrient = 0;
            double newTemperature = 0;

            try {
                newVolume = Double.parseDouble(volumeTxtFld.getText().trim());
                if (newVolume < 0) {
                    message += "Your volume must be larger than 0. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                volumeTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 14;
                final int lowerBound = 0;

                newPH = Double.parseDouble(pHTxtFld.getText().trim());
                if (newPH < lowerBound || newPH > upperBound) {
                    message += "Your pH must be between 0 and 14 inclusive. \n";
                    throw new Exception();

                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                volumeTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 1;
                final int lowerBound = 0;

                newNutrient = Double.parseDouble(nutrientTxtFld.getText().trim());
                if (newNutrient < lowerBound || newNutrient > upperBound) {
                    message += "Your nutrient coefficient must be between 0 and 1 inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                volumeTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 100;
                final int lowerBound = 0;
                newTemperature = Double.parseDouble(temperatureTxtFld.getText().trim());
                if (newTemperature < lowerBound || newTemperature > upperBound) {
                    message += "Your temperature must be between 0 and 100 inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                volumeTxtFld.setStyle("-fx-border-color: red");
            }

            if (inputErrorIndicator) {
                message += "One or more of your boxes has a non-numeric values.\n";
                poolErrorMessage.setText(message);
            } else {
                currentPool.setVolumeLitres(newVolume);
                currentPool.setpH(newPH);
                currentPool.setNutrientCoefficient(newNutrient);
                currentPool.setTemperatureCelsius(newTemperature);
            }

        }

        private void saveNewCreatureValues(ActionEvent event) {
            // turn off the error indicator
            inputErrorIndicator = false;

            int currentCreatureMaxAge = findCreatureMaxAgeByName(currentCreature);

            // create an error message for user
            String message = "";

            double newAmount = 0;
            double newMinAge = 0;
            double newMaxAge = 0;
            double newMinHealth = 0;
            double newMaxHealth = 0;
            double newFemChance = 0;

            try {
                final int lowerBound = 0;

                newAmount = Double.parseDouble(amountTxtFld.getText().trim());
                if (newAmount < lowerBound) {
                    message += "Your amount must be greater than or equal to 0. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                amountTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 1;
                final int lowerBound = 0;

                newFemChance = Double.parseDouble(femChanceTxtFld.getText().trim());
                if (newFemChance > upperBound || newFemChance < lowerBound) {
                    message += "Your female chance must be between 0 and 1 inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                femChanceTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int lowerBound = 0;

                newMinAge = Double.parseDouble(minAgeTxtFld.getText().trim());
                if (newMinAge < lowerBound || newMinAge > currentCreatureMaxAge) {
                    message += "Your minimum age must be between 0 and "
                            + currentCreatureMaxAge + " inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                minAgeTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int lowerBound = 0;

                newMaxAge = Double.parseDouble(maxAgeTxtFld.getText().trim());
                if (newMaxAge < lowerBound || newMaxAge > currentCreatureMaxAge) {
                    message += "Your maximum age must be between 0 and "
                            + currentCreatureMaxAge + " inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                maxAgeTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 1;
                final int lowerBound = 0;

                newMinHealth = Double.parseDouble(minHealthTxtFld.getText().trim());
                if (newMinHealth < lowerBound || newMinHealth > upperBound) {
                    message += "Your minimum health must be between 0 and 1 inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                minHealthTxtFld.setStyle("-fx-border-color: red");
            }

            try {
                final int upperBound = 1;
                final int lowerBound = 0;

                newMaxHealth = Double.parseDouble(maxHealthTxtFld.getText().trim());
                if (newMaxHealth < lowerBound || newMaxHealth > upperBound) {
                    message += "Your maximum health must be between 0 and 1 inclusive. \n";
                    throw new Exception();
                }
            } catch (Exception e) {
                inputErrorIndicator = true;
                maxHealthTxtFld.setStyle("-fx-border-color: red");
            }

            if (inputErrorIndicator) {
                message += "One or more of your boxes has a non-numeric values.\n";
                creatureErrorMessage.setText(message);
            } else {
                myEcosystem.applyCreatureVariables(currentPool, currentCreature, newAmount,
                        newFemChance, newMinAge, newMaxAge, newMinHealth, newMaxHealth);
            }

        }

        private int findCreatureMaxAgeByName(String name) {
            switch (name) {
              case "Guppy":
                  return Guppy.MAXIMUM_AGE_IN_WEEKS;

              case "Swordtail":
                  return Swordtail.MAXIMUM_AGE_IN_WEEKS;

              case "PondSnail":
                  return PondSnail.MAXIMUM_AGE_IN_WEEKS;

              default:
                  System.out.println("There is no creature with that name");
                  return 0;
            }
        }

    }

    private class SpawnVariablePane extends MyPoolsPane {
        // The text fields for the SpawnVariablePane.
        private TextField spawnChanceTxtFld;
        private TextField minFryTxtFld;
        private TextField maxFryTxtFld;
        private TextField femChanceTxtFld;

        /**
         * Create the SpawnVariablePane.
         * @return variables pane as a GridPane.
         */
        public GridPane createSpawnVariablesPane() {
            // create the buttons with the pool names
            GridPane pane = new GridPane();
            pane.add(createCreatureButtons(), 0, 0);

            // add the empty inner pane to the right place for now
            currentCreatureEditorPane = new Pane();
            pane.add(currentCreatureEditorPane, 0, 1);

            final int hGap = 70;
            pane.setHgap(hGap);

            return pane;
        }

        /**
         * Create the CreatureEditor based on the given name.
         * @param name a String
         * @return a GridPane containing all the needed elements.
         */
        @Override
        protected GridPane createCreatureEditor(String name) {
            Label creatureName = new Label(name + " Spawn Variables: ");
            creatureName.setFont(Font.font(24));

            Label spawnChance = new Label("Spawn Chance: ");
            spawnChance.setFont(Font.font(18));

            Label minFry = new Label("Minimum Fry Born (inclusive): ");
            minFry.setFont(Font.font(18));

            Label maxFry = new Label("Maximum Fry Born (inclusive): ");
            maxFry.setFont(Font.font(18));

            Label femaleChance = new Label("Born Female Chance: ");
            femaleChance.setFont(Font.font(18));

            creatureErrorMessage = new Label();
            creatureErrorMessage.setFont(Font.font(13));
            creatureErrorMessage.setTextFill(Color.RED);

            spawnChanceTxtFld = new TextField();
            minFryTxtFld = new TextField();
            maxFryTxtFld = new TextField();
            femChanceTxtFld = new TextField();

            // if name is PondSnail, set femChanceTxtFld to 0 and disable it.
            if (name.equals("PondSnail")) {
                femChanceTxtFld = new TextField("0");
                femChanceTxtFld.setEditable(false);
                femaleChance.setText("Born Female Chance (N/A)");
            }

            GridPane gp = new GridPane();

            gp.add(creatureName, 0, 0, 2, 1);
            gp.add(spawnChance, 0, 1);
            gp.add(minFry, 0, 2);
            gp.add(maxFry, 0, 3);
            gp.add(femaleChance, 0, 4);

            gp.add(spawnChanceTxtFld, 1, 1);
            gp.add(minFryTxtFld, 1, 2);
            gp.add(maxFryTxtFld, 1, 3);
            gp.add(femChanceTxtFld, 1, 4);

            Button saveBtn = new Button("Save Changes");
            gp.add(saveBtn, 0, 7);

            gp.add(creatureErrorMessage, 0, 8, 2, 1);

            final int hGap = 5;
            final int vGap = 10;
            gp.setHgap(hGap);
            gp.setVgap(vGap);

            currentCreature = name;
            return gp;
        }


    }

    private class SimulationZone {
        // Hold user input for length of simulation.
        private TextField numOfWeeksTxtFld;

        // Holds the simulation result.
        private Pane simulationResultPane;

        // Display the simulation result.
        private Label simulationResultLbl;

        // Display the pool break down.
        private Label poolBreakDownLeftLbl;

        // Display the pool break down.
        private Label poolBreakDownRightLbl;

        // Display the current week of the simulation.
        private Label currentWeekNum;

        // The next button that continues the simulation.
        private Button nextBtn;

        // Display the error message to user;
        private Label errorMessage;

        /**
         * Create the SimulationZone pane.
         * @return variables pane as a GridPane.
         */
        public GridPane createSimulationZonePane() {
            // create the buttons with the pool names
            GridPane pane = new GridPane();

            pane.add(createUserPrompt(), 0, 0);

            errorMessage = new Label();
            errorMessage.setFont(Font.font(12));
            errorMessage.setTextFill(Color.RED);
            pane.add(errorMessage, 0, 1);

            simulationResultPane = new Pane();
            pane.add(simulationResultPane, 0, 2);

            return pane;
        }

        private GridPane createUserPrompt() {
            Label weekPrompt = new Label("Enter length of simulation "
                    + "in weeks: ");
            weekPrompt.setFont(Font.font(20));

            numOfWeeksTxtFld = new TextField();

            Button runBtn = new Button("Run");
            runBtn.setOnAction(this::checkUserInput);

            GridPane pane = new GridPane();

            pane.add(weekPrompt, 0, 0);
            pane.add(numOfWeeksTxtFld, 1, 0);
            pane.add(runBtn, 2,0);

            final int hGap = 70;
            pane.setHgap(hGap);

            final int padding = 10;
            pane.setPadding(new Insets(padding, padding, padding, padding));

            return pane;
        }
        private void checkUserInput(ActionEvent event) {
            try {
                int userInput = Integer.parseInt(numOfWeeksTxtFld.getText().trim());
                if (userInput <= 0) {
                    throw new InvalidParameterException();
                } else {
                    myEcosystem.simulate(userInput);
                    simulationResultPane.getChildren().setAll(createSimulationResultPane());
                }
            } catch (InvalidParameterException e) {
                numOfWeeksTxtFld.setStyle("-fx-border-color: red");
                errorMessage.setText("Input must be an integer bigger than 0");
            }
        }

        private GridPane createSimulationResultPane() {
            GridPane pane = new GridPane();

            currentWeekNum = new Label("Week 1");
            currentWeekNum.setFont(Font.font(20));
            currentWeekNum.setAlignment(Pos.CENTER);
            pane.add(currentWeekNum, 0, 0);

            simulationResultLbl = new Label();
            simulationResultLbl.setFont(Font.font(20));
            pane.add(simulationResultLbl, 0, 1, 1, 2);

            nextBtn = new Button("Next Week");
            nextBtn.setOnAction(this::simulateNextWeek);
            nextBtn.setFont(Font.font(20));
            pane.add(nextBtn, 1, 1);

            poolBreakDownLeftLbl = new Label();
            poolBreakDownLeftLbl.setFont(Font.font(15));
            pane.add(poolBreakDownLeftLbl, 2, 0, 1, 2);

            poolBreakDownRightLbl = new Label();
            poolBreakDownRightLbl.setFont(Font.font(15));
            pane.add(poolBreakDownRightLbl, 3, 0, 1, 2);

            displayResult();
            return pane;
        }

        private void simulateNextWeek(ActionEvent event) {
            if (checkCanContinueWithSimulation()) {
                incrementWeek();
                displayResult();
            }
        }

        private boolean checkCanContinueWithSimulation() {
            int currentWeek = getNumberOfWeek();
            if (myEcosystem.getSimulationLengthInWeeks() == currentWeek) {
                nextBtn.setText("Done!");
                nextBtn.setDisable(true);
                return false;
            }

            return true;
        }

        /**
         * Get the number of week in the currentWeekNum.
         * @return the number of week as an int.
         */
        public int getNumberOfWeek() {
            String[] weekArray = currentWeekNum.getText().split(" ");

            final int indexOfNumber = 1;
            return Integer.parseInt(weekArray[indexOfNumber]);

        }

        private void incrementWeek() {
            int newWeek = getNumberOfWeek() + 1;
            currentWeekNum.setText("Week " + newWeek);
        }

        private void displayResult() {
            // get the creatures stats
            myEcosystem.simulateOneWeek();
            simulationResultLbl.setText(myEcosystem.getSimulationResult());

            // get the pool population stats
            myEcosystem.displayPoolPopulationBreakdown();
            poolBreakDownLeftLbl.setText(myEcosystem.getSimulationResult());

            // get the total population stats
            myEcosystem.storeTotalPopulationInSimulationResult();
            poolBreakDownRightLbl.setText(myEcosystem.getSimulationResult());
        }
    }
}

