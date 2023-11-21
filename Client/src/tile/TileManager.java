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
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){

        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map01.txt");
    }

    public void getTileImage(){
        //floor
        setup(0, "/tiles/floor", false);
        setup(2, "/tiles/floor_moss", false);
        //wall
        setup(1, "/tiles/wall", true);
        setup(3, "/tiles/wall_moss", true);
        //pp
        setup(4, "/tiles/pp_clover", false);
        //door
        setup(5, "/tiles/door_clover", true);


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

    public void loadMap(String mapFilePath){
        try{
            InputStream is = getClass().getResourceAsStream(mapFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();

                while (col < gp.maxWorldCol){
                    String[] numbers = line.split(" ");
                     int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if(col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }

            }

            br.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d){

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileType = mapTileNum[worldCol][worldRow];

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
