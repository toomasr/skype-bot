package org.zeroturnaround.skypebot.commands.defaultCommands;

import org.zeroturnaround.skypebot.Configuration;
import org.zeroturnaround.skypebot.SkypeEventHandler;
import org.zeroturnaround.skypebot.plugins.ReactiveCommand;

public class Alias implements ReactiveCommand {
  private static final String prefix = "alias";

  @Override
  public String getName() {
    return this.getClass().getSimpleName().toLowerCase();
  }

  @Override
  public String getHelp() {
    return "Gives an alias to this chat, so bot can recognize it even after rename";
  }

  @Override
  public String react(String conversationName, String author, String message) {
    if (!message.startsWith(prefix)) {
      return null;
    }

    if (!Configuration.isAdmin(author)) {
      return "Insufficient rights";
    }
    String alias = message.substring(prefix.length()).trim();
    boolean added = SkypeEventHandler.addAlias(conversationName, alias);
    if (added)
      return String.format("Great success, now this conversation has an alias '%s'", alias);
    else {
      return "Hmm, something isn't right, there already is a conversation with that alias";
    }
  }

}
