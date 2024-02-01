import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Server implements Runnable {
    
    //
    DB db = new DB();    

    // Define the server port and buffer size
    public static final int PORT = 2020;
    private static final int BUFFER_SIZE = 1024;

    // DatagramSocket to handle UDP communication
    private DatagramSocket socket;

    // Java Collection Framework Lists to store client information in the server
    private ArrayList<InetAddress> client_addresses;
    private ArrayList<Integer> client_ports;
    private HashSet<String> existing_clients;

    // Constructor initializes the server
    public Server() throws SocketException {

      this.socket = new DatagramSocket(PORT); 
      System.out.println("Server running and is listning on port " + PORT);
      
    // Initialize data structures to store client information
      client_addresses = new ArrayList<>();
      client_ports = new ArrayList<>();
      existing_clients = new HashSet<>();
    }

    // Main logic of the server    
    @Override
    public void run() {
        
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            try {
                // Clear the buffer for new data  
                Arrays.fill(buffer, (byte)0);
                
                // Receive incoming packet
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                socket.receive(packet);
                
                // Extract message and client information
                String msg = new String(packet.getData()).trim();
                InetAddress clinetAddress = packet.getAddress();
                int clientPort = packet.getPort();
                String id = clinetAddress.toString() + "|" + clientPort;
                
                // Add Msg and Client information to Database
                try {                                                
                    db.insertMsgTable(clinetAddress, clientPort, msg);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Failed to Insert ");
                }

                // If the client is new, add its information to the lists
                if (!existing_clients.contains(id)) {
                    existing_clients.add(id);
                    client_addresses.add(clinetAddress);
                    client_ports.add(clientPort);    
                }

                // Print received message and client information
                System.out.println(id + ":" + msg);
                ////////////end of receive////////////


                ////////////start sending/////////////

                // Prepare data to be sent to other clients
                byte[] data = (id + ":" + msg).getBytes();

                // Iterate through existing clients and send the message
                for (int i = 0; i < existing_clients.size(); i++) {
                
                    // Skip sending to the same client that sent the message
                    if(
                        clinetAddress.equals(client_addresses.get(i))
                        && clientPort == client_ports.get(i)
                    )continue;
                     
                    // Get the destination client's information
                    InetAddress clientAddressToSend = client_addresses.get(i);
                    int clientPortToSend = client_ports.get(i);
                    
                    // Create a new packet and send the data
                    packet = new DatagramPacket(data,data.length,clientAddressToSend,clientPortToSend);   
                    socket.send(packet);
                
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        try {
            
            Server server = new Server();
            new Thread(server).start();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
