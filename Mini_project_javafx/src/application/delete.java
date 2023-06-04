package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;





public class delete {
    public static void deleteStudent(Connection connection, Student student) throws SQLException {
        String query = "DELETE FROM students WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, student.getId());
        preparedStatement.executeUpdate();
    }
}
