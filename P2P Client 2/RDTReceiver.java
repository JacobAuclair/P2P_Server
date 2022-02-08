
import java.io.*;
import java.net.*;
import java.util.*;

public class RDTReceiver extends Thread 
{

    DatagramSocket receivingSocket = null;
    int i = 0;
    int port;

    public RDTReceiver(String name, int port) {
        super(name);
        this.port = port;
    }

    public void stopListening() {
        if (receivingSocket != null) {
            receivingSocket.close();
        }
    }

    // delivers data to earliest empty slot in array
    public void deliverData(byte[] data) {
        Main.i = 0;
        while (Main.i < 3){
            if ( Main.PeerData[Main.i] != new String(data) ){
                Main.PeerData[Main.i] = new String(data);
                break;
            }else{
                Main.i++;
            }
        }
        System.out.println("@@@ Receiver delivered packet with: '" + new String(data) + "'");
    }

    /**
     * Start the thread to begin listening
     */
    public void run() {
        try {
            receivingSocket = new DatagramSocket(4001);
            while (true) {
                System.out.println("@@@ Receiver waiting for packet");
                byte[] buf = new byte[16];
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                receivingSocket.receive(packet);
                byte[] packetData = Arrays.copyOf(packet.getData(), packet.getLength());
                deliverData(packetData);
            }
        } catch (Exception e) {
            stopListening();
        }
    }
}
