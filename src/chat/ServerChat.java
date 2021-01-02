package chat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ServerChat implements Chat {
    private ServerSocket server;
    private Set<ClientHandler> clients;
    private AuthenticationService authenticationService;

    public ServerChat() {
        start();
    }

    public void start() {
        try {
            server = new ServerSocket(8888);
            clients = new HashSet<>();
            authenticationService = new AuthenticationService();

            while (true) {
                System.out.println("Server is waiting for a connection...");
                Socket socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                System.out.printf("%s: client %s is successfully logged in\n", new Date(), clientHandler.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void broadcastMessage(String sender, String message) {
        for (ClientHandler client : clients) {
            if (message.startsWith("-pm")) {
                String[] msg = message.split("\\s",3);
                if (msg[1].equals(client.getName())) {
                    client.sendMessage(String.format("[%s]: %s", sender, msg[2]));
                    doHistory((sender + ": " + message), client.getName());
                    break;
                }
            } else {
                client.sendMessage(String.format("[%s]: %s", sender, message));
            }
            doHistory((sender + ": " + message), client.getName());
        }
    }

    public void doHistory(String message, String name) {

        File file = new File(name  + "-history.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }

    }

    @Override
    public synchronized boolean isNicknameOccupied(String nickName) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void subscribe(ClientHandler client) {
        broadcastMessage(client.getName(), "is logged in");
        clients.add(client);
    }

    @Override
    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
        broadcastMessage(client.getName(), "is logged out");
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
