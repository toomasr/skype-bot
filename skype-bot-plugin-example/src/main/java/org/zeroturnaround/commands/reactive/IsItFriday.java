package org.zeroturnaround.commands.reactive;

import java.util.Calendar;
import java.util.Random;

import org.zeroturnaround.skypebot.plugins.NameCommand;

public class IsItFriday extends NameCommand {
  private static Random r = new Random();
  private static String[] negative = { "Nope!", "(no)", "Get back to work!" };
  private static String[] positive = { "Oh yeah!", "(party)", "Yep!", "(y)", "(party)(party)(party)" };

  private String randomString(String[] possibleResponses) {
    return possibleResponses[r.nextInt(possibleResponses.length)];
  }

  @Override
  public String getHelp() {
    return "gives an answer to this quite important question";
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
      boolean yep = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
      String[] possibleResponses = yep ? positive : negative;
      return randomString(possibleResponses);
    }
}