package org.zeroturnaround.skypebot;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skype.api.Account;
import com.skype.api.Conversation;
import com.skype.api.Skype;
import com.skype.ipc.ClientConfiguration;

public class SkypeEngine {
  private static final Logger log = LoggerFactory.getLogger(SkypeEngine.class);
  private static Skype skype;
  private boolean isConnected = false;

  public SkypeEngine() {
    skype = new Skype();
  }

  public void connect() {
    ClientConfiguration conf = configureSkype();
    SkypeEventHandler listener = new SkypeEventHandler(this);

    skype.registerConnectionListener(listener);
    skype.registerSkypeListener(listener);
    skype.registerConversationListener(listener);
    skype.registerMessageListener(listener);

    if (skype.init(conf, listener)) {
      log.debug("Initing skype success, now starting");
      skype.start();
      String version = skype.getVersionString();
      log.debug("Skype version " + version);
      log.info("Logging in with user " + Configuration.skypeUsername);
      Account account = skype.getAccount(Configuration.skypeUsername);
      account.loginWithPassword(Configuration.skypePassword, false, true);
    }
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setConnected(boolean flag) {
    this.isConnected = flag;
  }

  private ClientConfiguration configureSkype() {
    ClientConfiguration conf = new ClientConfiguration();
    int port = 8963;
    String host = "127.0.0.1";

    conf.setTcpTransport(host, port);
    log.debug("Using host {} and port {}", host, port);
    conf.setCertificate(Configuration.pemFile);
    log.debug("Using certificate file {}", Configuration.pemFile);
    return conf;
  }

  public static boolean post(String conversationName, String message) {
    Conversation[] conversations = skype.getConversationList(Conversation.ListType.ALL_CONVERSATIONS);
    for (int i = 0; i < conversations.length; i++) {
      if (conversations[i].getDisplayName().equals(conversationName)) {
        conversations[i].postText(message, false);
        SkypeEventHandler.addConversation(conversationName, conversations[i]);
        return true;
      }
    }
    return false;
  }

  public static void post(Map<String, String[]> messages) {
    for (Map.Entry<String, String[]> me : messages.entrySet()) {
      for (String message : me.getValue()) {
        post(me.getKey(), message);
      }
    }
  }
}
