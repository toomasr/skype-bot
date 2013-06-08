package org.zeroturnaround.skypebot.commands;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.plugins.ReactiveCommand;

public class ReactiveCommands {
  private static final Logger log = LoggerFactory.getLogger(ReactiveCommands.class);
  private static final List<ReactiveCommand> activeCommands = new ArrayList<ReactiveCommand>();

  public void add(ReactiveCommand command) {
    activeCommands.add(command);
  }

  public void clear() {
    activeCommands.clear();
  }

  public static String handle(String conversation, String author, String message) {
    // currently this can easily throw a concurrent modification exception if some command is added or
    // plugins get reinitialized.
    // that's why we stop iterations after the first non-null result. Reload reinits plugins and iterating further
    // will throw an exception.
    for (ReactiveCommand command : activeCommands) {
      try {
        String response = command.react(conversation, author, message);
        log.trace("Command {} reaction to message {} is {}", new Object[] { command, message, response });
        if (response != null) {
          return response;
        }
      }
      catch (Exception e) {
        log.error("Command {} failed to react to conversation: {}, author: {}, message: {}",
            new Object[] { command.getClass().getSimpleName(), conversation, author, message }, e);
      }
    }
    return null;
  }
}
