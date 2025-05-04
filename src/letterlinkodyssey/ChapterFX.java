package letterlinkodyssey;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.TAB;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import java.io.File;


public class ChapterFX {
    private Stage stage;
    private ModelFX model;
    private ControllerFX controller;
    private BackgroundManage backgroundManage;
    private CharacterManage characterManage;
    private Label dialogueLabel, speakerLabel;
    private int currentDialogueIndex = 0;
    private StackPane layout;
    private boolean isAnimating, choicesVisible, autoForwardMode = false;
    private String fullTextBuffer = "";
    private HBox bottomButtons;
    private Label autoButton, skipButton, backButton, prefButton;
    private boolean isAutoForwarding, skipModeEnabled = false;
    private int currentChapter = 1;


    public ChapterFX(Stage stage, ControllerFX controller, ModelFX model, int currentChapter) {
        this.stage = stage;
        this.controller = controller;
        this.model = model;
        this.currentChapter = currentChapter;
        this.backgroundManage = new BackgroundManage();
        this.characterManage = new CharacterManage();
    }

    public void display() {
        System.out.println("display() called");

        bedroomScene();
        // always render the current index without advancing
        startDialogue();
    }


    
    public void setDialogueIndex(int dialogueIndex) {
        this.currentDialogueIndex = dialogueIndex;
    }
    
    
    public void loadGame(int dialogueIndex) {
        System.out.println("loadGame() called with index " + dialogueIndex);
        this.currentDialogueIndex = dialogueIndex;

        // Reset UI states
        isAnimating = false;
        choicesVisible = false;
        isAutoForwarding = false;
        skipModeEnabled = false;

        // Get the current dialogue entry based on the saved index
        DialogueEntry dialogue = model.getCurrentDialogue(dialogueIndex);

        if (dialogue != null) {
            // Update the UI
            updateUI(dialogue);

            // Now trigger the dialogue to start from this point
            startDialogue();
        } else {
            System.out.println("Error: Invalid dialogue index.");
        }
    }

    private void updateUI(DialogueEntry dialogue) {
        // Update character image, background image, and dialogue text
        setCharacterImage(dialogue.getCharacterImg());
        setBackground(dialogue.getBackgroundImg());
        setText(dialogue.getText());

        // If there are choices available, display them
        List<ChoiceEntry> choices = model.getChoicesForDialogue(dialogue.getId());
        if (!choices.isEmpty()) {
            showChoices(choices);
        }
    }

    private void showChoices(List<ChoiceEntry> choices) {
        // Logic to show the choices in the UI
        choicesVisible = true;
        // Update the choice buttons or panels here
    }

    private void setCharacterImage(String imagePath) {
        characterManage.setCharacterImage(imagePath); // Set the character image
    }

    private void setBackground(String imagePath) {
        backgroundManage.setBackgroundImage(imagePath); // Set the background image
    }

    private void setText(String text) {
        // Display the dialogue text
        dialogueLabel.setText(text);
    }
    
 
    private void bedroomScene() {
        String characterPath = controller.getCharacterImage();
        characterManage.setCharacterImage(characterPath);

        dialogueLabel = new Label("");
        dialogueLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        dialogueLabel.setTranslateY(150);
        dialogueLabel.setTranslateX(-10);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(700);
        dialogueLabel.setVisible(true);

        speakerLabel = new Label("");
        speakerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gold; -fx-font-weight: bold;"
                + "-fx-background-color: rgba(0, 0, 0, 0.6); -fx-background-radius: 10; -fx-padding: 6 12;");
        speakerLabel.setTranslateY(100);
        speakerLabel.setTranslateX(-350);
        speakerLabel.setVisible(true);

        layout = new StackPane();
        layout.setOnMouseClicked(e -> {
            if (e.getTarget() instanceof Button || e.getTarget() instanceof Label || e.getPickResult().getIntersectedNode().getStyleClass().contains("ignore-click")) {
                return;
            }
            
            if (choicesVisible) {
                return;
            }
 
            // Interrupt animation and show full dialogue immediately
            if (isAnimating) {
                isAnimating = false;
                dialogueLabel.setText(fullTextBuffer);
                return;
            }

            String rollback = GameSettings.getInstance().getRollbackMode();
            double x = e.getSceneX();

            boolean triggeredRollback = false;

            if ("Left".equals(rollback) && x < 400 && currentDialogueIndex > 0) {
                currentDialogueIndex--;
                showNextDialogue();
                triggeredRollback = true;
            } else if ("Right".equals(rollback) && x >= 400 && currentDialogueIndex > 0) {
                currentDialogueIndex--;
                showNextDialogue();
                triggeredRollback = true;
            }

            // If rollback was NOT triggered, proceed normally
            if (!triggeredRollback) {
                showNextDialogue();
            }
        });



        layout.getChildren().addAll(
            backgroundManage.getBackgroundImageView(),
            characterManage.getCharacterImageView(),
            speakerLabel,
            dialogueLabel   
        );
        
