import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryDatabase {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try {
            // Establishing a connection
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Example: Insert a book
            insertBook(connection, "The Great Gatsby", "F. Scott Fitzgerald", "1234567890123");

            // Example: Retrieve all books
            retrieveBooks(connection);

            // Example: Update book information
            updateBook(connection, 1, "The Great Gatsby", "F. Scott Fitzgerald", "9876543210123");

            // Example: Delete a book
            deleteBook(connection, 1);

            // Closing the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertBook(Connection connection, String title, String author, String isbn) throws SQLException {
        String query = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, isbn);
            preparedStatement.executeUpdate();
            System.out.println("Book inserted successfully");
        }
    }

    private static void retrieveBooks(Connection connection) throws SQLException {
        String query = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("book_id") +
                        ", Title: " + resultSet.getString("title") +
                        ", Author: " + resultSet.getString("author") +
                        ", ISBN: " + resultSet.getString("isbn"));
            }
        }
    }

    private static void updateBook(Connection connection, int bookId, String title, String author, String isbn)
            throws SQLException {
        String query = "UPDATE books SET title=?, author=?, isbn=? WHERE book_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, isbn);
            preparedStatement.setInt(4, bookId);
            preparedStatement.executeUpdate();
            System.out.println("Book updated successfully");
        }
    }

    private static void deleteBook(Connection connection, int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.executeUpdate();
            System.out.println("Book deleted successfully");
        }
    }
}
