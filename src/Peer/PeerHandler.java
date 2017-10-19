package Peer;

import Factory.SocketFactory;
import Handlers.IHandler;
import Sockets.ISocket;

import java.net.Socket;
import java.util.Hashtable;

/**
 * each peer has a peer handler that will handle all of the connections that peer makes to a specific peer that connects to it
 *
 * the peer handler also controls the massages being passed to the proper handler that can do the proper thing for
 * that message according to the protocol
 */
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

        // we get the message here
        PeerMessage peerMessage = peerConnection.receiveData();
        Hashtable<Integer, IHandler> handlers = parentPeer.getHandlers();

        // pass it to a handler that can act according to the protocol for the specific message type
        handlers.get(peerMessage.getType()).handleMessage(peerConnection, peerMessage);

        peerConnection.close();
    }
}
