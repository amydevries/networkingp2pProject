package DefaultProcesses;

import FileHandling.CommonReader;
import Peer.Peer;
import IntervalTimer.IntervalTimer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;
import static sun.misc.PostVMInitHook.run;

public class peerProcess{

    static ExecutorService executorService;

    public static void main(String[] args) throws Exception{

        if(args.length != 1){
            throw new IllegalArgumentException("Peer ID must be specified.");
        }

        int peerID = Integer.parseInt(args[0]);

        new peerProcess(peerID);

    }

    public peerProcess(int peerID){
        final Peer peer = new Peer(peerID);

        executorService = Executors.newCachedThreadPool();
        executorService.execute(new IncomingConnections());

        peer.runFileSharing(executorService);

        //read the delay from the config file and then pass in the peerID
        CommonReader comRead = CommonReader.getCommonReader();


        IntervalTimer unchokingIntervalTimer = new IntervalTimer(comRead.getUnchokingInterval(), peerID);
        unchokingIntervalTimer.unchokingIntervalTimerStart();

        IntervalTimer optimisticIntervalTimer = new IntervalTimer(comRead.getOptimisticUnchokingInterval(), peerID);
        optimisticIntervalTimer.optimisticTimerStart();
    }

}
