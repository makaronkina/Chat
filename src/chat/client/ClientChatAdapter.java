package chat.client;

import chat.gui.ChatFrame;

import java.util.function.Consumer;

public class ClientChatAdapter {
    private Client client;
    private ChatFrame chatFrame;

    public ClientChatAdapter(String host, int port) {
        client = new Client(host, port);
        chatFrame = new ChatFrame(new Consumer<String>() {
            @Override
            public void accept(String message) {
                client.sendMessage(message);
            }
        });
        read();
    }

    private void read() {
        new Thread(() -> {
            try {
                while (true) {
                chatFrame.append(client.receiveMessage());
                }
            } catch (ClientConnectionException e) {
                throw e;
            } finally {
                if(client != null) {
                    chatFrame.close();
                    client.close();
                }
            }
        }).start();
    }
}
