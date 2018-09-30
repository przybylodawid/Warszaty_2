package pl.coderslab.warsztaty2.models;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Solution {
    private int id;
    private String description;
    // TODO: Zmienić te pola na DATOWE
    private String created;
    private String updated;
    private int user_id;
    private int exercise_id;


    // ------------ KONSTRUKTORY ---------------------

    // konstruktor bezargumentowy, gdybym chciał stworzyć obiekt, a potem do niego przypisywać
    public Solution() {
    }

    public Solution (String description, String created, String updated, int user_id, int exercise_id) {
                this.description = description;
                this.created = created;
                this.updated = updated;
                this.user_id = user_id;
                this.exercise_id = exercise_id;
    }



    // ------------ SETTERY i GETTERY*  --------------------- * celowo nie ma setId - id przypisuje nam baza

    public int getId() {
        return id;
    }

        public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public void setDescription(String description) {
        this.description = description;


    }
    // ------------ METODY POMOCNICZE ---------------------

    public String toString(){
        return "Solution { id: "+ id + ", description: "+description + " created: " +created+ " updated: "+ updated +" userid: "+user_id+" exerciseid: "+ exercise_id + "}";
    }



    // ------------ METODY DO PRACY Z BAZĄ ---------------------

    // ----- 1. Dodanie / modyfikacja rekordu ------

    public static final String insertQuery = "INSERT INTO solution (description, created, updated, user_id, exercise_id) VALUES (?, ?, ?, ?, ?);";
    public static final String modifyQuery = "UPDATE solution SET description = ?, created = ?, updated = ?, user_id = ?, exercise_id = ? WHERE id = ?;";
    public void saveToDb(Connection conn) throws SQLException {
        // sprawdza, czy ID = 0, jeśli tak, to obiekt nie został jeszcze zsynchronizowany z bazą danych
        // jeśli nie, to znaczy, że obiekt jest już w bazie i robi jego update.
        if (this.id ==0) {
            String[] generatedColumns = { "ID" };
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, generatedColumns);
            preparedStatement.setString(1, this.description);
            preparedStatement.setString(2, this.created);
            preparedStatement.setString(3, this.updated);
            preparedStatement.setInt(4, this.user_id);
            preparedStatement.setInt(5, this.exercise_id);
            preparedStatement.executeUpdate();
            // TODO: zrozumieć o co tu chodzi...
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            while (generatedKeys.next()){
                this.id = generatedKeys.getInt(1);
            }

        }else {
            PreparedStatement preparedStatement = conn.prepareStatement(modifyQuery);
            preparedStatement.setString(1, this.description);
            preparedStatement.setString(2, this.created);
            preparedStatement.setString(3, this.updated);
            preparedStatement.setInt(4, this.user_id);
            preparedStatement.setInt(5, this.exercise_id);
            preparedStatement.setInt(6, this.id);
            preparedStatement.executeUpdate();
        }

    }

    // ----- 2. Pobranie rekordu ------

    public static final String getSolutionByIdQuery = "Select * from solution WHERE id = ?;";
    public static Solution getSolutionById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(getSolutionByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Solution solutionFromDb = new Solution();
            solutionFromDb.id = resultSet.getInt("id");
            solutionFromDb.description = resultSet.getString("description");
            solutionFromDb.created = resultSet.getString("created");
            solutionFromDb.updated = resultSet.getString("updated");
            solutionFromDb.exercise_id = resultSet.getInt("exercise_id");
            solutionFromDb.user_id = resultSet.getInt("user_id");
            return solutionFromDb;
        }
        System.out.println("NIE MA REKORDU O PODANYM ID");
        return null;


    }

    // ----- 3. Pobranie wszystkich rekordów  ------

    public static final String getAllSolutionsQuery = "SELECT * FROM solution;";
    public static Solution[] getAllSolutions(Connection conn) throws SQLException {
        Solution[] Solutions = new Solution[0];
        PreparedStatement preparedStatement = conn.prepareStatement(getAllSolutionsQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            // to samo co przedtem, ale wywołane konstruktorem dla praktyki
            Solution solutionFromDb = new Solution();
            solutionFromDb.id = resultSet.getInt("id");
            solutionFromDb.description = resultSet.getString("description");
            solutionFromDb.created = resultSet.getString("created");
            solutionFromDb.updated = resultSet.getString("updated");
            solutionFromDb.exercise_id = resultSet.getInt("exercise_id");
            solutionFromDb.user_id = resultSet.getInt("user_id");
            Solutions = ArrayUtils.add(Solutions, solutionFromDb);

        }
        return Solutions;
    }

    // ----- 4. Usunięcie rekordu  ------

    public static final String deleteSolutionByIdQuery ="DELETE FROM solution WHERE id = ?;";
    public void deleteSolutionById(Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(deleteSolutionByIdQuery);
        preparedStatement.setInt(1, this.id);
        preparedStatement.executeUpdate();
        this.id = 0; // << gdybyśmy chcieli zmodyfikować obiekt i zapisać go ponownie w bazie danych
    }
}
