/**
 * Created by DJ on 1/26/2018.
 */
import java.net.*;

import java.io.*;

import java.util.*;



/*

 * The Client that can be run both as a console or a GUI

 */

public class Client  {
009

        010
        // for I/O
        011
    private ObjectInputStream sInput;       // to read from the socket
012
    private ObjectOutputStream sOutput;     // to write on the socket
013
    private Socket socket;
014

        015
        // if I use a GUI or not
        016
    private ClientGUI cg;
017

        018
        // the server, the port and the username
        019
    private String server, username;
020
    private int port;
021

        022
    /*
023
     *  Constructor called by console mode
024
     *  server: the server address
025
     *  port: the port number
026
     *  username: the username
027
     */
        028
    Client(String server, int port, String username) {
        029
        // which calls the common constructor with the GUI set to null
        030
        this(server, port, username, null);
        031
    }
032

        033
    /*
034
     * Constructor call when used from a GUI
035
     * in console mode the ClienGUI parameter is null
036
     */
        037
    Client(String server, int port, String username, ClientGUI cg) {
        038
        this.server = server;
        039
        this.port = port;
        040
        this.username = username;
        041
        // save if we are in GUI mode or not
        042
        this.cg = cg;
        043
    }
044

        045
    /*
046
     * To start the dialog
047
     */
        048
    public boolean start() {
        049
        // try to connect to the server
        050
        try {
            051
            socket = new Socket(server, port);
            052
        }
        053
        // if it failed not much I can so
        054
        catch(Exception ec) {
            055
            display("Error connectiong to server:" + ec);
            056
            return false;
            057
        }
        058

        059
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        060
        display(msg);
        061

        062
        /* Creating both Data Stream */
        063
        try
        064
        {
            065
            sInput  = new ObjectInputStream(socket.getInputStream());
            066
            sOutput = new ObjectOutputStream(socket.getOutputStream());
            067
        }
        068
        catch (IOException eIO) {
            069
            display("Exception creating new Input/output Streams: " + eIO);
            070
            return false;
            071
        }
        072

        073
        // creates the Thread to listen from the server
        074
        new ListenFromServer().start();
        075
        // Send our username to the server this is the only message that we
        076
        // will send as a String. All other messages will be ChatMessage objects
        077
        try
        078
        {
            079
            sOutput.writeObject(username);
            080
        }
        081
        catch (IOException eIO) {
            082
            display("Exception doing login : " + eIO);
            083
            disconnect();
            084
            return false;
            085
        }
        086
        // success we inform the caller that it worked
        087
        return true;
        088
    }
089

        090
    /*
091
     * To send a message to the console or the GUI
092
     */
        093
    private void display(String msg) {
        094
        if(cg == null)
            095
        System.out.println(msg);      // println in console mode
        096
        else
        097
        cg.append(msg + "\n");      // append to the ClientGUI JTextArea (or whatever)
        098
    }
099

        100
    /*
101
     * To send a message to the server
102
     */
        103
    void sendMessage(ChatMessage msg) {
        104
        try {
            105
            sOutput.writeObject(msg);
            106
        }
        107
        catch(IOException e) {
            108
            display("Exception writing to server: " + e);
            109
        }
        110
    }
111

        112
    /*
113
     * When something goes wrong
114
     * Close the Input/Output streams and disconnect not much to do in the catch clause
115
     */
        116
    private void disconnect() {
        117
        try {
            118
            if(sInput != null) sInput.close();
            119
        }
        120
        catch(Exception e) {} // not much else I can do
        121
        try {
            122
            if(sOutput != null) sOutput.close();
            123
        }
        124
        catch(Exception e) {} // not much else I can do
        125
        try{
            126
            if(socket != null) socket.close();
            127
        }
        128
        catch(Exception e) {} // not much else I can do
        129

        130
        // inform the GUI
        131
        if(cg != null)
            132
        cg.connectionFailed();
        133

        134
    }
135
    /*
136
     * To start the Client in console mode use one of the following command
137
     * > java Client
138
     * > java Client username
139
     * > java Client username portNumber
140
     * > java Client username portNumber serverAddress
141
     * at the console prompt
142
     * If the portNumber is not specified 1500 is used
143
     * If the serverAddress is not specified "localHost" is used
144
     * If the username is not specified "Anonymous" is used
145
     * > java Client
146
     * is equivalent to
147
     * > java Client Anonymous 1500 localhost
148
     * are eqquivalent
149
     *
150
     * In console mode, if an error occurs the program simply stops
151
     * when a GUI id used, the GUI is informed of the disconnection
152
     */
        153
    public static void main(String[] args) {
        154
        // default values
        155
        int portNumber = 1500;
        156
        String serverAddress = "localhost";
        157
        String userName = "Anonymous";
        158

        159
        // depending of the number of arguments provided we fall through
        160
        switch(args.length) {
            161
            // > javac Client username portNumber serverAddr
            162
            case 3:
                163
                serverAddress = args[2];
                164
                // > javac Client username portNumber
                165
            case 2:
                166
                try {
                    167
                    portNumber = Integer.parseInt(args[1]);
                    168
                }
                169
                catch(Exception e) {
                170
                System.out.println("Invalid port number.");
                171
                System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
                172
                return;
                173
            }
            174
            // > javac Client username
            175
            case 1:
                176
                userName = args[0];
                177
                // > java Client
                178
            case 0:
                179
                break;
            180
            // invalid number of arguments
            181
            default:
                182
                System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
                183
                return;
            184
        }
        185
        // create the Client object
        186
        Client client = new Client(serverAddress, portNumber, userName);
        187
        // test if we can start the connection to the Server
        188
        // if it failed nothing we can do
        189
        if(!client.start())
            190
        return;
        191

        192
        // wait for messages from user
        193
        Scanner scan = new Scanner(System.in);
        194
        // loop forever for message from the user
        195
        while(true) {
            196
            System.out.print("> ");
            197
            // read message from user
            198
            String msg = scan.nextLine();
            199
            // logout if message is LOGOUT
            200
            if(msg.equalsIgnoreCase("LOGOUT")) {
                201
                client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                202
                // break to do the disconnect
                203
                break;
                204
            }
            205
            // message WhoIsIn
            206
            else if(msg.equalsIgnoreCase("WHOISIN")) {
                207
                client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
                208
            }
            209
            else {              // default to ordinary message
                210
                client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
                211
            }
            212
        }
        213
        // done disconnect
        214
        client.disconnect();
        215
    }
216

        217
    /*
218
     * a class that waits for the message from the server and append them to the JTextArea
219
     * if we have a GUI or simply System.out.println() it in console mode
220
     */
        221
    class ListenFromServer extends Thread {
222

        223
        public void run() {
            224
            while(true) {
                225
                try {
                    226
                    String msg = (String) sInput.readObject();
                    227
                    // if console mode print the message and add back the prompt
                    228
                    if(cg == null) {
                        229
                        System.out.println(msg);
                        230
                        System.out.print("> ");
                        231
                    }
                    232
                    else {
                        233
                        cg.append(msg);
                        234
                    }
                    235
                }
                236
                catch(IOException e) {
                    237
                    display("Server has close the connection: " + e);
                    238
                    if(cg != null)
                        239
                    cg.connectionFailed();
                    240
                    break;
                    241
                }
                242
                // can't happen with a String object but need the catch anyhow
                243
                catch(ClassNotFoundException e2) {
                    244
                }
                245
            }
            246
        }
247
    }
248
}
