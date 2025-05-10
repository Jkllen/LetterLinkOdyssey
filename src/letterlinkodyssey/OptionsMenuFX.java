package letterlinkodyssey;

import java.io.File;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;


public class OptionsMenuFX {
    private Stage stage;
    private ControllerFX controller;
    private Button btnSave, btnLoad, btnPreferences, btnReturn;
    private final VBox rightPane = new VBox();
    private final GridPane settingsGrid = new GridPane();
    private Label titleLabel; // Title label for dynamic updates
    private boolean isAccessedFromMainMenu;

    public OptionsMenuFX(Stage stage, ControllerFX controller, boolean isAccessedFromMainMenu) {
        this.stage = stage;
        this.controller = controller;
        this.isAccessedFromMainMenu = isAccessedFromMainMenu;
    }
    
        public void display() {
        
        AudioClip clickSound = new AudioClip(new File("src\\assets\\sfx\\btnclick.wav").toURI().toString());
        
        // Background
        String bgPath = controller.getModel().getBackgroundImagePathByName("settingsmenu");
        Image bgImage = new Image("file:" + bgPath);
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(800);
        bgView.setFitHeight(455);

        // Left Panel
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(40, 10, 10, 40));
        leftPanel.setPrefWidth(180);
        
        Button btnStart = createNavButton("Start", e -> {
                updateTitle("Start");
                new NameEntryFX(stage, controller).display();
            });

        Button btnSave = createNavButton("Save", e -> {
            showSaveLoadPanel(true);
            updateTitle("Save");
        });
        
        // Disable the save button if accessed from the main menu
        if (isAccessedFromMainMenu) {
            btnSave.setDisable(true); // Disable the button when accessed from the main menu
        }
        
        Button btnLoad = createNavButton("Load", e -> {
            showSaveLoadPanel(false);
            updateTitle("Load");
        });
        Button btnPreferences = createNavButton("Preferences", e -> {
            showPreferencesPanel(); // Show preferences when clicked
            updateTitle("Preferences");
        });
        
        Button btnMainMenu = createNavButton("Main Menu", e -> {
            /*
            * Go Back to the Main Menu
            */
            stage.setScene(controller.getView().getScene());
        });
        
        Button btnQuit = createNavButton("Quit", e -> {
            System.out.println("Exiting game...");
            System.exit(0);
        });

        btnReturn = createNavButton("Return", e -> {
            Scene returnTo = GameSettings.getInstance().getPreviousScene();
            if (returnTo != null) {
                stage.setScene(returnTo);
            } else {
                stage.setScene(controller.getView().getScene()); // fallback to main menu
            }
        });
        
        // Sound Button
        addClickSound(btnStart, clickSound);
        addClickSound(btnSave, clickSound);
        addClickSound(btnLoad, clickSound);
        addClickSound(btnPreferences, clickSound);
        addClickSound(btnMainMenu, clickSound);
        addClickSound(btnQuit, clickSound);
        addClickSound(btnReturn, clickSound);

        leftPanel.getChildren().addAll(btnStart, btnSave, btnLoad, btnPreferences, btnMainMenu, btnQuit, btnReturn);

        // Right Panel
        rightPane.setPadding(new Insets(40));
        rightPane.setAlignment(Pos.TOP_LEFT);
        rightPane.setPrefWidth(600);
        rightPane.setStyle("-fx-background-color: transparent;");

        // Initialize settings grid
        settingsGrid.setHgap(50);
        settingsGrid.setVgap(20);
        settingsGrid.setPadding(new Insets(20));

        // Title Label
        titleLabel = new Label("Letter Link Odyssey");
        titleLabel.setPrefWidth(500);
        titleLabel.setTranslateX(25);
        titleLabel.setTranslateY(25);
        titleLabel.setPadding(new Insets(20, 0, 20, 0));
        titleLabel.setStyle("-fx-text-fill: darkred; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox layout = new HBox(leftPanel, rightPane);
        VBox mainLayout = new VBox(titleLabel, layout);

        StackPane root = new StackPane(bgView, mainLayout);

        Scene scene = new Scene(root, 800, 455);
        stage.setScene(scene);
        stage.show();
    }

