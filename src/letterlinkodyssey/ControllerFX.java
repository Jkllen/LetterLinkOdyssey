package letterlinkodyssey;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class ControllerFX {
    private ModelFX model;
    private MainMenuFX view;
    private Stage stage;
    private int dialogueIndex = 0;
    private ChapterFX currentChapter;
    private int currentChapterNumber = 1;

    public ControllerFX(ModelFX model, MainMenuFX view, Stage stage) {
        this.model = model;
        this.view = view;
        this.stage = stage;

        updateView();      

        view.getNewGameButton().setOnAction(event -> showNameEntry());

        view.getLoadGameButton().setOnAction(event -> {
            // Create a dialog to input the slot number.
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Enter save slot number (e.g., 1-3):");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(slotStr -> {
                try {
                    // Parse the slot number entered by the user
                    int slot = Integer.parseInt(slotStr);

                    // Log the slot number for debugging
                    System.out.println("Attempting to load game from slot: " + slot);

                    // Try loading the game from the specified slot
                    boolean loadSuccess = model.loadGame(slot);  // Store the result of loading

                    if (loadSuccess) {
                        System.out.println("Game loaded successfully from slot: " + slot);
                        showChapter();
                    } else {
                        // Log and show error if no save found for this slot
                        System.out.println("No save found in slot: " + slot);

                        // Optionally show an error message if no save was found
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("No Save Found");
                        alert.setContentText("There is no saved game in this slot.");
                        alert.showAndWait();
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input (non-integer values)
                    System.out.println("Invalid slot input: " + slotStr);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("Invalid Slot Number");
                    alert.setContentText("Please enter a valid slot number.");
                    alert.showAndWait();
                }
            });
        });


        
        view.getOptionsButton().setOnAction(event -> {
            // Set the flag to indicate the user is accessing from the Main Menu
            GameSettings.getInstance().setAccessedFromMainMenu(true);

            // Store the current scene to return to it later
            GameSettings.getInstance().setPreviousScene(stage.getScene());

            // Display the Options Menu
            OptionsMenuFX options = new OptionsMenuFX(stage, this, GameSettings.getInstance().isAccessedFromMainMenu());
            options.display();
        });

    }
    
    public int getChapterStartIndex(int chapterNumber) {
        return switch (chapterNumber) {
            case 1 ->
                0;     // Chapter 1 starts at dialogue index 0 (ID 1)
            case 2 ->
                17;    // Chapter 2 starts at dialogue index 17 (ID 18)
            case 3 ->
                52;    // Chapter 3 starts at dialogue index 53 (ID 54)
            case 4 ->
                107; // Chapter 4 starts at dialogue index 108
            default ->
                0;    // fallback to beginning if undefined
        };
    }

    public int getCurrentChapterNumber() {
        return currentChapterNumber;
    }
    
    public void setCurrentDialogueIndex(int index){
        this.dialogueIndex = index;
    }
    
    public void startNextChapter() {
        currentChapterNumber++;
        
        int nextChapterStartIndex = getChapterStartIndex(currentChapterNumber);
        model.setDialogueIndex(nextChapterStartIndex);
        showChapter();
           
    }
    
    private void showChapter() {
        currentChapter = new ChapterFX(stage, this, model, currentChapterNumber);
        currentChapter.setDialogueIndex(model.getDialogueIndex());
        currentChapter.display();
    }


    private void updateView() {
        DialogueEntry current = model.getCurrentDialogue(dialogueIndex);
        if (current != null && current.getBackgroundImg() != null && !current.getBackgroundImg().isEmpty()) {
            view.setBackgroundImage(current.getBackgroundImg());
        }
    }
    
    public int getDialogueIndex() {
        if (currentChapter != null) {
            return currentChapter.getDialogueIndex();
        }
        return 0;
    }

    public String getCharacterImage() {
        return "file:src/assets/characters/protagonist.png";
    }

    public void savePlayerName(String name) {
        model.setPlayerName(name);
        model.savePlayerNameToDatabase(); 
        showChapter();
    }

    public String getPlayerName() {
        return model.getPlayerName();
    }

    public void showNameEntry() {
        NameEntryFX nameEntry = new NameEntryFX(stage, this);
        nameEntry.display();
    }
    
    public ModelFX getModel() {
        return model;
    }
    
    public MainMenuFX getView() {
        return view;
    }

    public Stage getStage() {
        return stage;
    }


}
