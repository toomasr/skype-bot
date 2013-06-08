package org.zeroturnaround.commands;

import java.text.DateFormat;
import java.util.Date;

import org.zeroturnaround.skypebot.plugins.NameCommand;

public class Time extends NameCommand {

  @Override
  public String getHelp() {
    return "prints current time";
  }

  @Override
  protected String reactInternal(String conversationName, String author, String message) {
    DateFormat format = DateFormat.getDateTimeInstance();
    return "Current time: " + format.format(new Date());
  }

}
