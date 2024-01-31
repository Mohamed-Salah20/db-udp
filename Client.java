import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Client
 * MsgSender
 * MsgReceiver
 */ 

public class Client {

    public static void main(String[] args) throws Exception {
        
        String host = "localhost";
        System.out.println("UDP Chat Server: " + host);
        DatagramSocket socket = new DatagramSocket();
        
        MsgSender sender = new MsgSender(socket,host);
        MsgReceiver receiver = new MsgReceiver(socket);
        
        Thread sThread = new Thread(sender);
        Thread rThread = new Thread(receiver);

        sThread.start();
        rThread.start();

    } 
}

/**
 * 
 */
class MsgSender implements Runnable {
    
    public static final int PORT = 2020;
    private DatagramSocket socket;
    private String host;
    Scanner scanner = new Scanner(System.in);

    MsgSender(DatagramSocket socket, String host) {
        this.socket = socket;
        this.host = host;
    }

    private void sendMsg(String msg) throws IOException {
        // byte[] buffer = new byte[1000];
        byte[] buffer = msg.getBytes();
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length, address, PORT);
        socket.send(packet);
    }

    @Override
    public void run() {
        boolean isConnected = false;
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

        while (true) {
            try {

                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("/exit")) { //TODO LATER 
                    sendMsg("Disconnected");
                    System.out.println("Exiting the Application ...\n");
                    socket.close();
                    System.exit(0);
                }
                
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
 * 
 */
class MsgReceiver implements Runnable {

    DatagramSocket socket;
    byte[] buffer;

    MsgReceiver(DatagramSocket socket){
        this.buffer = new byte[1024]; // MTU?
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                socket.receive(packet);
                String msgReceived = new String(packet.getData(), 0,packet.getLength()).trim();
                System.out.println(msgReceived);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}