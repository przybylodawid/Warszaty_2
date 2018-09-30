package pl.coderslab.warsztaty2.models;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
    private int id;
    private String name;


    // ------------ KONSTRUKTORY ---------------------

    // konstruktor bezargumentowy, gdybym chciał stworzyć obiekt, a potem do niego przypisywać
    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    // ------------ SETTERY i GETTERY*  --------------------- * celowo nie ma setId - id przypisuje nam baza

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ------------ METODY POMOCNICZE ---------------------

    public String toString(){
        return "Group { id: "+id+ ", name: " + name + "}";
    }



    // ------------ METODY DO PRACY Z BAZĄ ---------------------

    // ----- 1. Dodanie / modyfikacja rekordu ------

    public static final String insertQuery = "INSERT INTO user_group (name) VALUES (?);";
    public static final String modifyQuery = "UPDATE user_group SET name = ? WHERE id = ?;";
    public void saveToDb(Connection conn) throws SQLException {
        // sprawdza, czy ID = 0, jeśli tak, to obiekt nie został jeszcze zsynchronizowany z bazą danych
        // jeśli nie, to znaczy, że obiekt jest już w bazie i robi jego update.
        if (this.id ==0) {
            String[] generatedColumns = { "ID" };
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, generatedColumns);
            preparedStatement.setString(1, this.name);
            preparedStatement.executeUpdate();
            // TODO: zrozumieć o co tu chodzi...
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            while (generatedKeys.next()){
                this.id = generatedKeys.getInt(1);
            }

        }else {
            PreparedStatement preparedStatement = conn.prepareStatement(modifyQuery);
            preparedStatement.setString(1,this.name);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }

    }

    // ----- 2. Pobranie rekordu ------

    public static final String getGroupByIdQuery = "Select * from user_group WHERE id = ?;";
    public static Group getGroupById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(getGroupByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Group groupFromDb = new Group();
            groupFromDb.id = resultSet.getInt("id");
            groupFromDb.name = resultSet.getString("name");

            return  groupFromDb;
        }
        System.out.println("NIE MA REKORDU O PODANYM ID");
        return null;


    }

    // ----- 3. Pobranie wszystkich rekordów  ------

    public static final String getAllGroupsQuery = "SELECT * FROM user_group;";
    public static Group[] getAllGroups(Connection conn) throws SQLException {
        Group[] groups = new Group[0];
        PreparedStatement preparedStatement = conn.prepareStatement(getAllGroupsQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            // to samo co przedtem, ale wywołane konstruktorem dla praktyki
            Group groupFromDb = new Group(resultSet.getString("name"));
            groupFromDb.id = resultSet.getInt("id");
            groups = ArrayUtils.add(groups, groupFromDb);

        }
        return groups;
    }

    // ----- 4. Usunięcie rekordu  ------

    public static final String deleteGroupByIdQuery ="DELETE FROM user_group WHERE id = ?;";
    public void deleteGroupById(Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(deleteGroupByIdQuery);
        preparedStatement.setInt(1, this.id);
        preparedStatement.executeUpdate();
        this.id = 0; // << gdybyśmy chcieli zmodyfikować obiekt i zapisać go ponownie w bazie danych
    }
}
