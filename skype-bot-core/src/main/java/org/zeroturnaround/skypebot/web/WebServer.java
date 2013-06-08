package org.zeroturnaround.skypebot.web;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(WebServer.class);

  private final Server server;

  public WebServer() {
    this(2500);
  }

  public WebServer(int port) {
    this.server = new Server(port);
    server.setStopAtShutdown(true);
    server.setHandler(new HTTPHandler());
  }

  @Override
  public void run() {
    try {
      server.start();
    }
    catch (Exception e) {
      log.error("Cannot start webserver", e);
    }
  }
}
