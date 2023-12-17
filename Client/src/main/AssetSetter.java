package main;

import entity.NPC_Rat;
import entity.NPC_Player;
import entity.NPC_Shark;
import monster.Monster_Eye;
import monster.Monster_Spike;
import monster.Monster_Stalker;
import object.*;

public class AssetSetter {
    GamePanel gp;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){
        gp.obj[0][0] = new OBJ_Helmet(gp, "iron");
        gp.obj[0][0].worldX = 8 * gp.tileSize;
        gp.obj[0][0].worldY = 13 * gp.tileSize;

        gp.obj[0][1] = new OBJ_Axe(gp);
        gp.obj[0][1].worldX = 8 * gp.tileSize;
        gp.obj[0][1].worldY = 14 * gp.tileSize;
        gp.obj[0][2] = new OBJ_Book(gp);
        gp.obj[0][2].worldX = 6 * gp.tileSize;
        gp.obj[0][2].worldY = 17 * gp.tileSize;

        int[][] temp = {
            {14, 17, 12},
            {14, 16, 8},
            {14, 15, 8},
            {14, 14, 13}
        };
        int[][] temp2 = {
            {14, 17, 4},
            {14, 16, 11},
            {14, 15, 11},
            {14, 14, 5}
        };
        gp.obj[0][3] = new OBJ_PP(gp, 4,temp, temp2);
        gp.obj[0][3].worldX = 14 * gp.tileSize;
        gp.obj[0][3].worldY = 17 * gp.tileSize;

        temp = new int[][]{
            {17, 14, 12},
            {17, 15, 8},
            {17, 16, 8},
            {17, 17, 13}
        };
        temp2 = new int[][]{
                {17, 14, 4},
                {17, 15, 11},
                {17, 16, 11},
                {17, 17, 5}
        };
        gp.obj[0][4] = new OBJ_PP(gp, 5, temp, temp2);
        gp.obj[0][4].worldX = 17 * gp.tileSize;
        gp.obj[0][4].worldY = 14 * gp.tileSize;

        //print all objects and their iD
        gp.obj[0][5] = new OBJ_Katana(gp);
        gp.obj[0][5].worldX = 8 * gp.tileSize;
        gp.obj[0][5].worldY = 15 * gp.tileSize;

        gp.obj[0][6] = new OBJ_Stick(gp);
        gp.obj[0][6].worldX = 8 * gp.tileSize;
        gp.obj[0][6].worldY = 16 * gp.tileSize;

        gp.obj[0][7] = new OBJ_Shield(gp);
        gp.obj[0][7].worldX = 8 * gp.tileSize;
        gp.obj[0][7].worldY = 17 * gp.tileSize;

        gp.obj[0][8] = new OBJ_Firefly(gp);
        gp.obj[0][8].worldX = 8 * gp.tileSize;
        gp.obj[0][8].worldY = 19 * gp.tileSize;

        gp.obj[0][9] = new OBJ_Rune(gp, 'l');
        gp.obj[0][9].worldX = 1 * gp.tileSize;
        gp.obj[0][9].worldY = 14 * gp.tileSize;

        gp.obj[0][10] = new OBJ_Rune(gp, 'p');
        gp.obj[0][10].worldX = 1 * gp.tileSize;
        gp.obj[0][10].worldY = 15 * gp.tileSize;

        gp.obj[0][11] = new OBJ_Rune(gp, 'x');
        gp.obj[0][11].worldX = 1 * gp.tileSize;
        gp.obj[0][11].worldY = 16 * gp.tileSize;

        gp.obj[0][12] = new OBJ_Rune(gp, 'f');
        gp.obj[0][12].worldX = 1 * gp.tileSize;
        gp.obj[0][12].worldY = 17 * gp.tileSize;



        gp.obj[0][13] = new OBJ_Armour(gp, "iron");
        gp.obj[0][13].worldX = 2 * gp.tileSize;
        gp.obj[0][13].worldY = 14 * gp.tileSize;

        gp.obj[0][14] = new OBJ_Helmet(gp, "iron");
        gp.obj[0][14].worldX = 2 * gp.tileSize;
        gp.obj[0][14].worldY = 13 * gp.tileSize;

        gp.obj[0][15] = new OBJ_Armour(gp, "gold");
        gp.obj[0][15].worldX = 2 * gp.tileSize;
        gp.obj[0][15].worldY = 15 * gp.tileSize;

        gp.obj[0][16] = new OBJ_Helmet(gp, "gold");
        gp.obj[0][16].worldX = 2 * gp.tileSize;
        gp.obj[0][16].worldY = 16 * gp.tileSize;

        //map 1
    }

    public void setPlayer2(){
        gp.player2 = new NPC_Player(gp, 0);
        gp.player2.worldX = gp.tileSize * 3;
        gp.player2.worldY = gp.tileSize * 15;
    }

    public void setNPC(){

        gp.npc[0][0] = new Monster_Eye(gp, 0);
        gp.npc[0][0].worldX =  gp.tileSize * 5;
        gp.npc[0][0].worldY =  gp.tileSize * 15;
        gp.npc[0][1] = new Monster_Eye(gp,0);
        gp.npc[0][1].worldX =  gp.tileSize * 7;
        gp.npc[0][1].worldY =  gp.tileSize * 15;

        gp.npc[0][2] = new Monster_Spike(gp, 0);
        gp.npc[0][2].worldX = 4 * gp.tileSize;
        gp.npc[0][2].worldY = 15 * gp.tileSize;

        gp.npc[0][3] = new Monster_Spike(gp,0);
        gp.npc[0][3].worldX = 6 * gp.tileSize;
        gp.npc[0][3].worldY = 16 * gp.tileSize;

        gp.npc[0][4] = new Monster_Stalker(gp,0);
        gp.npc[0][4].worldX = 5 * gp.tileSize;
        gp.npc[0][4].worldY = 16 * gp.tileSize;


        //map 2
        gp.npc[1][0] = new Monster_Eye(gp,1);
        gp.npc[1][0].worldX = 16 * gp.tileSize;
        gp.npc[1][0].worldY = 16 * gp.tileSize;

        gp.npc[1][1] = new Monster_Eye(gp,1);
        gp.npc[1][1].worldX = 1 * gp.tileSize;
        gp.npc[1][1].worldY = 2 * gp.tileSize;

        gp.npc[1][2] = new Monster_Spike(gp,1);
        gp.npc[1][2].worldX = 1 * gp.tileSize;
        gp.npc[1][2].worldY = 3 * gp.tileSize;


        //map 3
        /*
        gp.npc[2][0] = new NPC_Shark(gp);
        gp.npc[2][0].worldX =  gp.tileSize * 11;
        gp.npc[2][0].worldY =  gp.tileSize * 4;
        gp.npc[2][1] = new NPC_Shark(gp);
        gp.npc[2][1].worldX =  gp.tileSize * 12;
        gp.npc[2][1].worldY =  gp.tileSize *22;
        gp.npc[2][2] = new NPC_Shark(gp);
        gp.npc[2][2].worldX =  gp.tileSize * 12;
        gp.npc[2][2].worldY =  gp.tileSize * 5;
        gp.npc[2][3] = new NPC_Shark(gp);
        gp.npc[2][3].worldX =  gp.tileSize * 5;
        gp.npc[2][3].worldY =  gp.tileSize * 23;
        gp.npc[2][4] = new NPC_Shark(gp);
        gp.npc[2][4].worldX =  gp.tileSize * 5;
        gp.npc[2][4].worldY =  gp.tileSize * 6;
        */



    }

}
