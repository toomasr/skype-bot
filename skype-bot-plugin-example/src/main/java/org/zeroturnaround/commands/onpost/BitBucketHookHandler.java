package org.zeroturnaround.commands.onpost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.zeroturnaround.skypebot.plugins.OnPostCommand;

public class BitBucketHookHandler implements OnPostCommand {

  private static final String chatName = "Notifications";

  @Override
  public String getName() {
    return "bitbuckethook";
  }

  @Override
  public String getHelp() {
    return "accepts postcommit hooks from bitbucket";
  }

  @Override
  public Map<String, String[]> handle(Map<String, String[]> parameters, String requestBody) {
    try {
      JSONObject object = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(requestBody);
      JSONObject repo = (JSONObject) object.get("repository");
      String repoName = (String) repo.get("name");
      List<String> messages = new ArrayList<String>();
      JSONArray commits = (JSONArray) object.get("commits");
      for (int i = 0; i < commits.size(); i++) {
        JSONObject commit = (JSONObject) commits.get(i);
        String rawAuthor = (String) commit.get("raw_author");
        String message = (String) commit.get("message");
        messages.add(String.format("%s commited '%s' to %s", rawAuthor, message, repoName));
      }
      Map<String, String[]> replies = new HashMap<String, String[]>();
      replies.put(chatName, messages.toArray(new String[0]));
      return replies;
    }
    catch (ParseException e) {
      throw new RuntimeException("Cannot parse json: " + requestBody, e);
    }
  }

  @Override
  public String toString() {
    return "BitBucketHookHandler: -> " + chatName;
  }

}
