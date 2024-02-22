import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LibraryManagementSystem {
    private enum UserRole {
        ADMIN,
        LIBRARIAN,
        STUDENT
    }

    private static class User {
        private String username;
        private String passwordHash;
        private UserRole role;

        public User(String username, String password, UserRole role) {
            this.username = username;
            this.passwordHash = hashPassword(password);
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public UserRole getRole() {
            return role;
        }

        public boolean authenticate(String enteredPassword) {
            return hashPassword(enteredPassword).equals(passwordHash);
        }

        private String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedBytes) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class Book {
        private String title;
        private String author;
        private String genre;
        private String isbn;
        private Date publicationDate;
        private boolean available;
        private boolean reserved;
        private Member reservedBy;

        public Book(String title, String author, String genre, String isbn, Date publicationDate, boolean available) {
            this.title = title;
            this.author = author;
            this.genre = genre;
            this.isbn = isbn;
            this.publicationDate = publicationDate;
            this.available = available;
            this.reserved = false;
            this.reservedBy = null;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getGenre() {
            return genre;
        }

        public String getIsbn() {
            return isbn;
        }

        public Date getPublicationDate() {
            return publicationDate;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public boolean isReserved() {
            return reserved;
        }

        public Member getReservedBy() {
            return reservedBy;
        }

        public void reserve(Member member) {
            this.reserved = true;
            this.reservedBy = member;
        }

        public void cancelReservation() {
            this.reserved = false;
            this.reservedBy = null;
        }
        public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublicationDate(Date newPublicationDate) {
        this.publicationDate = newPublicationDate;
    }
    
    }

    private static class Member {
        private String memberId;
        private String name;
        private String contactInfo;
        private List<Book> borrowingHistory;
        private List<Book> reservedBooks;

        public Member(String memberId, String name, String contactInfo) {
            this.memberId = memberId;
            this.name = name;
            this.contactInfo = contactInfo;
            this.borrowingHistory = new ArrayList<>();
            this.reservedBooks = new ArrayList<>();
        }

        public String getMemberId() {
            return memberId;
        }

        public String getName() {
            return name;
        }

        public String getContactInfo() {
            return contactInfo;
        }

        public List<Book> getBorrowingHistory() {
            return borrowingHistory;
        }

        public void updateContactInfo(String newContactInfo) {
            this.contactInfo = newContactInfo;
        }

        public void addBookToHistory(Book book) {
            borrowingHistory.add(book);
        }

        public void removeBookFromHistory(Book book) {
            borrowingHistory.remove(book);
        }

        public List<Book> getReservedBooks() {
            return reservedBooks;
        }

        public void reserveBook(Book book) {
            reservedBooks.add(book);
            book.reserve(this);
        }

        public void cancelReservation(Book book) {
            reservedBooks.remove(book);
            book.cancelReservation();
        }

        public void setName(String newName) {
            this.name = newName;
        }
        
    }

    private Map<String, User> users;
    private Map<String, Book> books;
    private Map<String, Member> members; // Member management
    private Scanner scanner;

    public LibraryManagementSystem() {
        this.users = new HashMap<>();
        this.books = new HashMap<>();
        this.members = new HashMap<>(); // Initialize member map
        this.scanner = new Scanner(System.in);
        // Registering sample users
        registerUser("admin", "admin123", UserRole.ADMIN);
        registerUser("librarian", "librarian123", UserRole.LIBRARIAN);
        registerUser("student", "student123", UserRole.STUDENT);

        // Adding sample books
        addBook("To Kill a Mockingbird", "Harper Lee", "Fiction", "9780061120084", new Date(), true);
        addBook("1984", "George Orwell", "Dystopian", "9780451524935", new Date(), true);
        addBook("The Great Gatsby", "F. Scott Fitzgerald", "Classic", "9780743273565", new Date(), true);
        addBook("Pride and Prejudice", "Jane Austen", "Romance", "9780141439518", new Date(), true);
        addBook("The Catcher in the Rye", "J.D. Salinger", "Coming-of-age", "9780316769488", new Date(), true);
        addBook("Lord of the Flies", "William Golding", "Allegory", "9780399501487", new Date(), true);
        addBook("Brave New World", "Aldous Huxley", "Science Fiction", "9780060850524", new Date(), true);
        addBook("The Hobbit", "J.R.R. Tolkien", "Fantasy", "9780345339683", new Date(), true);
        addBook("The Alchemist", "Paulo Coelho", "Philosophical fiction", "9780061122415", new Date(), true);
        addBook("Animal Farm", "George Orwell", "Allegorical novella", "9780141182704", new Date(), true);
    }

    public void run() {
        while (true) {
            System.out.println("Welcome to Library Management System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User authenticatedUser = authenticateUser(username, password);

        if (authenticatedUser != null) {
            System.out.println("Authentication successful!");
            System.out.println("Welcome, " + authenticatedUser.getUsername() + "! Your role is: " + authenticatedUser.getRole());
            // Authorize access based on user role
            authorizeAccess(authenticatedUser.getRole());
        } else {
            System.out.println("Authentication failed. Please check your username and password.");
        }
    }

    private User authenticateUser(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.authenticate(password)) {
                return user;
            }
        }
        return null;
    }
    

    private void register() {
        System.out.println("Registration:");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Select Role:");
        System.out.println("1. Admin");
        System.out.println("2. Librarian");
        System.out.println("3. Student");
        System.out.print("Enter role choice: ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        UserRole role;
        switch (roleChoice) {
            case 1:
                role = UserRole.ADMIN;
                break;
            case 2:
                role = UserRole.LIBRARIAN;
                break;
            case 3:
                role = UserRole.STUDENT;
                break;
            default:
                System.out.println("Invalid role choice. Defaulting to Student role.");
                role = UserRole.STUDENT;
        }
        registerUser(username, password, role);
        System.out.println("Registration successful. You can now login.");
    }

    private void authorizeAccess(UserRole role) {
        switch (role) {
            case ADMIN:
                adminMenu();
                break;
            case LIBRARIAN:
                librarianMenu();
                break;
            case STUDENT:
                studentMenu();
                break;
            default:
                System.out.println("Invalid user role.");
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Remove Book");
            System.out.println("4. Search Books");
            System.out.println("5. Manage Members"); // Added member management option
            System.out.println("6. Logout");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        updateBookDetails();
                        break;
                    case 3:
                        removeBook();
                        break;
                    case 4:
                        searchBooks();
                        break;
                    case 5:
                        manageMembers(); // Added member management option
                        break;
                    case 6:
                        System.out.println("Logged out.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private void addBook() {
        System.out.println("Adding a book...");

        // Prompt user to enter book details
        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        // Check if the book already exists
        if (books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " already exists.");
            return;
        }

        // Prompt user for publication date (assuming date format for simplicity)
        System.out.print("Enter publication date (YYYY-MM-DD): ");
        String publicationDateString = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date publicationDate;
        try {
            publicationDate = dateFormat.parse(publicationDateString);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Publication date set to current date.");
            publicationDate = new Date(); // Default to current date
        }

        // Prompt user for availability
        System.out.print("Is the book available? (true/false): ");
        boolean available = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        // Create and add the book to the library
        Book newBook = new Book(title, author, genre, isbn, publicationDate, available);
        books.put(isbn, newBook);

        System.out.println("Book added successfully.");
    }

    private void updateBookDetails() {
        System.out.print("Enter ISBN of the book to update details: ");
        String isbn = scanner.nextLine();

        // Check if book with the given ISBN exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " does not exist.");
            return;
        }

        Book book = books.get(isbn);
        System.out.println("Current details:");
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Genre: " + book.getGenre());
        System.out.println("Publication Date: " + book.getPublicationDate());
        System.out.println("Availability: " + (book.isAvailable() ? "Available" : "Not Available"));

        // Prompt user for updated details
        System.out.println("Enter new details (leave blank to keep current): ");
        System.out.print("New Title: ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            book.setTitle(newTitle);
        }

        System.out.print("New Author: ");
        String newAuthor = scanner.nextLine();
        if (!newAuthor.isEmpty()) {
            book.setAuthor(newAuthor);
        }

        System.out.print("New Genre: ");
        String newGenre = scanner.nextLine();
        if (!newGenre.isEmpty()) {
            book.setGenre(newGenre);
        }

        System.out.print("New Publication Date (YYYY-MM-DD): ");
        String newPublicationDateString = scanner.nextLine();
        if (!newPublicationDateString.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date newPublicationDate = dateFormat.parse(newPublicationDateString);
                book.setPublicationDate(newPublicationDate);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Publication date not updated.");
            }
        }

        System.out.print("Is the book available? (true/false): ");
        String newAvailabilityString = scanner.nextLine();
        if (!newAvailabilityString.isEmpty()) {
            boolean newAvailability = Boolean.parseBoolean(newAvailabilityString);
            book.setAvailable(newAvailability);
        }

        System.out.println("Book details updated successfully.");
    }

    private void removeBook() {
        System.out.print("Enter ISBN of the book to remove: ");
        String isbn = scanner.nextLine();

        // Check if book with the given ISBN exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " does not exist.");
            return;
        }

        // Remove the book
        Book removedBook = books.remove(isbn);
        System.out.println("Book removed successfully: " + removedBook.getTitle());
    }

    private void searchBooks() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase();

        List<Book> matchingBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getTitle().toLowerCase().contains(query) ||
                    book.getAuthor().toLowerCase().contains(query) ||
                    book.getGenre().toLowerCase().contains(query) ||
                    book.getIsbn().toLowerCase().contains(query)) {
                matchingBooks.add(book);
            }
        }

        if (matchingBooks.isEmpty()) {
            System.out.println("No books found matching the query: " + query);
        } else {
            System.out.println("Matching books:");
            for (Book book : matchingBooks) {
                System.out.println(book.getTitle() + " by " + book.getAuthor() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }

    private void manageMembers() {
        while (true) {
            System.out.println("Member Management:");
            System.out.println("1. Add Member");
            System.out.println("2. Update Member Details");
            System.out.println("3. Remove Member");
            System.out.println("4. List Members");
            System.out.println("5. Back to Admin Menu");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addMember();
                        break;
                    case 2:
                        updateMemberDetails();
                        break;
                    case 3:
                        removeMember();
                        break;
                    case 4:
                        listMembers();
                        break;
                    case 5:
                        System.out.println("Returning to Admin Menu.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private void addMember() {
        System.out.println("Adding a member...");

        // Prompt user to enter member details
        System.out.print("Enter member ID: ");
        String memberId = scanner.nextLine();

        // Check if the member already exists
        if (members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " already exists.");
            return;
        }

        System.out.print("Enter member name: ");
        String name = scanner.nextLine();

        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();

        // Create and add the member to the system
        Member newMember = new Member(memberId, name, contactInfo);
        members.put(memberId, newMember);

        System.out.println("Member added successfully.");
    }

    private void updateMemberDetails() {
        System.out.print("Enter member ID to update details: ");
        String memberId = scanner.nextLine();

        // Check if member with the given ID exists
        if (!members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " does not exist.");
            return;
        }

        Member member = members.get(memberId);
        System.out.println("Current details:");
        System.out.println("Name: " + member.getName());
        System.out.println("Contact Info: " + member.getContactInfo());

        // Prompt user for updated details
        System.out.println("Enter new details (leave blank to keep current): ");
        System.out.print("New Name: ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            member.setName(newName);
        }

        System.out.print("New Contact Info: ");
        String newContactInfo = scanner.nextLine();
        if (!newContactInfo.isEmpty()) {
            member.updateContactInfo(newContactInfo);
        }

        System.out.println("Member details updated successfully.");
    }

    private void removeMember() {
        System.out.print("Enter ID of the member to remove: ");
        String memberId = scanner.nextLine();

        // Check if member with the given ID exists
        if (!members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " does not exist.");
            return;
        }

        // Remove the member
        Member removedMember = members.remove(memberId);
        System.out.println("Member removed successfully: " + removedMember.getName());
    }

    private void listMembers() {
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("List of Members:");
            for (Member member : members.values()) {
                System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName() + ", Contact Info: " + member.getContactInfo());
            }
        }
    }

    private void librarianMenu() {
        while (true) {
            System.out.println("Librarian Menu:");
            System.out.println("1. Search Books");
            System.out.println("2. Check Book Availability");
            System.out.println("3. Logout");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        searchBooks();
                        break;
                    case 2:
                        checkBookAvailability();
                        break;
                    case 3:
                        System.out.println("Logged out.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private void checkBookAvailability() {
        System.out.print("Enter ISBN of the book to check availability: ");
        String isbn = scanner.nextLine();

        // Check if book with the given ISBN exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " does not exist.");
            return;
        }

        Book book = books.get(isbn);
        if (book.isAvailable()) {
            System.out.println("The book \"" + book.getTitle() + "\" is available.");
        } else {
            System.out.println("The book \"" + book.getTitle() + "\" is not available.");
            if (book.isReserved()) {
                System.out.println("This book is reserved by: " + book.getReservedBy().getName());
            }
        }
    }

    private void studentMenu() {
        while (true) {
            System.out.println("Student Menu:");
            System.out.println("1. Search Books");
            System.out.println("2. Reserve a Book");
            System.out.println("3. Return to Main Menu");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        searchBooks();
                        break;
                    case 2:
                        reserveBook();
                        break;
                    case 3:
                        System.out.println("Returning to Main Menu.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private void reserveBook() {
        System.out.print("Enter ISBN of the book to reserve: ");
        String isbn = scanner.nextLine();

        // Check if book with the given ISBN exists
        if (!books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " does not exist.");
            return;
        }

        Book book = books.get(isbn);
        if (book.isAvailable()) {
            // Get the authenticated user
            User authenticatedUser = getAuthenticatedUser(isbn, isbn);
            if (authenticatedUser != null && authenticatedUser.getRole() == UserRole.STUDENT) {
                // Since the user is authenticated and is a student, we can proceed to reserve the book
                Member studentMember = getStudentMember(authenticatedUser.getUsername());
                if (studentMember != null) {
                    // Reserve the book
                    book.reserve(studentMember);
                    studentMember.reserveBook(book);
                    System.out.println("Book \"" + book.getTitle() + "\" reserved successfully.");
                    return;
                }
            }
            System.out.println("You are not authorized to reserve a book.");
        } else {
            System.out.println("Book \"" + book.getTitle() + "\" is not available for reservation.");
        }
    }
    
    

    private User getAuthenticatedUser(String username, String password) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.authenticate(password)) {
                return user;
            }
        }
        return getAuthenticatedUser(null, null);
    }
    
    private Member getStudentMember(String username) {
        // Assuming the username corresponds to a student member's ID
        // You can implement your logic to retrieve the member based on the username
        // For this example, we'll just return a sample member
        if (members.containsKey(username)) {
            return members.get(username);
        } else {
            System.out.println("Student member with username " + username + " not found.");
            return null;
        }
    }

    private void registerUser(String username, String password, UserRole role) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, role));
        } else {
            System.out.println("User with username " + username + " already exists.");
        }
    }

    private void addBook(String title, String author, String genre, String isbn, Date publicationDate, boolean available) {
        if (!books.containsKey(isbn)) {
            books.put(isbn, new Book(title, author, genre, isbn, publicationDate, available));
        } else {
            System.out.println("Book with ISBN " + isbn + " already exists.");
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystem librarySystem = new LibraryManagementSystem();
        librarySystem.run();
    }
}
