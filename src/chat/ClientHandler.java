package chat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
                            sendHistory(name);
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
                } else {
                    chat.broadcastMessage(name, message);
                }
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

    public void sendHistory(String name) {
        File file = new File(name  + "-history.txt");
        if (file.exists()) {
            int limit = 3;
            List<String> list = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String message;
                while ((message = reader.readLine()) != null) {
                    list.add(message);
                }
                if (list.size() > limit) {
                    for (int i = list.size() - limit; i < list.size(); i++) {
                        sendMessage(list.get(i));
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        sendMessage(list.get(i));
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }

//    public void sendHistory(String name) {
//        File file = new File(name  + "-history.txt");
//        int lineCount = 0;
//        int lineLimit = 25;
//        StringBuilder builder = new StringBuilder();
//        try (RandomAccessFile random = new RandomAccessFile(file, "r")) {
//            long fileLenght = file.length() - 1;
//            random.seek(fileLenght);
//            for (long pointer = fileLenght; pointer >= 0; pointer --) {
//                random.seek(pointer);
//                String line;
//                line = random.readUTF();
//                if(line == "\n") {
//                    lineCount++;
//                    if(lineCount == lineLimit) {
//                        break;
//                    }
//                }
//                builder.append(line);
//            }
//            builder.reverse();
//            sendMessage(builder.toString());
//        } catch (IOException e) {
//            throw new RuntimeException("SWW", e);
//        }
//    }
}
