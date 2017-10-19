package Logger;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class PeerLogger {

    private FileWriter logger;

    public void setup(int peerID){

        try{
            logger = new FileWriter("log_peer_"+peerID+".log");

        }catch(Exception e) {}
    }

    //if the file already exists, the FileWriter needs to append to the file not create a new one
    public void setup(int peerID, boolean fileExists){
        try{
            logger = new FileWriter("log_peer_"+peerID+".log", true);

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

    private void printToFile(String phrase){
        try {
            logger.write(phrase + "\n");
            logger.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tcpConnection(int peerGeneratingLog, int peerConnectedTo){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerGeneratingLog + " makes a connection to Peer " + peerConnectedTo);
    }

    public void changePreferredNeighbors(int peerID, int[] preferredNeighborList){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerID + " has the preferred neighbors " + Arrays.toString(preferredNeighborList));
    }

    public void changeOptimisticallyUnchockedNeighbor(int peerID, int optimisticallyUnchokedNeighbor){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerID + " has the optimistically unchoked neighbor " + optimisticallyUnchokedNeighbor);
    }

    public void unchoking(int peerUnchoked, int peerWhoUnchokes){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerUnchoked + " is unchoked by " + peerWhoUnchokes);
    }

    public void choking(int peerChoked, int peerWhoChokes){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerChoked + " is choked by " + peerWhoChokes);
    }

    public void receivedHaveMessage(int peerReceivedMessage, int peerSentMessage, int pieceIndex){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerReceivedMessage + " received the ‘have’ message from " + peerSentMessage + " for the piece " + pieceIndex);
    }

    public void receivedInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerReceivedMessage + " received the ‘interested’ message from " + peerSentMessage);
    }

    public void receivedNotInterestedMessage(int peerReceivedMessage, int peerSentMessage){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerReceivedMessage + " received the ‘not interested’ message from " + peerSentMessage);
    }

    public void downloadingPiece(int peerDownloadedPiece, int peerSentPiece, int pieceIndex, int numberOfPieces){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerDownloadedPiece + " has downloaded the piece " + pieceIndex + " from " +
                peerSentPiece + "Now the number of pieces it has is " + numberOfPieces);
    }

    public void completedDownload(int peerID){
        String timeStamp= getTimeStamp();
        printToFile(timeStamp+": Peer " + peerID + "has downloaded the complete file.");
    }
}
