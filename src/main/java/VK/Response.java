package VK;

import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.List;

public class Response {
    public String status;
    public List<WallpostFull> posts;

    public Response(String status) {
        this.status = status;
    }

    public Response(String status, List<WallpostFull> posts) {
        this.status = status;
        this.posts = posts;
    }
}
