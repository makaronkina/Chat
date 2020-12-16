package chat;

import java.util.Set;

public class AuthenticationService {
    private Set<CredentialsEntry> entries;

    public AuthenticationService() {
        entries = Set.of(
                new CredentialsEntry("Nick1", "l1", "p1"),
                new CredentialsEntry("Nick2", "l2", "p2"),
                new CredentialsEntry("Nick3", "l3", "p3")
        );
    }

    public String findNicknameByLoginAndPassword(String login, String password) {
        for (CredentialsEntry entry : entries) {
            if (entry.getLogin().equals(login) && entry.getPassword().equals(password)) {
                return entry.getNickname();
            }
        }
        return null;
    }

    public static class CredentialsEntry {
        private String nickname;
        private String login;
        private String password;

        public CredentialsEntry(String nickname, String login, String password) {
            this.nickname = nickname;
            this.login = login;
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}
