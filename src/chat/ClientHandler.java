package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {
    private String name;
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private Chat chat;
    private Timer timer;

    public ClientHandler(Socket client, Chat chat) {
        this.client = client;
        this.chat = chat;
        this.timer = new Timer();

        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
        listen();
    }

    public void listen() {
        new Thread(() -> {
            closeOnTimer();
        }).start();
        new Thread(() -> {
            doAuth();
            timer.cancel();
            receiveMessage();
        }).start();
    }

    public void doAuth() {
        sendMessage("Please enter credentials. Sample: [-auth login password]");
        try {
            while (true) {
                String maybeCred = in.readUTF();
                if (maybeCred.startsWith("-auth")) {
                    String[] credentials = maybeCred.split("\\s");
                    String maybeNickname = chat.getAuthenticationService()
                            .findNicknameByLoginAndPassword(credentials[1], credentials[2]);
                    if (maybeNickname != null) {
                        if (!chat.isNicknameOccupied(maybeNickname)) {
                            sendMessage("[INFO] Auth OK");
                            name = maybeNickname;
                            chat.subscribe(this);
                            return;
                        } else {
                            sendMessage("[INFO] Sorry, User is already logged in");
                        }
                    } else {
                        sendMessage("[INFO] Wrong login or password");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                String message = in.readUTF();

                if (message.startsWith("-exit")) {
                    chat.unsubscribe(this);
                    break;
                }
                if(message.startsWith("-change")) {
                    String[] msg = message.split("\\s");
                    name = chat.getAuthenticationService().changeNickname(msg[1], msg[2]);
                    sendMessage("Nickname is changed");
                } else chat.broadcastMessage(name, message);
            } catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }

    public void closeOnTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 120000);
    }
}
