package Connection;

import FileHandling.PeerInfoReader;
import Peer.Peer;

import static java.lang.System.exit;

public class peerProcess {

    public static void main(String[] args) {

        if(args.length != 1) exit(0);

        int peerID = Integer.parseInt(args[0]);

        Peer peer = new Peer(peerID);

    }
}
