package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Katana extends SuperObject{

    public OBJ_Katana(GamePanel gp){
        name = "Katana";
        type = "weapon";
        tier = 2;
        id = 1;
        equippable = true;
        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/katana/katana.png"));
            down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/katana/katana_down.png"));
            right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/katana/katana_right.png"));
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
