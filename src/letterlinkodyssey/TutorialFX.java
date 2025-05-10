package letterlinkodyssey;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class TutorialFX {
    private Stage stage;
    private ControllerFX controller;
    private int step = 0;
    private BackgroundManage backgroundManage;
    private VBox textWrapper;

    public TutorialFX(Stage stage, ControllerFX controller) {
        this.stage = stage;
        this.controller = controller;
        this.backgroundManage = new BackgroundManage();
    }

    public void display() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Text tutorialText = new Text("Welcome to the tutorial! Let's learn how to play.");
        tutorialText.setWrappingWidth(400); 
        tutorialText.setStyle("-fx-font-size: 18px; -fx-fill: white;");
        
        textWrapper = new VBox(tutorialText);
        textWrapper.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); -fx-padding: 15; -fx-background-radius: 10;");
        textWrapper.setMaxWidth(50);
        textWrapper.setMaxHeight(90);
        textWrapper.setAlignment(Pos.CENTER); 

        
        // Buttons for navigation
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setTranslateY(160);
        
        
        Button backBtn = new Button("←");
        backBtn.setAlignment(Pos.CENTER_LEFT);
        Button nextBtn = new Button("→");
        nextBtn.setAlignment(Pos.CENTER_RIGHT);

        String buttonStyle = "-fx-font-size: 24px; -fx-padding: 10 20; -fx-background-radius: 8; "
                           + "-fx-background-color: rgba(255,255,255,0.8); -fx-text-fill: black;";
        backBtn.setStyle(buttonStyle);
        nextBtn.setStyle(buttonStyle);

        // Button actions
        nextBtn.setOnAction(e -> {
            if (step < 16) {
                step++;
                updateStep(tutorialText, nextBtn);
            } else {
                stage.setScene(controller.getView().getScene()); // Return to main menu
            }
        });

        backBtn.setOnAction(e -> {
            if (step > 0) {
                step--;
                updateStep(tutorialText, nextBtn);
            }
        });

        buttonBox.getChildren().addAll(backBtn, nextBtn);

        // Layout to contain both the text and buttons
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundManage.getBackgroundImageView(), textWrapper, buttonBox);

        // Align the buttonBox to the bottom of the screen
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);

        // Align the textWrapper to the center of the screen
        StackPane.setAlignment(textWrapper, Pos.CENTER);

        updateBackground("tutorial_step_0");

        stage.setScene(new Scene(root, 800, 455));
    }


    private void updateStep(Text tutorialText, Button nextBtn) {
        
        if (step == 10) {
            StackPane.setAlignment(textWrapper, Pos.CENTER_RIGHT);
        } else if (step == 11) {
            StackPane.setAlignment(textWrapper, Pos.CENTER_LEFT);
        } else {
            StackPane.setAlignment(textWrapper, Pos.CENTER); // default
        }
     
        double targetX;
        if (step == 10) {
            targetX = 150; // Slide to right
        } else if (step == 11) {
            targetX = -4; // Slide to left
        } else {
            targetX = 0; // Center
        }
        
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), textWrapper);
        slide.setToX(targetX);
        slide.play();
        
        switch (step) {
            
            case 0 -> {
                tutorialText.setText("Welcome to the Letter Link Odyssey tutorial!");
                nextBtn.setText("→");
            }
            case 1 -> tutorialText.setText("Being here means you must be new to visual novels? Come sit tight and let me help you!");
            case 2 -> tutorialText.setText("Here’s the dialogue box. Characters will talk to you here. Click the screen to proceed to next dialogue.");
            case 3 -> tutorialText.setText("Below this, you'll see the bottom navigator preferences settings that would help you change settings. Try clicking one in the real game!");
            case 4 -> tutorialText.setText("First, the 'preference' will lead you to the preference settings!");
            case 5 -> tutorialText.setText("Second, 'skip', a powerful tool in case you want to skip dialogues and speed throughout the story. Incredibly helpful in case of loading bugs, and you want to get back to your previous scene quickly.");
            case 6 -> tutorialText.setText("Third, 'auto', the best settings in every visual novel game, lazy to click? just want an automatic slideshow? Then here it is!");
            case 7 -> tutorialText.setText("Fourth, 'back', missed a dialogue? Do not worry, rollback feature will help you just fine! Make sure it is enabled in the settings though. (NOTE: do not use back at a choice dialogue trust me on this >_<)");
            case 8 -> tutorialText.setText("Here at the left side is the choices button. Try clicking every single one of them when you try out the game!");
            case 9 -> tutorialText.setText("Welcome to the crossword! a minigame applying backtracking algorithm!");
            case 10 -> tutorialText.setText("Left side is the crossword grid.");
            case 11 -> tutorialText.setText("Right side is the list you need to figure out.");
            case 12 -> tutorialText.setText("Here is your check, health count and hint. NOTE: only check if you are sure otherwise you will lose 1 health points for every incorrect inputs! Also, hint is limited in each difficulty, use them wisely.");
            case 13 -> tutorialText.setText("This is the difficulty mode, change freely if you want!");
            case 14 -> tutorialText.setText("This is the help button! try clicking it in-game to show how the instructions!");
            case 15 -> tutorialText.setText("Finally, new game! this will generate new crossword grid!");
            case 16 -> {
                tutorialText.setText("This is the end of the tutorial I hope you will have fun!");
                nextBtn.setText("🏠"); // Optional: home icon
            }
        }
        updateBackground("tutorial_step_" + step);
    }

    private void updateBackground(String name) {
        String bgPath = controller.getModel().getBackgroundImagePathByName(name);
        if (bgPath != null) {
            backgroundManage.setBackgroundImage(bgPath);
        } else {
            System.out.println("No background found for: " + name);
        }
    }
}
