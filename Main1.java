package pl.coderslab.warsztaty2;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.warsztaty2.models.Exercise;
import pl.coderslab.warsztaty2.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main1 {
    public static void main(String[] args) {
        // --- początek
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/PS?useSSL=false&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "root", "coderslab")) {


            // NOWY WPIS DO BD
//            User user1 = new User("Adam", "adam@dawid.pl", "maslo" );
//            System.out.println(user1);
//
//            user1.seveToDb(conn);
//
//            System.out.println(user1);

//            Exercise ex1 = new Exercise("Cwiczenie 1", "stworzenie bazy danych dla szkoly programowania");
//            Exercise ex2 = new Exercise();
//            ex2.setTitle("Cwiczenie 2");
//            ex2.setDescription("stworzenie bazy danych dla szkoly programowania 2");
//
//            System.out.println(ex1);
//            System.out.println(ex2);
//
//            ex1.saveToDb(conn);
//            ex2.saveToDb(conn);
//
//            System.out.println(ex1);
//            System.out.println(ex2);




            // POBRANIE rekordu usera
//            User user = User.getUser(conn, 2);
//
//            System.out.println(user);

//            Exercise ex1 = Exercise.getExerciseById(conn, 4);
//            System.out.println(ex1);


            // POBRANIE WSZYSTKICH USERÓW
//            User[] users = User.getAllUsers(conn);
//            for (User user: users){
//                System.out.println(user);
//            }

//            Exercise[] exercises = Exercise.getAllExercises(conn);
//            for (Exercise recordInExercises: exercises){
//                System.out.println(recordInExercises);
//            }

            // MODYFIKACJA ISTNIEJACEGO USERA


//            User user = User.getUser(conn, 1);
//            System.out.println(user);
//
//            user.setUsername("Dawid Przybylo");
//
//            System.out.println(user);
//
//            user.seveToDb(conn);

//                Exercise ex1 = Exercise.getExerciseById(conn, 1);
//            System.out.println("Przed modyfikacja: "+ex1);
//                ex1.setTitle("Cwiczenie 2 UPDATEd");
//            System.out.println("Po modyfikacji: "+ ex1);
//
//            ex1.saveToDb(conn);




            // USUWANIE USERA


//            User user = User.getUser(conn, 1);
//            System.out.println(user);
//            user.deleteUser(conn);

            Exercise ex1 = Exercise.getExerciseById(conn, 1);
                    ex1.deleteExerciseById(conn);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // ---  koniec


    }
}
