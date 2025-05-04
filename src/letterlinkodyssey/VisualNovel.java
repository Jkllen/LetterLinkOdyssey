//Main runnable
package letterlinkodyssey;

import javafx.application.Application;
import javafx.stage.Stage;

public class VisualNovel extends Application {
    private ModelFX model;
    private MainMenuFX view;
    private ControllerFX controller;

    @Override
    public void start(Stage primaryStage) {
        model = new ModelFX();
        view = new MainMenuFX(primaryStage);
        controller = new ControllerFX(model, view, primaryStage);

        //Set the scene and show the window
        primaryStage.setScene(view.getScene());
        primaryStage.setTitle("Letter Link Odessey");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
