package main;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){
        String path = System.getenv("APPDATA") + "/HOWDY/config.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            if(gp.fullScreenOn){
                bw.write("On");
            }
            if(!gp.fullScreenOn){
                bw.write("Off");
            }
            bw.newLine();

            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            //bw.write(String.valueOf(gp.se.volumeScale));
            bw.write(String.valueOf(3));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadConfig(){
        String osName = System.getProperty("os.name").toLowerCase();
        String appDataPath = null;
        if(osName.contains("win")) {
            appDataPath = System.getenv("APPDATA");
        } else if(osName.contains("mac")){
            appDataPath = System.getProperty("user.home") + "/Library/Application Support";
        } else if(osName.contains("linux")){
            appDataPath = System.getProperty("user.home");
        }
        File directory = new File(appDataPath + "/HOWDY");
        File config = new File(appDataPath + "/HOWDY/config.txt");

        if(directory.exists()) {
            if (config.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(appDataPath + "/HOWDY/config.txt"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    config.createNewFile();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(appDataPath + "/HOWDY/config.txt"));
                    bw.write("Off");
                    bw.newLine();
                    bw.write("3");
                    bw.newLine();
                    bw.write("3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else{
            try {
                directory.mkdirs();
                config.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(appDataPath + "/HOWDY/config.txt"));
                bw.write("Off");
                bw.newLine();
                bw.write("3");
                bw.newLine();
                bw.write("3");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
