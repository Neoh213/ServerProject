/**
 * Created by DJ on 1/26/2018.
 */
import javax.swing.*;

import java.awt.*;

import java.awt.event.*;





/*
007
 * The Client with its GUI
008
 */

public class ClientGUI extends JFrame implements ActionListener {



    private static final long serialVersionUID = 1L;

        // will first hold "Username:", later on "Enter message"

    private JLabel label;

        // to hold the Username and later on the messages

    private JTextField tf;

        // to hold the server address an the port number

    private JTextField tfServer, tfPort;

        // to Logout and get the list of the users

    private JButton login, logout, whoIsIn;

        // for the chat room
        021
    private JTextArea ta;
022
        // if it is for connection
        023
    private boolean connected;
024
        // the Client object
        025
    private Client client;
026
        // the default port number
        027
    private int defaultPort;
028
    private String defaultHost;
029

        030
        // Constructor connection receiving a socket number
        031
    ClientGUI(String host, int port) {
        032

        033
        super("Chat Client");
        034
        defaultPort = port;
        035
        defaultHost = host;
        036

        037
        // The NorthPanel with:
        038
        JPanel northPanel = new JPanel(new GridLayout(3,1));
        039
        // the server name anmd the port number
        040
        JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
        041
        // the two JTextField with default value for server address and port number
        042
        tfServer = new JTextField(host);
        043
        tfPort = new JTextField("" + port);
        044
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);
        045

        046
        serverAndPort.add(new JLabel("Server Address:  "));
        047
        serverAndPort.add(tfServer);
        048
        serverAndPort.add(new JLabel("Port Number:  "));
        049
        serverAndPort.add(tfPort);
        050
        serverAndPort.add(new JLabel(""));
        051
        // adds the Server an port field to the GUI
        052
        northPanel.add(serverAndPort);
        053

        054
        // the Label and the TextField
        055
        label = new JLabel("Enter your username below", SwingConstants.CENTER);
        056
        northPanel.add(label);
        057
        tf = new JTextField("Anonymous");
        058
        tf.setBackground(Color.WHITE);
        059
        northPanel.add(tf);
        060
        add(northPanel, BorderLayout.NORTH);
        061

        062
        // The CenterPanel which is the chat room
        063
        ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
        064
        JPanel centerPanel = new JPanel(new GridLayout(1,1));
        065
        centerPanel.add(new JScrollPane(ta));
        066
        ta.setEditable(false);
        067
        add(centerPanel, BorderLayout.CENTER);
        068

        069
        // the 3 buttons
        070
        login = new JButton("Login");
        071
        login.addActionListener(this);
        072
        logout = new JButton("Logout");
        073
        logout.addActionListener(this);
        074
        logout.setEnabled(false);       // you have to login before being able to logout
        075
        whoIsIn = new JButton("Who is in");
        076
        whoIsIn.addActionListener(this);
        077
        whoIsIn.setEnabled(false);      // you have to login before being able to Who is in
        078

        079
        JPanel southPanel = new JPanel();
        080
        southPanel.add(login);
        081
        southPanel.add(logout);
        082
        southPanel.add(whoIsIn);
        083
        add(southPanel, BorderLayout.SOUTH);
        084

        085
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        086
        setSize(600, 600);
        087
        setVisible(true);
        088
        tf.requestFocus();
        089

        090
    }
091

        092
        // called by the Client to append text in the TextArea
        093
    void append(String str) {
        094
        ta.append(str);
        095
        ta.setCaretPosition(ta.getText().length() - 1);
        096
    }
097
        // called by the GUI is the connection failed
        098
        // we reset our buttons, label, textfield
        099
    void connectionFailed() {
        100
        login.setEnabled(true);
        101
        logout.setEnabled(false);
        102
        whoIsIn.setEnabled(false);
        103
        label.setText("Enter your username below");
        104
        tf.setText("Anonymous");
        105
        // reset port number and host name as a construction time
        106
        tfPort.setText("" + defaultPort);
        107
        tfServer.setText(defaultHost);
        108
        // let the user change them
        109
        tfServer.setEditable(false);
        110
        tfPort.setEditable(false);
        111
        // don't react to a <CR> after the username
        112
        tf.removeActionListener(this);
        113
        connected = false;
        114
    }
115

        116
    /*
117
    * Button or JTextField clicked
118
    */
        119
    public void actionPerformed(ActionEvent e) {
        120
        Object o = e.getSource();
        121
        // if it is the Logout button
        122
        if(o == logout) {
            123
            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
            124
            return;
            125
        }
        126
        // if it the who is in button
        127
        if(o == whoIsIn) {
            128
            client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
            129
            return;
            130
        }
        131

        132
        // ok it is coming from the JTextField
        133
        if(connected) {
            134
            // just have to send the message
            135
            client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));
            136
            tf.setText("");
            137
            return;
            138
        }
        139

        140

        141
        if(o == login) {
            142
            // ok it is a connection request
            143
            String username = tf.getText().trim();
            144
            // empty username ignore it
            145
            if(username.length() == 0)
                146
            return;
            147
            // empty serverAddress ignore it
            148
            String server = tfServer.getText().trim();
            149
            if(server.length() == 0)
                150
            return;
            151
            // empty or invalid port numer, ignore it
            152
            String portNumber = tfPort.getText().trim();
            153
            if(portNumber.length() == 0)
                154
            return;
            155
            int port = 0;
            156
            try {
                157
                port = Integer.parseInt(portNumber);
                158
            }
            159
            catch(Exception en) {
                160
                return;   // nothing I can do if port number is not valid
                161
            }
            162

            163
            // try creating a new Client with GUI
            164
            client = new Client(server, port, username, this);
            165
            // test if we can start the Client
            166
            if(!client.start())
                167
            return;
            168
            tf.setText("");
            169
            label.setText("Enter your message below");
            170
            connected = true;
            171

            172
            // disable login button
            173
            login.setEnabled(false);
            174
            // enable the 2 buttons
            175
            logout.setEnabled(true);
            176
            whoIsIn.setEnabled(true);
            177
            // disable the Server and Port JTextField
            178
            tfServer.setEditable(false);
            179
            tfPort.setEditable(false);
            180
            // Action listener for when the user enter a message
            181
            tf.addActionListener(this);
            182
        }
        183

        184
    }
185

        186
        // to start the whole thing the server
        187
    public static void main(String[] args) {
        188
        new ClientGUI("localhost", 1500);
        189
    }
190

        191
}
