package crosswordjava;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.Parent;
import javafx.geometry.Side;

import crosswordjava.model.CrosswordGrid;
import crosswordjava.model.GameState;
import crosswordjava.model.Word;
import javafx.scene.effect.DropShadow;

/**
 * Main game view
 */
public class GameView {
    private BorderPane root;
    private GridPane crosswordGridPane;
    private VBox acrossCluesBox;
    private VBox downCluesBox;
    private Button newGameButton;
    private Button checkButton;
    private Button hintButton;
    private Button helpButton;
    private Button difficultyButton;
    private Runnable onComplete;

    private GameState gameState;
    private TextField[][] gridCells;
    private VBox[][] numberLabels;
    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 28;

    private enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private Difficulty currentDifficulty = Difficulty.MEDIUM; // Default difficulty

    /**
     * Creates the game view with all UI components
     */
    public GameView() {
        gameState = new GameState(GRID_SIZE);
        gridCells = new TextField[GRID_SIZE][GRID_SIZE];
        numberLabels = new VBox[GRID_SIZE][GRID_SIZE];

        createUI();
        generateNewPuzzle();
    }

    /**
     * Returns the root node for this view
     */
    public Parent getRoot() {
        return root;
    }
    
    /*
    * Getter Method for the Help Button
    */
    public Button getHelpButton(){
        return helpButton;
    }
    
    
    /**
     * Creates all UI components programmatically
     */
    private void createUI() {
        // Main layout
        root = new BorderPane();

        // TOP: Header with title and buttons
        VBox topContainer = new VBox(5);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(5, 10, 5, 10));

        Label titleLabel = new Label("Crossword Puzzle");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        HBox buttonBar = new HBox(12);
        buttonBar.setAlignment(Pos.CENTER);

        newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-font-size: 12px;");
        newGameButton.setOnAction(e -> generateNewPuzzle());

        checkButton = new Button("Check");
        checkButton.setStyle("-fx-font-size: 12px;");
        checkButton.setOnAction(e -> checkPuzzle());

        hintButton = new Button("Hint");
        hintButton.setStyle("-fx-font-size: 12px;");
        hintButton.setOnAction(e -> provideHint());

        helpButton = new Button("Help");
        helpButton.setStyle("-fx-font-size: 12px;");
        helpButton.setOnAction(e -> App.switchToHelp());

        difficultyButton = new Button("Difficulty: Medium");
        difficultyButton.setStyle("-fx-font-size: 12px;");
        difficultyButton.setOnAction(e -> showDifficultyMenu());
        

        buttonBar.getChildren().addAll(newGameButton, checkButton, hintButton, helpButton, difficultyButton);
        topContainer.getChildren().addAll(titleLabel, buttonBar);
        root.setTop(topContainer);

        // CENTER: Crossword grid
        crosswordGridPane = new GridPane();
        crosswordGridPane.setAlignment(Pos.CENTER_LEFT);
        crosswordGridPane.setHgap(2);
        crosswordGridPane.setVgap(2);
        
        /*
        * Add Margin to the Border Pane to nudge away the crosswordGrid.
        * (This is an alternative method for to Manually adjust the Grid in BorderPane)
        * Insets: top = 0, right = 0, bottom = 0, left = 100;
        */ 
        BorderPane.setMargin(crosswordGridPane, new Insets(0, 0, 50, 100));
        crosswordGridPane.setPadding(new Insets(5));
        initializeGridUI();
        root.setCenter(crosswordGridPane);

        // RIGHT: Clues
        VBox rightContainer = new VBox(5);
        rightContainer.setTranslateX(-85);
        rightContainer.setTranslateY(15);
        rightContainer.setPrefWidth(235); //170 orginally
        rightContainer.setStyle("-fx-background-color: transparent;");
        rightContainer.setPadding(new Insets(5));

        // Across clues
        ScrollPane acrossScrollPane = new ScrollPane();
        acrossScrollPane.setStyle("-fx-background-color: FFF0DB;");
        acrossScrollPane.setFitToWidth(true);
        acrossScrollPane.setPrefHeight(150); //170 originally

        acrossCluesBox = new VBox(3);
        acrossCluesBox.setStyle("-fx-background-color: FFF0DB;");
        acrossCluesBox.setPadding(new Insets(3, 5, 3, 5));
        acrossScrollPane.setContent(acrossCluesBox);

        // Down clues
        ScrollPane downScrollPane = new ScrollPane();
        downScrollPane.setStyle("-fx-background-color: FFF0DB;");
        downScrollPane.setFitToWidth(true);
        downScrollPane.setPrefHeight(150);

        downCluesBox = new VBox(3);
        downCluesBox.setStyle("-fx-background-color: FFF0DB;");
        downCluesBox.setPadding(new Insets(3, 5, 3, 5));
        downScrollPane.setContent(downCluesBox);

