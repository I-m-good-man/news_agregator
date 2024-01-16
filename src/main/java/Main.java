import DB.DBInteractions;
import DB.DBUtils;
import DB.Post;
import JSON.Config;
import JSON.Group;
import JSON.ReadJSON;
import Telegram.Message;
import Telegram.TelegramUtils;
import VK.PostData;
import VK.Response;
import VK.VKUtils;
import VariousUtils.VariousUtils;
import com.pengrad.telegrambot.TelegramException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void mainTest1() {
        System.out.println(VariousUtils.getCurrentTimestampSeconds());
        System.out.println(VariousUtils.getCurrentTimestampSeconds() - 1673629894);
        System.out.println((VariousUtils.getCurrentTimestampSeconds() - 1673629894) > 10 * 24 * 60 * 60);

        DBInteractions dbInteractions = new DBInteractions();

        ArrayList<Post> postArrayList = dbInteractions.getPostsByGroupId(1);
        for (Post post :
                postArrayList) {
            System.out.println(post.postId + " " + post.date);
        }

        DBUtils.deleteOldPosts(postArrayList, dbInteractions, 10 * 24 * 60 * 60);

        postArrayList = dbInteractions.getPostsByGroupId(1);
        for (Post post :
                postArrayList) {
            System.out.println(post.postId + " " + post.date);
        }
    }

    public static void mainTest2() throws ClientException, ApiException, TelegramException, IOException, ParseException {
        ReadJSON readJSON = new ReadJSON();
        Config config = readJSON.getConfig();

        TelegramUtils telegramUtils = new TelegramUtils(config);

        VKUtils vkUtils = new VKUtils();

        DBInteractions dbInteractions = new DBInteractions();

        ArrayList<Group> groupArrayList = readJSON.getGroupList();

        Group testGroup = groupArrayList.get(groupArrayList.size() - 2);

        Response response = vkUtils.request(config, testGroup.groupId, 5);
        List<WallpostFull> posts = response.posts;
        ArrayList<Post> postArrayList = dbInteractions.getPostsByGroupId(-32128167);
        ArrayList<WallpostFull> newPostList = vkUtils.getNewPostList(posts, postArrayList, 50 * 24 * 60 * 60);

        for (WallpostFull post :
                newPostList) {
            System.out.println(post.getDate());

        }
//        System.out.println(posts.size());
//        WallpostFull post = posts.get(1);
//        PostData postData = vkUtils.parsePost(post, testGroup.groupId);
//
//        Message message = new Message(testGroup.groupName, postData);
//        String messageText = message.generateMessage();
//        telegramUtils.sendMessage(config.chatId, messageText);
    }

    public static void mainFunc() throws ClientException, ApiException, InterruptedException, TelegramException, IOException, ParseException {
        ReadJSON readJSON = new ReadJSON();
        Config config = readJSON.getConfig();

        TelegramUtils telegramUtils = new TelegramUtils(config);

        VKUtils vkUtils = new VKUtils();

        DBInteractions dbInteractions = new DBInteractions();

        ArrayList<Group> groupArrayList = readJSON.getGroupList();

        if (config.dataFromConfigFile != true) {
//            System.out.println("Данные не были считаны из конфигурационного файла config.json!");
        } else if (groupArrayList.isEmpty()) {
//            System.out.println("Данные из файла groups.json не были считаны, или файл пустой!");
        } else {
            try {
                telegramUtils.sendMessage(config.adminId, "Данные из конфигурационных файлов config.json и groups.json считаны верно!");
            } catch (Exception ee){
            }

            try {
                ArrayList<Message> allMessages = new ArrayList<>();
                for (Group group :
                        groupArrayList) {

                    String groupName = group.groupName;
                    Integer groupId = group.groupId;

                    // добавленные посты
                    ArrayList<Post> postArrayList = dbInteractions.getPostsByGroupId(groupId);

                    Response response = vkUtils.request(config, groupId, 5);
                    if (response.status == "ok") {
                        List<WallpostFull> posts = response.posts;
                        ArrayList<WallpostFull> newPostList = vkUtils.getNewPostList(posts, postArrayList, 24 * 60 * 60);
                        for (WallpostFull newPost :
                                newPostList) {
                            PostData postData = vkUtils.parsePost(newPost, groupId);
                            Message message = new Message(groupName, postData, config.telegramSymLimitPerMessage);
                            allMessages.add(message);
                            DBUtils.deleteOldPosts(postArrayList, dbInteractions, 10 * 24 * 60 * 60);
                        }
                    }

                }

                allMessages.sort((o1, o2) -> o1.postData.date.compareTo(o2.postData.date));

                for (Message message :
                        allMessages) {
                    String messageText = message.generateMessage();
                    telegramUtils.sendMessage(config.chatId, messageText);
                    dbInteractions.insert(message.postData.groupId, message.postData.postId, message.postData.date);
                }
            } catch (Exception e) {
                try {
                    telegramUtils.sendMessage(config.chatId, e.toString());
                } catch (Exception ee) {

                }
            }
        }

    }

    public static void loadLastPosts() throws ClientException, ApiException, InterruptedException, TelegramException, IOException, ParseException {
        ReadJSON readJSON = new ReadJSON();
        Config config = readJSON.getConfig();

        TelegramUtils telegramUtils = new TelegramUtils(config);

        VKUtils vkUtils = new VKUtils();

        DBInteractions dbInteractions = new DBInteractions();

        ArrayList<Group> groupArrayList = readJSON.getGroupList();

        if (config.dataFromConfigFile != true) {
            System.out.println("Данные не были считаны из конфигурационного файла config.json!");
        } else if (groupArrayList.isEmpty()) {
            System.out.println("Данные из файла groups.json не были считаны, или файл пустой!");
        } else {
            System.out.println("Данные из конфигурационных файлов config.json и groups.json считаны верно!");

            ArrayList<Message> allMessages = new ArrayList<>();
            for (Group group :
                    groupArrayList) {

                String groupName = group.groupName;
                Integer groupId = group.groupId;

                // добавленные посты
                ArrayList<Post> postArrayList = dbInteractions.getPostsByGroupId(groupId);

                Response response = vkUtils.request(config, groupId, 30);
                if (response.status == "ok") {
                    List<WallpostFull> posts = response.posts;

                    ArrayList<WallpostFull> newPostList = vkUtils.getNewPostList(posts, postArrayList, 30 * 24 * 60 * 60);
                    for (WallpostFull newPost :
                            newPostList) {
                        PostData postData = vkUtils.parsePost(newPost, groupId);
                        Message message = new Message(groupName, postData, config.telegramSymLimitPerMessage);
                        allMessages.add(message);
                        DBUtils.deleteOldPosts(postArrayList, dbInteractions, 30 * 24 * 60 * 60);
                    }
                }
            }

            allMessages.sort((o1, o2) -> o1.postData.date.compareTo(o2.postData.date));

            for (Message message :
                    allMessages) {
                String messageText = message.generateMessage();
                try {
                    telegramUtils.sendMessage(config.chatId, messageText);
                } catch (Exception e) {
                    System.out.println(e);
                }

                dbInteractions.insert(message.postData.groupId, message.postData.postId, message.postData.date);
            }
        }

    }

    public static void main(String[] args) throws TelegramException, ClientException, IOException, InterruptedException, ApiException {
//        loadLastPosts();

        while (true) {
            try {
                mainFunc();
            } catch (Exception e) {
                continue;
            }
        }
    }


}
