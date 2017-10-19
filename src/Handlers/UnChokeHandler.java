package Handlers;

import Logger.PeerLogger;
import Peer.*;

public class UnChokeHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public UnChokeHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.unchoking(1, 1);

    }
}
