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
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
