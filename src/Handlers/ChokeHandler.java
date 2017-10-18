package Handlers;

        import Peer.Peer;
        import Peer.PeerConnection;
        import Peer.PeerMessage;

public class ChokeHandler implements IHandler {

    private Peer peer;

    public ChokeHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

    }
}
