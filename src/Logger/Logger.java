package Logger;

import java.util.logging.FileHandler;


public class Logger {

    static private FileHandler fileTxt;

    public void setup(int peerID){

    }

    public void tcpConnection(int peerGeneratingLog, int peerConnectedTo){

    }

    public void changePreferredNeighbors(int peerID, int[] preferredNeighborList){

    }

    public void changeOptimisticallyUnchockedNeighbor(int peerID){

    }

    public void unchoking(int peerUnchoked, int peerWhoUnchokes){

    }

    public void choking(int peerChoked, int peerWhoChokes){

    }

    public void receivedHaveMessage(int peerReceivedMessage, int peerSentMessage){

    }

    public void receivedInterestedMessage(int peerReceivedMessage, int peerSentMessage){

    }

    public void receivedNotInterestedMessage(int peerReceivedMessage, int peerSentMessage){

    }

    public void downloadingPiece(int peerDownloadedPiece, int peerSentPiece){

    }

    public void completedDownload(int peerID){

    }
}
