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
import Peer.Peer;
import Sockets.BasicSocket;

import java.util.concurrent.TimeUnit;
import java.io.*;
import java.awt.GraphicsEnvironment;
import java.net.URISyntaxException;

/*
 * The StartRemotePeers class begins remote peer processes.
 * It reads configuration file PeerInfo.cfg and starts remote peer processes.
 */
public class startRemotePeers {



        public static void main (String [] args) throws Exception {
            PeerInfoReader infoReader = PeerInfoReader.getPeerInfoReader();
            for(int i = 0; i < infoReader.getNumberOfPeers(); i++) {

                String filename = "peerProcess.jar";
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar " + filename + " " + infoReader.getPeerIDS(i)});

                TimeUnit.MILLISECONDS.sleep(2400);

            }
        }


    /*public static void main(String[] args){
        for(int i = 1001; i < 1007; i++){
            String[] arguments = new String[1];
            arguments[0] = Integer.toString(i);
            new Thread(() -> {
                try {
                    peerProcess.main(arguments);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            try{
                TimeUnit.MILLISECONDS.sleep(1000);
            }catch (Exception e){

            }
        }
    }*/

    /**
     * @param args
     */
    /*public static void main(String[] args) {
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
                Runtime.getRuntime().exec("ssh " + peerInfoReader.getPeerHostNames(i) + " cd " + path + "; java peerProcess " + peerInfoReader.getPeerIDS(i));
            }

            System.out.println("All remote peers started." );

        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }*/

}
