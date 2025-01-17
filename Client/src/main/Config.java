package main;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    public void saveConfig() {
        String appDataPath = getappDataPath();
        String path = appDataPath + "/HOWDY/config.txt";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            if (gp.fullScreenOn) {
                bw.write("On");
            }
            if (!gp.fullScreenOn) {
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

    public void loadConfig() {

        String appDataPath = getappDataPath();

        File directory = new File(appDataPath + "/HOWDY");
        File config = new File(appDataPath + "/HOWDY/config.txt");

        if (!directory.exists()) { // if the directory does not exist, create it
            try {
                directory.mkdirs();
                config.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(appDataPath + "/HOWDY/config.txt"));
                bw.write("Off");
                bw.newLine();
                bw.write("3");
                bw.newLine();
                bw.write("3");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!config.exists()) { // if the config file does not exist, create it
            try {
                config.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(appDataPath + "/HOWDY/config.txt"));
                bw.write("Off");
                bw.newLine();
                bw.write("3");
                bw.newLine();
                bw.write("3");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(appDataPath + "/HOWDY/config.txt"));
            String s = br.readLine();
            if(s.equals("On")){
                gp.fullScreenOn = true;
            }
            if(s.equals("Off")) {
                gp.fullScreenOn = false;
            }

            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);

            s = br.readLine();
            //gp.se.volumeScale = Integer.parseInt(s);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String getappDataPath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String appDataPath = null;
        if (osName.contains("win")) {
            appDataPath = System.getenv("APPDATA");
        } else if (osName.contains("mac")) {
            appDataPath = System.getProperty("user.home") + "/Library/Application Support";
        } else if (osName.contains("linux")) {
            appDataPath = System.getProperty("user.home");
        }

        return appDataPath;
    }
}
