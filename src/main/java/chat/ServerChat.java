package chat;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerChat {
    private ServerSocket server;
    private Set<ClientHandler> clients;
    private AuthenticationService authenticationService;
    private static final Logger LOGGER = Logger.getLogger(ServerChat.class);

    public ServerChat() {
        start();
    }

    public void start() {
        try {
            server = new ServerSocket(8888);
            clients = new HashSet<>();
            authenticationService = new AuthenticationService();

            while (true) {
                LOGGER.info("Server is waiting for a connection...");
                Socket socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                LOGGER.info("Client: " + clientHandler.getName() + "  is successfully connected");
            }
        } catch (IOException e) {
            LOGGER.error("Something went wrong during connection!", e);
            e.printStackTrace();
        }
    }

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
            LOGGER.info("Something went wrong during do history!", e);
            throw new RuntimeException("SWW", e);
        }
    }

    public synchronized boolean isNicknameOccupied(String nickName) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickName)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void subscribe(ClientHandler client) {
        broadcastMessage(client.getName(), "is logged in");
        clients.add(client);
        LOGGER.info(client.getName() + " is logged in");
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
        broadcastMessage(client.getName(), "is logged out");
        LOGGER.info(client.getName() + " is logged out");
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

}
