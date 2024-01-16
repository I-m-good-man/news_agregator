package DB;

import java.sql.*;
import java.util.ArrayList;


public class DBInteractions {
    private Connection connect() {
        String url = "jdbc:sqlite:data.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(Integer groupId, Integer postId, Integer date) {
        String sql = "INSERT INTO posts(group_id, post_id, date) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, groupId.toString());
            pstmt.setString(2, postId.toString());
            pstmt.setString(3, date.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Post> getPostsByGroupId(Integer groupId){
        ArrayList<Post> posts = new ArrayList<Post>();

        String sql = "SELECT group_id, post_id, date "
                + "FROM posts WHERE group_id == ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,groupId.toString());
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("group_id"),
                        rs.getInt("post_id"),
                        rs.getInt("date"));
                posts.add(post);
            }
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }

        return posts;
    }

    public void delete(Integer postId) {
        String sql = "DELETE FROM posts WHERE post_id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postId.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
//            System.out.println(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//
//        DBInteractions DBInteractions = new DBInteractions();
//
//        DBInteractions.insert(1, 2, 3);
//        DBInteractions.insert(10, 22, 3);
//
////        System.out.println(DBInteractions.getPostsByGroupId(1).get(0).date);
//
//        DBInteractions.delete(2);
//    }

}
