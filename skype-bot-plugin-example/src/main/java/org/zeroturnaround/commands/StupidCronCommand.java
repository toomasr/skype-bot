package org.zeroturnaround.commands;

import java.util.HashMap;
import java.util.Map;

import org.zeroturnaround.skypebot.plugins.CronCommand;

public class StupidCronCommand implements CronCommand {

  @Override
  public String getName() {
    return "StupidCronCommand";
  }

  @Override
  public String getHelp() {
    return "spams 'SkypeBot-testbed' chat once in 2 seconds";
  }

  @Override
  public long getCooldown() {
    return 2;
  }

  @Override
  public String[] getNecessaryConversationNames() {
    return new String[] {"SkypeBot-testbed"};
  }

  @Override
  public Map<String, String> fire(String... availableConversationNames) {
    Map<String, String> responses = new HashMap<String, String>();
    responses.put("SkypeBot-testbed", "this is a very important message");
    return responses;
  }

}
