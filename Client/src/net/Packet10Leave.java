package net;

public class Packet10Leave extends Packet{


    public Packet10Leave(){
        super(10);
    }
    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {

    }

    @Override
    public  byte[] getData(){
        return ("10").getBytes();
    }


}
