package DefaultProcesses;

import FileHandling.CommonReader;
import Peer.Peer;
import IntervalTimer.IntervalTimer;

import static java.lang.System.exit;
import static sun.misc.PostVMInitHook.run;

public class peerProcess{


    public static void main(String[] args) throws Exception{

        if(args.length != 1) exit(0);

        int peerID = Integer.parseInt(args[0]);

        new peerProcess(peerID);

    }

    public peerProcess(int peerID){
        final Peer peer = new Peer(peerID);

        (new Thread() { public void run() { peer.runFileSharing(); }}).start();

        //read the delay from the config file and then pass in the peerID
        CommonReader comRead = new CommonReader();


        IntervalTimer unchokingIntervalTimer = new IntervalTimer(comRead.getUnchokingInterval(), peerID);
        unchokingIntervalTimer.unchokingIntervalTimerStart();

        IntervalTimer optimisticIntervalTimer = new IntervalTimer(comRead.getOptimisticUnchokingInterval(), peerID);
        optimisticIntervalTimer.optimisticTimerStart();
    }

}
