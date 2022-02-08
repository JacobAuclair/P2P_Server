
import java.net.*;
import java.io.*;
import java.lang.*;

public class Main
{
    static String[] PeerData = new String[3];
    static int i = 0;
    public static void main(String[] args) {

        // [1] Server UDP/RDT Receiver for Client -> Server
        RDTReceiver receiverThread1 = null;
        int RDTReceiverPort1 = 3001;
        try {
            receiverThread1 = new RDTReceiver("Receiver1", RDTReceiverPort1);
            receiverThread1.start();
        } catch (Exception e){
            e.printStackTrace();
        }

        // [2] Server UDP/RDT Receiver for Client -> Server
        RDTReceiver receiverThread2 = null;
        int RDTReceiverPort2 = 4001;
        try {
            receiverThread2 = new RDTReceiver("Receiver2", RDTReceiverPort2);
            receiverThread2.start();
        } catch (Exception e){
            e.printStackTrace();
        }

        
        // Receiving and Printing
        while (PeerData[2] == null){
            System.out.println("waiting for first client info");
            try{
                Thread.sleep(2500);
            }catch (InterruptedException e) {
            }
            while (PeerData[2] == null && PeerData[0] != null){
                System.out.println("waiting for second client info");
                try{
                    Thread.sleep(2500);
                }catch (InterruptedException e) {
                }
            }
            if( PeerData[2] != null && PeerData[0] != null ){
                //System.out.println(PeerData[0]);
                //System.out.println(PeerData[1]);
                //System.out.println(PeerData[2]);
                System.out.println("1");
                break;
            }
            //System.out.println(PeerData[0]);
            //System.out.println(PeerData[1]);
            //System.out.println(PeerData[2]);
            System.out.println("2");
        }
        
        // Server UDP/RDT Sender for Server -> Client
        try{
            //RDTReceiver r = new RDTReceiver("ReceiverR", RDTReceiverPort1);
            int n = 0;
            int mem = 0;
            String[] DataTemp = new String[10];
            for(String temp: PeerData[n].split("_")){
                DataTemp[mem] = temp;
                mem++;
                DataTemp[mem] = PeerData[n];
                n++;
            }

            for (int zx = 1; zx <= 2; zx ++){
                RDTSender sender = new RDTSender();

                byte[] IP1A = PeerData[0].getBytes();
                int Port1A = Integer.parseInt(PeerData[1]);
                byte[] IP2B = PeerData[2].getBytes();
                int Port2B = Integer.parseInt(PeerData[3]);

                sender.startSender(IP1A,Port1A);
                String data = (IP2B + "_" + Port2B);
                sender.rdtSend(data.getBytes());

                sender.startSender(IP2B,Port2B);
                String data1 = (IP1A + "_" + Port1A);
                sender.rdtSend(data1.getBytes());

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}