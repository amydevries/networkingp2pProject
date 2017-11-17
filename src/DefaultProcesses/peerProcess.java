package DefaultProcesses;

import FileHandling.CommonReader;
import Peer.Peer;
import IntervalTimer.IntervalTimer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;
import static sun.misc.PostVMInitHook.run;

public class peerProcess{

    public static void main(String[] args) throws Exception{

        if(args.length != 1){
            throw new IllegalArgumentException("Peer ID must be specified.");
        }

        int peerID = Integer.parseInt(args[0]);

        new peerProcess(peerID);


    }

    public peerProcess(int peerID){
        final Peer peer = new Peer(peerID);
        peer.runFileSharing();
    }

}
