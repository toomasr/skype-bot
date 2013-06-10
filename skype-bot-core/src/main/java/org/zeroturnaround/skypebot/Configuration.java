package org.zeroturnaround.skypebot;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
  private static final Logger log = LoggerFactory.getLogger(Configuration.class);

  public static Properties props;

  public static String skypeUsername;
  public static String skypePassword;
  public static String pemFile;
  public static String postApiKey;

  private static Set<String> admins = new HashSet<String>();

  public static String getProperty(String key) {
    return Configuration.props.getProperty(key);
  }

  public static boolean isAdmin(String username) {
    return !admins.isEmpty() || admins.contains(username);
  }

  static void setAdmins(String file) {
    if (file == null)
      return;
    try {
      List<String> lines = FileUtils.readLines(new File(file));
      log.info("Adding admins: {}", lines);
      for (String line : lines) {
        String trimmed = line.trim();
        if (!trimmed.isEmpty()) {
          admins.add(trimmed);
        }
      }
    }
    catch (Exception e) {
      log.error("Cannot read admin list file", e);
    }
  }
}
