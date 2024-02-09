// File name: ReportingAnalyticsManager.java


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportingAnalyticsManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            stmt = conn.createStatement();
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }

        while (true) {
            System.out.println("Reporting and Analytics Menu:");
            System.out.println("1. Generate report on popular books");
            System.out.println("2. Generate report on most borrowed books");
            System.out.println("3. Generate report on overdue books");
            System.out.println("4. Analytics for data-driven decisions");
            System.out.println("5. Exit");
            int choice = new java.util.Scanner(System.in).nextInt();

            switch (choice) {
                case 1:
                    generatePopularBooksReport();
                    break;
                case 2:
                    generateMostBorrowedBooksReport();
                    break;
                case 3:
                    generateOverdueBooksReport();
                    break;
                case 4:
                    performAnalytics();
                    break;
                case 5:
                    System.out.println("Exiting Reporting and Analytics Menu.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void generatePopularBooksReport() {
        // Implement logic to generate a report on popular books
        // Use appropriate SQL queries and printing logic
        // Example: Display books with the highest ratings or most views
        System.out.println("Generating report on popular books...");
        try {
            String sql = "SELECT * FROM books ORDER BY rating DESC LIMIT 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("Popular Books Report:");
            System.out.println("Book ID\tTitle\tAuthor\tRating");

            while (rs.next()) {
                int id = rs.getInt("bid");
                String title = rs.getString("book_title");
                String author = rs.getString("book_author");
                double rating = rs.getDouble("rating");

                System.out.println(id + "\t" + title + "\t" + author + "\t" + rating);
            }
        } catch (SQLException e) {
            System.out.println("Error generating popular books report.");
            e.printStackTrace();
        }

    }

    private static void generateMostBorrowedBooksReport() {
        // Implement logic to generate a report on most borrowed books
        // Use appropriate SQL queries and printing logic
        // Example: Display books with the highest number of borrowings
        System.out.println("Generating report on most borrowed books...");
        try {
            String sql = "SELECT books.*, COUNT(issued_books.BID) AS borrow_count " +
                         "FROM books LEFT JOIN issued_books ON books.BID = issued_books.BID " +
                         "GROUP BY books.BID ORDER BY borrow_count DESC LIMIT 10";
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("Most Borrowed Books Report:");
            System.out.println("Book ID\tTitle\tAuthor\tBorrow Count");

            while (rs.next()) {
                int id = rs.getInt("bid");
                String title = rs.getString("book_title");
                String author = rs.getString("book_author");
                int borrowCount = rs.getInt("borrow_count");

                System.out.println(id + "\t" + title + "\t" + author + "\t" + borrowCount);
            }
        } catch (SQLException e) {
            System.out.println("Error generating most borrowed books report.");
            e.printStackTrace();
        }
    }

    private static void generateOverdueBooksReport() {
        // Implement logic to generate a report on overdue books
        // Use appropriate SQL queries and printing logic
        // Example: Display books that are overdue and need to be returned
        System.out.println("Generating report on overdue books...");
        try {
            String sql = "SELECT issued_books.*, books.book_title, users.user_name " +
                         "FROM issued_books " +
                         "INNER JOIN books ON issued_books.BID = books.BID " +
                         "INNER JOIN users ON issued_books.UID = users.UID " +
                         "WHERE DATEDIFF(NOW(), issued_books.ISSUED_DATE) > issued_books.PERIOD";
            PreparedStatement ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            System.out.println("Overdue Books Report:");
            System.out.println("Issue ID\tUser Name\tBook Title\tIssue Date\tDue Date");

            while (rs.next()) {
                int issueId = rs.getInt("IID");
                String userName = rs.getString("user_name");
                String bookTitle = rs.getString("book_title");
                String issueDate = rs.getString("ISSUED_DATE");
                // Assuming the due date is ISSUED_DATE + PERIOD days
                String dueDate = rs.getString("ISSUED_DATE") + rs.getInt("PERIOD");

                System.out.println(issueId + "\t" + userName + "\t" + bookTitle + "\t" + issueDate + "\t" + dueDate);
            }
        } catch (SQLException e) {
            System.out.println("Error generating overdue books report.");
            e.printStackTrace();
        }
        
    }

    private static void performAnalytics() {
        // Implement analytics for data-driven decisions
        // Use appropriate SQL queries and printing logic
        // Example: Display trends, patterns, or insights from the data
        System.out.println("Performing analytics for data-driven decisions...");
        try {
            // Example analytics: Display the average number of books borrowed per user
            String avgBooksBorrowedSQL = "SELECT AVG(borrow_count) AS avg_borrow_count " +
                                         "FROM (SELECT UID, COUNT(BID) AS borrow_count " +
                                         "      FROM issued_books GROUP BY UID) AS user_borrow_counts";
    
            PreparedStatement avgBooksBorrowedPS = conn.prepareStatement(avgBooksBorrowedSQL);
            ResultSet avgBooksBorrowedRS = avgBooksBorrowedPS.executeQuery();
    
            double avgBooksBorrowed = 0;
    
            if (avgBooksBorrowedRS.next()) {
                avgBooksBorrowed = avgBooksBorrowedRS.getDouble("avg_borrow_count");
            }
    
            System.out.println("Analytics Report:");
            System.out.println("Average Number of Books Borrowed Per User: " + avgBooksBorrowed);
    
        } catch (SQLException e) {
            System.out.println("Error performing analytics.");
            e.printStackTrace();
        }
    }
}
