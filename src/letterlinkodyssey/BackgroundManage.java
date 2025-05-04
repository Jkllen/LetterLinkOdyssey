package letterlinkodyssey;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BackgroundManage {
    private ImageView backgroundImageView;

    public BackgroundManage() {
        backgroundImageView = new ImageView();
        backgroundImageView.setFitWidth(800);
        backgroundImageView.setFitHeight(455);
        backgroundImageView.setPreserveRatio(false);
    }

    public void setBackgroundImage(String path) {
        Image image = new Image("file:" + path);
        backgroundImageView.setImage(image);
    }

    public ImageView getBackgroundImageView() {
        return backgroundImageView;
    }
}
