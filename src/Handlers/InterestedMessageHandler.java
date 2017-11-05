package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.Message;

public class InterestedMessageHandler implements IHandler {

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public InterestedMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        peerLogger.receivedInterestedMessage(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID());

        // the peer is now interested in some of the pieces we have
        if(peer.notInterestedPeers.containsKey(peerConnection.getPeerInfo().getPeerID())) peer.notInterestedPeers.remove(peerConnection.getPeerInfo().getPeerID());
        if(!peer.interestedPeers.containsKey(peerConnection.getPeerInfo().getPeerID())) peer.interestedPeers.put(peerConnection.getPeerInfo().getPeerID(), peerConnection.getPeerInfo());

    }
}
