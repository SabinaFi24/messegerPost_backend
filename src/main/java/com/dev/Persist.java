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
                    "SELECT token FROM users WHERE username = ? AND password = ?");
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
    public String getTokenByUsername(String username) {
        String token = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token FROM users WHERE username = ? ");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                token = resultSet.getString("token");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token;
    }
    public String getUsernameByToken (String token) {
        String username = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement
                    ("SELECT username FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;

    }
    public Integer getUserIdByToken (String token) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement
                    ("SELECT id FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;

    }

    public boolean addAccount (UserObject userObject) {
        boolean success = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement
                    ("INSERT INTO users (username, password, token) VALUE (?, ?, ?)");
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

    public boolean addMessage (String token,String receiverUsername, String title, String content ) {
        boolean success = false;
        try {
            //String username = getUsernameByToken(token);
            String receiverToken = getTokenByUsername(receiverUsername);
            int userId = getUserIdByToken(token);

            if (receiverToken != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages ( title ,content ,send_date, id_sender, id_receiver,reading_date)" +
                                " VALUE (?,?, NOW(), ?,?,?)");
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, userId);
                preparedStatement.setString(4, receiverUsername);
                preparedStatement.setBoolean(5,false);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }



    public List<MessageObject> getMessagesByUser (String token) {
        List<MessageObject> messageObjects = new ArrayList<>();
        try {
            String username = getUsernameByToken(token);
            if (username != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("SELECT (title,content,id_sender,reading_date)" +
                                " FROM messages WHERE id_receiver = ? ORDER BY send_date DESC");
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MessageObject messageObject = new MessageObject();

                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    int sender = resultSet.getInt("id_sender");
                    boolean read = resultSet.getBoolean("reading_date");

                    messageObject.setMessageId(resultSet.getInt("id"));

                    messageObject.setTitle(title);
                    messageObject.setContent(content);
                    messageObject.setSenderId(sender);
                    messageObject.setIsRead(read);

                    messageObjects.add(messageObject);

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
            String username = getUsernameByToken(token);
            if (username != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("DELETE FROM messages WHERE message_id = ?  ");
                preparedStatement.setInt(1, MessageId);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }


    public boolean readMessage(String token, int messageId) {
        boolean read = false;
        try {
            String username = getUsernameByToken(token);
            if (username != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement
                        ("INSERT INTO messages ( reading_date)  VALUE ?");
                preparedStatement.setBoolean(1,true);
                preparedStatement.executeUpdate();
                read = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return read;

    }
}
