package pl.coderslab.warsztaty2.models;

import org.apache.commons.lang3.ArrayUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String email;
    private String username;
    private String password;

    public User(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.setPassword(password);
    }

    // konstruktor bezargumentowy
    public User() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // TODO: HASH password before setting
    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static final String insertQuerry = "INSERT INTO users(username, email, password) VALUES (?, ?, ?);";
    public static final String modifyQuerry = "UPDATE users SET username=?, email=?, password=? where id = ?;";

    public void seveToDb(Connection conn) throws SQLException {
        if (this.id == 0) {
            String[] generatedColumns = {"ID"};
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuerry, generatedColumns);
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            while (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
        } else {
            PreparedStatement preparedStatement = conn.prepareStatement(modifyQuerry);
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.id);
            preparedStatement.executeUpdate();


        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    public static final String getUserQuery = "Select * FROM users Where id= ?;";

    public static User getUser(Connection conn, int id) throws SQLException {
        PreparedStatement preparedstatement = conn.prepareStatement(getUserQuery);
        preparedstatement.setInt(1, id);
        ResultSet resultSet = preparedstatement.executeQuery();

        while (resultSet.next()) {
            User userfromdb = new User();
            userfromdb.id = resultSet.getInt("id");
            userfromdb.username = resultSet.getString("username");
            userfromdb.email = resultSet.getString("email");
            userfromdb.password = resultSet.getString("password");
            return userfromdb;
        }


        return null;
    }

    public static final String getAllUsersQuery = "Select * FROM users;";

    public static User[] getAllUsers(Connection conn) throws SQLException {

        User[] users = new User[0];

        PreparedStatement preparedStatement = conn.prepareStatement(getAllUsersQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User userfromdb = new User();
            userfromdb.id = resultSet.getInt("id");
            userfromdb.username = resultSet.getString("username");
            userfromdb.email = resultSet.getString("email");
            userfromdb.password = resultSet.getString("password");
            users = ArrayUtils.add(users, userfromdb);
        }

        return users;
    }

    public static final String deleteUserByIdQuery = "DELETE FROM users WHERE id = ?;";

    public void deleteUser(Connection conn) throws SQLException {
        if (this.id != 0) {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteUserByIdQuery);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0; // dlaczego?
        }
    }
}
