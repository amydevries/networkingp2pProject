package Logger;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PeerLoggerTest{

    private final int PEER_ID = -1;
    private final String fileName = "log_peer_" + PEER_ID + ".log";
    private PeerLogger logger;

    @BeforeEach
    public void setUp(){
        logger = new PeerLogger();
    }

    @AfterEach
    public void tearDown(){
        logger.close();
        try {
            Path path = Paths.get(fileName);
            Files.delete(path);
        } catch (Exception e) {}
    }

    @Test
    public void test01_LoggerSetupCreatesFile(){
        // Act
        logger.setup(PEER_ID);
        File file = new File(fileName);

        // Assert
        assertTrue(file.exists());
    }

    @Test
    public void test02_LoggerSetupCreatesFileWithRightName(){
        // Act
        logger.setup(PEER_ID);
        File file = new File("log_peer_" + (PEER_ID+1) + ".log");

        // Assert
        assertFalse(file.exists());
    }

    @Test
    public void test03_tcpConnectionPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerGeneratingLog = 1;
        int peerConnectedTo = 2;

        // Act
        logger.setup(PEER_ID);
        logger.tcpConnection(peerGeneratingLog, peerConnectedTo);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerGeneratingLog + " makes a connection to Peer " + peerConnectedTo));
    }

    @Test
    public void test04_changePreferredNeighborsPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerID = 1;
        int[] preferredNeighborList = {1, 2, 3};

        // Act
        logger.setup(PEER_ID);
        logger.changePreferredNeighbors(peerID, preferredNeighborList);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerID + " has the preferred neighbors " + Arrays.toString(preferredNeighborList)));
    }

    @Test
    public void test05_changeOptimisticallyUnchockedNeighborPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerID = 1;
        int optimisticallyUnchokedNeighbor = 2;

        // Act
        logger.setup(PEER_ID);
        logger.changeOptimisticallyUnchockedNeighbor(peerID, optimisticallyUnchokedNeighbor);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerID + " has the optimistically unchoked neighbor " + optimisticallyUnchokedNeighbor));
    }

    @Test
    public void test06_unchokingPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerUnchoked = 1;
        int peerWhoUnchokes = 2;

        // Act
        logger.setup(PEER_ID);
        logger.unchoking(peerUnchoked, peerWhoUnchokes);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerUnchoked + " is unchoked by " + peerWhoUnchokes));
    }

    @Test
    public void test07_chokingPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerChoked = 1;
        int peerWhoChokes = 2;

        // Act
        logger.setup(PEER_ID);
        logger.choking(peerChoked, peerWhoChokes);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerChoked + " is choked by " + peerWhoChokes));
    }

    @Test
    public void test08_receivedHaveMessagePrintsCorrectly(){
        // Arrange
        String line = null;
        int peerReceivedMessage = 1;
        int peerSentMessage = 2;
        int pieceIndex = 3;

        // Act
        logger.setup(PEER_ID);
        logger.receivedHaveMessage(peerReceivedMessage, peerSentMessage, pieceIndex);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerReceivedMessage + " received the ‘have’ message from " + peerSentMessage + " for the piece " + pieceIndex));
    }

    @Test
    public void test09_receivedInterestedMessagePrintsCorrectly(){
        // Arrange
        String line = null;
        int peerReceivedMessage = 1;
        int peerSentMessage = 2;

        // Act
        logger.setup(PEER_ID);
        logger.receivedInterestedMessage(peerReceivedMessage, peerSentMessage);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerReceivedMessage + " received the ‘interested’ message from " + peerSentMessage));
    }

    @Test
    public void test10_receivedNotInterestedMessagePrintsCorrectly(){
        // Arrange
        String line = null;
        int peerReceivedMessage = 1;
        int peerSentMessage = 2;

        // Act
        logger.setup(PEER_ID);
        logger.receivedNotInterestedMessage(peerReceivedMessage, peerSentMessage);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerReceivedMessage + " received the ‘not interested’ message from " + peerSentMessage));
    }

    @Test
    public void test11_downloadingPiecePrintsCorrectly(){
        // Arrange
        String line = null;
        int peerDownloadedPiece = 1;
        int peerSentPiece = 2;
        int pieceIndex = 3;
        int numberOfPieces = 4;

        // Act
        logger.setup(PEER_ID);
        logger.downloadingPiece(peerDownloadedPiece, peerSentPiece, pieceIndex, numberOfPieces);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerDownloadedPiece + " has downloaded the piece " + pieceIndex + " from " +
                peerSentPiece + "Now the number of pieces it has is " + numberOfPieces));
    }

    @Test
    public void test12_completedDownloadPrintsCorrectly(){
        // Arrange
        String line = null;
        int peerID = 1;

        // Act
        logger.setup(PEER_ID);
        logger.completedDownload(peerID);

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) { }

        // Assert
        assertTrue(line.contains(": Peer " + peerID + "has downloaded the complete file."));
    }
}