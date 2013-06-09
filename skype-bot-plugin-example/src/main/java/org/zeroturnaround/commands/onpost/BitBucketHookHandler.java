package org.zeroturnaround.commands.onpost;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

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
      requestBody = URLDecoder.decode(requestBody, "UTF-8");
      requestBody = requestBody.replaceFirst("payload=", "");
      JSONObject object = (JSONObject) new JSONParser(JSONParser.MODE_PERMISSIVE).parse(requestBody);
      JSONObject repo = (JSONObject) object.get("repository");
      // String repoName = (String) repo.get("name");
      String absoluteUrl = (String) repo.get("absolute_url");
      String repoUrl = "https://bitbucket.org" + absoluteUrl;
      List<String> messages = new ArrayList<String>();
      JSONArray commits = (JSONArray) object.get("commits");
      for (int i = 0; i < commits.size(); i++) {
        JSONObject commit = (JSONObject) commits.get(i);
        String rawAuthor = (String) commit.get("raw_author");
        rawAuthor = rawAuthor.replaceFirst(" <.*>", "");
        String message = (String) commit.get("message");
        String branch = (String) commit.get("branch");
        messages.add(String.format("%s commited '%s' to %s (branch '%s')", rawAuthor, message, repoUrl, branch));
      }
      Map<String, String[]> replies = new HashMap<String, String[]>();
      replies.put(chatName, messages.toArray(new String[0]));
      return replies;
    }
    catch (Exception e) {
      throw new RuntimeException("Cannot parse json: " + requestBody, e);
    }
  }

  @Override
  public String toString() {
    return "BitBucketHookHandler: -> " + chatName;
  }

}
