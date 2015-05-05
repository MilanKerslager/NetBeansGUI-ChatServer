// thread implementation for maintaining connection with a client

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JTextArea;

class ChatServerThread implements Runnable {
    private final Socket socket;
    private final JTextArea jtA;
    private final int id;
    
    public ChatServerThread(Socket s, int id, JTextArea t) {
        this.jtA = t;
        this.socket = s;
        this.id = id;
        jtA.append("Client thread "+id+" created.\n");
    }

    @Override
    public void run() {
        jtA.append("Client accepted by thread "+id+".\n");
        try {
            BufferedReader streamIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = streamIn.readLine();
                jtA.append("thread "+id+": "+line+"\n");
                if (line.equals("QUIT")) break;
            }
            jtA.append("thread "+id+" asked to quit.\n");
            socket.close();
            streamIn.close();
        } catch (SocketException ex) {
            jtA.append("Thread socket closed.\n");
        } catch (IOException ex) {
            jtA.append("ERROR: problem with accepting the client.\n");
        }
        jtA.append("thread "+id+" ended, because client QUITs.\n");
    }
}
