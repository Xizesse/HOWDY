package net;

public class Packet02Move extends Packet{

    private char entityID;
    //0 for the other player
    private int x,y;
    private String direction;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.entityID = dataArray[0].charAt(0);
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.direction = dataArray[3];

    }

    public Packet02Move(char entityID, int x, int y, String direction) {
        super(02);
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.direction = direction;


    }
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
    }

    @Override
    public byte[] getData() {
        return ("02" + this.entityID + "," + this.x + "," + this.y + "," + this.direction).getBytes();
    }


    public char getEntityID(){
        return entityID;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public String getDirection(){
        return direction;
    }

}
