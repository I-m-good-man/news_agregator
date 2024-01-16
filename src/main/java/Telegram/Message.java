package Telegram;

import VK.PostData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    public String groupName;
    public PostData postData;
    public Integer telegramSymLimitPerMessage;

    public Message(String groupName, PostData postData, Integer telegramSymLimitPerMessage) {
        this.groupName = groupName;
        this.postData = postData;
        this.telegramSymLimitPerMessage = telegramSymLimitPerMessage;
    }

    public String generateMessage() {
        String groupNameHashTag = "#" + groupName.replace(" ", "_");
        String header = "<b>" + "#" + groupName.replace(" ", "_") + "</b>" + "\n";

        String postLink = "https://vk.com/wall" + postData.groupId.toString() + "_" + postData.postId.toString();
        String source = "⭕ <b><a href=\"" + postLink + "\">Оригинал записи</a></b>\n";

        String generalContent = this.upgradeGeneralContent(this.postData.postContent);


        Integer restSymLimit = this.telegramSymLimitPerMessage;
        restSymLimit -= header.length();
        restSymLimit -= source.length();
        restSymLimit -= 20;

        if (generalContent.length() > restSymLimit){
            generalContent = generalContent.substring(0, restSymLimit - 20) + " ...см. Оригинал записи";
        }

        String resString = header + "\n" + source + "\n" + generalContent;
        return resString;
    }

    private static String upgradeGeneralContent(String generalContent) {
        generalContent = replaceGroupId(generalContent);
        generalContent = replaceUserId(generalContent);
        return generalContent;
    }
    private static String replaceGroupId(String generalContent) {
        try {
            ArrayList <ArrayList<String>> replacements = new ArrayList<>();

            String regexExpression = "(\\[club[^\\[\\]]+\\])";
            Pattern patternLink = Pattern.compile(regexExpression, Pattern.UNICODE_CASE);
            Matcher toReplaceSnippets = patternLink.matcher(generalContent);
            while (toReplaceSnippets.find()) {
                String toReplaceString = generalContent.substring(toReplaceSnippets.start(), toReplaceSnippets.end());
                String toReplaceStringContent = toReplaceString.substring(1, toReplaceString.length()-1);

                ArrayList<String> subStrings = new ArrayList<String>(Arrays.asList(toReplaceStringContent.split("\\|")));

                if (subStrings.size() > 1) {
                    String id = subStrings.get(0);
                    subStrings.remove(0);
                    String name = String.join("", subStrings);

                    String link = "https://vk.com/" + id;
                    String newSnippet = "<a href=\"" + link + "\">" + name + "</a>";

                    ArrayList<String> listOldNew = new ArrayList<>();
                    listOldNew.add(toReplaceString);
                    listOldNew.add(newSnippet);
                    replacements.add(listOldNew);
                }
            }

            for (ArrayList<String> listOldNew :
                    replacements) {
                String oldString = listOldNew.get(0);
                String newString = listOldNew.get(1);
                generalContent = generalContent.replace(oldString, newString);
            }

        } catch (Exception e){

        }

        return generalContent;
    }

    private static String replaceUserId(String generalContent) {
        try {
            ArrayList <ArrayList<String>> replacements = new ArrayList<>();

            String regexExpression = "(\\[id[^\\[\\]]+\\])";
            Pattern patternLink = Pattern.compile(regexExpression, Pattern.UNICODE_CASE);
            Matcher toReplaceSnippets = patternLink.matcher(generalContent);
            while (toReplaceSnippets.find()) {
                String toReplaceString = generalContent.substring(toReplaceSnippets.start(), toReplaceSnippets.end());
                String toReplaceStringContent = toReplaceString.substring(1, toReplaceString.length()-1);

                ArrayList<String> subStrings = new ArrayList<String>(Arrays.asList(toReplaceStringContent.split("\\|")));

                if (subStrings.size() > 1) {
                    String id = subStrings.get(0);
                    subStrings.remove(0);
                    String name = String.join("", subStrings);

                    String link = "https://vk.com/" + id;
                    String newSnippet = "<a href=\"" + link + "\">" + name + "</a>";

                    ArrayList<String> listOldNew = new ArrayList<>();
                    listOldNew.add(toReplaceString);
                    listOldNew.add(newSnippet);
                    replacements.add(listOldNew);
                }
            }

            for (ArrayList<String> listOldNew :
                    replacements) {
                String oldString = listOldNew.get(0);
                String newString = listOldNew.get(1);
                generalContent = generalContent.replace(oldString, newString);
            }

        } catch (Exception e){

        }

        return generalContent;
    }


//    public static void main(String[] args) {
//        String text = "\uD83D\uDD39 [club1|нашего|паблика] \n" +
//                "\uD83D\uDD39 [id240650417|Савелий Чернышов] \n" +
//                "\uD83D\uDD39 [id674583598|Иван Иванов] \n" +
//                "\uD83D\uDD39 [club2|нашего паблика] \n" +
//                "\uD83D\uDD39 [club3|нашего паблика]";
//
//        String nText = Message.upgradeGeneralContent(text);
//        System.out.println(nText);
//
//        String stroka = "";
//        for (int i=0; i <5000; i++){
//            stroka += 'A';
//        }
//
//        PostData postData = new PostData(1, 2, 21, stroka);
//        Message msg = new Message("fdsf", postData, 4000);
//
//        System.out.println(msg.generateMessage());
//    }

}
