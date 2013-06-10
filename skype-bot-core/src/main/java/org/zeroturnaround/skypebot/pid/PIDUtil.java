package org.zeroturnaround.skypebot.pid;

import java.lang.management.ManagementFactory;

public class PIDUtil {
  public static String getPID() {
    try {
      String name = ManagementFactory.getRuntimeMXBean().getName();
      return name.substring(0, name.indexOf('@'));
    }
    catch (Exception e) {
      throw new RuntimeException("Cannot find my own pid, very unfortunate!");
    }
  }
}
