package org.zeroturnaround.skypebot.commands.defaultCommands;

import org.zeroturnaround.skypebot.plugins.NameCommand;
import org.zeroturnaround.skypebot.plugins.Plugins;

public class Reload extends NameCommand {

  @Override
  public String getHelp() {
    return "reloads all plugin commands";
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
    Plugins.reload();
    return "Holy skype batman! All commands are reloaded. Use '~ help' to see available commands.";
  }

}
