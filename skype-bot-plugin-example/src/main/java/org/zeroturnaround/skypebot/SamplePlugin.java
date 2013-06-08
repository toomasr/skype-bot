package org.zeroturnaround.skypebot;

import java.util.Arrays;
import java.util.Collection;

import org.zeroturnaround.commands.FunTrivia;
import org.zeroturnaround.commands.IsItFriday;
import org.zeroturnaround.commands.StupidCronCommand;
import org.zeroturnaround.commands.Time;
import org.zeroturnaround.commands.Weather;
import org.zeroturnaround.skypebot.plugins.BotPlugin;
import org.zeroturnaround.skypebot.plugins.Command;
import org.zeroturnaround.skypebot.plugins.SkypeBotPlugin;

@SkypeBotPlugin
public class SamplePlugin implements BotPlugin {

  @Override
  public Collection<Command> getCommands() {
    return Arrays.asList(new Command[] {
        new FunTrivia(),
        new Time(),
        new Weather(),
        new IsItFriday(),
        // it is stupid, believe me
        // new StupidCronCommand()
    });
  }

}
