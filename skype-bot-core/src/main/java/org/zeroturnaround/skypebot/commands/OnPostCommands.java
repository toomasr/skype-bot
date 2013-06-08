package org.zeroturnaround.skypebot.commands;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.plugins.OnPostCommand;

public class OnPostCommands {

  private static final Logger log = LoggerFactory.getLogger(OnPostCommands.class);
  private static final Map<String, OnPostCommand> activeCommands = new HashMap<String, OnPostCommand>();

  public void add(OnPostCommand command) {
    activeCommands.put(command.getName(), command);
  }

  public void clear() {
    activeCommands.clear();
  }

  public static Map<String, String> handle(String commandName, Map<String, String[]> map, String requestBody) {
    OnPostCommand command = activeCommands.get(commandName);
    if (command == null) {
      log.info("OnPostCommand for commandName {} is not found", commandName);
    }
    try {
      return command.handle(map, requestBody);
    }
    catch (Exception e) {
      log.error("Command {} failed to react to parameters: {}",
          new Object[] { command.getClass().getSimpleName(), map }, e);
    }
    return null;
  }

}
