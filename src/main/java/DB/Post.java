package DB;

public class Post {
    public Integer groupId;
    public Integer postId;
    public Integer date;

    public Post(int groupId, int postId, int date) {
        this.groupId = groupId;
        this.postId = postId;
        this.date = date;

    }

    public Post() {

    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