        // Bottom UI buttons
        
        backButton = createBottomButton("Back", "Back");
        autoButton = createBottomButton("Auto", "Auto");
        skipButton = createBottomButton("Skip", "Skip");
        prefButton = createBottomButton("Preferences", "Preferences");
        

        bottomButtons = new HBox(30, backButton, autoButton, skipButton, prefButton);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setTranslateY(200); // Position below dialogue
        bottomButtons.setTranslateX(0);
        layout.getChildren().add(bottomButtons);


        Scene bedroomScene = new Scene(layout, 800, 455);

        // ✅ Listen for TAB key for Auto-forward
        bedroomScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB && !choicesVisible) {
                autoForwardMode = true;
                autoForwardNext();
            }
        });

        stage.setScene(bedroomScene);
        stage.setResizable(false);
        stage.show();
        
        layout.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case TAB ->
                    toggleAutoForward();
            }
        });
        layout.setFocusTraversable(true); // important for key press detection
    }
    

    private void startDialogue() {
        dialogueLabel.setVisible(true);
        dialogueLabel.setText("");

        DialogueEntry current = model.getCurrentDialogue(currentDialogueIndex);
        if (current == null) return;

        String playerName = model.getPlayerName();

        String speaker = current.getSpeaker().replace("{player}", playerName);
        String fullText = current.getText().replace("{player}", playerName);
        speakerLabel.setText(speaker);

        String characterImage = current.getCharacterImg();
        if (characterImage != null && !characterImage.isEmpty()) {
            characterManage.setCharacterImage("src/assets/characters/" + characterImage);
        }

        String backgroundImage = current.getBackgroundImg();
        if (backgroundImage != null && !backgroundImage.isEmpty()) {
            backgroundManage.setBackgroundImage("src/assets/backgrounds/" + backgroundImage);
        }

        animateText(dialogueLabel, fullText);
    }

    private void showNextDialogue() {
        System.out.println("✅ showNextDialogue() called (index = " + currentDialogueIndex + ")");

        if (isAnimating) {
            isAnimating = false;
            dialogueLabel.setText(fullTextBuffer);
            return;
        }

        if (currentDialogueIndex >= model.getTotalDialogues()) {
            proceedToCrosswordGame(); // Final fallback
            return;
        }

        DialogueEntry current = model.getCurrentDialogue(currentDialogueIndex);
        if (current == null) {
            return;
        }
        
        String sfx = current.getSfx();
        if (sfx != null && !sfx.isEmpty()) {
            SoundManager.playSFX(sfx);
        }

        // Set speaker and text
        String playerName = model.getPlayerName();
        speakerLabel.setText(current.getSpeaker().replace("{player}", playerName));
        fullTextBuffer = current.getText().replace("{player}", playerName);
        dialogueLabel.setText("");

        // Images
        if (current.getCharacterImg() != null && !current.getCharacterImg().isEmpty()) {
            characterManage.setCharacterImage("src/assets/characters/" + current.getCharacterImg());
        }

        if (current.getBackgroundImg() != null && !current.getBackgroundImg().isEmpty()) {
            backgroundManage.setBackgroundImage("src/assets/backgrounds/" + current.getBackgroundImg());
        }

        // Choices
        if ("choice".equalsIgnoreCase(current.getType())) {
            displayChoices(current.getId());
            return;
        }

        animateText(dialogueLabel, fullTextBuffer); // Animate text

        // Advance to next
        int nextId = current.getNextDialogueId();
        if (nextId > 0) {
            currentDialogueIndex = model.getDialogueIndexById(nextId);
        } else {
            currentDialogueIndex++;
        }

        controller.setCurrentDialogueIndex(currentDialogueIndex);

        // If chapter ends, proceed
        if (currentDialogueIndex > getChapterEndIndex(currentChapter)) {
            proceedToCrosswordGame();
        }
    }

    
    private void proceedToCrosswordGame() {
        System.out.println("Chapter finished, proceeding to crossword...");
        CrosswordPlaceholderFX crossword = new CrosswordPlaceholderFX(stage, controller);
        crossword.display();
    }
    
    private int getChapterEndIndex(int chapterNumber) {
        return switch (chapterNumber) {
            case 1 ->
                17;  // End index for Chapter 1
            case 2 ->
                51;  // End index for Chapter 2
            case 3 ->
                106;  // End index for Chapter 3
            case 4 ->
                120; // End index for Chapter 4
            default ->
                Integer.MAX_VALUE; // Assume no end for future chapters yet
        };
    }


    private void animateText(Label label, String text) {
        isAnimating = true;
        fullTextBuffer = text;

        new Thread(() -> {
            try {
                long delay = GameSettings.getInstance().isSkipUnseenText() ? 0 : (long) (30 / GameSettings.getInstance().getTextSpeed());

                for (int i = 0; i < text.length(); i++) {
                    int index = i;
                    if (!isAnimating) {
                        break;
                    }

                    String currentText = text.substring(0, index + 1);
                    Platform.runLater(() -> label.setText(currentText));
                    if (delay > 0) {
                        Thread.sleep(delay);
                    }
                }

                Platform.runLater(() -> {
                    label.setText(text);
                    isAnimating = false;
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void displayChoices(int dialogueId) {
        choicesVisible = true;
        VBox choiceBox = new VBox(15);
        choiceBox.setTranslateY(100);
        choiceBox.setStyle("");

        List<ChoiceEntry> options = model.getChoicesForDialogue(dialogueId);
        for (ChoiceEntry choice : options) {
            Button btn = new Button(choice.getChoiceText());
            btn.setOnAction(e -> {
                int nextId = choice.getNextDialogueId();
                int nextIndex = model.getDialogueIndexById(nextId);
                if (nextIndex != -1) {
                    currentDialogueIndex = nextIndex;
                    controller.setCurrentDialogueIndex(currentDialogueIndex);
                    layout.getChildren().remove(choiceBox);
                    choicesVisible = false;
                }
            });
            choiceBox.getChildren().add(btn);
        }

        layout.getChildren().add(choiceBox);
    }
    
    private void runSkipMode() {
    new Thread(() -> {
        try {
            while (currentDialogueIndex < model.getTotalDialogues()) {
                if (choicesVisible) break;

                // Allow skip to be ~5x faster than normal
                long delay = (long) ((30 / GameSettings.getInstance().getTextSpeed()) / 5);
                isAnimating = false; // Skip animation
                Platform.runLater(this::showNextDialogue);
                Thread.sleep(delay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();
}

    
    private Label createBottomButton(String text, String type) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        label.setOnMouseEntered(e -> label.setStyle("-fx-text-fill: gold; -fx-font-size: 14px; -fx-font-weight: bold;"));
        label.setOnMouseExited(e -> {
            if (label != autoButton || !isAutoForwarding) {
                label.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            }
        });

        // Define behaviors
        if ("Auto".equals(type)) {
            label.setOnMouseClicked(e -> toggleAutoForward());
        } else if ("Skip".equals(type)) {
            label.setOnMouseClicked(e -> {
                e.consume();
                if (!GameSettings.getInstance().isSkipUnseenText()) {
                    System.out.println("⚠ Skipping disabled in preferences.");
                    return;
                }

                skipModeEnabled = !skipModeEnabled;
                autoForwardMode = skipModeEnabled; // enable/disable auto-forward mode

                if (skipModeEnabled) {
                    skipButton.setStyle("-fx-text-fill: darkred; -fx-font-size: 14px; -fx-font-weight: bold;");
                    autoForwardNext(); // ✅ Same behavior as TAB
                } else {
                    skipButton.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
                }
            });
        } else if ("Back".equals(type)) {
            label.setOnMouseClicked(e -> {
                e.consume();
                if (!GameSettings.getInstance().getRollbackMode().equals("Disable") && currentDialogueIndex > 0) {
                    currentDialogueIndex--;
                    showNextDialogue();
                }
            });
        } else if ("Preferences".equals(type)) {
            label.setOnMouseClicked(e -> {
                e.consume();  // ← prevent this click from reaching the layout handler

                GameSettings.getInstance().setAccessedFromMainMenu(false);
                GameSettings.getInstance().setPreviousScene(stage.getScene());

                OptionsMenuFX options = new OptionsMenuFX(stage, controller, GameSettings.getInstance().isAccessedFromMainMenu());
                options.display();
            });
        }


        return label;
    }
    // Toggle the AutoForward
    private void toggleAutoForward() {
    isAutoForwarding = !isAutoForwarding;
    if (isAutoForwarding) {
        autoButton.setStyle("-fx-text-fill: darkred; -fx-font-size: 14px; -fx-font-weight: bold;");
        runAutoForward();
    } else {
        autoButton.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }
}

    private void runAutoForward() {
        new Thread(() -> {
            try {
                while (isAutoForwarding && currentDialogueIndex < model.getTotalDialogues()) {
                    // Wait for animation to finish first
                    while (isAnimating) {
                        Thread.sleep(50); // check again shortly
                    }

                    Thread.sleep(GameSettings.getInstance().getAutoForwardDelay());

                    Platform.runLater(() -> {
                        if (isAutoForwarding && !choicesVisible) {
                            showNextDialogue();
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void autoForwardNext() {
        if (currentDialogueIndex >= model.getTotalDialogues() || choicesVisible || isAnimating) {
            return;
        }

        showNextDialogue();

        new Thread(() -> {
            try {
                long delay = GameSettings.getInstance().isSkipUnseenText()
                        ? 100 // Insta skip delay
                        : GameSettings.getInstance().getAutoForwardDelay();

                Thread.sleep(delay);

                Platform.runLater(() -> {
                    if (!choicesVisible && autoForwardMode && !isAnimating) {
                        autoForwardNext();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public int getDialogueIndex() {
        return currentDialogueIndex;
    }
    
}
