
import java.io.*;
import java.net.*;
import java.util.*;


public class RDTSender 
{
    private int receiverPortNumber = 0;
    private DatagramSocket socket = null;
    private InetAddress internetAddress = null;


    public void startSender(byte[] targetAddress, int receiverPortNumber) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        internetAddress = InetAddress.getByAddress(targetAddress);
        this.receiverPortNumber = receiverPortNumber;
    }
    
    public void stopSender(){
        if (socket!=null){
            socket.close();
        }
    }
    
    /**
     * Receive data and pass it to the current state
     *
     * @param data
     */
    public void rdtSend(byte[] data) throws SocketException, IOException, InterruptedException {
        // Just as an example assume packet size is a maximum of 256 bytes
        // Actual ethernet packet size is 1500 bytes, so any message
        // larger than that would have to be split across packets
        
        // For simplicity using a stream to read off packet size chunks
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        int packetNumber = 0;
        while (byteStream.available()>0){
            byte[] packetData = new byte[16];
            int bytesRead = byteStream.read(packetData);
            if (bytesRead<packetData.length){
                packetData = Arrays.copyOf(packetData, bytesRead);
            }
            System.out.println("### Sender sending packet("+new String((packetNumber++)+")")+": '"+new String(packetData)+"'");
            DatagramPacket packet = new DatagramPacket(packetData, packetData.length, internetAddress, receiverPortNumber);
            socket.send(packet);
            // Minor pause for easier visualization only
            Thread.sleep(1200);
        }
        System.out.println("### Sender done sending");
    }
}
