package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Rune extends SuperObject{
    char shape = 'a';
    public OBJ_Rune(GamePanel gp, char shape) {
        name = "Rune";
        type = "run";
        this.shape = shape;

        id = 1;
        equippable = false;

        if (shape == 'l') {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/runes/rune_L.png"));
                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (shape == 'p') {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/runes/rune_pause.png"));
                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (shape == 'f') {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/runes/rune_fire.png"));
                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (shape == 'x') {
            try {
                image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/runes/rune_X.png"));
                image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
