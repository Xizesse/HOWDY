package object;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{
    public OBJ_Key(){
        name = "Key";

        try{
            sprite = ImageIO.read(getClass().getResourceAsStream("res/player/ironHelmet_down.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