    private void showPreferencesPanel() {
        rightPane.getChildren().clear(); // Clear the right pane
        addPreferencesSettings(); // Add preferences settings to the right pane
        rightPane.getChildren().add(settingsGrid); // Add the settings grid for preferences
    }

    private void updateTitle(String title) {
        titleLabel.setText(title); // Update the title label text
    }

    private Button createNavButton(String text, EventHandler<ActionEvent> eventHandler) {
        Button btn = new Button(text);
        btn.setPrefWidth(160);
        btn.setTranslateY(-25);
        btn.setStyle("-fx-text-fill: #8b6b4a; -fx-font-size: 16px; -fx-font-weight: bold; " +
                      "-fx-background-radius: 15; -fx-padding: 5 15; -fx-cursor: hand;");
        
        btn.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> e.consume());
        
        btn.setOnAction(eventHandler); // Set the action event
        return btn;
    }
    
    

    private void showSaveLoadPanel(boolean isSaveMode) {
        rightPane.getChildren().clear();

        // Create the SaveLoadFX instance and pass in a refresh callback
        SaveLoadFX saveLoadFX = new SaveLoadFX(controller, isSaveMode, false, () -> showSaveLoadPanel(isSaveMode));

        VBox saveLoadLayout = saveLoadFX.createLayoutForEmbedding();
        rightPane.getChildren().add(saveLoadLayout);
    }



    private void addPreferencesSettings() {
        // Rollback Mode
        Label rollbackTitle = new Label("Rollback Mode");
        rollbackTitle.setStyle("-fx-text-fill: #5a3921; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label disable = new Label("Disable");
        Label left = new Label("Left");
        Label right = new Label("Right");


        List<Label> rollbackOptions = List.of(disable, left, right);
        for (Label label : rollbackOptions) {
            label.setStyle("-fx-text-fill: #8b6b4a; -fx-font-size: 16px; -fx-font-weight: bold; " +
                      "-fx-background-radius: 10; -fx-padding: 5 15; -fx-cursor: hand;");
            label.setOnMouseClicked(e -> {
                GameSettings.getInstance().setRollbackMode(label.getText());

                // Highlight selected one
                for (Label lbl : rollbackOptions) {
                    boolean isSelected = lbl == label;
                    lbl.setStyle("-fx-text-fill: " + (isSelected ? "#ffffff" : "#8b6b4a")
                            + "; -fx-font-size: 16px; -fx-font-weight: bold; "
                            + "-fx-background-radius: 10; -fx-padding: 5 15; -fx-cursor: hand; "
                            + (isSelected ? "-fx-background-color: #5a3921;" : ""));
                }

                // Save to DB
                GameSettings.getInstance().saveSettingsToDatabase();
            });
        }

        // Initialize selected style
        String current = GameSettings.getInstance().getRollbackMode();
        for (Label label : rollbackOptions) {
            if (label.getText().equalsIgnoreCase(current)) {
                label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold; "
                        + "-fx-background-radius: 10; -fx-padding: 5 15; -fx-cursor: hand; "
                        + "-fx-background-color: #5a3921;");
            }
        }

        HBox rollbackBox = new HBox(20, disable, left, right);
        rollbackBox.setTranslateX(-20);

        // Skip unseen checkbox
        Label skipLabel = new Label("Skip");
        skipLabel.setTranslateX(50);
        skipLabel.setStyle("-fx-text-fill: #5a3921; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button skipUnseenBtn = new Button("Unseen Text");
        skipUnseenBtn.setStyle("-fx-text-fill: #8b6b4a; -fx-font-size: 16px; -fx-font-weight: bold; " +
                      "-fx-background-radius: 10; -fx-padding: 5 15; -fx-cursor: hand;");
        skipUnseenBtn.setPrefWidth(160);
        skipUnseenBtn.setTranslateX(-10);
        skipUnseenBtn.setStyle(getToggleStyle(GameSettings.getInstance().isSkipUnseenText()));

        skipUnseenBtn.setOnAction(e -> {
            boolean newValue = !GameSettings.getInstance().isSkipUnseenText();
            GameSettings.getInstance().setSkipUnseenText(newValue);
            skipUnseenBtn.setStyle(getToggleStyle(newValue));
            GameSettings.getInstance().saveSettingsToDatabase(); // persist
        });

        // Text Speed slider
        Label textSpeedLabel = new Label("Text Speed");
        textSpeedLabel.setStyle("-fx-text-fill: #5a3921; -fx-font-size: 18px; -fx-font-weight: bold;");
        Slider textSpeedSlider = new Slider(0, 1, GameSettings.getInstance().getTextSpeed());
        textSpeedSlider.getStylesheets().add(
                getClass().getResource("styling.css").toExternalForm()
        );
        textSpeedSlider.setShowTickLabels(true);
        textSpeedSlider.setShowTickMarks(true);
        textSpeedSlider.setMajorTickUnit(1);
        textSpeedSlider.setBlockIncrement(0.1);

        // Replace 0 and 1 tick labels with custom strings
        textSpeedSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == 0.0) {
                    return "Slow";
                }
                if (value == 1.0) {
                    return "Fast";
                }
                return "";
            }

            @Override
            public Double fromString(String string) {
                return switch (string) {
                    case "Slow" -> 0.0;
                    case "Fast" -> 1.0;
                    default -> 0.5;
                };
            }
        });

        textSpeedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            GameSettings.getInstance().setTextSpeed(newVal.doubleValue());
        });

        textSpeedSlider.setTranslateX(-25);

        // Auto-forward slider
        Label autoForwardLabel = new Label("Auto-Forward Time");
        autoForwardLabel.setStyle("-fx-text-fill: #5a3921; -fx-font-size: 18px; -fx-font-weight: bold;");
        Slider autoForwardSlider = new Slider(0, 1, GameSettings.getInstance().getAutoForwardTime());
        autoForwardSlider.getStylesheets().add(
                getClass().getResource("styling.css").toExternalForm()
        );
            
        autoForwardSlider.setShowTickLabels(true);
        autoForwardSlider.setShowTickMarks(true);
        autoForwardSlider.setMajorTickUnit(1);
        autoForwardSlider.setBlockIncrement(0.1);

        // Replace 0 and 1 tick labels with custom strings
        autoForwardSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                if (value == 0.0) {
                    return "Fast";
                }
                if (value == 1.0) {
                    return "Slow";
                }
                return "";
            }

            @Override
            public Double fromString(String string) {
                return switch (string) {
                    case "Fast" -> 0.0;
                    case "Slow" -> 1.0;
                    default -> 0.5;
                };
            }
        });

        autoForwardSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            GameSettings.getInstance().setAutoForwardTime(newVal.doubleValue());
        });

        // Add all components to the settings grid
        settingsGrid.add(rollbackTitle, 0, 0);
        settingsGrid.add(rollbackBox, 0, 1);
        settingsGrid.add(skipLabel, 1, 0); // Add the "Skip" label
        settingsGrid.add(skipUnseenBtn, 1, 1); // Add the "Unseen Text" button
        settingsGrid.add(textSpeedLabel, 0, 2);
        settingsGrid.add(textSpeedSlider, 0, 3);
        settingsGrid.add(autoForwardLabel, 1, 2);
        settingsGrid.add(autoForwardSlider, 1, 3);
    }
    
     private String getToggleStyle(boolean enabled) {
        return enabled
                ? "-fx-background-color: rgba(139, 107, 74, 0.3); "
                + // Light brown semi-transparent
                "-fx-text-fill: #5a1a1a; "
                + // Darker red-brown for active state
                "-fx-font-size: 14px; "
                + "-fx-font-weight: bold; "
                + "-fx-border-color: #5a1a1a; "
                + "-fx-border-radius: 10; "
                + "-fx-background-radius: 10; "
                + "-fx-padding: 5 10;"
                : "-fx-background-color: transparent; "
                + "-fx-text-fill: #8b6b4a; "
                + // Medium brown for inactive
                "-fx-font-size: 14px; "
                + "-fx-border-color: transparent; "
                + "-fx-padding: 5 10;";
    }

    private RadioButton styledRadio(String label, ToggleGroup group, boolean selected) {
        RadioButton rb = new RadioButton(label);
        rb.setToggleGroup(group);
        rb.setSelected(selected);
        rb.setStyle("-fx-text-fill: white;");
        return rb;
    }

    public Button getReturnButton() {
        return btnReturn;
    }
    
    public void addClickSound(Button button, AudioClip sound) {
        button.addEventHandler(ActionEvent.ACTION, e -> sound.play());
    }
}