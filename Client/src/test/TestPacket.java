package test;

import net.Packet;
import net.Packet00Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PacketTest {

    @Test
    void testReadDataShouldReturnCorrectString() {
        Packet packet = new Packet00Login(); // Assume Packet00Login is a concrete class.
        String data = "00LOGIN DATA";
        assertEquals("LOGIN DATA", packet.readData(data.getBytes()), "The packet ID should be stripped.");
    }

    @Test
    void testLookupPacketShouldReturnCorrectType() {
        assertEquals(Packet.PacketTypes.LOGIN, Packet.lookupPacket("00"), "Should return LOGIN type for '00'.");
        assertEquals(Packet.PacketTypes.LOGOUT, Packet.lookupPacket("01"), "Should return DISCONNECT type for '01'.");
        assertEquals(Packet.PacketTypes.MOVE, Packet.lookupPacket("02"), "Should return MOVE type for '02'.");
        assertEquals(Packet.PacketTypes.ATTACK, Packet.lookupPacket("03"), "Should return ATTACK type for '03'.");
        assertEquals(Packet.PacketTypes.OBJECT, Packet.lookupPacket("04"), "Should return OBJECT type for '04'.");
        assertEquals(Packet.PacketTypes.HEALTH, Packet.lookupPacket("05"), "Should return HEALTH type for '05'.");
        assertEquals(Packet.PacketTypes.MAPCHANGE, Packet.lookupPacket("06"), "Should return MAPCHANGE type for '06'.");
        assertEquals(Packet.PacketTypes.READY, Packet.lookupPacket("07"), "Should return READY type for '07'.");

        assertEquals(Packet.PacketTypes.INVALID, Packet.lookupPacket(-1), "Should return INVALID type for an undefined ID.");
    }

    @Test
    void testLookupPacketShouldReturnInvalidForNonNumeric() {
        assertEquals(Packet.PacketTypes.INVALID, Packet.lookupPacket("xx"), "Should return INVALID for non-numeric input.");
    }
}