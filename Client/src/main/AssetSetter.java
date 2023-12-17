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

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {
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
        gp.obj[0][3] = new OBJ_PP(gp, 4, temp, temp2);
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


        temp = new int[][]{
                {7, 8, 12},
                {8, 8, 9},
                {8, 8, 9},
                {9, 8, 9},
                {10, 8, 9},
                {11, 8, 9},
                {11, 7, 8},
                {11, 5, 9},
                {12, 5, 9},
                {13, 5, 9},
                {14, 5, 9},
                {14, 4, 8},
                {15, 4, 9},
                {16, 4, 9},
                {17, 4, 9},
                {18, 4, 8},
                {18, 3, 9},
                {19, 3, 9},
                {20, 3, 9},
                {21, 3, 9},
                {23, 3, 8},
                {23, 4, 9},
                {24, 4, 8},
                {24, 5, 9},
                {25, 5, 13}
        };
        temp2 = new int[][]{
                {7, 8, 4},
                {8, 8, 10},
                {8, 8, 10},
                {9, 8, 10},
                {10, 8, 10},
                {11, 8, 10},
                {11, 7, 11},
                {11, 5, 10},
                {12, 5, 10},
                {13, 5, 10},
                {14, 5, 10},
                {14, 4, 11},
                {15, 4, 10},
                {16, 4, 10},
                {17, 4, 10},
                {18, 4, 11},
                {18, 3, 10},
                {19, 3, 10},
                {20, 3, 10},
                {21, 3, 10},
                {23, 3, 11},
                {23, 4, 10},
                {24, 4, 11},
                {24, 5, 10},
                {25, 1, 4},
                {25, 5, 5}
        };
        gp.obj[1][1] = new OBJ_PP(gp, 11,temp, temp2);
        gp.obj[1][1].worldX = 7 * gp.tileSize;
        gp.obj[1][1].worldY = 8 * gp.tileSize;

        temp = new int[][]{
                {25, 8, 12},
                {23, 8, 9},
                {22, 8, 9},
                {22, 7, 8},
                {21, 7, 9},
                {21, 5, 8},
                {20, 5, 9},
                {19, 5, 9},
                {18, 5, 9},
                {18, 4, 8},
                {17, 4, 9},
                {16, 4, 9},
                {15, 4, 9},
                {14, 4, 8},
                {14, 3, 9},
                {13, 3, 9},
                {12, 3, 9},
                {11, 3, 9},
                {9, 3, 8},
                {9, 4, 9},
                {8, 4, 8},
                {8, 5, 9},
                {7, 5, 13}
        };
        temp2 = new int[][]{
                {25, 8, 4},
                {23, 8, 10},
                {22, 8, 10},
                {22, 7, 11},
                {21, 7, 10},
                {21, 5, 11},
                {20, 5, 10},
                {19, 5, 10},
                {18, 5, 10},
                {18, 4, 11},
                {17, 4, 10},
                {16, 4, 10},
                {15, 4, 10},
                {14, 4, 11},
                {14, 3, 10},
                {13, 3, 10},
                {12, 3, 10},
                {11, 3, 10},
                {9, 3, 11},
                {9, 4, 10},
                {8, 4, 11},
                {8, 5, 10},
                {7, 1, 4},
                {7, 5, 5}
        };
        gp.obj[1][2] = new OBJ_PP(gp, 12,temp, temp2);
        gp.obj[1][2].worldX = 25 * gp.tileSize;
        gp.obj[1][2].worldY = 8 * gp.tileSize;


        temp = new int[][]{
                {25, 1, 12},
                {25, 5, 13}
        };
        temp2 = new int[][]{};
        gp.obj[1][3] = new OBJ_PP(gp, 13,temp, temp2);
        gp.obj[1][3].worldX = 25 * gp.tileSize;
        gp.obj[1][3].worldY = 1 * gp.tileSize;

        temp = new int[][]{
                {7, 1, 12},
                {7, 5, 13}
        };
        temp2 = new int[][]{};
        gp.obj[1][4] = new OBJ_PP(gp, 13,temp, temp2);
        gp.obj[1][4].worldX = 7 * gp.tileSize;
        gp.obj[1][4].worldY = 1 * gp.tileSize;

        temp = new int[][]{
                {12, 12, 12},
                {13, 12, 10},
                {14, 12, 10},
                {15, 12, 10}
        };
        temp2 = new int[][]{
                {12, 12, 4},
                {13, 12, 9},
                {14, 12, 9},
                {15, 12, 9}
        };
        gp.obj[1][5] = new OBJ_PP(gp, 15, temp, temp2);
        gp.obj[1][5].worldX = 12 * gp.tileSize;
        gp.obj[1][5].worldY = 12 * gp.tileSize;

        temp = new int[][]{
                {20, 12, 12},
                {19, 12, 10},
                {18, 12, 10},
                {17, 12, 10}
        };
        temp2 = new int[][]{
                {20, 12, 4},
                {19, 12, 9},
                {18, 12, 9},
                {17, 12, 9}
        };
        gp.obj[1][6] = new OBJ_PP(gp, 16, temp, temp2);
        gp.obj[1][6].worldX = 20 * gp.tileSize;
        gp.obj[1][6].worldY = 12 * gp.tileSize;

        gp.obj[1][0] = new OBJ_Rune(gp, 'l');
        gp.obj[1][0].worldX = 7 * gp.tileSize;
        gp.obj[1][0].worldY = 29 * gp.tileSize;

        gp.obj[1][7] = new OBJ_RuneDoor(gp, 'l');
        gp.obj[1][7].worldX = 12 * gp.tileSize;
        gp.obj[1][7].worldY = 24 * gp.tileSize;

        gp.obj[1][8] = new OBJ_Rune(gp, 'p');
        gp.obj[1][8].worldX = 16 * gp.tileSize;
        gp.obj[1][8].worldY = 16 * gp.tileSize;

        gp.obj[1][9] = new OBJ_RuneDoor(gp, 'p');
        gp.obj[1][9].worldX = 26 * gp.tileSize;
        gp.obj[1][9].worldY = 15 * gp.tileSize;

        temp = new int[][]{
                {7, 23, 12},
                {7, 26, 13}
        };
        temp2 = new int[][]{
                {7, 23, 4},
                {7, 30, 4},
                {7, 26, 5}
        };
        gp.obj[1][10] = new OBJ_PP(gp, 3, temp, temp2);
        gp.obj[1][10].worldX = 7 * gp.tileSize;
        gp.obj[1][10].worldY = 23 * gp.tileSize;

        temp = new int[][]{
                {7, 30, 12},
                {7, 26, 13}
        };
        temp2 = new int[][]{};
        gp.obj[1][11] = new OBJ_PP(gp, 13, temp, temp2);
        gp.obj[1][11].worldX = 7 * gp.tileSize;
        gp.obj[1][11].worldY = 30 * gp.tileSize;

        temp = new int[][]{
                {25, 24, 12},
                {25, 26, 13}
        };
        temp2 = new int[][]{
                {25, 24, 4},
                {25, 30, 4},
                {25, 26, 5}
        };
        gp.obj[1][12] = new OBJ_PP(gp, 3, temp, temp2);
        gp.obj[1][12].worldX = 25 * gp.tileSize;
        gp.obj[1][12].worldY = 24 * gp.tileSize;

        temp = new int[][]{
                {25, 30, 12},
                {25, 26, 13}
        };
        temp2 = new int[][]{};
        gp.obj[1][13] = new OBJ_PP(gp, 13, temp, temp2);
        gp.obj[1][13].worldX = 25 * gp.tileSize;
        gp.obj[1][13].worldY = 30 * gp.tileSize;

        temp = new int[][]{
                {15, 23, 12},
                {16, 24, 9},
                {17, 24, 8},
                {17, 23, 8},
                {17, 22, 8},
                {17, 21, 9},
                {16, 21, 13}
        };
        temp2 = new int[][]{
                {15, 23, 4},
                {16, 24, 10},
                {17, 24, 11},
                {17, 23, 11},
                {17, 22, 11},
                {17, 21, 10},
                {16, 21, 5}
        };
        gp.obj[1][14] = new OBJ_PP(gp, 3, temp, temp2);
        gp.obj[1][14].worldX = 15 * gp.tileSize;
        gp.obj[1][14].worldY = 23 * gp.tileSize;

        temp = new int[][]{
                {14, 20, 12},
                {14, 21, 9},
                {15, 21, 9},
                {16, 21, 13}
        };
        temp2 = new int[][]{
                {14, 20, 4},
                {14, 21, 10},
                {15, 21, 10},
                {16, 21, 5}
        };
        gp.obj[1][15] = new OBJ_PP(gp, 3, temp, temp2);
        gp.obj[1][15].worldX = 14 * gp.tileSize;
        gp.obj[1][15].worldY = 20 * gp.tileSize;
    }

    public void setPlayer2() {
        gp.player2 = new NPC_Player(gp, 0);
        gp.player2.worldX = gp.tileSize * 3;
        gp.player2.worldY = gp.tileSize * 15;
    }

    public void setNPC() {

//        gp.npc[0][0] = new Monster_Eye(gp, 0);
//        gp.npc[0][0].worldX =  gp.tileSize * 5;
//        gp.npc[0][0].worldY =  gp.tileSize * 15;
//        gp.npc[0][1] = new Monster_Eye(gp,0);
//        gp.npc[0][1].worldX =  gp.tileSize * 7;
//        gp.npc[0][1].worldY =  gp.tileSize * 15;
//
        gp.npc[0][2] = new Monster_Spike(gp, 0);
        gp.npc[0][2].worldX = 4 * gp.tileSize;
        gp.npc[0][2].worldY = 15 * gp.tileSize;

        //map 2
        gp.npc[1][0] = new Monster_Eye(gp, 1);
        gp.npc[1][0].worldX = 16 * gp.tileSize;
        gp.npc[1][0].worldY = 16 * gp.tileSize;

        gp.npc[1][1] = new Monster_Eye(gp, 1);
        gp.npc[1][1].worldX = 1 * gp.tileSize;
        gp.npc[1][1].worldY = 2 * gp.tileSize;

        gp.npc[1][2] = new Monster_Spike(gp, 1);
        gp.npc[1][2].worldX = 1 * gp.tileSize;
        gp.npc[1][2].worldY = 3 * gp.tileSize;




    }

}
