package Peer;

import FileHandling.CommonReader;
import FileHandling.FileHandler;
import FileHandling.PeerInfoReader;
import Logger.PeerLogger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class Peer extends Thread{

    public static ExecutorService executorService;

    private static boolean programFinished = false;
    private static PeerInfo peerInfo;

    private static Hashtable<Integer, PeerInfo> peers = new Hashtable<Integer,PeerInfo>();
    public static ArrayList<PeerConnection> connections = new ArrayList<PeerConnection>();

    private PeerInfoReader peerReader = PeerInfoReader.getPeerInfoReader();
    private CommonReader commonReader = CommonReader.getCommonReader();
    private PeerLogger peerLogger;

    public static FileHandler fileHandler;

    public Peer(int myID){
        peerInfo = new PeerInfo(myID);
        peerReader.parse();
        peers = peerInfo.getNeighborPeers(myID);

        fileHandler = new FileHandler();
        peerLogger = PeerLogger.getLogger();
    }

    public void runFileSharing(){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //loop though peers and add them to the hashtable and connect with the ones that are already in the hashtable?
        for(int i = 0; i < peerReader.getNumberOfPeers(); i++){

            PeerInfo infoToAdd = new PeerInfo(peerReader.getPeerIDS(i), peerReader.getPeerHostNames(i), peerReader.getPeerPorts(i),
                    peerReader.getPeerFullFileOrNot(i));

            peers.put(peerReader.getPeerIDS(i), infoToAdd);
        }

        executorService = Executors.newCachedThreadPool();

        //listens to incoming connections
        executorService.execute(new IncomingConnections());

        for(int key: peers.keySet()){
            if(key < peerInfo.getPeerID()){
                executorService.execute(new PeerConnection(peers.get(key)));
            }
        }

        //read the delay from the config file and then pass in the peerID
        CommonReader comRead = CommonReader.getCommonReader();

        Timer unchokingIntervalTimer = new Timer();
        unchokingIntervalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                unChokingInterval();
            }
        }, 0, comRead.getUnchokingInterval() * 1000);

        Timer optimisticUnchokingIntervalTimer = new Timer();
        optimisticUnchokingIntervalTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                optimisticUnchokingInterval();
            }
        }, 0, comRead.getOptimisticUnchokingInterval() * 1000);

    }

    public static PeerInfo getPeerInfo(){
        return peerInfo;
    }

    public PeerInfo getPeer(int peerID){
        for (int key : peers.keySet())
            if (peerInfo.getPeerID() == peerID)
                return peers.get(key);
        return null;
    }

    public static Hashtable<Integer, PeerInfo> getPeers() {
        return peers;
    }

    private void unChokingInterval(){
        PeerLogger peerLogger = PeerLogger.getLogger();

        if(fileHandler.isFull() && connections.size() > 0){

            ArrayList<Integer> interestedConnections = new ArrayList<>();
            for(int i = 0; i < connections.size(); ++i){
                if(connections.get(i).getPeerInfo().isInterested()) interestedConnections.add(connections.get(i).getPeerInfo().getPeerID());
                if(!connections.get(i).isChoked()){
                    connections.get(i).sendChoke();
                }
                if(connections.get(i).doesPeerThinkWereInterested()) connections.get(i).sendNotInterested();
            }

            int preferredNeighbor = 0;
            Random random = new Random();
            if(interestedConnections.size()> 0) {
                for (int interestedConnection = 0; interestedConnection < interestedConnections.size() && preferredNeighbor < commonReader.getNumberPreferredNeighbors(); ++interestedConnection) {
                    int randomNeighbor = Math.abs(random.nextInt(interestedConnections.size()));
                    for (int connection = 0; connection < connections.size(); ++connection) {
                        if(interestedConnections.size() <= 0) break;
                        if (connections.get(connection).getPeerInfo().getPeerID() == interestedConnections.get(randomNeighbor)) {
                            connections.get(connection).sendUnchoke();
                            preferredNeighbor++;
                            interestedConnections.remove(randomNeighbor);
                            break;
                        }
                    }
                }
            }

        }
        else {

            ArrayList<DownloadPerPeer> downloadPerPeers = new ArrayList<DownloadPerPeer>();

            for(PeerConnection connection : connections){
                downloadPerPeers.add(new DownloadPerPeer(connection.getPeerInfo().getPeerID(), connection.getPiecesReceived()));
            }

            Collections.sort(downloadPerPeers);

            //for the top n peers, unchoke the n that have uploaded the most

            int neighborsSendingTo = 0;
            int[] neighbors = new int[commonReader.getNumberPreferredNeighbors()];
            for(DownloadPerPeer downloadPerPeer: downloadPerPeers){
                for(PeerConnection connection: connections){
                    if(connection.getPeerInfo().getPeerID() == downloadPerPeer.getPeerID() &&
                            connection.getConnectionEstablished() &&
                            connection.getPeerInfo().isInterested() &&
                            neighborsSendingTo < commonReader.getNumberPreferredNeighbors()){
                        if(connection.getPeerInfo().isChoked()){
                            connection.sendUnchoke();
                            connection.setChoked(false);
                        }
                        neighbors[neighborsSendingTo] = connection.getPeerInfo().getPeerID();
                        neighborsSendingTo++;
                        downloadPerPeer.setUsed(true);
                    }
                }
            }

            for(DownloadPerPeer downloadPerPeer: downloadPerPeers){
                if(!downloadPerPeer.isUsed()){
                    for(PeerConnection peerConnection: connections){
                        if(peerConnection.getPeerInfo().getPeerID() == downloadPerPeer.getPeerID() &&
                                peerConnection.getConnectionEstablished() &&
                                !peerConnection.getPeerInfo().isChoked()){
                            peerConnection.setChoked(true);
                            peerConnection.sendChoke();
                        }
                    }
                }
            }

            for (int k = 0; k < connections.size(); k++) {
                if (connections.get(k).getConnectionEstablished()) {
                    connections.get(k).resetPiecesReceived();
                }
            }

            //setup the logger for use; need to have "true" to indicate that the file already exists
            peerLogger.changePreferredNeighbors(peerInfo.getPeerID(), neighbors);
        }
        if(connections.size()> 0){
            programFinished = true;
            if(!fileHandler.getBitField().isFull()) programFinished = false;
            for(int k =0; k < connections.size(); k++){
                if(!connections.get(k).getPeerInfo().getBitFieldOfRemotePeer().isFull()){
                    programFinished = false;
                }
            }
            if(programFinished){
                finishProgram();
            }
        }
    }

    private void optimisticUnchokingInterval(){
        ArrayList<Integer> potentialConnections = new ArrayList<Integer>();
        PeerLogger peerLogger = PeerLogger.getLogger();
        for (int i = 0; i < connections.size(); i++) {
            //check to see if it is unchoked AND we are interested in it
            if (connections.get(i).getConnectionEstablished() && connections.get(i).getPeerInfo() != null && connections.get(i).isChoked() && connections.get(i).getPeerInfo().isInterested()) {
                potentialConnections.add(connections.get(i).getPeerInfo().getPeerID());
            }
        }
        Random random = new Random();
        if (potentialConnections.size() > 0) {

            //generate random number to select random neighbor
            int randomNeighbor= Math.abs(random.nextInt(potentialConnections.size()));
            for(int i = 0; i < connections.size(); ++i){
                if(potentialConnections.get(randomNeighbor) == connections.get(i).getPeerInfo().getPeerID()){
                    connections.get(i).sendUnchoke();
                    //setup the logger for use; need to have "true" to indicate that the file already exists
                    peerLogger.changeOptimisticallyUnchockedNeighbor(peerInfo.getPeerID(), potentialConnections.get(randomNeighbor));
                    break;
                }
            }

        }
    }

    public static void finishProgram(){
        for(int k =0; k < connections.size(); k++){
            connections.get(k).setCloseConnection(true);
        }
        PeerInfoReader peerReader = PeerInfoReader.getPeerInfoReader();
        for(int k = 0; k< peerReader.getNumberOfPeers(); k ++){
            if(peerReader.getPeerFullFileOrNot(k) != 1 && peerReader.getPeerIDS(k) == peerInfo.getPeerID()){
                fileHandler.writingFile();
            }
        }

        executorService.shutdownNow();
        exit(0);
    }

}
