package net;

public class Packet02Move extends Packet{

    private int entityID;

    //0 for the other player
    private int map;
    private int x,y;

    private String direction;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.entityID = Integer.parseInt(dataArray[0]);
        this.map = Integer.parseInt(dataArray[1]);
        this.x = Integer.parseInt(dataArray[2]);
        this.y = Integer.parseInt(dataArray[3]);
        this.direction = dataArray[4];

    }

    public Packet02Move(int entityID,int map, int x, int y, String direction) {
        super(02);
        this.entityID = entityID;
        this.map = map;
        this.x = x;
        this.y = y;
        this.direction = direction;


    }
    @Override
    public void writeData(GameClient client) {
        if (client!=null) {
            client.sendData(getData());
        }
    }

    @Override
    public void writeData(GameServer server) {

    }

    @Override
    public byte[] getData() {
        return ("02" + this.entityID + "," + this.map + "," + this.x + "," + this.y + "," + this.direction).getBytes();
    }

    public int getEntityID(){
        return entityID;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getMap(){
        return map;
    }
    public String getDirection(){
        return direction;
    }

}
