package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;
import Peer.PeerMessage;

import java.util.ArrayList;

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
        int peerHasPiece = PeerMessage.byteArrayToInt(peerMessage.getData());

        peerConnection.getPeerInfo().getBitField().setPiece(peerHasPiece);

        // check our bit field to see if the new piece is a piece we are interested in
        // update the bitfield we have for the sending peer with the new piece from the have message
        ArrayList<Integer> interestedBits =  peer.getBitField().compareTo(peerConnection.getPeerInfo().getBitField());

        byte[] payload = new byte[0];

        if (interestedBits.isEmpty()) {
            peerConnection.sendMessage(PeerMessage.createActualMessage("not interested", payload));
        }


    }
}
