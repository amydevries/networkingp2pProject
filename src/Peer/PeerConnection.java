package Peer;

import Logger.PeerLogger;
import Sockets.BasicSocket;
import Sockets.ISocket;
import Peer.PeerInfo;

import java.io.IOException;

// a peerConnection wraps a socket with information about the peer the socket is connecting to
public class PeerConnection implements Comparable<PeerConnection>{

    // peer connection now remembers what peer it is acting as a connection for so we can more easily access bitfield/other information
    // idk if this is necessary it might need to be removed later
    //parentPeer is the 'server' peer; the one that receives the messages
    private Peer parentPeer;
    //peerInfo is the 'client' peer; the one that sends the messages
    private PeerInfo peerInfo;
    private ISocket iSocket;
    private PeerLogger peerLogger = new PeerLogger();

    private int piecesReceived = 0;

    private boolean isConnectionEstablished;    //variable for if the connection between these peers is established

    public PeerConnection(Peer parentPeer, PeerInfo receivingPeerInfo){
        this.parentPeer = parentPeer;
        this.peerInfo = receivingPeerInfo;
        try {
            iSocket = new BasicSocket(peerInfo.getHostID(), peerInfo.getPort());

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

    public void setChoked(boolean isChoked) { this.peerInfo.setIsChoked(isChoked); }

    public boolean isChoked(){ return peerInfo.isChoked(); }

    public int getPiecesReceived() {
        return piecesReceived;
    }

    public void resetPiecesReceived() {
        this.piecesReceived = 0;
    }

    public void incrementPiecesReceived(){
        piecesReceived++;
    }

    @Override
    public int compareTo(PeerConnection differentConnection) {
        if(differentConnection == null)
            return -1;
        if(this == null)
            return 1;

        //sort the connections from the highest to the lowest
        int value = differentConnection.getPiecesReceived() - this.getPiecesReceived();
        return value;
    }
}
