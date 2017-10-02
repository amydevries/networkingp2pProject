package Peer;

import Factory.SocketFactory;
import Handlers.IHandler;
import Singleton.StorageSingleton;
import Sockets.ISocket;

import java.net.Socket;
import java.util.Hashtable;

public class PeerHandler extends Thread {

    private ISocket iSocket;
    StorageSingleton storageSingleton = StorageSingleton.getInstance();
    private Hashtable<Integer,PeerInfo> peers = storageSingleton.getPeers();
    private Hashtable<String, IHandler> handlers = storageSingleton.getHandlers();

    public PeerHandler(Socket socket){
        iSocket = SocketFactory.getSocketFactory().makeSocket(socket);
    }

    public void run(){
        PeerConnection peerConnection = new PeerConnection(null, iSocket);
        PeerMessage peerMessage = peerConnection.receiveData();

        handlers.get(peerMessage.getType()).handleMessage(peerConnection, peerMessage);

    }
}
