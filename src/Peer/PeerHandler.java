package Peer;

import Factory.SocketFactory;
import Handlers.IHandler;
import Sockets.ISocket;

import java.net.Socket;
import java.util.Hashtable;

public class PeerHandler extends Thread {

    private ISocket iSocket;

    // the peer that we are handling messages for
    private Peer parentPeer;

    public PeerHandler(Peer parentPeer, Socket socket){
        this.parentPeer = parentPeer;
        iSocket = SocketFactory.getSocketFactory().makeSocket(socket);
    }

    public void run(){
        PeerConnection peerConnection = new PeerConnection(parentPeer, iSocket);
        PeerMessage peerMessage = peerConnection.receiveData();
        Hashtable<Integer, IHandler> handlers = parentPeer.getHandlers();

        handlers.get(peerMessage.getType()).handleMessage(peerConnection, peerMessage);

        peerConnection.close();
    }
}
