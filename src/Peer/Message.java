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
        else return (type[0]);
    }

    public byte[] getData() {
        return data;
    }

    public int getPeerID() {return byteArrayToInt(peerID); }

    public Message(BasicSocket socket) throws IOException {
        synchronized (this) {
            header = new byte[4];
            peerID = new byte[4];
            int length;
            socket.read(header);
            String header_str = new String(header, Charset.forName("US-ASCII"));

            if (header_str.equals("P2PF")) {
                byte[] header_cont = new byte[14];
                byte[] zeros = new byte[10];

                peerID = new byte[4];
                //type = intToByteArray(99);
                socket.read(header_cont);  // TODO: check that we read 14 bytes and if not throw the exception
                String header_cont_str = new String(header_cont, Charset.forName("US-ASCII"));
                if (!header_cont_str.equals("ILESHARINGPROJ")) {
                    return;
                }
                int size = socket.read(zeros);


                socket.read(peerID);

            } else {
                type = new byte[1];
                length = byteArrayToInt(header);
                socket.read(type);

                if (length > 0) {
                    data = new byte[length - 1];
                    if (socket.read(data) > length - 1) {
                        throw new IOException("EOF in Message constructor: " +
                                "Unexpected message data getNumberOfBits");
                    }

                }
            }
        }
    }

    static public byte[] createActualMessage(String type, byte[] messagePayload){

        byte messageType = 0;


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
        byte[] length = intToByteArray(messagePayload.length + 1);
        if(Message.byteArrayToInt(length) < 0){
            System.exit(0);
        }

        ByteBuffer actualMsgBuffer = ByteBuffer.allocate(Message.byteArrayToInt(length)+4);
        actualMsgBuffer.put(length);
        actualMsgBuffer.put(messageType);
        actualMsgBuffer.put(messagePayload);
        actualMessage = actualMsgBuffer.array();

        return actualMessage;
    }

    static public byte[] createHandshakeMessage(int peerID){

        //Handshake message is 32 bytes
        String header = "P2PFILESHARINGPROJ";
        byte[] headerAsByteArray = header.getBytes();
        byte[] zeroPads = new byte[10];
        Arrays.fill(zeroPads, (byte)0);
        byte[] peerIDAsByteArray = new byte[4];
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.putInt(peerID);
        buff.flip();
        buff.get(peerIDAsByteArray);

        byte[] message = concatAll(headerAsByteArray, zeroPads, peerIDAsByteArray);
        try {
            String str = new String(message, "US-ASCII");
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

        return ByteBuffer.wrap(byteArray).getInt();
    }

    public static byte[] intToByteArray(final int number){
        int byteNum = (40 - Integer.numberOfLeadingZeros (number < 0 ? ~number : number)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte) (number >>> (n * 8));

        return (byteArray);
    }

}

