package chat;

import chat.database.ConnectionService;
import java.sql.*;
import java.util.Objects;


public class AuthenticationService {

    public String findNicknameByLoginAndPassword(String login, String password) {
        Objects.requireNonNull(login, "Login can't be null");
        Objects.requireNonNull(password, "Password can't be null");

        if (login.isBlank()) {
            throw new IllegalArgumentException("Login can't be empty or null");
        }
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password can't be empty or null");
        }

        Connection connection = ConnectionService.connect();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?");
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                if((rs.getString("login")).equals(login) && (rs.getString("password")).equals(password)) {
                    return rs.getString("nickname");
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        } finally {
            ConnectionService.close(connection);
        }
        return null;
    }

    public String changeNickname(String nickname,String login) {
        Objects.requireNonNull(nickname, "Nickname can't be null");
        Objects.requireNonNull(login, "Login can't be null");

        Connection connection = ConnectionService.connect();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET nickname = ? WHERE login = ?");
            statement.setString(1, nickname);
            statement.setString(2, login);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            ConnectionService.rollback(connection);
            throw new RuntimeException("SWW", throwables);
        } finally {
            ConnectionService.close(connection);
        }
        return nickname;
    }

    public static void create(String[] user) {
        Connection connection = ConnectionService.connect();
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (nickname, login, password) VALUES (?, ?, ?)");

            statement.setString(1, user[0]);
            statement.setString(2, user[1]);
            statement.setString(3, user[2]);

            statement.executeUpdate();

            connection.commit();
//            System.out.println("Check it!");

        } catch (SQLException throwables) {
            ConnectionService.rollback(connection);
            throw new RuntimeException("SWW", throwables);
        } finally {
            ConnectionService.close(connection);
        }
    }
}
