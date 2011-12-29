package org.zeroturnaround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.web.WebServer;

public class SkypeChatBot {
  private static final Logger log = LoggerFactory.getLogger(SkypeChatBot.class);

  public static void main(String[] args) throws Exception {
    initConfiguration();
    WebServer.startWebServer();
    SkypeEngine bot = new SkypeEngine();
    bot.start();
  }

  public static void stop() {
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
  }
}
