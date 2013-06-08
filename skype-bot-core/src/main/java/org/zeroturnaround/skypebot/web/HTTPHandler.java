package org.zeroturnaround.skypebot.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.zeroturnaround.skypebot.Configuration;
import org.zeroturnaround.skypebot.SkypeEngine;

public class HTTPHandler extends AbstractHandler {

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    String message = null;
    String group = null;

    if ("/post".equals(target)) {
      if (!Configuration.postApiKey.equals(getParameter(request, "apikey"))) {
        ((Request) request).setHandled(true);
        return;
      }
      message = getParameter(request, "message");
      group = getParameter(request, "group");

      boolean result = SkypeEngine.post(group, message);
      if (result)
        response.getWriter().println("OK");
      else
        response.getWriter().println("FAIL");
    }
    ((Request) request).setHandled(true);
  }

  public String getParameter(HttpServletRequest request, String key) {
    String[] tmp = request.getParameterValues(key);
    if (tmp != null && tmp.length > 0) {
      return tmp[0];
    }
    return "";
  }
}
