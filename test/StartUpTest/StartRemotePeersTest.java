package StartUpTest;

import DefaultProcesses.startRemotePeers;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class StartRemotePeersTest {
    @Test
    public void testSsh(){
        //Arrange
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        String expectedOutput  = "Start remote peer 1002 at lin114-01.cise.ufl.edu\n" +
                "Start remote peer 1003 at lin114-02.cise.ufl.edu\n" +
                "Start remote peer 1004 at lin114-03.cise.ufl.edu\n" +
                "Start remote peer 1005 at lin114-04.cise.ufl.edu\n" +
                "Start remote peer 1006 at lin114-05.cise.ufl.edu\n" +
                "All remote peers started.";
        System.out.println(expectedOutput + "\n");

        //Act
        startRemotePeers.main(new String[] {});

        //Assert

        System.out.println(consoleOutput.toString());
        //assertTrue(expectedOutput == consoleOutput.toString());
    }
}
