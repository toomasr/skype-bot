package com.zeroturnaround;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.zeroturnaround.web.WebServer;

public class ZTChatBot {

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

    Configuration.skypeUsername = props.getProperty("username", "");
    Configuration.skypePassword = props.getProperty("password", "");
    Configuration.pemFile = props.getProperty("pemfile", "");
  }
}
