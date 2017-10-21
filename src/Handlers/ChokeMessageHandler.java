package Handlers;

        import Logger.PeerLogger;
        import Peer.Peer;
        import Peer.PeerConnection;
        import Peer.PeerMessage;

public class ChokeMessageHandler implements IHandler {

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public ChokeMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        peerLogger.choking(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID());


        // not sure what to do with this one

        // it seems like we dont have to do much with it since whether or not a peer is choking us only matters when were calculating the transmission rate

        //once the connection is choked, update the variable in PeerConnection
        peerConnection.setChoked(true);
    }
}
