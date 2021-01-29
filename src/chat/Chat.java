package chat;

public interface Chat {
    void broadcastMessage(String message, String sender);
    boolean isNicknameOccupied(String nickName);
    void subscribe(ClientHandler client);
    void unsubscribe(ClientHandler client);
    AuthenticationService getAuthenticationService();
}
