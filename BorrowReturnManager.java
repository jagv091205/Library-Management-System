// File name: BorrowReturnManager.java


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class BorrowReturnManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection conn = null;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }

        while (true) {
            System.out.println("Borrowing and Returning Menu:");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. Exit");
            int choice = new Scanner(System.in).nextInt();

            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    System.out.println("Exiting Borrowing and Returning Menu.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void borrowBook() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the user ID to borrow a book:");
            int uid = sc.nextInt();

            System.out.println("Enter the book ID to borrow:");
            int bid = sc.nextInt();

            System.out.println("Enter the issue date (yyyy-mm-dd):");
            String date = sc.next();

            System.out.println("Enter the period (in days):");
            int period = sc.nextInt();

            String sql = "INSERT INTO issued_books (UID, BID, ISSUED_DATE, PERIOD) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, bid);
                ps.setString(3, date);
                ps.setInt(4, period);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Book borrowed successfully.");
                } else {
                    System.out.println("Book borrowing failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error borrowing book.");
            e.printStackTrace();
        }
    }

    static void returnBook() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the user ID to return a book:");
            int uid = sc.nextInt();

            System.out.println("Enter the book ID to return:");
            int bid = sc.nextInt();

            System.out.println("Enter the return date (yyyy-mm-dd):");
            String date = sc.next();

            String sql = "INSERT INTO returned_books (UID, BID, RETURNED_DATE) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, bid);
                ps.setString(3, date);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Book returned successfully.");
                } else {
                    System.out.println("Book return failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error returning book.");
            e.printStackTrace();
        }
    }
}
