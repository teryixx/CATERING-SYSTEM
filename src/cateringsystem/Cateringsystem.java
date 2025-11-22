package cateringsystem;

import java.sql.*;
import java.util.*;

public class Cateringsystem {

    Scanner sc = new Scanner(System.in);

    // ============================================
    // MAIN MENU
    // ============================================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cateringsystem app = new Cateringsystem();
        String resp;

        do {
            System.out.println("\n==== CATERING SYSTEM ====");
            System.out.println("1. REGISTER USER");
            System.out.println("2. LOGIN");
            System.out.println("3. ADD USER");
            System.out.println("4. VIEW USER");
            System.out.println("5. UPDATE USER");
            System.out.println("6. DELETE USER");
            System.out.println("7. EXIT");
            System.out.print("Enter Action: ");
            int action = sc.nextInt();

            switch (action) {
                case 1 -> app.registerUser();
                case 2 -> app.loginUser();
                case 3 -> app.addUser();
                case 4 -> app.viewUser();
                case 5 -> {
                    app.viewUser();
                    app.updateUser();
                }
                case 6 -> {
                    app.viewUser();
                    app.deleteUser();z
                }
                case 7 -> {
                    System.out.println("Thank You!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }

            System.out.print("\nContinue? yes/no: ");
            resp = sc.next();

        } while (resp.equalsIgnoreCase("yes"));

        System.out.println("Thank You!");
    }

    // ============================================
    // REGISTER USER
    // ============================================
    private void registerUser() {
        sc.nextLine();
        System.out.print("\nEnter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        System.out.print("Enter User Type (Admin/Customer): ");
        String type = sc.nextLine();

        String hashed = hashPassword(pass);

        String sql = "INSERT INTO tbl_user(u_email, u_pass, u_type, u_status) VALUES (?, ?, ?, ?)";

        addRecord(sql, email, hashed, type, "Active");

        System.out.println("User registered successfully!");
    }

    // ============================================
    // LOGIN USER
    // ============================================
    private void loginUser() {
        sc.nextLine();
        System.out.print("\nEnter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        String hashed = hashPassword(pass);

        String sql = "SELECT * FROM tbl_user WHERE u_email = ? AND u_pass = ?";
        var result = fetchRecords(sql, email, hashed);

        if (!result.isEmpty()) {
            var user = result.get(0);
            System.out.println("\nLOGIN SUCCESS!");
            System.out.println("User Type: " + user.get("u_type"));
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    // ============================================
    // ADD USER
    // ============================================
    private void addUser() {
        sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Contact: ");
        String contact = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Role (Admin/Customer): ");
        String role = sc.nextLine();

        String sql = "INSERT INTO tbl_user (u_name, u_contact, u_email, u_role) VALUES (?, ?, ?, ?)";

        addRecord(sql, name, contact, email, role);

        System.out.println("User added successfully!");
    }

    // ============================================
    // VIEW USERS
    // ============================================
    private void viewUser() {
        String sql = "SELECT * FROM tbl_user";
        var records = fetchRecords(sql);

        System.out.println("\n===== USER LIST =====");

        if (records.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (var row : records) {
            System.out.println(
                row.get("u_id") + " | " +
                row.get("u_name") + " | " +
                row.get("u_contact") + " | " +
                row.get("u_email") + " | " +
                row.get("u_role")
            );
        }
    }

    // ============================================
    // UPDATE USER
    // ============================================
    private void updateUser() {
        System.out.print("\nEnter User ID to Update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("New Name: ");
        String name = sc.nextLine();

        System.out.print("New Contact: ");
        String contact = sc.nextLine();

        System.out.print("New Email: ");
        String email = sc.nextLine();

        System.out.print("New Role (Admin/Customer): ");
        String role = sc.nextLine();

        String sql = "UPDATE tbl_user SET u_name=?, u_contact=?, u_email=?, u_role=? WHERE u_id=?";

        updateRecord(sql, name, contact, email, role, id);

        System.out.println("User updated successfully!");
    }

    // ============================================
    // DELETE USER
    // ============================================
    private void deleteUser() {
        System.out.print("\nEnter User ID to Delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tbl_user WHERE u_id=?";
        deleteRecord(sql, id);

        System.out.println("User deleted!");
    }

    // ============================================
    // DATABASE CONNECTION
    // ============================================
    private Connection connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/catering_db";
            String user = "root";
            String pass = "";

            return DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
            return null;
        }
    }

    // ============================================
    // EXECUTE INSERT
    // ============================================
    public void addRecord(String sql, Object... params) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    // ============================================
    // EXECUTE UPDATE
    // ============================================
    public void updateRecord(String sql, Object... params) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    // ============================================
    // EXECUTE DELETE
    // ============================================
    public void deleteRecord(String sql, Object... params) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    // ============================================
    // FETCH RECORDS (SELECT)
    // ============================================
    public List<Map<String, Object>> fetchRecords(String sql, Object... params) {

        List<Map<String, Object>> records = new ArrayList<>();

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, params);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                records.add(row);
            }

        } catch (SQLException e) {
            System.out.println("Fetch Error: " + e.getMessage());
        }

        return records;
    }

    // ============================================
    // HELPER — SET PREPAREDSTATEMENT VALUES
    // ============================================
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }
    }

    // ============================================
    // HASH PASSWORD (SHA-256)
    // ============================================
    public static String hashPassword(String password) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();

        } catch (Exception e) {
            return null;
        }
    }
}
