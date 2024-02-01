# UDP Chat Application

## Overview

The UDP Chat Application is a simple console-based chat system that allows communication between multiple clients and a server using the User Datagram Protocol (UDP).

### Components

1. **Server**
   - Listens for incoming messages from clients.
   - Distributes received messages to all connected clients.
   - Maintains a list of connected clients.

2. **Client**
   - Sends messages to the server.
   - Receives and displays messages from other clients through the server.

### Technologies Used

- **Java:** The application is implemented in Java, utilizing its DatagramSocket and DatagramPacket classes for UDP communication.