package pl.coderslab.warsztaty2.models;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Exercise {
    private int id;
    private String title;
    private String description;


    // ------------ KONSTRUKTORY ---------------------

    // konstruktor bezargumentowy, gdybym chciał stworzyć obiekt, a potem do niego przypisywać
    public Exercise() {
    }

    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // ------------ SETTERY i GETTERY*  --------------------- * celowo nie ma setId - id przypisuje nam baza

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // ------------ METODY POMOCNICZE ---------------------

    public String toString(){
        return "Exercise { id: "+id+ ", title: " + title + ", description: "+description +"}";
    }



    // ------------ METODY DO PRACY Z BAZĄ ---------------------

    // ----- 1. Dodanie / modyfikacja rekordu ------

    public static final String insertQuery = "INSERT INTO exercise (title, description) VALUES (?, ?);";
    public static final String modifyQuery = "UPDATE exercise SET title = ?, description = ? WHERE id = ?;";
    public void saveToDb(Connection conn) throws SQLException {
        // sprawdza, czy ID = 0, jeśli tak, to obiekt nie został jeszcze zsynchronizowany z bazą danych
        // jeśli nie, to znaczy, że obiekt jest już w bazie i robi jego update.
        if (this.id ==0) {
            String[] generatedColumns = { "ID" };
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery, generatedColumns);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.description);
            preparedStatement.executeUpdate();
            // TODO: zrozumieć o co tu chodzi...
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            while (generatedKeys.next()){
                this.id = generatedKeys.getInt(1);
            }

        }else {
            PreparedStatement preparedStatement = conn.prepareStatement(modifyQuery);
            preparedStatement.setString(1,this.title);
            preparedStatement.setString(2,this.description);
            preparedStatement.setInt(3, this.id);
            preparedStatement.executeUpdate();
        }

    }

    // ----- 2. Pobranie rekordu ------

    public static final String getExerciseByIdQuery = "Select * from exercise WHERE id = ?;";
    public static Exercise getExerciseById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(getExerciseByIdQuery);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Exercise exerciseFromDb = new Exercise();
            exerciseFromDb.id = resultSet.getInt("id");
            exerciseFromDb.title = resultSet.getString("title");
            exerciseFromDb.description = resultSet.getString("description");
            return exerciseFromDb;
        }
        System.out.println("NIE MA REKORDU O PODANYM ID");
        return null;


    }

    // ----- 3. Pobranie wszystkich rekordów  ------

    public static final String getAllExercisesQuery = "SELECT * FROM exercise;";
    public static Exercise[] getAllExercises(Connection conn) throws SQLException {
        Exercise[] exercises = new Exercise[0];
        PreparedStatement preparedStatement = conn.prepareStatement(getAllExercisesQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
        // to samo co przedtem, ale wywołane konstruktorem dla praktyki
        Exercise exerciseFromDb = new Exercise(resultSet.getString("title"), resultSet.getString("description"));
        exerciseFromDb.id = resultSet.getInt("id");
        exercises = ArrayUtils.add(exercises, exerciseFromDb);

        }
        return exercises;
    }

    // ----- 4. Usunięcie rekordu  ------

    public static final String deleteExerciseByIdQuery ="DELETE FROM exercise WHERE id = ?;";
    public void deleteExerciseById(Connection connection) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(deleteExerciseByIdQuery);
        preparedStatement.setInt(1, this.id);
        preparedStatement.executeUpdate();
        this.id = 0; // << gdybyśmy chcieli zmodyfikować obiekt i zapisać go ponownie w bazie danych
    }
}
