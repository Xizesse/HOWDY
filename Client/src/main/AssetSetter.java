package main;

import entity.NPC_Player2;
import object.OBJ_Axe;
import object.OBJ_Helmet;
import object.OBJ_Book;
import object.OBJ_PP;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Helmet(gp);
        gp.obj[0].worldX = 8 * gp.tileSize;
        gp.obj[0].worldY = 15 * gp.tileSize;

        gp.obj[1] = new OBJ_Axe(gp);
        gp.obj[1].worldX = 6 * gp.tileSize;
        gp.obj[1].worldY = 14 * gp.tileSize;

        gp.obj[2] = new OBJ_Book(gp);
        gp.obj[2].worldX = 6 * gp.tileSize;
        gp.obj[2].worldY = 17 * gp.tileSize;

        gp.obj[3] = new OBJ_PP(gp);
        gp.obj[3].worldX = 14 * gp.tileSize;
        gp.obj[3].worldY = 17 * gp.tileSize;

    }

    public void setNPC(){

        gp.player2 = new NPC_Player2(gp);
        gp.player2.worldX = gp.tileSize * 2;
        gp.player2.worldY = gp.tileSize * 2;
    }
}
