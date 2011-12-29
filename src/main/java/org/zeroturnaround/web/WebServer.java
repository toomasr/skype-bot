package org.zeroturnaround.web;

import org.eclipse.jetty.server.Server;

public class WebServer {
  private static Server server;

  public static void startWebServer() {
    server = new Server(2500);
    server.setHandler(new HTTPHandler());
    try {
      server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void stopWebServer() {
    try {
      server.stop();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
