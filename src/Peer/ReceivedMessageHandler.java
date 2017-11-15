package Peer;

import Handlers.IHandler;
import Sockets.BasicSocket;

import java.net.Socket;
import java.util.Hashtable;

/**
 * each peer has a peer handler that will handle all of the connections that peer makes to a specific peer that connects to it
 *
 * the peer handler also controls the massages being passed to the proper handler that can do the proper thing for
 * that message according to the protocol
 */
public class ReceivedMessageHandler extends Thread {

    private BasicSocket iSocket;

    // the peer that we are handling messages for
    private Peer parentPeer;

    public ReceivedMessageHandler(Peer parentPeer, Socket socket){
        this.parentPeer = parentPeer;
        iSocket = new BasicSocket(socket);

    }

    public void run(){
        PeerConnection peerConnection = new PeerConnection(parentPeer, iSocket);

        // we get the message here
        Message message = peerConnection.receiveData();
        Hashtable<Integer, IHandler> handlers = parentPeer.getHandlers();

        // pass it to a handler that can act according to the protocol for the specific message type
        int handler = (byte)(message.getType());
        System.out.println(handlers.get(message.getType()));
        System.out.println(handler);

        handlers.get(handler).handleMessage(peerConnection, message);

        peerConnection.close();
    }
}
