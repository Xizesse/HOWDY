package main;

import entity.NPC;
import entity.NPC_Player2;
import monster.Monster_Spike;
import object.OBJ_Axe;
import object.OBJ_Helmet;
import object.OBJ_Book;
import object.OBJ_PP;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
        System.out.println("Asset setter created");
    }

    public void setObject(){
        System.out.println("Setting up objects from asset setter");
        gp.obj[0] = new OBJ_Helmet(gp);
        gp.obj[0].worldX = 8 * gp.tileSize;
        gp.obj[0].worldY = 15 * gp.tileSize;

        System.out.println("Object 0 is "+gp.obj[0]);
        gp.obj[1] = new OBJ_Axe(gp);
        gp.obj[1].worldX = 6 * gp.tileSize;
        gp.obj[1].worldY = 14 * gp.tileSize;
        System.out.println("Object 1 is "+gp.obj[1]);
        gp.obj[2] = new OBJ_Book(gp);
        gp.obj[2].worldX = 6 * gp.tileSize;
        gp.obj[2].worldY = 17 * gp.tileSize;
        System.out.println("Object 2 is "+gp.obj[2]);
        gp.obj[3] = new OBJ_PP(gp);
        gp.obj[3].worldX = 14 * gp.tileSize;
        gp.obj[3].worldY = 17 * gp.tileSize;
        System.out.println("Object 3 is "+gp.obj[3]);
        //print all objects and their iD
        for(int j = 0; j < gp.obj.length; j++){
            if(gp.obj[j] != null){
                System.out.println("Object index: "+j+" Object ID: "+gp.obj[j].id);
            }
        }
    }

    public void setPlayer2(){

        gp.player2 = new NPC_Player2(gp);
        gp.player2.worldX = gp.tileSize * 3;
        gp.player2.worldY = gp.tileSize * 15;

    }

    public void setNPC(){
        System.out.println("Setting up NPC");

        gp.npc[0] = new NPC(gp);
        gp.npc[0].worldX =  gp.tileSize * 5;
        gp.npc[0].worldY =  gp.tileSize * 15;

        gp.npc[1] = new NPC(gp);
        gp.npc[1].worldX =  gp.tileSize * 7;
        gp.npc[1].worldY =  gp.tileSize * 15;

        gp.npc[2] = new NPC(gp);
        gp.npc[2].worldX =  gp.tileSize * 5;
        gp.npc[2].worldY =  gp.tileSize * 17;

        gp.npc[3] = new NPC(gp);
        gp.npc[3].worldX =  gp.tileSize * 3;
        gp.npc[3].worldY =  gp.tileSize * 17;

        System.out.println("NPC set up");
    }
    public void setMonster(){
        gp.monster[0] = new Monster_Spike(gp);
        gp.monster[0].worldX = 4 * gp.tileSize;
        gp.monster[0].worldY = 16 * gp.tileSize;

        gp.monster[1] = new Monster_Spike(gp);
        gp.monster[1].worldX = 2 * gp.tileSize;
        gp.monster[1].worldY = 16 * gp.tileSize;

    }
}
