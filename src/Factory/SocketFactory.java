package Factory;

import Sockets.BasicSocket;
import Sockets.ISocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketFactory {

    private static SocketFactory socketFactory = new SocketFactory();

    public static SocketFactory getSocketFactory() { return socketFactory; }

    public ISocket makeSocket(String host, int port) throws IOException {
        return new BasicSocket(host, port);
    }

    public ISocket makeSocket(Socket socket){
        return new BasicSocket(socket);
    }

    public ServerSocket makeServerSocket(int port){
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
