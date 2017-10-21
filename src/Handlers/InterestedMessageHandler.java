package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

public class InterestedMessageHandler implements IHandler {

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public InterestedMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        peerLogger.receivedInterestedMessage(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID());

        // the peer is now interested in some of the pieces we have

        // get the peerInfo from the peerMessage

        // add the peer to our list of interested peers

    }
}