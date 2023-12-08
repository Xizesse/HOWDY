package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Katana extends SuperObject{

    public OBJ_Katana(GamePanel gp){
        name = "Katana";
        type = "weapon";
        id = 1;

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/katana/katana.png"));
            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
