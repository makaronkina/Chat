package chat.aplication;

import chat.client.ClientChatAdapter;

public class ClientApp {
    public static void main(String[] args) {
        new ClientChatAdapter("localhost", 8888);
    }
}
