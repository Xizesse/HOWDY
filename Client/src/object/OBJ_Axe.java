package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Axe extends SuperObject{

    public OBJ_Axe(GamePanel gp){
        name = "Axe";
        id = 1;
        tier = 2;
        type = "weapon";
        equippable = true;

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/axe/axe.png"));
            up = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/axe/axe_up.png"));
            down = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/axe/axe_down.png"));
            left = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/axe/axe_left.png"));
            right = ImageIO.read(ClassLoader.getSystemResourceAsStream("items/axe/axe_right.png"));

            image = uT.scaleImage(image, gp.tileSize, gp.tileSize);
            up = uT.scaleImage(up, gp.tileSize, gp.tileSize);
            down = uT.scaleImage(down, gp.tileSize, gp.tileSize);
            left = uT.scaleImage(left, gp.tileSize, gp.tileSize);
            right = uT.scaleImage(right, gp.tileSize, gp.tileSize);


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}