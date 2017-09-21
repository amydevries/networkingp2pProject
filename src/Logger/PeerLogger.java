package Logger;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PeerLogger {

    private FileWriter logger;

    public void setup(int peerID){

        try{
            logger = new FileWriter("log_peer_"+peerID+".log");


        }catch(Exception e) {}
    }

    public void close(){
        try{
            logger.close();
        }catch (Exception e) {}
    }

    private String getTimeStamp(){
       return new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public void tcpConnection(int peerGeneratingLog, int peerConnectedTo){
        String timeStamp= getTimeStamp();

        try {
            logger.write(timeStamp+": Peer " + peerGeneratingLog + " makes a connection to Peer " + peerConnectedTo);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changePreferredNeighbors(int peerID, int[] preferredNeighborList){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerID + " has the preferred neighbors " + preferredNeighborList);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeOptimisticallyUnchockedNeighbor(int peerID, int optimisticallyUnchokedNeighbor){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerID + " has the optimistically unchoked neighbor " + optimisticallyUnchokedNeighbor);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unchoking(int peerUnchoked, int peerWhoUnchokes){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerUnchoked + " is unchoked by " + peerWhoUnchokes);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choking(int peerChoked, int peerWhoChokes){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerChoked + " is choked by " + peerWhoChokes);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedHaveMessage(int peerReceivedMessage, int peerSentMessage, int pieceIndex){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerReceivedMessage + " received the ‘have’ message from " + peerSentMessage + " for the piece " + pieceIndex);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerReceivedMessage + " received the ‘interested’ message from " + peerSentMessage);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedNotInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerReceivedMessage + " received the ‘not interested’ message from " + peerSentMessage);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadingPiece(int peerDownloadedPiece, int peerSentPiece, int pieceIndex, int numberOfPieces){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerDownloadedPiece + " has downloaded the piece " + pieceIndex + " from " +
                    peerSentPiece + "Now the number of pieces it has is " + numberOfPieces);
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void completedDownload(int peerID){
        String timeStamp= getTimeStamp();
        try {
            logger.write(timeStamp+": Peer " + peerID + "has downloaded the complete file.");
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
