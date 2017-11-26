package FileHandling;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PeerInfoReaderTests {

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
    public void test01_readsPeerIdsProperly(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getPeerIDS(0) == 1001);
        assertTrue(reader.getPeerIDS(1) == 1002);
        assertTrue(reader.getPeerIDS(2) == 1003);
        assertTrue(reader.getPeerIDS(3) == 1004);
        assertTrue(reader.getPeerIDS(4) == 1005);
        assertTrue(reader.getPeerIDS(5) == 1006);
    }

    @Test
    public void test02_readsHostNamesProperly(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getPeerHostNames(0).equals("lin114-00.cise.ufl.edu"));
        assertTrue(reader.getPeerHostNames(1).equals("lin114-01.cise.ufl.edu"));
        assertTrue(reader.getPeerHostNames(2).equals("lin114-02.cise.ufl.edu"));
        assertTrue(reader.getPeerHostNames(3).equals("lin114-03.cise.ufl.edu"));
        assertTrue(reader.getPeerHostNames(4).equals("lin114-04.cise.ufl.edu"));
        assertTrue(reader.getPeerHostNames(5).equals("lin114-05.cise.ufl.edu"));
    }

    @Test
    public void test03_readsPeerPortsProperly(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getPeerPorts(0) == 6008);
        assertTrue(reader.getPeerPorts(1) == 6008);
        assertTrue(reader.getPeerPorts(2) == 6008);
        assertTrue(reader.getPeerPorts(3) == 6008);
        assertTrue(reader.getPeerPorts(4) == 6008);
        assertTrue(reader.getPeerPorts(5) == 6008);
    }

    @Test
    public void test04_readsPeerFullFileOrNotProperly(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getPeerFullFileOrNot(0) == 1);
        assertTrue(reader.getPeerFullFileOrNot(1) == 0);
        assertTrue(reader.getPeerFullFileOrNot(2) == 0);
        assertTrue(reader.getPeerFullFileOrNot(3) == 0);
        assertTrue(reader.getPeerFullFileOrNot(4) == 0);
        assertTrue(reader.getPeerFullFileOrNot(5) == 0);
    }

    @Test
    public void test05_getNumberOfPeersProperly(){
        // Arrange
        PeerInfoReader reader = new PeerInfoReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getNumberOfPeers() == 6);
    }
}