        rightContainer.getChildren().addAll(acrossScrollPane, downScrollPane);
        root.setRight(rightContainer);

        // BOTTOM: Status message
        HBox bottomContainer = new HBox(10);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(2, 10, 5, 10));

        Label statusLabel = new Label("Fill in the crossword puzzle using the clues provided.");
        statusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: white; -fx-font-weight: bold;");
        bottomContainer.getChildren().add(statusLabel);

        root.setBottom(bottomContainer);
    }

    /**
     * Initializes the grid UI.
     */
    private void initializeGridUI() {
        crosswordGridPane.getChildren().clear();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = createGridCell(row, col);
                gridCells[row][col] = cell;
                crosswordGridPane.add(cell, col, row);
            }
        }
    }

    /**
     * Creates a grid cell.
     */
    private TextField createGridCell(int row, int col) {
        TextField cell = new TextField();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setMaxSize(CELL_SIZE, CELL_SIZE);
        cell.setMinSize(CELL_SIZE, CELL_SIZE);
        cell.setFont(Font.font("System", FontWeight.BOLD, 13));
        cell.setStyle("-fx-alignment: center; -fx-padding: 0;");

        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                cell.setText(newValue.substring(0, 1).toUpperCase());
            } else if (!newValue.isEmpty()) {
                cell.setText(newValue.toUpperCase());
            }

            if (!newValue.isEmpty()) {
                char input = newValue.charAt(0);
                if (gameState.checkInput(row, col, input)) {
                    cell.setStyle("-fx-alignment: center; -fx-text-fill: black;");
                    gameState.getGrid().setUserInput(row, col, true);
                } else {
                    cell.setStyle("-fx-alignment: center; -fx-text-fill: red;");
                }

                if (gameState.isPuzzleComplete()) {
                    showCompletionMessage();
                }
            }
        });

        return cell;
    }

    /**
     * Generates a new crossword puzzle.
     */
    private void generateNewPuzzle() {
        clearGrid();

        boolean success = gameState.generatePuzzleWithBacktracking();
        if (!success) {
            success = gameState.generatePuzzle();
            if (!success) {
                showAlert("Error", "Failed to generate a puzzle. Please try again.");
                return;
            }
        }

        updateGridUI();
        updateCluesUI();
    }

    /**
     * Clears the grid.
     */
    private void clearGrid() {
        crosswordGridPane.getChildren().clear();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = createGridCell(row, col);
                gridCells[row][col] = cell;
                cell.setText("");
                cell.setEditable(false);
                cell.setStyle("-fx-alignment: center; -fx-background-color: black;");

                crosswordGridPane.add(cell, col, row);
            }
        }

        acrossCluesBox.getChildren().clear();
        downCluesBox.getChildren().clear();
    }

    /**
     * Updates the grid UI based on the current game state.
     */
    private void updateGridUI() {
        List<Word> words = gameState.getWords();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = gridCells[row][col];
                cell.setText("");
                cell.setEditable(false);
                cell.setStyle("-fx-alignment: center; -fx-background-color: black;");

                numberLabels[row][col] = null;
            }
        }

        for (Word word : words) {
            int startRow = word.getRow();
            int startCol = word.getColumn();
            int length = word.getLength();

            if (numberLabels[startRow][startCol] == null) {
                VBox labelBox = new VBox(2);
                labelBox.setTranslateX(2);
                labelBox.setTranslateY(2);
                labelBox.setMouseTransparent(true);

                Label numberLabel = new Label(String.valueOf(word.getNumber()));
                numberLabel.setFont(Font.font("System", 8));
                numberLabel.setMouseTransparent(true);
                labelBox.getChildren().add(numberLabel);

                numberLabels[startRow][startCol] = labelBox;
                crosswordGridPane.add(labelBox, startCol, startRow);
            } else {
                Label numberLabel = new Label(String.valueOf(word.getNumber()));
                numberLabel.setFont(Font.font("System", 8));
                numberLabel.setMouseTransparent(true);
                numberLabels[startRow][startCol].getChildren().add(numberLabel);
            }

            for (int i = 0; i < length; i++) {
                int row = (word.getDirection() == Word.Direction.ACROSS) ? startRow : startRow + i;
                int col = (word.getDirection() == Word.Direction.ACROSS) ? startCol + i : startCol;

                if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
                    TextField cell = gridCells[row][col];
                    cell.setEditable(true);
                    cell.setStyle("-fx-alignment: center; -fx-background-color: white;");
                    cell.setFocusTraversable(true);
                }
            }
        }
    }

    /**
     * Updates the clues UI based on the current game state.
     */
    private void updateCluesUI() {
        acrossCluesBox.getChildren().clear();
        downCluesBox.getChildren().clear();

        Label acrossHeader = new Label("Across");
        acrossHeader.setFont(Font.font("System", FontWeight.BOLD, 16));
        acrossCluesBox.getChildren().add(acrossHeader);

        Label downHeader = new Label("Down");
        downHeader.setFont(Font.font("System", FontWeight.BOLD, 16));
        downCluesBox.getChildren().add(downHeader);

        List<Word> acrossWords = gameState.getWordsInDirection(Word.Direction.ACROSS);
        for (Word word : acrossWords) {
            Label clueLabel = new Label(word.getNumber() + ". " + word.getClue());
            clueLabel.setWrapText(true);
            acrossCluesBox.getChildren().add(clueLabel);
        }

        List<Word> downWords = gameState.getWordsInDirection(Word.Direction.DOWN);
        for (Word word : downWords) {
            Label clueLabel = new Label(word.getNumber() + ". " + word.getClue());
            clueLabel.setWrapText(true);
            downCluesBox.getChildren().add(clueLabel);
        }
    }

    /**
     * Checks the puzzle for correctness.
     */
    private void checkPuzzle() {
        CrosswordGrid grid = gameState.getGrid();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                TextField cell = gridCells[row][col];
                if (cell.isEditable()) {
                    if (!cell.getText().isEmpty()) {
                        char input = cell.getText().charAt(0);
                        if (gameState.checkInput(row, col, input)) {
                            cell.setStyle("-fx-alignment: center; -fx-background-color: lightgreen;");
                            grid.setUserInput(row, col, true);
                        } else {
                            cell.setStyle("-fx-alignment: center; -fx-background-color: #ff9999;");
                        }
                    } else {
                        cell.setStyle("-fx-alignment: center; -fx-background-color: #ffffaa;");
                    }
                }
            }
        }

        if (gameState.isPuzzleComplete()) {
            showCompletionMessage();
        }
    }

    /**
     * Provides a hint by revealing a random cell.
     */
    private void provideHint() {
        List<Word> words = gameState.getWords();
        CrosswordGrid grid = gameState.getGrid();

        for (Word word : words) {
            int startRow = word.getRow();
            int startCol = word.getColumn();
            String wordText = word.getWord();

            for (int i = 0; i < wordText.length(); i++) {
                int row = (word.getDirection() == Word.Direction.ACROSS) ? startRow : startRow + i;
                int col = (word.getDirection() == Word.Direction.ACROSS) ? startCol + i : startCol;

                if (!grid.isUserInput(row, col)) {
                    char correctChar = wordText.charAt(i);
                    TextField cell = gridCells[row][col];
                    cell.setText(String.valueOf(correctChar));
                    cell.setStyle("-fx-alignment: center; -fx-text-fill: blue;");
                    grid.setUserInput(row, col, true);
                    return;
                }
            }
        }

        showAlert("Hint", "All cells are already filled correctly!");
    }
    
    
    /*
    * Called when the player completes the puzzle
    */
    public void setOnComplete(Runnable r){
        this.onComplete = r;
    }
    
    /**
     * Shows a completion message when the puzzle is complete.
     */
    private void showCompletionMessage() {
        showAlert("Congratulations", "You have completed the crossword puzzle!");
        
        if (onComplete != null){
            onComplete.run();
        }
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showDifficultyMenu() {
        ContextMenu menu = new ContextMenu();

        MenuItem easyItem = new MenuItem("Easy");
        easyItem.setOnAction(e -> setDifficulty(Difficulty.EASY));

        MenuItem mediumItem = new MenuItem("Medium");
        mediumItem.setOnAction(e -> setDifficulty(Difficulty.MEDIUM));

        MenuItem hardItem = new MenuItem("Hard");
        hardItem.setOnAction(e -> setDifficulty(Difficulty.HARD));

        menu.getItems().addAll(easyItem, mediumItem, hardItem);
        menu.show(difficultyButton, Side.BOTTOM, 0, 0);
    }

    // Sets the difficulty level and updates the game state accordingly.
    private void setDifficulty(Difficulty difficulty) {
        currentDifficulty = difficulty;

        switch (difficulty) {
            case EASY:
                gameState.setDifficultyParameters(10, 150, 4); // wordTarget, depthLimit, minWords
                difficultyButton.setText("Difficulty: Easy");
                break;
            case MEDIUM:
                gameState.setDifficultyParameters(20, 300, 4);
                difficultyButton.setText("Difficulty: Medium");
                break;
            case HARD:
                gameState.setDifficultyParameters(25, 400, 6);
                difficultyButton.setText("Difficulty: Hard");
                break;
        } // 25 words is the max words to be generated by the grid

        // Generate a new puzzle with updated difficulty
        generateNewPuzzle();
    }
}
