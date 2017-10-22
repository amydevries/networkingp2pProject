package StartUpTest;

import FileHandling.PeerInfoReader;
import Peer.Peer;
import Peer.PeerInfo;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PeerProcessStarter {
    //Try to start up the peer processes to see if anything actually does anything.
   /* private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static void runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        System.out.println(command + " exitValue() " + pro.exitValue());
    }

    public static void main(String[] args) {
        try {
            runProcess("cmd cd C:\\Users\\amy_d\\Documents\\GitHub\\networkingp2pProject\\src\\DefaultProcesses javac PeerProcess.java");
            runProcess("java PeerProcess 1001");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Test
    public void test01_startPeersPlease() {
        // Arrange
        PeerInfoReader reader = new PeerInfoReader("PeerInfo.cfg");


        // Act
        reader.parse();
        PeerInfo peer1 = new PeerInfo(reader.getPeerIDS(0));
        PeerInfo peer2 = new PeerInfo(reader.getPeerIDS(1));
        PeerInfo peer3 = new PeerInfo(reader.getPeerIDS(2));
        PeerInfo peer4 = new PeerInfo(reader.getPeerIDS(3));
        PeerInfo peer5 = new PeerInfo(reader.getPeerIDS(4));

        try{
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "java peerProcess " + reader.getPeerIDS(0));
            Process process = pb.start();
            process.waitFor();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
