package Connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommonSocket {
    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public CommonSocket(String host, int port) {
        try {
            socket = new Socket(host, port);
            bufferedReader = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommonSocket(int port){
        try {
            serverSocket = new ServerSocket(port);
            bufferedReader = new BufferedReader(new
                    InputStreamReader(serverSocket.getInputStream()));
            printWriter = new PrintWriter(serverSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            socket.close();
            bufferedReader.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(){
        try {
            bufferedReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytes){
        try {
            printWriter.write();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}