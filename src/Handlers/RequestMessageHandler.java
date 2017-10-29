package Handlers;

import Peer.Peer;
import Peer.PeerConnection;
import Peer.Message;

public class RequestMessageHandler implements IHandler{

    private Peer peer;

    public RequestMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, Message message) {

        // recieved a request message from an unchoked peer
        // double check to make sure the peer really is unchoked
        if(!peerConnection.getChoked()){
            // if the peer is unchoked start transmitting the piece it asked for
            int pieceInterestedIn = Message.byteArrayToInt(message.getData());
            byte[] piece = peer.getFileHandler().getPiece(pieceInterestedIn).getData();
            peerConnection.sendMessage(Message.createActualMessage("piece", piece));
        }
        // else if it isnt unchoked then just ignore it

    }
}
