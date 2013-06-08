package org.zeroturnaround.skypebot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.plugins.Plugins;
import org.zeroturnaround.skypebot.web.WebServer;

public class SkypeChatBot extends Thread {

  private static final Logger log = LoggerFactory.getLogger(SkypeChatBot.class);

  public static void main(String[] args) throws Exception {
    initConfiguration();
    initCommands();
    startWebServer();
    startSkypeEngine();
  }

  private static void initCommands() {
    Plugins.reload();
  }

  public static void startSkypeEngine() throws InterruptedException {
    SkypeEngine sEngine = new SkypeEngine();
    sEngine.connect();
    for (int i = 0; i < 3; i++) {
      if (sEngine.isConnected()) {
        break;
      }
      Thread.sleep(5000);
      sEngine.connect();
    }
  }

  public static void startWebServer() {
    Executors.newSingleThreadExecutor().execute(new WebServer());
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
    Configuration.props = props;

    Configuration.skypeUsername = props.getProperty("username", null);
    Configuration.skypePassword = props.getProperty("password", null);
    Configuration.pemFile = props.getProperty("pemfile", null);
    // empty string here is important.
    Configuration.postApiKey = props.getProperty("postApiKey", "");
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
      throw new RuntimeException("You need to have an accompanying DER file! Use the command "
          + "'openssl pkcs8 -topk8 -nocrypt -inform PEM -outform DER -in myKeyPair.pem -out myKeyPair.der'");
    }

    Configuration.setAdmins(Configuration.getProperty("adminfile"));
  }
}
