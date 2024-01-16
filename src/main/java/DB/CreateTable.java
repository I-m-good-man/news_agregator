package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void createNewTable(String url) {

        String sql = "CREATE TABLE IF NOT EXISTS posts (\n"
                + "	post_id integer PRIMARY KEY,\n"
                + "	group_id integer,\n"
                + "date integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        String url = "jdbc:sqlite:data.sqlite";
//        createNewTable(url);
//    }

}
