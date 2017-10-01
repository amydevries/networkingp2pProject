package Peer;

import Factory.SocketFactory;
import Sockets.ISocket;

import java.net.Socket;

public class PeerHandler extends Thread {

    private ISocket iSocket;

    public PeerHandler(Socket socket){
        iSocket = SocketFactory.getSocketFactory().makeSocket(socket);
    }

    public void run(){


    }
}
