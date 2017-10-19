package DefaultProcesses;

import Peer.Peer;

import static java.lang.System.exit;
import static sun.misc.PostVMInitHook.run;

public class peerProcess{


    public static void main(String[] args) throws Exception{

        if(args.length != 1) exit(0);

        int peerID = Integer.parseInt(args[0]);

        new peerProcess(peerID);
    }

    public peerProcess(int peerID){
        Peer peer = new Peer(peerID);

        (new Thread() { public void run() { peer.runFileSharing(); }}).start();


    }
}
