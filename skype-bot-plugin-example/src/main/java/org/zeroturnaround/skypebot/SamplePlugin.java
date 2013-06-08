package org.zeroturnaround.skypebot;

import java.util.Arrays;
import java.util.Collection;

import org.zeroturnaround.commands.cron.StupidCronCommand;
import org.zeroturnaround.commands.onpost.BitBucketHookHandler;
import org.zeroturnaround.commands.reactive.FunTrivia;
import org.zeroturnaround.commands.reactive.IsItFriday;
import org.zeroturnaround.commands.reactive.Time;
import org.zeroturnaround.commands.reactive.Weather;
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
        new BitBucketHookHandler()
        // it is stupid, believe me
        // new StupidCronCommand()
    });
  }

}
