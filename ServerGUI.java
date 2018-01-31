/**
 * Created by DJ on 1/26/2018.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
006
 * The server as a GUI
007
 */
        008
public class ServerGUI extends JFrame implements ActionListener, WindowListener {
009

        010
    private static final long serialVersionUID = 1L;
011
        // the stop and start buttons
        012
    private JButton stopStart;
013
        // JTextArea for the chat room and the events
        014
    private JTextArea chat, event;
015
        // The port number
        016
    private JTextField tPortNumber;
017
        // my server
        018
    private Server server;
019

        020

        021
        // server constructor that receive the port to listen to for connection as parameter
        022
    ServerGUI(int port) {
        023
        super("Chat Server");
        024
        server = null;
        025
        // in the NorthPanel the PortNumber the Start and Stop buttons
        026
        JPanel north = new JPanel();
        027
        north.add(new JLabel("Port number: "));
        028
        tPortNumber = new JTextField("  " + port);
        029
        north.add(tPortNumber);
        030
        // to stop or start the server, we start with "Start"
        031
        stopStart = new JButton("Start");
        032
        stopStart.addActionListener(this);
        033
        north.add(stopStart);
        034
        add(north, BorderLayout.NORTH);
        035

        036
        // the event and chat room
        037
        JPanel center = new JPanel(new GridLayout(2,1));
        038
        chat = new JTextArea(80,80);
        039
        chat.setEditable(false);
        040
        appendRoom("Chat room.\n");
        041
        center.add(new JScrollPane(chat));
        042
        event = new JTextArea(80,80);
        043
        event.setEditable(false);
        044
        appendEvent("Events log.\n");
        045
        center.add(new JScrollPane(event));
        046
        add(center);
        047

        048
        // need to be informed when the user click the close button on the frame
        049
        addWindowListener(this);
        050
        setSize(400, 600);
        051
        setVisible(true);
        052
    }
053

        054
        // append message to the two JTextArea
        055
        // position at the end
        056
    void appendRoom(String str) {
        057
        chat.append(str);
        058
        chat.setCaretPosition(chat.getText().length() - 1);
        059
    }
060
    void appendEvent(String str) {
        061
        event.append(str);
        062
        event.setCaretPosition(chat.getText().length() - 1);
        063

        064
    }
065

        066
        // start or stop where clicked
        067
    public void actionPerformed(ActionEvent e) {
        068
        // if running we have to stop
        069
        if(server != null) {
            070
            server.stop();
            071
            server = null;
            072
            tPortNumber.setEditable(true);
            073
            stopStart.setText("Start");
            074
            return;
            075
        }
        076
        // OK start the server
        077
        int port;
        078
        try {
            079
            port = Integer.parseInt(tPortNumber.getText().trim());
            080
        }
        081
        catch(Exception er) {
            082
            appendEvent("Invalid port number");
            083
            return;
            084
        }
        085
        // ceate a new Server
        086
        server = new Server(port, this);
        087
        // and start it as a thread
        088
        new ServerRunning().start();
        089
        stopStart.setText("Stop");
        090
        tPortNumber.setEditable(false);
        091
    }
092

        093
        // entry point to start the Server
        094
    public static void main(String[] arg) {
        095
        // start server default port 1500
        096
        new ServerGUI(1500);
        097
    }
098

        099
    /*
100
     * If the user click the X button to close the application
101
     * I need to close the connection with the server to free the port
102
     */
        103
    public void windowClosing(WindowEvent e) {
        104
        // if my Server exist
        105
        if(server != null) {
            106
            try {
                107
                server.stop();          // ask the server to close the conection
                108
            }
            109
            catch(Exception eClose) {
                110
            }
            111
            server = null;
            112
        }
        113
        // dispose the frame
        114
        dispose();
        115
        System.exit(0);
        116
    }
117
        // I can ignore the other WindowListener method
        118
    public void windowClosed(WindowEvent e) {}
119
    public void windowOpened(WindowEvent e) {}
120
    public void windowIconified(WindowEvent e) {}
121
    public void windowDeiconified(WindowEvent e) {}
122
    public void windowActivated(WindowEvent e) {}
123
    public void windowDeactivated(WindowEvent e) {}
124

        125
    /*
126
     * A thread to run the Server
127
     */
        128
    class ServerRunning extends Thread {
129
        public void run() {
            130
            server.start();         // should execute until if fails
            131
            // the server failed
            132
            stopStart.setText("Start");
            133
            tPortNumber.setEditable(true);
            134
            appendEvent("Server crashed\n");
            135
            server = null;
            136
        }
137
    }
138

        139
}
