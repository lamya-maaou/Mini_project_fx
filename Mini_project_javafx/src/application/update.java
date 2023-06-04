package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.update;

public class update {
    public static void updatePerson(Connection connection, Student student) throws SQLException {
        String query = "UPDATE students SET NOM=?, PRENOM=?, CNE=? WHERE ID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, student.getNom());
        preparedStatement.setString(2, student.getPrenom());
        preparedStatement.setString(3, student.getCne());
        preparedStatement.setInt(4, student.getId());
        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Mise à jour réussie.");
        }
    }
}