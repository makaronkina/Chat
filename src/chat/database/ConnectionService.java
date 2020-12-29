package chat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionService {
    public ConnectionService() {}

    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/Chat", "root", "12345678");
        } catch (SQLException throwables) {
            throw new RuntimeException("SWW", throwables);
        }
    }

    public static void rollback(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void close(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
