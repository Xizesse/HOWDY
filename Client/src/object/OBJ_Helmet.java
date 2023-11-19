package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Helmet extends SuperObject{

    public OBJ_Helmet(GamePanel gp){
        name = "Helmet";

        try{
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream("player/ironHelmet_down.png"));
            uT.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
