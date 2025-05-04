package letterlinkodyssey;

import crosswordjava.GameView;
import crosswordjava.HelpView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CrosswordPlaceholderFX {
    private Stage stage;
    private ControllerFX controller;
    private BackgroundManage backgroundManage;

    public CrosswordPlaceholderFX(Stage stage, ControllerFX controller) {
        this.stage = stage;
        this.controller = controller;
        this.backgroundManage = new BackgroundManage();
    }

    public void display() {
        String bgPath = controller.getModel().getBackgroundImagePathByName("crosstemplate");
        Image bgImage = new Image("file:" + bgPath);
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(800);
        bgView.setFitHeight(455);
        
        // Create crossword views
        GameView gameView = new GameView();
        HelpView helpView = new HelpView();

        // Create a root pane that can swap between game/help
        BorderPane root = new BorderPane();
        root.setCenter(gameView.getRoot());

        // Hook up the “Help” button inside GameView to swap to helpView:
        gameView.getHelpButton().setOnAction(e -> root.setCenter(helpView.getRoot()));
        // And hook the back button in helpView to go back to the grid:
        helpView.getBackButton().setOnAction(e -> root.setCenter(gameView.getRoot()));

        // Intercept crossword completion in GameView:
        //    We’ll piggy‑back on the moment it shows its “Congratulations” alert.
        //    First, expose a callback hook in GameView:
        gameView.setOnComplete(() -> {
            // after they dismiss the “You’ve completed…” alert:
            controller.startNextChapter();
        });
        
        // StackPane to layer background + game
        StackPane layeredRoot = new StackPane();
        
        layeredRoot.getChildren().addAll(bgView, root);

        // Final: show it all
        Scene scene = new Scene(layeredRoot, 800, 455);
        stage.setScene(scene);
        stage.setTitle("Crossword Puzzle");
        stage.show();
    }
}
