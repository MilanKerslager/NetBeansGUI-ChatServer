// this class defines a listening server thread

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JTextArea;

public class ChatServerSocket implements Runnable {

    private JTextArea jtA;
    private ServerSocket serverSocket;
    private Socket newSocket;
    private int count = 0;
    private boolean running = true;

    public ChatServerSocket(int port, JTextArea t) {
        this.jtA = t;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            jtA.append("ERROR: unable to create socket at port " + port + "\n");
        }
        jtA.append("Server started at port " + port + "\n");
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            jtA.append("ERROR: problem with closing server socket.\n");
        }
        jtA.append("Shutting down the server socket.\n");
    }

    @Override
    public void run() {
        jtA.append("Server thread started.\n");
        try {
            while (running) {
                // wait for new connection
                newSocket = serverSocket.accept();
                // we need separate thread to cummunicate with the client
                Thread thread = new Thread(new ChatServerThread(
                        newSocket, ++count, jtA));
                thread.start();
            }
            serverSocket.close();
        } catch (SocketException ex) {
            jtA.append("Server socket closed.\n");
        } catch (IOException ex) {
            jtA.append("ERROR: problem with server socket.\n");
        }
        jtA.append("Server thread ended. Bye.\n");
    }
}