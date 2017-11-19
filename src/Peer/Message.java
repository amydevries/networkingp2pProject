package Peer;

import Sockets.BasicSocket;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.Arrays;

public class Message {

    private byte[] header;
    private byte[] type = null;
    private byte[] data;
    private byte[] peerID;

    public byte[] getHeader() {
        return header;
    }

    public int getType() {
        if(type == null) return 99;
        else return Message.byteArrayToInt(type);
    }

    public byte[] getData() {
        return data;
    }

    public int getPeerID() {return byteArrayToInt(peerID); }

    public Message() {}

    public Message(BasicSocket socket) throws IOException {
        header = new byte[4];
        peerID = new byte[4];
                    System.out.println("Here1");
        int length;

        socket.read(header);
        String header_str = new String(header, Charset.forName("US-ASCII"));
                    System.out.println(header_str);

        if (header_str.equals("P2PF")) {
                byte [] header_cont = new byte[14];
                byte [] zeros = new byte[10];

                peerID = new byte[4];
                        System.out.println("Here2");
                //type = intToByteArray(99);
                        System.out.println("Here3");
                socket.read(header_cont);  // TODO: check that we read 14 bytes and if not throw the exception
                String header_cont_str = new String(header_cont, Charset.forName("US-ASCII"));
                        System.out.println("Here4");
                if(!header_cont_str.equals("ILESHARINGPROJ")) {
                    // TODO: throw exception
                        System.out.println("Here5");
                    return;
                }
                    System.out.println("Here6");
                int size = socket.read(zeros);
                    if(size != 10){
                        System.out.println("nope");
                    }
                    System.out.println("Here7");
                    System.out.println("zeros: " + byteArrayToInt(zeros));

                    System.out.println("Here8");

                int size2 = socket.read(peerID);
                if(size2 != 4){
                    System.out.println("nope2");
                }
                    System.out.println("bytePeerId: " + byteArrayToInt(peerID));
        } else {
            type = new byte[1];
            length = byteArrayToInt(header);
            socket.read(type);

            data = new byte[length-1];

            if (length > 0) {
                if (socket.read(data) != length) {
                    throw new IOException("EOF in Message constructor: " +
                            "Unexpected message data getNumberOfBits");
                }

            }
        }
    }

    static public byte[] createActualMessage(String type, byte[] messagePayload){

        int messageType = 0;


        //determine the message type code
        switch(type){
            case "choke": messageType = 0; break;
            case "unchoke": messageType = 1; break;
            case "interested": messageType = 2; break;
            case "not interested": messageType = 3; break;
            case "have": messageType = 4; break;
            case "bitfield": messageType = 5; break;
            case "request": messageType =6; break;
            case "piece": messageType = 7; break;
            case "handshake": messageType = 99; break;
        }

        byte[] actualMessage;

        //the size of the actual message is the payload + 1 for type + 4 for message getNumberOfBits
        int length = messagePayload.length + 1;

        ByteBuffer actualMsgBuffer = ByteBuffer.allocate(length);
        actualMsgBuffer.putInt(length);
        actualMsgBuffer.putInt(messageType);
        actualMsgBuffer.put(messagePayload);
        actualMessage = actualMsgBuffer.array();

        return actualMessage;
    }

    static public byte[] createHandshakeMessage(int peerID){

        System.out.println(peerID);
        //Handshake message is 32 bytes
        String header = "P2PFILESHARINGPROJ";
        byte[] headerAsByteArray = header.getBytes();
        byte[] zeroPads = new byte[18];
        Arrays.fill(zeroPads, (byte)0);
        byte[] peerIDAsByteArray = new byte[4];
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.putInt(peerID);
        buff.flip();
        buff.get(peerIDAsByteArray);

        byte[] message = concatAll(headerAsByteArray, zeroPads, peerIDAsByteArray);
        try {
            String str = new String(message, "UTF-8");
            System.out.println("str: " + str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static int byteArrayToInt(byte[] byteArray) {
        int integer = 0;
        for (int i = 0; i < byteArray.length; i++) {
            integer = (integer << 8) | ( ((int)byteArray[i]) & 0xff );
        }

        return integer;
    }

    public static byte[] intToByteArray(final int number){
        int byteNum = (40 - Integer.numberOfLeadingZeros (number < 0 ? ~number : number)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (number >>> (n * 8));

        return (byteArray);
    }

}

