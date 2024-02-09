import java.sql.*;
import java.util.Scanner;

public class LibrarySystem {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        LibraryManager libraryManager = new LibraryManager();
        libraryManager.runLibrarySystem();
    }

    static class LibraryDatabaseInitializer {
        public static void main(String[] args) {
            try {
                createTable();
                insertData();

                System.out.println("Database initialization completed successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void createTable() throws SQLException {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {

                String createTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                        "book_id VARCHAR(20) PRIMARY KEY," +
                        "category CHAR(50)," +
                        "book_name CHAR(50)," +
                        "author CHAR(20)," +
                        "copies INT" +
                        ")";
                statement.executeUpdate(createTableSQL);
            }
        }

        private static void insertData() throws SQLException {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                 Statement statement = connection.createStatement()) {

                // Inserting data for each book
                insertBook(statement, "b001", "Data structure and algorithms", "learn python", "Narasimha Karumanchi", 12);
                insertBook(statement, "b002", "science friction", "mistery of earth", "Rk narayanna", 11);
                insertBook(statement, "b003", "technical", "learn java", "Narasimha", 20);
                insertBook(statement, "b004", "Biology", "Ecosystem", "Herry", 6);
                insertBook(statement, "b005", "History", "Event in independent", "Rakesh sharma", 19);
                insertBook(statement, "b006", "chemistry", "chemistry in everyday life", "VD tiwari", 12);
                insertBook(statement, "b007", "maths", "math is fun", "Anil kumar", 21);
            }
        }

        private static void insertBook(Statement statement, String bookId, String category, String bookName, String author, int copies) throws SQLException {
            String insertDataSQL = String.format(
                    "INSERT INTO books VALUES ('%s', '%s', '%s', '%s', %d)",
                    bookId, category, bookName, author, copies
            );
            statement.executeUpdate(insertDataSQL);
        }
    }

    static class LibraryManager {
        private final Scanner scanner;
        private final BookManager bookManager;
        private final MemberManager memberManager;

        public LibraryManager() {
            this.scanner = new Scanner(System.in);
            this.bookManager = new BookManager();
            this.memberManager = new MemberManager();
        }

        public void runLibrarySystem() {
            LibraryDatabaseInitializer.main(null); // Initialize the database before running the system

            while (true) {
                System.out.println("\n1. Borrow a book\n2. Return a book\n3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        borrowBook();
                        break;
                    case 2:
                        returnBook();
                        break;
                    case 3:
                        System.out.println("Exiting the system. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private void borrowBook() {
            System.out.print("Enter your member ID: ");
            String memberID = scanner.nextLine();

            System.out.print("Enter the book ID you want to borrow: ");
            String bookID = scanner.nextLine();

            if (memberManager.canBorrow(memberID)) {
                if (bookManager.borrowBook(memberID, bookID)) {
                    System.out.println("Book borrowed successfully!");
                } else {
                    System.out.println("Book borrowing failed. Please check the book availability.");
                }
            } else {
                System.out.println("You have reached the maximum limit of borrowed books.");
            }
        }

        private void returnBook() {
            System.out.print("Enter your member ID: ");
            String memberID = scanner.nextLine();

            System.out.print("Enter the book ID you want to return: ");
            String bookID = scanner.nextLine();

            if (bookManager.returnBook(memberID, bookID)) {
                System.out.println("Book returned successfully!");
            } else {
                System.out.println("Book return failed. Please check your member ID and book ID.");
            }
        }
    }

    static class BookManager {
        public boolean borrowBook(String memberID, String bookID) {
            // Implement the logic to borrow a book and update the database
            // Return true if successful, false otherwise
            return false;
        }

        public boolean returnBook(String memberID, String bookID) {
            // Implement the logic to return a book and update the database
            // Return true if successful, false otherwise
            return false;
        }
    }

    static class MemberManager {
        private static final int MAX_BORROWED_BOOKS = 3; // Maximum number of books a member can borrow

        public boolean canBorrow(String memberID) {
            // Implement the logic to check if a member can borrow a book
            // Return true if the member can borrow, false otherwise
            return true;
        }
    }
}