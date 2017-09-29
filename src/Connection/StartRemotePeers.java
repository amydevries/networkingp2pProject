package Connection;

/*
 *
 * This is the program starting remote processes.
 * This program was only tested on CISE SunOS environment.
 * If you use another environment, for example, linux environment in CISE
 * or other environments not in CISE, it is not guaranteed to work properly.
 * It is your responsibility to adapt this program to your running environment.
 */

import FileHandling.PeerInfoReader;
import peerbase.PeerInfo;

import java.io.*;
import java.util.*;

/*
 * The StartRemotePeers class begins remote peer processes.
 * It reads configuration file PeerInfo.cfg and starts remote peer processes.
 */
public class StartRemotePeers {

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
                System.out.println("Start remote peer " + peerInfoReader.getPeerIDS(0) +  " at " + peerInfoReader.getPeerHostNames(0) );

                Runtime.getRuntime().exec("ssh " + peerInfoReader.getPeerHostNames(0) + " cd " + path + "; java peerProcess " + peerInfoReader.getPeerIDS(0));

            }
            System.out.println("All remote peers started." );

        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
