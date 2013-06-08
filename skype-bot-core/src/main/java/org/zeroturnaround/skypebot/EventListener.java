package org.zeroturnaround.skypebot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.plugins.ReactiveCommands;

import com.skype.api.Conversation;
import com.skype.api.Message;

public class EventListener extends SkypeAdapter {
  private static final Logger log = LoggerFactory.getLogger(EventListener.class);
  private static final String BOT_MSG_PREFIX = "~";

  private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();
  private final SkypeEngine skypeEngine;

  public EventListener(SkypeEngine skype) {
    this.skypeEngine = skype;
  }

  public void onMessage(Conversation conversation, Message message) {
    if (Configuration.skypeUsername.equals(message.getAuthor())) {
      // log.debug("Ignoring command because it is from " + Configuration.skypeUsername);
      return;
    }
    conversations.put(conversation.getDisplayName(), conversation);
    String messageText = message.getBodyXml();
    log.trace("Got message {}", messageText);
    if (messageText.startsWith(BOT_MSG_PREFIX)) {
      String commandText = messageText.substring(BOT_MSG_PREFIX.length()).trim();

      String result = processCommand(conversation.getDisplayName(), message.getAuthor(), commandText);
      log.trace("Got responses {}", result);
      if (result != null) {
          conversation.postText(result, false);
      }
    }
  }

  private String processCommand(String conversation, String author, String commandText) {
    return ReactiveCommands.handle(conversation, author, commandText);
  }

  public static List<String> getAvailableConversationNames(String... neededNames) {
    List<String> available = new ArrayList<String>();
    for (String name : neededNames) {
      if (conversations.containsKey(name)) {
        available.add(name);
      }
    }
    return available;
  }

  public static void postToChat(String chatName, String message) {
    // log.debug("PostToChat=" + chatName);
    Conversation convo = conversations.get(chatName);
    if (convo != null) {
      // log.debug("Found the conversation, posting");
      convo.postText(message, false);
    }
  }

  public void sidOnDisconnected(String cause) {
    log.error("Disconnected from the runtime, {}", cause);
    System.exit(1);
  }

  public void sidOnConnected() {
    skypeEngine.setConnected(true);
  }

  public static void post(Map<String, String> result) {
    for (Map.Entry<String, String> me : result.entrySet()) {
      postToChat(me.getKey(), me.getValue());
    }
  }

}
