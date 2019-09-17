import java.sql.*;

public class DatabaseManager {
    private static final String DB_NAME = "testdb.db";
    private static final String URL_TO_FILE_DIRECTORY = "jdbc:sqlite:./";

    public static void createNewDatabase() throws ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");

        String url = URL_TO_FILE_DIRECTORY + DB_NAME;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = URL_TO_FILE_DIRECTORY + DB_NAME;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS csv_imported_data (\n"
                + "    A TEXT,\n"
                + "    B TEXT,\n"
                + "    C TEXT,\n"
                + "    D TEXT,\n"
                + "    E TEXT,\n"
                + "    F TEXT,\n"
                + "    G TEXT,\n"
                + "    H TEXT,\n"
                + "    I TEXT,\n"
                + "    J TEXT\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection connect() {
        // SQLite connection string
        String url = URL_TO_FILE_DIRECTORY + DB_NAME;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
