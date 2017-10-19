package Handlers;

import Logger.PeerLogger;
import Peer.*;

public class UnChokeMessageHandler implements IHandler{

    private Peer peer;
    private PeerLogger peerLogger = new PeerLogger();

    public UnChokeMessageHandler(Peer peer) { this.peer = peer; }

    @Override
    public void handleMessage(PeerConnection peerConnection, PeerMessage peerMessage) {

        //setup the logger for use; need to have "true" to indicate that the file already exists
        peerLogger.setup(peerConnection.getPeerInfo().getPeerID(), true);
        //Writes to log file: update the 1s with variables when they're known
        peerLogger.unchoking(1, 1);


        // yay we got an unchoke message
        // look through the bitfield of the peer who unchoked us for interesting pieces

        // while we havent timed out

            // randomly select a piece we need from the available pieces
            // mark that piece as currently being requested

            // send a request message to the peer who unchoked us

            // wait for pieces to come back
            // if a piece comes back add it to our file

            // if we time out then that means that the peer changed peers it was sending to while we were waiting
                //unmark the piece as requested and break loop

        // go back to start of while


    }
}
