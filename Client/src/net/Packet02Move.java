package net;

public class Packet02Move extends Packet{


    private int x,y;

    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.x = Integer.parseInt(dataArray[0]);
        this.y = Integer.parseInt(dataArray[1]);
    }

    public Packet02Move(int x, int y) {
        super(02);
        this.x = x;
        this.y = y;
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
        return ("02"+this.x+","+this.y).getBytes();
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
