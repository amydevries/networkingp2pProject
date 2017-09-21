package Logger;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}