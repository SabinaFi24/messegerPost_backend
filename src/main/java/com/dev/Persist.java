package com.dev;

import com.dev.objects.MessageObject;
import com.dev.objects.UserObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class Persist {
    private Connection connection;

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project1", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM project_users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                token = resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }

    public boolean addAccount (UserObject userObject) {
        boolean success = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement
                    ("INSERT INTO project_users (username, password, token, is_blocked) VALUE (?, ?, ?,1)");
            preparedStatement.setString(1, userObject.getUsername());
            preparedStatement.setString(2, userObject.getPassword());
            preparedStatement.setString(3, userObject.getToken());
            //preparedStatement.setBoolean(4, userObject.getIsBlocked());
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;

    }

    public boolean addMessage (String token, int senderUsername,int receiverUsername, String title, String content ) {
        boolean success = false;
        try {
            Integer username = getUsernameByToken(token);
            if (username != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages ( title ,content ,send_date, id_sender, id_receiver)" +
                                " VALUE (?,?, NOW(), ?,?)");
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, username);
                preparedStatement.setInt(4, receiverUsername);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public Integer getUsernameByToken (String token) {
        Integer username = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement
                    ("SELECT username FROM project_users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getInt("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;

    }

    public List<MessageObject> getMessagesByUser (String token) {
        List<MessageObject> messageObjects = new ArrayList<>();
        try {
            Integer username = getUsernameByToken(token);
            if (username != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("SELECT * FROM messages WHERE id_receiver = ? ORDER BY send_date DESC");
                preparedStatement.setInt(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MessageObject messageObject = new MessageObject();
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String date = resultSet.getString("send_date");

                   // messageObject.set(resultSet.getInt("id"));

                    messageObject.setTitle(title);
                    messageObject.setContent(content);
                   // messageObject.set
                   // messageObject.add(messageObject);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageObjects;
    }

    public boolean removeMessage (String token, int MessageId) {
        boolean success = false;
        try {
            Integer userId = getUsernameByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("DELETE FROM messages WHERE message_id = ? AND sender_id = ? ");
                preparedStatement.setInt(1, MessageId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }




}
