package org.zeroturnaround;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.commands.Command;
import org.zeroturnaround.commands.CommandFactory;

import com.skype.api.Conversation;
import com.skype.api.Message;
import com.skype.api.Skype;

public class EventListener extends DummyEventListener {
  private static final String BOT_MSG_PREFIX = "bot:";
  private static final Logger log = LoggerFactory.getLogger(EventListener.class);
  private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();
  private final SkypeEngine skypeEngine;

  public EventListener(SkypeEngine skype) {
    super();
    this.skypeEngine = skype;
  }

  public void onMessage(Skype skype, Message message,
      boolean changesInboxTimestamp, Message supersedesHistoryMessage,
      Conversation conversation) {

    log.debug("Incoming message from " + message.getAuthor());

    log.debug("SKYPE.OnMessage." + message.getAuthor() + " Message::TYPE = "
        + message.mType);
    log.debug("CHAT." + message.getAuthor() + ";oid=" + conversation.getOid() + ":"
        + message.mBodyXml);

    conversations.put(conversation.mDisplayName, conversation);
    log.info("convo display name = " + conversation.mDisplayName);
    log.info("convo identity = " + conversation.mIdentity);

    String msgStr = message.getBodyXml();

    // ignore the processing of our own messages to avoid
    // indefinite loop
    if (Configuration.skypeUsername.equals(message.getAuthor())) {
      log.debug("Ignoring command because it is from "
          + Configuration.skypeUsername);
      return;
    }

    if (msgStr.startsWith(BOT_MSG_PREFIX)) {
      String result = processCommand(msgStr);
      if (result != null) {
        conversation.postText(result, false);
      }
    }
    else {
      log.debug("Not an actual command, the prefix " + BOT_MSG_PREFIX
          + " is not present");
    }
  }

  private String processCommand(String msgStr) {
    String cmdStr = msgStr.replaceFirst(BOT_MSG_PREFIX, "").trim();
    log.info("Got command " + cmdStr);

    Command cmd = CommandFactory.getCommand(cmdStr);
    if (cmd != null) {
      return cmd.execute(cmdStr);
    }
    else {
      log.error("Unable to get an actual command for the command string "
          + cmdStr);
    }
    return null;
  }

  public static void postToChat(String chatName, String message) {
    log.debug("PostToChat=" + chatName);
    Conversation convo = conversations.get(chatName);
    if (convo != null) {
      log.debug("Found the conversation, posting");
      convo.postText(message, false);
    }
  }

  public void sidOnDisconnected(String cause) {
    skypeEngine.setConnected(false);
  }

  public void sidOnConnected() {
    skypeEngine.setConnected(true);
  }

}
