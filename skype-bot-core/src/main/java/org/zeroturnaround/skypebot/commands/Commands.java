package org.zeroturnaround.skypebot.commands;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.skypebot.commands.defaultCommands.Help;
import org.zeroturnaround.skypebot.commands.defaultCommands.Reload;
import org.zeroturnaround.skypebot.plugins.Command;
import org.zeroturnaround.skypebot.plugins.CronCommand;
import org.zeroturnaround.skypebot.plugins.OnPostCommand;
import org.zeroturnaround.skypebot.plugins.ReactiveCommand;

public class Commands {
  private static final Logger log = LoggerFactory.getLogger(Commands.class);

  private static final List<Command> allCommands = new ArrayList<Command>();
  private static final ReactiveCommands reactiveCommands = new ReactiveCommands();
  private static final CronCommands cronCommands = new CronCommands();
  private static final OnPostCommands onPostCommands = new OnPostCommands();

  public static void add(Collection<Command> commands) {
    for (Command command : commands) {
      if (command == null) {
        continue;
      }
      add(command);
    }
  }

  public static void add(Command command) {
    log.info("Adding command {}", command);
    allCommands.add(command);
    if (command instanceof ReactiveCommand) {
      reactiveCommands.add((ReactiveCommand) command);
    }
    else if (command instanceof CronCommand) {
      cronCommands.add((CronCommand) command);
    }
    else if (command instanceof OnPostCommand) {
      onPostCommands.add((OnPostCommand) command);
    }
  }

  public static void getCommandsHelp(PrintWriter writer) {
    for (Command command : allCommands) {
      writer.print(command.getName());
      writer.print(" - ");
      writer.println(command.getHelp());
    }
  }

  public static void init() {
    clear();
  }

  public static void clear() {
    allCommands.clear();
    reactiveCommands.clear();
    cronCommands.clear();
    onPostCommands.clear();

    // admin commands that are already present
    add(new Help());
    add(new Reload());
    log.info("Cleared all commands. currently available: {}", allCommands);
  }

}
