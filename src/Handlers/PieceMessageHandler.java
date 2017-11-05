package Handlers;

import FileHandling.Piece;
import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class PieceMessageHandler implements IHandler{

    // handler has a reference to the parent peer
    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public PieceMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.downloadingPiece(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID(), 1, 1);

        // received a message with a piece of data that we wanted
        byte[] data = message.getData();

        // get the first 4 bytes which is the piece index field
        byte[] pieceLocationArray = new byte[4];
        for(int i = 0; i < 4; i++) pieceLocationArray[i] = data[i];

        // convert the byte index field into an int
        int pieceLocation = Message.byteArrayToInt(pieceLocationArray);

        // move the data into a piece
        Piece piece = new Piece(Arrays.copyOfRange(data, 4, data.length));

        // write the data to the correct location
        peer.getFileHandler().setPiece(pieceLocation, piece);

        // update our bit field and currently requesting field
        peer.getBitField().setPiece(pieceLocation);

        // look through our neighbors and if they no longer have interesting pieces send them a not interested message
        Hashtable<Integer, PeerConnection> connectionHashtable =  peer.getConnections();
        for(int key: connectionHashtable.keySet()){
            ArrayList<Integer> pieces = peer.getBitField().getInterestingBits(connectionHashtable.get(key).getPeerInfo().getBitField());
            if(pieces.isEmpty()) connectionHashtable.get(key).sendMessage(Message.createActualMessage("not interested", new byte[0]));

            // send a have message to all neighbors letting them know that we just got a new piece
            connectionHashtable.get(key).sendMessage(Message.createActualMessage("have", pieceLocationArray));
        }

    }
}
