package main;

import java.io.*;

public class Config {
    GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){
        //String currentDir = System.getProperty("user.dir");
        //String filePath = currentDir + File.separator + "Client" + File.separator+ "config.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
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
        //String currentDir = System.getProperty("user.dir");
        //String filePath = currentDir + File.separator + "Client" + File.separator + "config.txt";
        //System.out.println(filePath);
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));

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
}
