package DB;

import VariousUtils.VariousUtils;
import javafx.geometry.Pos;

import java.util.ArrayList;

public class DBUtils {
    public static void deleteOldPosts(ArrayList<Post> posts, DBInteractions dbInteractions, Integer MaxElapsedTime){
        for (Post post :
                posts) {
            Integer currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();

            if (currentTimestampSeconds - post.date > MaxElapsedTime) {
                dbInteractions.delete(post.postId);
            }
        }
    }


}
