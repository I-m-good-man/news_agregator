package VK;

import DB.Post;
import JSON.Config;
import VariousUtils.VariousUtils;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiWallLinksForbiddenException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VKUtils {
    private Integer lastTimeOfRequest = -1;

    public Response request(Config config, Integer groupId, Integer numOfPosts) throws ClientException, ApiException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);

        ServiceActor serviceActor = new ServiceActor(config.appId, config.serviceToken);

        try {
            Integer currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();
            Integer timeDelta = VariousUtils.calculateTimeDelta(config.requestLimitPerDay);

            while (true){
                if (this.lastTimeOfRequest == -1){
                    break;
                }
                if ((currentTimestampSeconds - this.lastTimeOfRequest) * 1000 > timeDelta){
                    break;
                }
                currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();
            }
            this.lastTimeOfRequest = VariousUtils.getCurrentTimestampSeconds();

            GetResponse getResponse = vk.wall().get(serviceActor)
                    .ownerId(groupId)
                    .count(numOfPosts)
                    .offset(0)
                    .filter(GetFilter.ALL)
                    .execute();

            // возвращает число результатов запроса
            Integer countOfPosts = getResponse.getCount();
            // возвращает массив объектов записей на стене
            List<WallpostFull> posts = getResponse.getItems();

            if (countOfPosts == 0 || posts.isEmpty()) {
                return new Response("too_many_requests_error");
            } else {
                return new Response("ok", posts);
            }
        } catch (ApiWallLinksForbiddenException e) {
            return new Response("links_posting_is_prohibited");
        } catch (ApiException e) {
            return new Response("business_logic_error");
        } catch (ClientException e) {
            return new Response("transport_layer_error");
        }

    }

    public PostData parsePost(WallpostFull post, Integer groupId) {
        Integer postId = post.getId();
        String postText = post.getText();
        List attachments = post.getAttachments();
        Integer date = post.getDate();

        String postContent = "";
        if (postText.trim().length() > 0) {
            postContent = postText;
        } else {
            if (!attachments.isEmpty()) {
                for (int i = 0; i < attachments.size(); i++) {
                    WallpostAttachment attachment = (WallpostAttachment) attachments.get(i);

                    String title = attachment.getLink().getTitle();
                    if (!title.isEmpty()) {
                        postContent = title;
                    }
                }
            }
        }

        if (postContent.isEmpty()) {
            postContent = "Новая запись!";
        }
        return new PostData(groupId, postId, date, postContent);
    }

    public static ArrayList<Integer> getNewPostIdList(List<WallpostFull> posts, ArrayList<Integer> dbPostIdList) {
        ArrayList<Integer> newPostIdList = new ArrayList<Integer>();
        for (WallpostFull post : posts) {
            Integer postId = post.getId();
            if (!dbPostIdList.contains(postId)) {
                newPostIdList.add(postId);
            }
        }
        return newPostIdList;
    }

    public ArrayList<WallpostFull> getNewPostList(List<WallpostFull> posts, ArrayList<DB.Post> postArrayList, Integer MaxElapsedTime) {
        ArrayList<Integer> dbPostIdList = new ArrayList<>();
        for (Post post :
                postArrayList) {
            dbPostIdList.add(post.postId);
        }

        ArrayList<WallpostFull> newPostList = new ArrayList<WallpostFull>();
        for (WallpostFull post : posts) {
            Integer postId = post.getId();
            Integer date = post.getDate();

            Integer currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();

            if (!dbPostIdList.contains(postId) && (currentTimestampSeconds - date < MaxElapsedTime)) {
                newPostList.add(post);
            }
        }

        newPostList.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
//        Collections.reverse(newPostList);


        return newPostList;
    }

    public static boolean checkPostId(ArrayList<Post> postArrayList, Integer postId) {
        ArrayList<Integer> postIdList = new ArrayList<Integer>();
        for (Post post :
                postArrayList) {
            postIdList.add(post.getPostId());
        }

        if (postIdList.contains(postId)) {
            return true;
        }
        return false;
    }

}
