package DefaultProcesses;

/*
 *
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

import FileHandling.PeerInfoReader;

/*
 * The StartRemotePeers class begins remote peer processes.
 * It reads configuration file PeerInfo.cfg and starts remote peer processes.
 */
public class startRemotePeers {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            PeerInfoReader peerInfoReader = new PeerInfoReader();
            peerInfoReader.parse();

            int numPeers = peerInfoReader.getNumberOfPeers();
            // get current path
            String path = System.getProperty("user.dir");

            // start clients at remote hosts
            for (int i = 0; i < numPeers; i++) {
                System.out.println("Start remote peer " + peerInfoReader.getPeerIDS(i) +  " at " + peerInfoReader.getPeerHostNames(i) );

                //the line below is what is supposed to work to connect to ssh
                //Runtime.getRuntime().exec("ssh " + peerInfoReader.getPeerHostNames(i) + " cd " + path + "; java peerProcess " + peerInfoReader.getPeerIDS(i));

                //the lines below is playing with stuff to see if I can get it to just run on our machines for the time being
                //Runtime.getRuntime().exec("java peerProcess " + peerInfoReader.getPeerIDS(i));
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "java peerProcess " + peerInfoReader.getPeerIDS(i));
                Process process = pb.start();
                process.waitFor();
                //Process myProcess = Runtime.getRuntime().exec("peerProcess.java " + peerInfoReader.getPeerIDS(i));
            }
            System.out.println("All remote peers started." );

        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
