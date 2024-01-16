package VK;

public class PostData {
    public Integer groupId;
    public Integer postId;
    public Integer date;
    public String postContent;

    public PostData(Integer groupId, Integer postId, Integer date, String postContent) {
        this.groupId = groupId;
        this.postId = postId;
        this.date = date;
        this.postContent = postContent;
    }
}
