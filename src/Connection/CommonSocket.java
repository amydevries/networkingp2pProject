package Connection;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

public class CommonSocket {
    private Socket theSocket;
    private InputStream input;