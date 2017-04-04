package org.asysob;

import java.io.IOException;
import java.net.*;

public class UDP_RoundRobin {

    public static void main(String[] args) throws IOException {
        if (args.length < 3) Usage("\"join <my_port> <peer_host> <peer_port>\" or \"start|stop <host> <port>\"");
        switch (args[0].toLowerCase()) {
            case "join":
                if (args.length != 4) Usage("Arguments: join <my_port> <peer_host> <peer_port>");
                int my_port = Integer.parseInt(args[1]);
                String peer_host = args[2];
                int peer_port = Integer.parseInt(args[3]);
                Node(my_port, peer_host, peer_port);
                break;
            case "start":
            case "stop":
                if (args.length != 3) Usage("Arguemnts: start|stop <host> <port>");
                String command = args[0];
                String host = args[1];
                int port = Integer.parseInt(args[2]);
                Control(command, host, port);
                break;
            default:
                System.err.println("First argument must be join, start or stop");
        }
    }

    public static void Node(int my_port, String peer_host, int peer_port) throws IOException {
        Statistics stats = new Statistics(1);
        System.out.printf("Peer forwarding messages from port <%d> to <%s,%d>\n", my_port, peer_host, peer_port);
        DatagramSocket my_socket = new DatagramSocket(my_port);
        InetSocketAddress peer_address = new InetSocketAddress(peer_host, peer_port);
        boolean keep_on_running = true;
        while (keep_on_running) {
            stats.StartMeasure();
            String message = Receive(my_socket);
            stats.StopMeasure();
            System.out.printf("Received <%s> after %f ms waiting\n", message, stats.Duration());
            switch (message.toLowerCase()) {
                case "stop":
                    keep_on_running = false;
                    break;
                case "start":
                    message = "token";
                    break;
                default:
                    // todo adding some information to the message
            }
            Send(my_socket, peer_address, message);
        }
        my_socket.close();
        System.out.printf("Statistics: %s\n",stats.Report());
    }

    public static String Receive(DatagramSocket socket) throws IOException {
        final int buffer_size = 1024;
        byte[] buffer = new byte[buffer_size];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
        return message;
    }

    public static void Send(DatagramSocket socket, InetSocketAddress receiver, String message) throws IOException {
        byte[] buffer = message.getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiver);
        socket.send(packet);
    }

    private static void Control(String command, String host, int port) throws IOException {
        System.out.printf("Sending command <%s> to <%s,%d>\n", command, host, port);
        InetSocketAddress address = new InetSocketAddress(host, port);
        DatagramSocket my_socket = new DatagramSocket();
        Send(my_socket, address, command);
        my_socket.close();
    }

    private static void Usage(String s) {
        System.err.println(s);
        System.exit(-1);
    }
}
