package org.zeroturnaround.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.zeroturnaround.SkypeEngine;

public class HTTPHandler extends AbstractHandler {

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    String message = null;
    String group = null;

    if ("/post".equals(target)) {
      String[] tmp = request.getParameterValues("message");
      if (tmp != null && tmp.length > 0) {
        message = tmp[0];
      }

      tmp = request.getParameterValues("group");
      if (tmp != null && tmp.length > 0) {
        group = tmp[0];
      }

      boolean result = SkypeEngine.post(group, message);
      if (result)
        response.getWriter().println("OK");
      else
        response.getWriter().println("FAIL");
    }
    ((Request) request).setHandled(true);
  }
}
