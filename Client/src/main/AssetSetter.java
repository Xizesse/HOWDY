package main;

import entity.NPC_Player2;
import object.OBJ_Axe;
import object.OBJ_Helmet;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Helmet(gp);
        gp.obj[0].worldX = 1 * gp.tileSize;
        gp.obj[0].worldY = 2 * gp.tileSize;

        gp.obj[1] = new OBJ_Axe(gp);
        gp.obj[1].worldX = 2 * gp.tileSize;
        gp.obj[1].worldY = 0 * gp.tileSize;

    }

    public void setNPC(){

        gp.player2 = new NPC_Player2(gp);
        gp.player2.worldX = gp.tileSize * 2;
        gp.player2.worldY = gp.tileSize * 2;
    }
}
