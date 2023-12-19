package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Firefly extends SuperObject{
    public OBJ_Firefly(GamePanel gp){
        name = "firefly";
        type = "pet";
        id = 1;
        equippable = true;
        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/firefly.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/firefly_small.png"));
            up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
            down = up;
            left = up;
            right = up;


        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
