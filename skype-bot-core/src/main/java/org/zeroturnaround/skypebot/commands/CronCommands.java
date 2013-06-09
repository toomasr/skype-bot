package org.zeroturnaround.skypebot.commands;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.zeroturnaround.skypebot.SkypeEventHandler;
import org.zeroturnaround.skypebot.SkypeEngine;
import org.zeroturnaround.skypebot.plugins.CronCommand;

public class CronCommands {

  private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

  public void add(final CronCommand command) {
    long delay = command.getCooldown();
    executorService.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        List<String> availableConversations = SkypeEventHandler.getAvailableConversationNames(command.getNecessaryConversationNames());
        Map<String, String[]> result = command.fire(availableConversations.toArray(new String[0]));
        SkypeEventHandler.post(result);
      }
    }, delay, delay, TimeUnit.SECONDS);
  }

  public void clear() {
    // stop old
    executorService.shutdownNow();
    // create new service
    executorService = Executors.newScheduledThreadPool(2);
  }

}
