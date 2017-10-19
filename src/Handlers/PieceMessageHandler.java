package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class PieceMessageHandler implements IHandler{

    // handler has a reference to the parent peer
    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public PieceMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.downloadingPiece(1, 1, 1, 1);

        // recieved a message with a piece of data that we wanted

        // write the data to the correct location

        // update our bitfield and currently requesting field

        // look through our neighbors and if they no longer have intersting pieces send them a not interested message

        // send a have message to all neighbors letting them know that we just got a new piece
    }
}
