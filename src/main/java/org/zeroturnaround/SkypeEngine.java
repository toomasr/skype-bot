package org.zeroturnaround;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skype.api.Account;
import com.skype.api.Conversation;
import com.skype.api.Conversation.LIST_TYPE;
import com.skype.api.Message;
import com.skype.api.Skype;
import com.skype.ipc.TCPSocketTransport;
import com.skype.ipc.TLSServerTransport;
import com.skype.ipc.Transport;
import com.skype.util.PemReader;

public class SkypeEngine extends Thread {
  private static final Logger log = LoggerFactory.getLogger(SkypeEngine.class);
  private static Transport transport;
  private static Skype skype_instance;

  public SkypeEngine() {
    skype_instance = new Skype();
  }

  public void run() {
    try {
      SkypeGlobalListener listener = connectToSkype();
      while (transport.isConnected()) {
        log.debug("Connected");
        Thread.sleep(60000);
      }
      tearDownSkype(listener);
    }
    catch (IOException e) {
      log.debug("Skype thread hickup", e);
    }
    catch (InterruptedException e) {
      log.debug("Skype thread hickup", e);
    }
    finally {
      SkypeChatBot.stop();
    }
  }

  private void tearDownSkype(SkypeGlobalListener listener) throws IOException {
    log.debug("Connection lost. Cleaning up.");

    unRegisterAllListeners(skype_instance, listener);

    if (transport != null) {
      transport.disconnect();
    }

    if (skype_instance != null) {
      skype_instance.Close();
    }
  }

  private SkypeGlobalListener connectToSkype() {
    log.debug("skypeConnect()");

    PemReader certAsPem;
    X509Certificate cert;
    PrivateKey privateKey;
    try {
      log.debug("Using pem file " + Configuration.pemFile);
      certAsPem = new PemReader(Configuration.pemFile);
      cert = certAsPem.getCertificate();
      privateKey = certAsPem.getKey();
    }
    catch (FileNotFoundException e1) {
      throw new RuntimeException(e1);
    }
    catch (InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    Transport socketTransport = new TCPSocketTransport("127.0.0.1", 8963);

    transport = new TLSServerTransport(socketTransport, cert, privateKey);

    log.debug("new TLSServerTransport created, calling skype.Init()...");
    SkypeGlobalListener skypeListener = new SkypeGlobalListener();

    registerAllListeners(skype_instance, skypeListener);

    try {
      skype_instance.Init(transport);
      skype_instance.Start();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      if (transport.isConnected()) {
        log.debug("calling GetVersionString");
        String version = skype_instance.GetVersionString();
        log.debug("Skype version " + version);

        Account account = skype_instance.GetAccount(Configuration.skypeUsername);
        account.LoginWithPassword(Configuration.skypePassword, false, true);
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return skypeListener;
  }

  private static void registerAllListeners(Skype skype, SkypeGlobalListener skypeListener) {
    skype.RegisterListener(Skype.getmoduleid(), skypeListener);
    skype.RegisterListener(Message.moduleID(), skypeListener);
    skype.RegisterListener(Conversation.moduleID(), skypeListener);
    skype.SetErrorListener(skypeListener);
  }

  private static void unRegisterAllListeners(Skype skype, SkypeGlobalListener skypeListener) {
    skype.UnRegisterListener(Skype.getmoduleid(), skypeListener);
    skype.UnRegisterListener(Message.moduleID(), skypeListener);
    skype.UnRegisterListener(Conversation.moduleID(), skypeListener);
  }

  public static boolean post(String group, String message) {
    Conversation[] convos = skype_instance.GetConversationList(LIST_TYPE.ALL_CONVERSATIONS);
    for (int i = 0; i < convos.length; i++) {
      String groupName = convos[i].GetStrProperty(Conversation.PROPERTY.displayname);
      if (groupName.equals(group)) {
        convos[i].PostText(message, false);
        return true;
      }
    }
    return false;
  }
}
