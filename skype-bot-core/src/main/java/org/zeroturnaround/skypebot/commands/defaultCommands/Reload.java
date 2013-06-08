package org.zeroturnaround.skypebot.commands.defaultCommands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.Configuration;
import org.zeroturnaround.skypebot.plugins.NameCommand;
import org.zeroturnaround.skypebot.plugins.Plugins;

public class Reload extends NameCommand {
  private static final Logger log = LoggerFactory.getLogger(Reload.class);

  @Override
  public String getHelp() {
    return "reloads all plugin commands";
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
    log.info("Reload by {}", author);
    if(!Configuration.isAdmin(author)) {
      return "Insufficient rights";
    }
    Plugins.reload();
    return "Holy skype batman! All commands are reloaded. Use '~ help' to see available commands.";
  }

}
