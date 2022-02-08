
import java.io.*;
import java.net.*;

public class TCPSender extends Thread 
{

    private int serverPort;

    public TCPSender (String name, int serverPort) {
        super(name);
        this.serverPort = serverPort;
    }

    /**
     * Start the thread to connect and begin sending
     */
    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("CLIENT opening socket");
            clientSocket = new Socket("localhost", serverPort);
            System.out.println("CLIENT connected to server");
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            for (int i = 0; i < 4; i++) {
                sentence = "Mixed case sentence " + i;
                System.out.println(this.getName() + ": sending '" + sentence + "'");
                outToServer.writeBytes(sentence + '\n');
                modifiedSentence = inFromServer.readLine();

                System.out.println(this.getName() + " received from server: " + modifiedSentence);
                Thread.sleep(1500);
            }
            clientSocket.close();
            System.out.println(this.getName() + " closed connection to server");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (Exception cse) {
                // ignore exception here
            }
        }
    }
}
