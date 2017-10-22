package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class HaveMessageHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public HaveMessageHandler(Peer peer) {this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.receivedHaveMessage(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID(),1);

        // when a peer gets a piece it updates its neighbor peers with what it has

        // check our bit field to see if the new piece is a piece we are interested in
        // update the bitfield we have for the sending peer with the new piece form the have message
    }
}
