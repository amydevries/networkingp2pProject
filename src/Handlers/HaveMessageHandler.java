package Handlers;

import Logger.PeerLogger;
import Peer.Peer;
import Peer.PeerConnection;

import java.util.ArrayList;
import Peer.Message;

public class HaveMessageHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public HaveMessageHandler(Peer peer) {this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);

        // when a peer gets a piece it updates its neighbor peers with what it has
        int peerHasPieceIndex = Message.byteArrayToInt(message.getData());

        peerLogger.receivedHaveMessage(peerConnection.getParentPeer().getPeerInfo().getPeerID(), peerConnection.getPeerInfo().getPeerID(), peerHasPieceIndex);

        // update the bitfield we have for the sending peer with the new piece from the have message
        peerConnection.getPeerInfo().setBitField(peerHasPieceIndex);

        // check our bit field to see if the new piece is a piece we are interested in
        ArrayList<Integer> interestedBits =  peer.getBitField().getInterestingBits(peerConnection.getPeerInfo().getBitField());

        // if there are no interesting pieces send a not interested message
        if (interestedBits.isEmpty()) {
            peerConnection.sendMessage(Message.createActualMessage("not interested", new byte[0]));
        }else{
            peerConnection.sendMessage(Message.createActualMessage("interested", new byte[0]));
        }


    }
}
