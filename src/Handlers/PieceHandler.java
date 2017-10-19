package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class PieceHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public PieceHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.downloadingPiece(1, 1, 1, 1);
    }
}
