import java.net.*;
import java.io.*;

public class Main
{
    static String[] PeerData = new String[3];
    static int i = 0;
    public static void main(String[] args) {

        RDTReceiver receiverThread = null;
        int RDTReceiverPort = 3001;
        // Client UDP/RDT Receiver for Server -> Client
        try {
            receiverThread = new RDTReceiver("Receiver", RDTReceiverPort);
            receiverThread.start();
        } catch (Exception e){
            e.printStackTrace();
        }

        TCPReceiver clientThreadIn = null;
        // Client Data Receiver for P2P
        try {
            clientThreadIn = new TCPReceiver("Client",2001);
            clientThreadIn.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Client UDP/RDT Sender for Client -> Server
        try{
            RDTReceiver r = new RDTReceiver("ReceiverR", RDTReceiverPort);
            InetAddress tarAddress = InetAddress.getByName("10.8.96.181");
            byte[] targetAddress = tarAddress.getAddress();
            RDTSender sender = new RDTSender();
            sender.startSender(targetAddress,3001);   
            while (r.receivingSocket == null){
                String data = InetAddress.getLocalHost() + "_" + RDTReceiverPort + "                  ";
                sender.rdtSend(data.getBytes());
                Thread.sleep(1000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        TCPSender clientThreadOut = null;
        // Client Data Sender for P2P
        try {
            // Create client1
            byte[] targetAdddress = {127, 0, 0, 1};
            TCPSender client1 = new TCPSender("CLIENT1OUT", 2001);
            client1.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
