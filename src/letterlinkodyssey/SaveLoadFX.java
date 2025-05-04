package letterlinkodyssey;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Scene;
import java.util.Optional;

public class SaveLoadFX {

    private final ControllerFX controller;
    private final boolean isSaveMode;
    private final boolean isAccessedFromMainMenu;
    private final Runnable refreshCallback;

    public SaveLoadFX(ControllerFX controller, boolean isSaveMode, boolean isAccessedFromMainMenu, Runnable refreshCallback) {
        this.controller = controller;
        this.isSaveMode = isSaveMode;
        this.isAccessedFromMainMenu = isAccessedFromMainMenu;
        this.refreshCallback = refreshCallback;
    }
    
    // This method will be used to get the layout for embedding in OptionsMenuFX
    public VBox createLayoutForEmbedding() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: transparent;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);

        int slot = 1;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                VBox slotBox = createSlotBox(slot);
                grid.add(slotBox, col, row);
                slot++;
            }
        }

        root.getChildren().addAll(grid);
        return root;
    }

    // Creates the UI for a single save/load slot
    private VBox createSlotBox(int slot) {
        StackPane slotPane = new StackPane();
        slotPane.setPrefSize(150, 85);
        slotPane.setMaxSize(150, 85);
        slotPane.setMinSize(150, 85);
        slotPane.setStyle("-fx-border-color: white; -fx-border-width: 2; -fx-background-color: #222;");

        VBox box = new VBox(5);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(5));

        // Slot label
        Label slotLabel = new Label("Slot " + slot);
        slotLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");
        box.getChildren().add(slotLabel);

        // Fetch data for the slot
        int dialogueIndex = controller.getModel().getDialogueIndexFromDB(slot);
        String player = controller.getModel().getPlayerNameForSlot(slot);
        boolean saveExists = (player != null && !player.isEmpty()) && dialogueIndex > 0;  // Check if a save exists

        // Add player data label if a save exists
        if (saveExists) {
            Label dataLabel = new Label(player + " @ " + dialogueIndex);
            dataLabel.setStyle("-fx-text-fill: lightgray; -fx-font-size: 11px;");
            box.getChildren().add(dataLabel);
        }

        // Load screenshot thumbnail if exists
        String screenshotPath = "screenshots/slot_" + slot + ".png";
        java.io.File file = new java.io.File(screenshotPath);
        if (file.exists()) {
            ImageView thumbnail = new ImageView("file:" + file.getAbsolutePath());
            thumbnail.setFitWidth(150);
            thumbnail.setFitHeight(80);
            thumbnail.setPreserveRatio(true);
            thumbnail.setSmooth(true);
            thumbnail.setClip(new Rectangle(150, 80));

            box.getChildren().add(thumbnail);
        }

        slotPane.getChildren().add(box);

        // Add a delete button only if in save mode
        if (isSaveMode) {
            Button deleteBtn = new Button("❌");
            deleteBtn.setTooltip(new Tooltip("Delete this slot"));
            deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;");
            deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px;"));
            deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px;"));
            deleteBtn.setOnAction(e -> {
                e.consume();
                controller.getModel().deleteSave(slot);
                refreshCallback.run(); // ✅ refresh UI correctly
            });

            StackPane.setAlignment(deleteBtn, Pos.TOP_RIGHT);
            StackPane.setMargin(deleteBtn, new Insets(5, 5, 0, 0));
            slotPane.getChildren().add(deleteBtn);
        }

        // Handle saving or loading a game when a slot is clicked
        slotPane.setOnMouseClicked(e -> {
            e.consume();
            System.out.println("Slot clicked!");  // Debugging confirmation

            // If in Save Mode
            if (isSaveMode) {
                // If the save already exists in this slot
                if (saveExists) {
                    // Show a confirmation dialog for overwriting the save
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Overwrite Save?");
                    confirm.setHeaderText(null);
                    confirm.setContentText("This slot already contains a save. Overwrite?");
                    Optional<ButtonType> result = confirm.showAndWait();

                    // If user selects Cancel or closes the dialog, do nothing
                    if (result.isEmpty() || result.get() != ButtonType.OK) {
                        System.out.println("Save not overwritten.");
                        return;  // Exit the method without overwriting
                    }
                }
                

                // Proceed to save the game
                System.out.println("Saving game to Slot " + slot);

                // 🔥 Correctly retrieve the latest dialogue index
              
                int currentIndex = controller.getDialogueIndex();
                controller.getModel().saveGameWithScreenshot(slot, GameSettings.getInstance().getPreviousScene(), currentIndex);


                // ♻ Refresh the UI after saving
                refreshCallback.run();

                System.out.println("Game saved successfully.");

            } // If in Load Mode
            else {
                // Proceed only if a save exists in the slot
                if (saveExists) {
                    System.out.println("Attempting to load game from Slot " + slot);
                    boolean loaded = controller.getModel().loadGame(slot);

                    // If loading is successful, continue the game from the correct slot
                    if (loaded) {
                        controller.getModel().continueGameFromSlot(slot, controller);
                        System.out.println("Game loaded successfully from Slot " + slot);
                    } // If loading fails, show an error
                    else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Failed to load the game from this slot.");
                        errorAlert.showAndWait();
                    }
                } else {
                    System.out.println("No save found in Slot " + slot);
                }
            }
        });


        VBox wrapper = new VBox(slotPane);
        wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }
}
