package Sockets;

import Sockets.ISocket;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class BasicSocket implements ISocket{

    Socket socket;           //socket connect to the server
    OutputStream out;         //stream write to the socket
    InputStream in;          //stream read from the socket

    public BasicSocket(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    public BasicSocket(Socket socket) {
        this.socket = socket;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void write(byte[] bytes) {
        try {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() {
        try {
            return in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int read(byte[] bytes) {
        try {
            return in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
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
