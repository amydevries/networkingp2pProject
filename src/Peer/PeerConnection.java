package Peer;

import Factory.SocketFactory;
import Logger.PeerLogger;
import Sockets.ISocket;

import java.io.IOException;

// a peerConnection wraps a socket with information about the peer the socket is connecting to
public class PeerConnection {

    // peer connection now remembers what peer it is acting as a connection for so we can more easily access bitfield/other information
    // idk if this is necessary it might need to be removed later
    //parentPeer is the 'server' peer; the one that receives the messages
    private Peer parentPeer;
    //peerInfo is the 'client' peer; the one that sends the messages
    private PeerInfo peerInfo;
    private ISocket iSocket;
    private PeerLogger peerLogger = new PeerLogger();

    private boolean isConnectionEstablished;    //variable for if the connection between these peers is established
    private boolean isChoked;                   //variable to determine if the one peer in the connection has choked the other (true == choked)

    public PeerConnection(Peer parentPeer, PeerInfo receivingPeerInfo){
        this.parentPeer = parentPeer;
        this.peerInfo = receivingPeerInfo;
        try {
            iSocket = SocketFactory.getSocketFactory().makeSocket(peerInfo.getHostID(), peerInfo.getPort());

            //not sure if this is where the logger should go for the TCP connection
            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.setup(peerInfo.getPeerID(), true);
            //Writes to log file: update the 1s with variables when they're known
            peerLogger.tcpConnection(1, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection(Peer parentPeer, ISocket iSocket){
        this.parentPeer = parentPeer;
        this.iSocket = iSocket;
    }

    public void close(){
        if(iSocket != null){
            iSocket.close();
        }
        iSocket = null;
    }

    public void sendMessage(byte[] bytes){
        iSocket.write(bytes);
    }

    public Message receiveData(){
        Message msg = null;
        try {
            msg = new Message(iSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public PeerInfo getPeerInfo() { return peerInfo; }

    public Peer getParentPeer() {
        return parentPeer;
    }

    public void setConnectionEstablished(boolean isConnectionEstablished) { this.isConnectionEstablished = isConnectionEstablished; }

    public boolean getConnectionEstablished(){ return isConnectionEstablished; }

    public void setChoked(boolean isChoked) { this.isChoked= isChoked; }

    public boolean getChoked(){ return isChoked; }

}
