# UDP Chat Application

## Overview

The UDP Chat Application is a simple console-based chat system that allows communication between multiple clients and a server using the User Datagram Protocol (UDP).

### Components

1. **Server**
   - Listens for incoming messages from clients on **PORT** 2020.
   - Distributes received messages to all connected clients.
   - Maintains a list of connected clients.

2. **Client**
   - Sends messages to the server.
   - Receives and displays messages from other clients through the server.

### Technologies Used

- **Java:** The application is implemented in Java, utilizing its DatagramSocket and DatagramPacket classes for UDP communication.
- **Java Threads:** The application employs Java threads to enable concurrent execution of tasks. Threads are used for both the server and client components, allowing the server to handle multiple client connections concurrently and enabling clients to send and receive messages simultaneously.

### Database Integration

The application uses a PostgreSQL database to store chat logs.
#### Create Database in psql with name chat_udp
`CREATE DATABASE chat_udp;`
**default user: postgres**
**default passsword: postgres**
**default PORT: 5432**
Make sure to replace the database connection details with the appropriate values for your setup.

### -To Use The Application:
##### -First Download:
`git clone https://github.com/Mohamed-Salah20/udp.git`
##### -then go to the project directory:
`cd tcp`
#### -To install:
##### -First check java jdk is installed & working :
`java --version`
##### -compile two programs that include main functions Client.java, Server.java:
`javac Client.java ;`
`javac Server.java ;`
#### To run the app
##### -make sure to run the server first:
`java Server`
##### -in other terminals run any number of clients:
`java Client`
#### -To exit program:
`/exit`
