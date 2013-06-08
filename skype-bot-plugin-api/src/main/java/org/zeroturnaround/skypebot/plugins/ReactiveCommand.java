package org.zeroturnaround.skypebot.plugins;

public interface ReactiveCommand extends Command {
  String react(String conversationName, String author, String message);
}
