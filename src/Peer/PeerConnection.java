package Peer;

import FileHandling.Piece;
import Logger.PeerLogger;
import Sockets.BasicSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// a peerConnection wraps a socket with information about the peer the socket is connecting to
public class PeerConnection implements Runnable, Comparable<PeerConnection>{

    // peer connection now remembers what peer it is acting as a connection for so we can more easily access bitfield/other information
    // idk if this is necessary it might need to be removed later
    //parentPeer is the 'server' peer; the one that receives the messages
    private Peer parentPeer;
    //peerInfo is the 'client' peer; the one that sends the messages
    private PeerInfo peerInfo;
    private BasicSocket bSocket;
    private PeerLogger peerLogger = new PeerLogger();

    private ArrayList<Integer> interestingPieces = new ArrayList<Integer>();

    private int piecesReceived = 0;

    private boolean isConnectionEstablished;    //variable for if the connection between these peers is established

    private boolean closeConnection = false;

    public PeerConnection(Peer parentPeer, PeerInfo receivingPeerInfo){
        this.parentPeer = parentPeer;
        this.peerInfo = receivingPeerInfo;
        try {
            bSocket = new BasicSocket(peerInfo.getHostID(), peerInfo.getPort());

            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.setup(peerInfo.getPeerID(), true);
            //Writes to log file
            peerLogger.tcpConnection(Peer.getPeerInfo().getPeerID(), receivingPeerInfo.getPeerID());
            System.out.println("c: " + Peer.getPeerInfo().getPeerID());
            System.out.println("d: " + receivingPeerInfo.getPeerID());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerConnection(Peer parentPeer, BasicSocket bSocket){
        this.parentPeer = parentPeer;
        this.bSocket = bSocket;
    }

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

    public void close(){
        if(bSocket != null){
            bSocket.close();
        }
        bSocket = null;
    }

    public void sendMessage(byte[] bytes){
        bSocket.write(bytes);
    }

    public Message receiveData(){

        synchronized(interestingPieces){
            if(interestingPieces.size() > 0 && !isChoked()){
                Random random = new Random();
                int reqPieceIndex = Math.abs(random.nextInt()) % interestingPieces.size();
                SendingMessages.sendingRequest(bSocket, interestingPieces.get(reqPieceIndex));
                try{
                    Thread.sleep(250);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        Message msg = null;
        try {
            msg = new Message(bSocket);

            if (msg.getType() == (byte)0){
                setChoked(true);
                //setup the logger for use; need to have "true" to indicate that the file already exists
                peerLogger.setup(getPeerInfo().getPeerID(), true);
                peerLogger.choking(getParentPeer().getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == (byte)1){
                setChoked(false);

                peerLogger.setup(getPeerInfo().getPeerID(), true);
                peerLogger.unchoking(getParentPeer().getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == (byte)2){
                // the peer is now interested in some of the pieces we have
                peerInfo.setInterested(true);

                peerLogger.setup(getPeerInfo().getPeerID(), true);
                peerLogger.receivedInterestedMessage(getParentPeer().getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == (byte)3){
                peerInfo.setInterested(false);

                peerLogger.setup(getPeerInfo().getPeerID(), true);
                peerLogger.receivedNotInterestedMessage(getParentPeer().getPeerInfo().getPeerID(), getPeerInfo().getPeerID());
            }
            if (msg.getType() == (byte)4){
                int peerHasPieceIndex = Message.byteArrayToInt(msg.getData());

                peerLogger.setup(getPeerInfo().getPeerID(), true);
                peerLogger.receivedHaveMessage(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID(), peerHasPieceIndex);

                // update the bitfield we have for the sending peer with the new piece from the have message
                getPeerInfo().setBitField(peerHasPieceIndex);

                // check our bit field to see if the new piece is a piece we are interested in
                ArrayList<Integer> interestedBits =  parentPeer.getBitField().getInterestingBits(getPeerInfo().getBitField());

                // if there are no interesting pieces send a not interested message
                if (interestedBits.isEmpty()) {
                    sendMessage(Message.createActualMessage("not interested", new byte[0]));
                }else{
                    sendMessage(Message.createActualMessage("interested", new byte[0]));
                }
            }
            if (msg.getType() == (byte)5){
                BitField messageBitField = new BitField(msg.getData().length);
                messageBitField.setBitField(msg.getData());

                ArrayList<Integer> interestedBits =  parentPeer.getBitField().getInterestingBits(messageBitField);

                byte[] payload = new byte[0];

                if (interestedBits.isEmpty()) {
                    sendMessage(Message.createActualMessage("not interested", payload));
                }
                else {
                    sendMessage(Message.createActualMessage("interested", payload));
                }
            }
            if (msg.getType() == (byte)6){
                if(!isChoked()){
                    // if the peer is unchoked start transmitting the piece it asked for
                    int pieceInterestedIn = Message.byteArrayToInt(msg.getData());
                    byte[] piece = parentPeer.getFileHandler().getPiece(pieceInterestedIn).getData();
                    sendMessage(Message.createActualMessage("piece", piece));
                }
                // else if it isnt unchoked then just ignore it
            }
            if (msg.getType() == (byte)7){
                peerLogger.setup(getPeerInfo().getPeerID(), true);

                // received a message with a piece of data that we wanted
                byte[] data = msg.getData();

                // get the first 4 bytes which is the piece index field
                byte[] pieceLocationArray = new byte[4];
                for(int i = 0; i < 4; i++) pieceLocationArray[i] = data[i];

                // convert the byte index field into an int
                int pieceLocation = Message.byteArrayToInt(pieceLocationArray);

                // move the data into a piece
                Piece piece = new Piece(Arrays.copyOfRange(data, 4, data.length));

                // write the data to the correct location
                parentPeer.getFileHandler().setPiece(pieceLocation, piece);

                // update our bit field and currently requesting field
                parentPeer.getBitField().setPiece(pieceLocation);

                // increase our download rate for this connection
                incrementPiecesReceived();
                peerLogger.downloadingPiece(Peer.getPeerInfo().getPeerID(), getPeerInfo().getPeerID(), pieceLocation, parentPeer.getBitField().getNumberOfPieces());

                // look through our neighbors and if they no longer have interesting pieces send them a not interested message

                for(int i=0; i<Peer.connections.size(); i++){
                    ArrayList<Integer> pieces = parentPeer.getBitField().getInterestingBits(Peer.connections.get(i).getPeerInfo().getBitField());
                    if(pieces.isEmpty()) Peer.connections.get(i).sendMessage(Message.createActualMessage("not interested", new byte[0]));

                    // send a have message to all neighbors letting them know that we just got a new piece
                    Peer.connections.get(i).sendMessage(Message.createActualMessage("have", pieceLocationArray));
                }

            }

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

    public void sendHave(int index){

    }

    public ArrayList<Integer> getInterestingPieces() {
        return interestingPieces;
    }

    @Override
    public void run() {

        while(true){
            Message handshakeMesage = null;
            try {
                handshakeMesage = new Message(bSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(handshakeMesage.getType() == Byte.parseByte(null)){
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
            break;
        }

        SendingMessages.sendingBitField(bSocket, Peer.getFileHandler().getBitField().getBitField());
        Message bitFieldMessage = null;

        try {
            bitFieldMessage = new Message(bSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitFieldMessage.getType() == (byte)5){
            peerInfo.getBitField().setBitField(bitFieldMessage.getData());
        }

        synchronized (interestingPieces){
            interestingPieces = Peer.getFileHandler().getBitField().getInterestingBits(peerInfo.getBitField());

            if(interestingPieces.size()>0){
                SendingMessages.sendingInterested(bSocket);
            }
            else{
                SendingMessages.sendingNotInterested(bSocket);
            }
        }

        Message interestedMessage = null;

        try {
            interestedMessage = new Message(bSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(interestedMessage.getType() == (byte)2){
            peerInfo.setInterested(true);
        }
        else if(interestedMessage.getType() == (byte)3){
            peerInfo.setInterested(false);
        }

        isConnectionEstablished = true;

        while(true){
            receiveData();
            if(closeConnection){
                break;
            }
        }
    }

    public void setCloseConnection(boolean closeConnection){
        this.closeConnection = closeConnection;
    }
}
