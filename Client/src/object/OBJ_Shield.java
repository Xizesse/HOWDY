package object;


import main.GamePanel;


import object.SuperObject;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Shield extends SuperObject {

    public OBJ_Shield(GamePanel gp){
        name = "Stick";
        id = 1;

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/shield.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
