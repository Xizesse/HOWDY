package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;

    public TileManager(GamePanel gp){

        this.gp = gp;
        tile = new Tile[30];
        mapTileNum = new int[gp.maxMaps][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map01.txt", 0);
        loadMap("/maps/map02.txt", 1);
        loadMap("/maps/map3.txt", 2);
        loadMap("/maps/map4.txt", 3);
        loadMap("/maps/map5.txt", 4);

    }

    public void getTileImage(){
        //floor
        setup(0, "/tiles/floor0", false);
        setup(2, "/tiles/floor_moss2", false);
        setup(6, "/tiles/floor_vert_magic6", false);
        setup(7, "/tiles/floor_hor_magic7", false);
        //wall
        setup(1, "/tiles/wall1", true);
        setup(3, "/tiles/wall_moss3", true);
        setup(8, "/tiles/wall_vert_magic8", true);
        setup(9, "/tiles/wall_hor_magic9", true);
        setup(10, "/tiles/wall_crack_horH", true);
        setup(11, "/tiles/wall_crack_vertV", true);
        //pp
        setup(4, "/tiles/pp_clover4", false);
        setup(12, "/tiles/pp_clover_activeA", false);
        //door
        setup(5, "/tiles/door_clover5", true);
        setup(13, "/tiles/door_clover_openC", false);

        //outside
        setup(14, "/tiles/grass1", false);
        setup(15, "/tiles/grass2", false);
        setup(16, "/tiles/bush", true);
        setup(17, "/tiles/water", false);
        setup(18, "/tiles/upplank", true);
        setup(19, "/tiles/downplank", true);
        setup(20, "/tiles/planks", false);
        setup(21, "/tiles/leftSand", true);
        setup(22, "/tiles/rightSand", true);

        setup(23, "/tiles/rockright", true);
        setup(24, "/tiles/rockleft", true);

        setup(25, "/tiles/rockupleft", true);
        setup(26, "/tiles/rockupright", true);
        setup(27, "/tiles/rockdownleft", true);
        setup(28, "/tiles/rockdownright", true);



    }

    public void setup(int index, String imagePath, boolean collision){

        UtilityTool uT = new UtilityTool();


        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            tile[index].image = uT.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].Collision = collision;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String mapFilePath, int mapIndex) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    char tileChar = line.charAt(col * 2); // Assuming there's a space between each char in the map file
                    int num = charToTileIndex(tileChar); // Convert the character to a tile index

                    mapTileNum[mapIndex][col][row] = num;
                    col++;
                }

                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int charToTileIndex(char tileChar) {
        // Here you map characters to indices. You can use a switch statement or a HashMap for a larger set.
        switch(tileChar) {
            //floor
            case '0': return 0; // floor0
            case '2': return 2; // floor_moss2
            case '6': return 6; // floor_vert_magic6
            case '7': return 7; // floor_hor_magic7
            case '8': return 8; // wall_vert_magic8
            case '9': return 9; // wall_hor_magic9
            //wall
            case '1': return 1; // wall1
            case '3': return 3; // wall_moss3
            case 'H': return 10; // wall_crack_horH
            case 'V': return 11; // wall_crack_vertV
            //pp
            case '4': return 4; // pp_clover4
            case 'A': return 12; // pp_clover4
            //door
            case '5': return 5; // door_clover5
            case 'C': return 13; // door_clover open

            case 'G': return 14; // grass1
            case 'g': return 15; // grass2
            case 'b': return 16; // bush

            //outside
            case 'W' : return 17; // water
            case 'P':return 18; // plank top
            case 'p':return 19; // plank bottom
            case 'B':return 20; // bridge

            case 's': return 21; // sandLeft
            case 'S': return 22; // sandRight

            case 'r': return 23; // rockright
            case 'l': return 24; // rockleft

            case 'u': return 25; // rockupleft
            case 'U': return 26; // rockupright
            case 'd': return 27; // rockdownleft
            case 'D': return 28; // rockdownright


            // ... Add more cases for additional tiles
            default: return -1; // Error case, character not recognized
        }
    }

    public void updateMap(int mapIndex, int x, int y, int newTile){

        mapTileNum[mapIndex][x][y] = newTile;
    }

    public void draw(Graphics2D g2d){

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileType = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //Stop camera movement at the edge of the map
            //left
            if(gp.player.screenX > gp.player.worldX){
                screenX = worldX;
            }
            //top
            if(gp.player.screenY > gp.player.worldY){
                screenY = worldY;
            }
            //right
            if(gp.player.screenX < gp.player.worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth){
                screenX = worldX - gp.maxWorldCol * gp.tileSize + gp.screenWidth;
            }
            //bottom
            if(gp.player.screenY < gp.player.worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight){
                screenY = worldY - gp.maxWorldRow * gp.tileSize + gp.screenHeight;
            }


            // only draw tiles that are on the screen
            if(worldX > gp.player.worldX - gp.screenWidth &&
                    worldX < gp.player.worldX + gp.screenWidth &&
                    worldY > gp.player.worldY - gp.screenHeight &&
                    worldY < gp.player.worldY + gp.screenHeight){

                g2d.drawImage(tile[tileType].image, screenX, screenY, null);
            }
            worldCol++;

            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }

    }

}
