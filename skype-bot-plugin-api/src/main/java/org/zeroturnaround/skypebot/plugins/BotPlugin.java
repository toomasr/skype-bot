package org.zeroturnaround.skypebot.plugins;

import java.util.Collection;

public interface BotPlugin {
  Collection<Command> getCommands();
}
