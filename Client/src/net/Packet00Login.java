package net;
import net.GameServer;
import net.GameClient;
import net.Packet;
public class Packet00Login extends Packet{


    public Packet00Login(){
        super(00);

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
        return ("00").getBytes();
    }


}
