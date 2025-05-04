package letterlinkodyssey;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CharacterManage {
    private ImageView characterImageView;

    public CharacterManage() {
        characterImageView = new ImageView();
        characterImageView.setFitWidth(350);
        characterImageView.setScaleX(1.1);
        characterImageView.setScaleY(1.1);

        characterImageView.setPreserveRatio(true);
        characterImageView.setTranslateY(-60); // Position adjustment
        characterImageView.setTranslateX(90);
    }

    public void setCharacterImage(String path) {
        if (path != null && !path.isEmpty()) {
            Image image = new Image("file:" + path);
            characterImageView.setImage(image);
        }
    }

    public ImageView getCharacterImageView() {
        return characterImageView;
    }
}
