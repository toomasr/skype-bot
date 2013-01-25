package org.zeroturnaround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.web.WebServer;

public class SkypeChatBot extends Thread {
  private static final Logger log = LoggerFactory.getLogger(SkypeChatBot.class);

  public static void main(String[] args) throws Exception {
    initConfiguration();
    WebServer.startWebServer();

    SkypeEngine sEngine = new SkypeEngine();
    sEngine.connect();

    // sometimes good if skypekit is not started
    // but will be any second
    for (int i = 0; i < 3; i++) {
      log.debug("Is Skype connected? " + sEngine.isConnected());
      sEngine.connect();
      Thread.sleep(5000);
    }
  }

  public static void stopChatBot() {
    WebServer.stopWebServer();
  }

  private static void initConfiguration() {
    Properties props = new Properties();

    File propsFile = new File("personal.properties");
    try {
      if (propsFile.exists()) {
        props.load(new FileReader(propsFile));
      }
      else {
        propsFile = new File("project.properties");
        if (propsFile.exists()) {
          props.load(new FileReader(propsFile));
        }
      }
    }
    catch (FileNotFoundException e) {
      throw new RuntimeException("Unable to read properties file " + propsFile.getAbsolutePath(), e);
    }
    catch (IOException e) {
      throw new RuntimeException("Unable to read properties file " + propsFile.getAbsolutePath(), e);
    }

    Configuration.skypeUsername = props.getProperty("username", null);
    Configuration.skypePassword = props.getProperty("password", null);
    Configuration.pemFile = props.getProperty("pemfile", null);
    if (Configuration.pemFile == null || Configuration.skypePassword == null || Configuration.skypeUsername == null) {
      String msg = "Unable to find username, password or pemfile from project.properties nor persona.properties. Exiting";
      log.error(msg);
      System.err.println(msg);
      System.exit(1);
    }

    // normalize pem file
    File file = new File(Configuration.pemFile);
    if (file.exists()) {
      try {
        Configuration.pemFile = file.getCanonicalPath();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    else {
      throw new RuntimeException("Unable to find the configuration file " + Configuration.pemFile);
    }

    // make sure that there is an accompanying DER file
    file = new File(Configuration.pemFile.replaceAll(".pem", ".der"));
    if (!file.exists()) {
      throw new RuntimeException(
          "You need to have an accompanying DER file! Use the command 'openssl pkcs8 -topk8 -nocrypt -inform PEM -outform DER -in myKeyPair.pem -out myKeyPair.der'");
    }

  }
}
