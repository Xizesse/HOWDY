package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Stick extends SuperObject{

    public OBJ_Stick(GamePanel gp){
        name = "Stick";
        type = "weapon";
        id = 1;

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/stick/stick.png"));
            down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/stick/stick_down.png"));
            right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/stick/stick_right.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
            right = uT.scaleImage(right, gp.tileSize, gp.tileSize);
            left = down;
            up = left;

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
