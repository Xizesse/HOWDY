package net;

import net.GameServer;
import net.GameClient;
import net.Packet;
public class Packet01Logout extends Packet{

    //00 for login
    //001 to : player already logged in
    //000 to : no players logged in

    public Packet01Logout(){
        super(01);
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
        return ("01").getBytes();
    }


}
