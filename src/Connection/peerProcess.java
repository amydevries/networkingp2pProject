package Connection;

import FileHandling.PeerInfoReader;

import static java.lang.System.exit;

public class peerProcess {

    public static void main(String[] args) {

        if(args.length != 1) exit(0);

        int peerID = Integer.parseInt(args[0]);

        PeerInfoReader peerInfoReader = new PeerInfoReader();
        peerInfoReader.parse();

        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(peerID == peerInfoReader.getPeerIDS(i)) break;

        }

    }

    private void startPeers(int peerID, PeerInfoReader peerInfoReader){
        for(int i = 0; i < peerInfoReader.getNumberOfPeers(); ++i){
            if(peerID == peerInfoReader.getPeerIDS(i)) break;

            SampleClient sampleClient = new SampleClient();
        }
    }
}
