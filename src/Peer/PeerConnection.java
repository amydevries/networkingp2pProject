package Peer;

import Logger.PeerLogger;
import Sockets.BasicSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.exit;

// a peerConnection wraps a socket with information about the peer the socket is connecting to
public class PeerConnection implements Runnable, Comparable<PeerConnection>{

    //peerInfo is the 'client' peer; the one that sends the messages
    private PeerInfo peerInfo;
    private BasicSocket bSocket;
    private PeerLogger peerLogger = PeerLogger.getLogger();

    private ArrayList<Integer> interestingPieces = new ArrayList<Integer>();

    private int piecesReceived = 0;

    private boolean isConnectionEstablished;    //variable for if the connection between these peers is established

    private boolean closeConnection = false;

    private boolean remotePeerChokingUs = true;

    private boolean peerThinksWereInterested = false;

    public PeerConnection(BasicSocket basicSocket){
        this.bSocket = basicSocket;
        Peer.connections.add(this);
    }

    public PeerConnection (PeerInfo peerInfo){
        this.peerInfo = peerInfo;
        Peer.connections.add(this);
        try {
            this.bSocket = new BasicSocket(peerInfo.getHostID(), peerInfo.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendHave(int index){
        SendingMessages.sendingHave(bSocket, index);
    }

    public void receiveData(){
        synchronized(interestingPieces){
            if(interestingPieces.size() > 0 && !remotePeerChokingUs){
                sendRequest();
            }
        }
        Message msg = null;
        try {
            msg = new Message(bSocket);

            if (msg.getType() == 0){
                remotePeerChokingUs = true;
                //setup the logger for use; need to have "true" to indicate that the file already exists
                peerLogger.choking(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }

            if (msg.getType() == 1){
                remotePeerChokingUs = false;

                //peer is unchoked. send request message back to this peer right away
                //check to make sure the size is greater than zero, otherwise there's an error for generating the random number for the index
                if(interestingPieces.size() > 0)
                    sendRequest();

                peerLogger.unchoking(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == 2){
                // the peer is now interested in some of the pieces we have
                peerInfo.setInterested(true);

                peerLogger.receivedInterestedMessage(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == 3){
                peerInfo.setInterested(false);

                peerLogger.receivedNotInterestedMessage(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == 4){
                int peerHasPieceIndex = Message.byteArrayToInt(msg.getData());

                peerLogger.receivedHaveMessage(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID(), peerHasPieceIndex);

                // update the bitfield we have for the sending peer with the new piece from the have message
                peerInfo.setBitField(peerHasPieceIndex);

                // check our bit field to see if the new piece is a piece we are interested in
                synchronized(interestingPieces){
                    interestingPieces = Peer.fileHandler.getBitField().getInterestingBits(peerInfo.getBitFieldOfRemotePeer());

                    if(!Peer.fileHandler.getPiece(peerHasPieceIndex).isFull()){
                        sendInterested();
                    }
                }
            }
            if (msg.getType() == 6){
                if(!isChoked()){

                    byte[] data = msg.getData();

                    int index = Message.byteArrayToInt(data);
                    if(Peer.fileHandler.getBitField().getBitField()[index] == (byte)1){
                        byte[] dataToSend = Peer.fileHandler.getPiece(index).getData();
                        SendingMessages.sendingPiece(bSocket, index, dataToSend);
                    }
                }

            }
            if (msg.getType() == 7){
                byte[] data = msg.getData();

                ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
                byteBuffer.put(data);
                byteBuffer.position(0);

                int index = byteBuffer.getInt();


                if(Peer.fileHandler.getPiece(index).isFull()){
                    synchronized(this) {
                        for (int i = 0; i < Peer.connections.size(); i++) {
                            for (int j = 0; j < Peer.connections.get(i).interestingPieces.size(); ++j) {
                                if (Peer.connections.get(i).interestingPieces.get(j) == index)
                                    Peer.connections.get(i).interestingPieces.remove(j);
                            }
                        }
                    }
                }

                else{

                    byte[] pieceData = new byte[data.length - 4];
                    for(int i = 0; i < data.length - 4; ++i){
                        pieceData[i] = byteBuffer.get();
                    }
                    synchronized(Peer.fileHandler){
                        Peer.fileHandler.receive(index, pieceData);
                    }

                    // send have messages and fix interesting pieces
                    for(int i=0; i<Peer.connections.size();i++){
                        if(Peer.connections.get(i).getConnectionEstablished()) {
                            Peer.connections.get(i).sendHave(index);
                        }
                    }

                    if (interestingPieces.size() ==0) sendNotInterested();

                    peerInfo.setDownloadRate(peerInfo.getDownloadRate() + 1);

                    Peer.fileHandler.increaseNumberOfPiecesDownloaded();
                    incrementPiecesReceived();


                    peerLogger.downloadingPiece(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID(), index, Peer.fileHandler.getBitField().getNumberOfPieces());
                }


            }

        } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public PeerInfo getPeerInfo() { return peerInfo; }

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

    public ArrayList<Integer> getInterestingPieces() {
        synchronized(interestingPieces) {
            return interestingPieces;
        }
    }

    @Override
    public void run() {

        SendingMessages.sendingHandshake(bSocket, Peer.getPeerInfo().getPeerID());

        while(true){
            Message handshakeMesage = null;
            try {
                handshakeMesage = new Message(bSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(handshakeMesage.getType() == 99){
              if(peerInfo != null)  {
                  if(peerInfo.getPeerID() == handshakeMesage.getPeerID()){
                  }else {
                      throw new RuntimeException();
                  }
              }

              else{
                  for(int key: Peer.getPeers().keySet()){
                      if(Peer.getPeers().get(key).getPeerID() == handshakeMesage.getPeerID()){
                          peerInfo = Peer.getPeers().get(key);
                      }
                  }
              }
            }
            isConnectionEstablished = true;
            peerLogger.tcpConnection(Peer.getPeerInfo().getPeerID(), peerInfo.getPeerID());
            break;
        }

        SendingMessages.sendingBitField(bSocket, Peer.fileHandler.getBitField().getBitField());
        Message bitFieldMessage = null;

        try {
            bitFieldMessage = new Message(bSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitFieldMessage.getType() == 5){
            peerInfo.getBitFieldOfRemotePeer().setBitField(bitFieldMessage.getData());
        }

        synchronized (interestingPieces){

            interestingPieces = Peer.fileHandler.getBitField().getInterestingBits(peerInfo.getBitFieldOfRemotePeer());

            if(interestingPieces.size()>0){
                sendInterested();
            }
            else{
                sendNotInterested();
            }
        }

        Message interestedMessage = null;

        try {
            interestedMessage = new Message(bSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(interestedMessage.getType() == 2){
            peerInfo.setInterested(true);
        }
        else if(interestedMessage.getType() == 3){
            peerInfo.setInterested(false);
        }

        isConnectionEstablished = true;

        while(true){
            receiveData();
           /* if(closeConnection){
                break;
            }*/
        }
    }

    public void sendUnchoke(){
        peerInfo.setIsChoked(false);
        SendingMessages.sendingUnChoke(bSocket);
    }

    public void sendRequest(){
        Random random = new Random();
        int requestedPieceIndex =  Math.abs(random.nextInt(interestingPieces.size()));
        synchronized (SendingMessages.class){SendingMessages.sendingRequest(bSocket, interestingPieces.get(requestedPieceIndex));}
        interestingPieces.remove(requestedPieceIndex);
    }

    public void setCloseConnection(boolean closeConnection){
        this.closeConnection = closeConnection;
    }

    public void sendChoke() {
        peerInfo.setIsChoked(true);
        SendingMessages.sendingChoke(bSocket);
    }

    public boolean doesPeerThinkWereInterested(){
        return peerThinksWereInterested;
    }

    public void sendInterested(){
        peerThinksWereInterested = true;
        SendingMessages.sendingInterested(bSocket);
    }

    public void sendNotInterested(){
        peerThinksWereInterested = false;
        SendingMessages.sendingNotInterested(bSocket);
    }

    public boolean isRemotePeerChokingUs(){
        return remotePeerChokingUs;
    }
}
