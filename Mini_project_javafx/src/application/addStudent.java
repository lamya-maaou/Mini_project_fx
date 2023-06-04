package application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class addStudent {
    public static void addPerson(Connection connection, Student person) throws SQLException {
        String query = "INSERT INTO students (NOM, PRENOM, CNE, ID) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, person.getNom());
        preparedStatement.setString(2, person.getPrenom());
        preparedStatement.setString(3, person.getCne());
        preparedStatement.setInt(4, person.getId());
        preparedStatement.executeUpdate();
    }
}

