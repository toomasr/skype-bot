package org.zeroturnaround;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.commands.Command;
import org.zeroturnaround.commands.CommandFactory;

import com.skype.api.Contact;
import com.skype.api.ContactGroup;
import com.skype.api.Conversation;
import com.skype.api.Conversation.ConversationListener;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.api.Message;
import com.skype.api.Message.MessageListener;
import com.skype.api.Message.PROPERTY;
import com.skype.api.Skype.PROXYTYPE;
import com.skype.api.Skype.SkypeListener;
import com.skype.api.SkypeObject;
import com.skype.ipc.RootObject.ErrorListener;

public class SkypeGlobalListener implements MessageListener, SkypeListener, ConversationListener, ErrorListener {
  private static final String BOT_MSG_PREFIX = "zt-bot:";
  private static final Logger log = LoggerFactory.getLogger(SkypeGlobalListener.class);
  private static Map<String, Conversation> conversations = new HashMap<String, Conversation>();

  @Override
  public void OnPropertyChange(SkypeObject obj, PROPERTY prop, Object value) {
    log.debug("OnPropChange");
    log.debug(obj.toString());
    log.debug(prop.toString());
    if (value != null)
      log.debug(value.toString());
    log.debug("OnPropChange");
  }

  @Override
  public void OnNewCustomContactGroup(ContactGroup group) {
  }

  @Override
  public void OnContactOnlineAppearance(Contact contact) {
  }

  @Override
  public void OnContactGoneOffline(Contact contact) {
  }

  @Override
  public void OnConversationListChange(Conversation conversation, LIST_TYPE type, boolean added) {
  }

  @Override
  public void OnMessage(Message message, boolean changesInboxTimestamp, Message supersedesHistoryMessage,
      Conversation conversation) {

    String author = message.GetStrProperty(Message.PROPERTY.author);
    log.debug("Incoming message from " + author);

    int type = message.GetIntProperty(Message.PROPERTY.type);
    log.debug("SKYPE.OnMessage." + author + " Message::TYPE = " + Message.TYPE.get(type));
    log.debug("CHAT." + author + ";oid=" + conversation.getOid() + ":"
        + message.GetStrProperty(Message.PROPERTY.body_xml));

    String conversationStr = conversation.GetStrProperty(Conversation.PROPERTY.displayname);
    conversations.put(conversationStr, conversation);
    String identity = conversation.GetStrProperty(Conversation.PROPERTY.identity);
    log.info("convo display name = " + conversationStr);
    log.info("convo identity = " + identity);

    String msgStr = message.GetStrProperty(Message.PROPERTY.body_xml);

    // ignore the processing of our own messages to avoid
    // indefinite loop
    if (Configuration.skypeUsername.equals(author)) {
      log.debug("Ignoring command because it is from " + Configuration.skypeUsername);
      return;
    }

    if (msgStr.startsWith(BOT_MSG_PREFIX)) {
      String result = processCommand(msgStr);
      if (result != null) {
        conversation.PostText(result, false);
      }
    }
    else {
      log.debug("Not an actual command, the prefix " + BOT_MSG_PREFIX + " is not present");
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
      log.error("Unable to get an actual command for the command string " + cmdStr);
    }
    return null;
  }

  @Override
  public void OnAvailableVideoDeviceListChange() {
  }

  @Override
  public void OnAvailableDeviceListChange() {
  }

  @Override
  public void OnNrgLevelsChange() {
  }

  @Override
  public void OnProxyAuthFailure(PROXYTYPE type) {
  }

  @Override
  public void OnPropertyChange(SkypeObject obj, com.skype.api.Conversation.PROPERTY prop, Object value) {
    log.debug("onPropertyCHange listener called - doing nothing");
  }

  @Override
  public void OnParticipantListChange(SkypeObject obj) {
  }

  @Override
  public void OnMessage(SkypeObject obj, Message message) {
    log.debug("OnMessage" + obj + message);
  }

  @Override
  public void OnSpawnConference(SkypeObject obj, Conversation spawned) {
  }

  @Override
  public void OnSkypeKitFatalError() {
    log.debug("OnSkypeKitFatalError");
  }

  @Override
  public void OnSkypeKitConnectionClosed() {
    log.debug("OnSkypeKitConnectionClosed");
  }

  public static void postToChat(String chatName, String message) {
    log.debug("PostToChat=" + chatName);
    Conversation convo = conversations.get(chatName);
    if (convo != null) {
      log.debug("Found the conversation, posting");
      convo.PostText(message, false);
    }
  }

  @Override
  public void OnH264Activated() {
    log.debug("OnH264Activated");
  }
}
