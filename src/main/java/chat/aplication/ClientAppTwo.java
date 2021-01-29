package chat.aplication;

import chat.client.ClientChatAdapter;

public class ClientAppTwo {
    public static void main(String[] args) {
        new ClientChatAdapter("localhost", 8888);
    }
}
