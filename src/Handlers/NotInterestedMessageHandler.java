package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.Message;

public class NotInterestedMessageHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public NotInterestedMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        peerLogger.receivedNotInterestedMessage(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID());

        // when we receive a not interested message remove that peer from out list of interested neighbors
        peer.interestedPeers.remove(peerConnection.getPeerInfo().getPeerID());
        peer.notInterestedPeers.put(peerConnection.getPeerInfo().getPeerID(), peerConnection.getPeerInfo());

    }
}
