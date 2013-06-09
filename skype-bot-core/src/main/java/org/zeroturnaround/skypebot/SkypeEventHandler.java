package org.zeroturnaround.skypebot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.commands.ReactiveCommands;

import com.skype.api.Conversation;
import com.skype.api.Message;

public class SkypeEventHandler extends SkypeAdapter {
  private static final Logger log = LoggerFactory.getLogger(SkypeEventHandler.class);
  private static final String BOT_MSG_PREFIX = "~";

  private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();
  private static Map<String, Conversation> aliases = new HashMap<String, Conversation>();

  private final SkypeEngine skypeEngine;

  public SkypeEventHandler(SkypeEngine skype) {
    this.skypeEngine = skype;
  }

  private static void addConversation(String name, Conversation conversation) {
    conversations.put(name, conversation);
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
      if (isConversationAvailable(name)) {
        available.add(name);
      }
    }
    return available;
  }

  private static boolean isConversationAvailable(String name) {
    if (conversations.containsKey(name) || aliases.containsKey(name)) {
      return true;
    }
    return SkypeEngine.findConversation(name) != null;
  }

  public static boolean postToChat(String chatName, String message) {
    Conversation conversation = conversations.get(chatName);
    if (conversation != null) {
      conversation.postText(message, false);
      return true;
    }
    Conversation aliasedConversation = aliases.get(chatName);
    if (aliasedConversation != null) {
      aliasedConversation.postText(message, false);
      return true;
    }
    Conversation found = SkypeEngine.findConversation(chatName);
    if (found != null) {
      addConversation(chatName, found);
      found.postText(message, false);
      return true;
    }
    return false;
  }

  public static void post(Map<String, String[]> messages) {
    if (messages == null) {
      return;
    }
    for (Map.Entry<String, String[]> me : messages.entrySet()) {
      for (String message : me.getValue()) {
        postToChat(me.getKey(), message);
      }
    }
  }

  public void sidOnDisconnected(String cause) {
    log.error("Disconnected from the runtime, {}", cause);
    System.exit(1);
  }

  public void sidOnConnected() {
    skypeEngine.setConnected(true);
  }

  public static boolean addAlias(String conversationName, String alias) {
    if (aliases.containsKey(alias)) {
      StringBuilder sb = new StringBuilder("There exists following aliases: ");
      for (Map.Entry<String, Conversation> me : aliases.entrySet()) {
        sb.append(me.getKey());
        sb.append(" -> '");
        sb.append(me.getValue().getDisplayName());
        sb.append("',");
      }
      log.info(sb.toString());
      return false;
    }
    aliases.put(alias, conversations.get(conversationName));
    return true;
  }

}
