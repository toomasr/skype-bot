package org.zeroturnaround.skypebot;

import java.util.Properties;

public class Configuration {
  public static Properties props;

  public static String skypeUsername;
  public static String skypePassword;
  public static String pemFile;
  public static String postApiKey;

  public static String getProperty(String key) {
    return Configuration.props.getProperty(key);
  }
}
