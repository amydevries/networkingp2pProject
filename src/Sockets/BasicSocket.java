package Sockets;

import Peer.Peer;
import Peer.Message;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class BasicSocket {

    Socket socket;           //socket connect to the server
    ObjectOutputStream out;         //stream write to the socket
    ObjectInputStream in;          //stream read from the socket

    public BasicSocket(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public BasicSocket(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
           Peer.finishProgram();
        }
    }

    public Message read() {
        try {
            return (Message)in.readObject();
        } catch (IOException e) {
            Peer.finishProgram();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
