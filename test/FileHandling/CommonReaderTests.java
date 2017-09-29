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

public class CommonReaderTests {

    private static String testFileName = "CommonTest.cfg";

    @BeforeAll
    public static void createFile(){
        try {
            FileWriter writer = new FileWriter(testFileName);

            writer.write("NumberOfPreferredNeighbors 2\n" +
                    "UnchokingInterval 5\n" +
                    "OptimisticUnchokingInterval 15\n" +
                    "FileName TheFile.dat\n" +
                    "FileSize 10000232\n" +
                    "PieceSize 32768");
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
    public void test01_testAllTheStuff(){
        // Arrange
        CommonReader reader = new CommonReader(testFileName);

        // Act
        reader.parse();

        // Assert
        assertTrue(reader.getNumberPreferredNeighbors() == 2);
        assertTrue(reader.getUnchokingInterval() == 5);
        assertTrue(reader.getOptimisticUnchokingInterval() == 15);
        assertTrue(reader.getFileName().equals("TheFile.dat"));
        assertTrue(reader.getFileSize() == 10000232);
        assertTrue(reader.getPieceSize() == 32768);

    }
}
