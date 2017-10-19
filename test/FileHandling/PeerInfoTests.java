package FileHandling;

import Peer.PeerInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeerInfoTests {
    private static String testFileName = "PeerInfoTest.cfg";

    @BeforeAll
    public static void createFile(){
        try {
            FileWriter writer = new FileWriter(testFileName);

            writer.write("1001 lin114-00.cise.ufl.edu 6008 1\n" +
                    "1002 lin114-01.cise.ufl.edu 6008 0\n" +
                    "1003 lin114-02.cise.ufl.edu 6008 0\n" +
                    "1004 lin114-03.cise.ufl.edu 6008 0\n" +
                    "1005 lin114-04.cise.ufl.edu 6008 0\n" +
                    "1006 lin114-05.cise.ufl.edu 6008 0");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void destroyFile(){
        try {
            Path path = Paths.get(testFileName);
            Files.delete(path);
        } catch (Exception e) {}
    }

    @Test
    public void test01_createsLogFile(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();
        //create new PeerInfo for each peer based on ID from file
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        //Assert
        assertTrue(peer1.getPeerLog().equals(reader.getPeerLogs(0)));
        assertTrue(peer2.getPeerLog().equals(reader.getPeerLogs(1)));
        assertTrue(peer3.getPeerLog().equals(reader.getPeerLogs(2)));
        assertTrue(peer4.getPeerLog().equals(reader.getPeerLogs(3)));
        assertTrue(peer5.getPeerLog().equals(reader.getPeerLogs(4)));
    }

    @Test
    public void test02_getsPeerIDs(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();
        //create new PeerInfo for each peer based on ID from file
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        //Assert
        assertTrue(peer1.getPeerID() == reader.getPeerIDS(0));
        assertTrue(peer2.getPeerID() == reader.getPeerIDS(1));
        assertTrue(peer3.getPeerID() == reader.getPeerIDS(2));
        assertTrue(peer4.getPeerID() == reader.getPeerIDS(3));
        assertTrue(peer5.getPeerID() == reader.getPeerIDS(4));
    }

    @Test
    public void test03_getsPeerHostIDs(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();
        //create new PeerInfo for each peer based on ID from file
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        //Assert
        assertTrue(peer1.getHostID().equals(reader.getPeerHostNames(0)));
        assertTrue(peer2.getHostID().equals(reader.getPeerHostNames(1)));
        assertTrue(peer3.getHostID().equals(reader.getPeerHostNames(2)));
        assertTrue(peer4.getHostID().equals(reader.getPeerHostNames(3)));
        assertTrue(peer5.getHostID().equals(reader.getPeerHostNames(4)));
    }

    @Test
    public void test04_getsPeerPorts(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();
        //create new PeerInfo for each peer based on ID from file
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        //Assert
        assertTrue(peer1.getPort() == reader.getPeerPorts(0));
        assertTrue(peer2.getPort() == reader.getPeerPorts(1));
        assertTrue(peer3.getPort() == reader.getPeerPorts(2));
        assertTrue(peer4.getPort() == reader.getPeerPorts(3));
        assertTrue(peer5.getPort() == reader.getPeerPorts(4));
    }

    @Test
    public void test05_getsFullFileOrNot(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();
        //create new PeerInfo for each peer based on ID from file
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        //Assert
        assertTrue(peer1.getFileFinished() == reader.getPeerFullFileOrNot(0));
        assertTrue(peer2.getFileFinished() == reader.getPeerFullFileOrNot(1));
        assertTrue(peer3.getFileFinished() == reader.getPeerFullFileOrNot(2));
        assertTrue(peer4.getFileFinished() == reader.getPeerFullFileOrNot(3));
        assertTrue(peer5.getFileFinished() == reader.getPeerFullFileOrNot(4));
    }

    @Test
    public void test06_fileExperiment() throws IOException {
        // Arrange
        try {
            FileWriter writer = new FileWriter("amy.txt");

            writer.write("1001 lin114-00.cise.ufl.edu 6008 1\n" +
                    "1002 lin114-01.cise.ufl.edu 6008 0\n" +
                    "1003 lin114-02.cise.ufl.edu 6008 0\n" +
                    "1004 lin114-03.cise.ufl.edu 6008 0\n" +
                    "1005 lin114-04.cise.ufl.edu 6008 0\n" +
                    "1006 lin114-05.cise.ufl.edu 6008 0\n");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act
        try {
            FileWriter writer2 = new FileWriter( "amy.txt", true);
            writer2.write("Hello world");
            writer2.flush();
            //writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
