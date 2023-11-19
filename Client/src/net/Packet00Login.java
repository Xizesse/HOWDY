package net;
import net.GameServer;
import net.GameClient;
import net.Packet;
public class Packet00Login extends Packet{

    //00 for login
    //001 to : player already logged in
    //000 to : no players logged in

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
