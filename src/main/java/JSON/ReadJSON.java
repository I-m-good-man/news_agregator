package JSON;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
    @SuppressWarnings("unchecked")
    public Config getConfig() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("config.json")) {
            Object obj = jsonParser.parse(reader);

            JSONObject configObject = (JSONObject) obj;
            String accessToken = (String) configObject.get("serviceToken");
            String botToken = (String) configObject.get("botToken");
            Long appId = (Long) configObject.get("appId");
            Long requestLimitPerDay = (Long) configObject.get("requestLimitPerDay");
            Long chatId = (Long) configObject.get("chatId");
            Long adminId = (Long) configObject.get("adminId");

            Long telegramSymLimitPerMessage = (Long) configObject.get("telegramSymLimitPerMessage");


            Config config = new Config(accessToken, appId.intValue(), requestLimitPerDay.intValue(), botToken, chatId, telegramSymLimitPerMessage.intValue(), adminId.intValue());

            return config;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Config();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Group> getGroupList() throws IOException, ParseException {
        ArrayList<Group> groupArrayList = new ArrayList<Group>();

        JSONParser jsonParser = new JSONParser();

        JSONArray groupArray = (JSONArray) jsonParser.parse(
                new InputStreamReader(new FileInputStream("groups.json"), "UTF-8"));

        for (Object groupObject :
                groupArray) {
            JSONObject groupJSONObject = (JSONObject) groupObject;
            String groupName = (String) groupJSONObject.get("groupName");
            Long groupId = (Long) groupJSONObject.get("groupId");
            Group group = new Group(groupName, groupId.intValue());
            groupArrayList.add(group);

        }
        return groupArrayList;
    }

    public static void main(String[] args) throws IOException, ParseException {
        ReadJSON readJSON = new ReadJSON();
        System.out.println(readJSON.getGroupList());
    }
}