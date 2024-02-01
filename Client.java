import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Classes:
 * Client
 * MsgSender
 * MsgReceiver
 */ 

public class Client {

    public static void main(String[] args) throws Exception {
        
        // Server host address
        String host = "localhost";
        System.out.println("UDP Chat Server: " + host);
        
        // Create a DatagramSocket for the client
        DatagramSocket socket = new DatagramSocket();
        
        // Create sender and receiver instances
        MsgSender sender = new MsgSender(socket,host);
        MsgReceiver receiver = new MsgReceiver(socket);
        
        // Create threads for sender and receiver
        Thread sThread = new Thread(sender);
        Thread rThread = new Thread(receiver);

        sThread.start();
        rThread.start();

    } 
}

/**
 * MsgSender class handles sending messages from the client to the server.
 */
class MsgSender implements Runnable {
    
    // Server port for sending messages
    public static final int PORT = 2020;

    // DatagramSocket for communication
    private DatagramSocket socket;
    private String host; //Server host address
    Scanner scanner = new Scanner(System.in);

    // Constructor to initialize MsgSender
    MsgSender(DatagramSocket socket, String host) {
        this.socket = socket;
        this.host = host;
    }

    // Method to send messages to the server
    private void sendMsg(String msg) throws IOException {
        // byte[] buffer = new byte[1000];
        byte[] buffer = msg.getBytes();
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length, address, PORT);
        socket.send(packet);
    }

    // Run method for sender thread
    @Override
    public void run() {
        boolean isConnected = false;

        // Loop to send a connection message to the server until connected
        do{
            try {
                sendMsg("New Client Connected!\n");
                isConnected = true;
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }while(!isConnected);     

        // Continuously send user input to the server
        while (true) {
            try {

                String input = scanner.nextLine();
                
                // Check for exit command
                if (input.equalsIgnoreCase("/exit")) { //TODO LATER 
                    sendMsg("Disconnected");
                    System.out.println("Exiting the Application ...\n");
                    socket.close();
                    System.exit(0);
                }
                
                // Sleep to avoid conflicts with receiver thread
                Thread.sleep(100);
                sendMsg(input);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }    
}

/**
 * MsgReceiver class handles receiving and displaying messages from the server.
 */
class MsgReceiver implements Runnable {

    // DatagramSocket for communication
    DatagramSocket socket;
    byte[] buffer;

    // Constructor to initialize MsgReceiver
    MsgReceiver(DatagramSocket socket){
        this.buffer = new byte[1024]; // MTU?
        this.socket = socket;
    }

    // Run method for receiver thread
    @Override
    public void run() {
        while (true) {
            try {
                // Receive incoming messages from the server
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                socket.receive(packet);
                String msgReceived = new String(packet.getData(), 0,packet.getLength()).trim(); //String(packet.getData()) 
                System.out.println(msgReceived);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}