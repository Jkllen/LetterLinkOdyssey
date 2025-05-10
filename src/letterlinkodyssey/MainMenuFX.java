package letterlinkodyssey;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

public class MainMenuFX {
    private Button newGame, loadGame, options, tutorial, exit;
    private ImageView backgroundImageView;
    private Pane root;
    private Scene scene;

    public MainMenuFX(Stage primaryStage) {
        
        AudioClip clickSound = new AudioClip(new File("src\\assets\\sfx\\btnclick.wav").toURI().toString());
        
        SoundManager.playBGM("mainmenutheme.mp3", true);
        
        newGame = new Button("New Game");
        loadGame = new Button("Load Game");
        options = new Button("Options");
        tutorial = new Button ("?");
        
        exit = new Button("Exit");
        
        exit.setOnAction(e -> {
            System.out.println("Exiting game...");
            System.exit(0);
        });
        
        
        backgroundImageView = new ImageView(new Image("file:src/assets/backgrounds/mainmenu.png"));
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(455);
        backgroundImageView.setPreserveRatio(true);
        
        backgroundImageView.setOnMouseClicked(e -> {
            e.consume();  // Prevent the event from propagating further to the layout
        });
        
        // Sound Button
        addClickSound(newGame, clickSound);
        addClickSound(loadGame, clickSound);
        addClickSound(options, clickSound);
        addClickSound(tutorial, clickSound);
        addClickSound(exit, clickSound);
        
        // Style buttons
        styleButton(newGame, "Irish Grover");
        styleButton(loadGame, "Irish Grover");
        styleButton(tutorial, "Irish Grover");
        styleButton(options, "Irish Grover");
        styleButton(exit, "Irish Grover");

        // Set button positions {X,Y}
        setPosition(newGame, 70, 90);
        setPosition(loadGame, 70, 170);
        setPosition(options, 70, 250);
        setPosition(exit, 70, 330);
        setPosition(tutorial, 70, 395);

        // Drop shadow
        DropShadow glow = new DropShadow(20, Color.GOLD);
        addHoverEffect(newGame, glow);
        addHoverEffect(loadGame, glow);
        addHoverEffect(tutorial, glow);
        addHoverEffect(options, glow);
        addHoverEffect(exit, glow);

        root = new Pane();
        root.getChildren().addAll(backgroundImageView, newGame, loadGame, tutorial, options, exit);

        scene = new Scene(root, 800, 455);
    }
    
    

    // Helper Methods
    private void styleButton(Button button, String fontFamily) {
        button.setStyle("-fx-font-size: 24px; -fx-font-family: '" + fontFamily + "'; -fx-text-fill: white; -fx-background-color: transparent; -fx-font-weight: bold;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 24px; -fx-font-family: '" + fontFamily + "'; -fx-text-fill: gold; -fx-background-color: transparent; -fx-font-weight: bold;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 24px; -fx-font-family: '" + fontFamily + "'; -fx-text-fill: white; -fx-background-color: transparent; -fx-font-weight: bold;"));
    }

    private void setPosition(Button button, int x, int y) {
        button.setPrefSize(200, 50);
        button.setLayoutX(x);
        button.setLayoutY(y);
    }

    private void addHoverEffect(Button button, DropShadow glow) {
        button.setOnMouseEntered(e -> button.setEffect(glow));
        button.setOnMouseExited(e -> button.setEffect(null));
    }    
    
    public void addClickSound(Button button, AudioClip sound) {
    button.addEventHandler(ActionEvent.ACTION, e -> sound.play());
    }
    
    
    // Public getters
    public Scene getScene() {
        return scene;
    }

    public Button getNewGameButton() {
        return newGame;
    }

    public Button getLoadGameButton() {
        return loadGame;
    }
    
    public Button getOptionsButton(){
        return options;
    }
    
    public Button getTutorialButton(){
        return tutorial;
    }

    public Pane getRootPane() {
        return root;
    }

    public void setBackgroundImage(String imagePath) {
        backgroundImageView.setImage(new Image("file:src/assets/backgrounds/mainmenu.png"));
    }
}
