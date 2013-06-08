package org.zeroturnaround.skypebot.commands.defaultCommands;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.zeroturnaround.skypebot.commands.Commands;
import org.zeroturnaround.skypebot.plugins.NameCommand;

public class Help extends NameCommand {

  @Override
  public String getHelp() {
    return "lists available commands";
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
    StringWriter out = new StringWriter();
    PrintWriter writer = new PrintWriter(out);
    writer.println("Available commands:");
    Commands.getCommandsHelp(writer);
    writer.flush();
    return out.toString();
  }

}
