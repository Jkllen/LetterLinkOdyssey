package letterlinkodyssey;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

public class NameEntryFX {
    private Stage stage;
    private ControllerFX controller;

    public NameEntryFX(Stage stage, ControllerFX controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void display() {
        String bgPath = controller.getModel().getBackgroundImagePathByName("namefield");

        if (bgPath == null || bgPath.isEmpty()) {
            System.out.println("No background found for namefield.");
            return;
        }
        
        AudioClip clickSound = new AudioClip(new File("src\\assets\\sfx\\btnclick.wav").toURI().toString());
        
        Image bgImage = new Image("file:" + bgPath);
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(800);
        bgView.setFitHeight(455);
        bgView.setPreserveRatio(false);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.setPrefWidth(360);
        nameField.setLayoutX(200);
        nameField.setLayoutY(194);
        nameField.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
        
        Button submitBtn = new Button("SUBMIT");
        submitBtn.setPrefSize(345, 43);
        submitBtn.setLayoutX(205);
        submitBtn.setLayoutY(275);
        submitBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 25px; -fx-font-weight: bold;");
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-font-size: 24px; -fx-font-family: 'Irish Grover'; -fx-text-fill: gold; -fx-background-color: transparent; -fx-font-weight: bold;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-font-size: 24px; -fx-font-family: 'Irish Grover'; -fx-text-fill: white; -fx-background-color: transparent; -fx-font-weight: bold;"));
        
        // Sound Button
        addClickSound(submitBtn, clickSound);

        submitBtn.setOnAction(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                System.out.println("Name submitted: " + playerName);
                controller.savePlayerName(playerName);
            }
        });

        Pane root = new Pane();
        root.getChildren().addAll(bgView, nameField, submitBtn);

        Scene scene = new Scene(root, 800, 455);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public void addClickSound(Button button, AudioClip sound) {
        button.addEventHandler(ActionEvent.ACTION, e -> sound.play());
    }
}
