package object;


import main.GamePanel;


import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shield extends SuperObject {

    public OBJ_Shield(GamePanel gp){
        name = "Stick";
        type = "shield";
        id = 1;
        tier = 1;
        equippable = true;
        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/shield/shield.png"));
            up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/shield/shield_up.png"));
            down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/shield/shield_down.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
            down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
            left = down;
            right = up;
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
