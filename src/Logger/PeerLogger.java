package Logger;


import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PeerLogger {

    private PrintWriter logger;

    public void setup(int peerID){

        try{
            logger = new PrintWriter("log_peer_"+peerID+".log");


        }catch(Exception e) {}
    }

    private String getTimeStamp(){
       return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public void tcpConnection(int peerGeneratingLog, int peerConnectedTo){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerGeneratingLog + " makes a connection to Peer " + peerConnectedTo);
    }

    public void changePreferredNeighbors(int peerID, int[] preferredNeighborList){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerID + " has the preferred neighbors " + preferredNeighborList);
    }

    public void changeOptimisticallyUnchockedNeighbor(int peerID, int optimisticallyUnchokedNeighbor){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerID + " has the optimistically unchoked neighbor " + optimisticallyUnchokedNeighbor);
    }

    public void unchoking(int peerUnchoked, int peerWhoUnchokes){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerUnchoked + " is unchoked by " + peerWhoUnchokes);
    }

    public void choking(int peerChoked, int peerWhoChokes){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerChoked + " is choked by " + peerWhoChokes);
    }

    public void receivedHaveMessage(int peerReceivedMessage, int peerSentMessage, int pieceIndex){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerReceivedMessage + " received the ‘have’ message from " + peerSentMessage + " for the piece " + pieceIndex);
    }

    public void receivedInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerReceivedMessage + " received the ‘interested’ message from " + peerSentMessage);
    }

    public void receivedNotInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerReceivedMessage + " received the ‘not interested’ message from " + peerSentMessage);
    }

    public void downloadingPiece(int peerDownloadedPiece, int peerSentPiece, int pieceIndex, int numberOfPieces){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerDownloadedPiece + " has downloaded the piece " + pieceIndex + " from " +
                peerSentPiece + "Now the number of pieces it has is " + numberOfPieces);
    }

    public void completedDownload(int peerID){
        String timeStamp= getTimeStamp();
        logger.println(timeStamp+": Peer " + peerID + "has downloaded the complete file.");
    }
}
